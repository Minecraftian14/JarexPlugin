package com.mcxiv.app;

import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.mcxiv.app.ui.RowElement;
import com.mcxiv.app.util.GithubUtil;
import com.mcxiv.app.util.HttpDownloadUtility;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.common.network.model.GithubReleaseData;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JarexMediator extends Mediator<JarexHUD> {

    public static final String CLASS_NAME = JarexMediator.class.getName();

    private final JarexPlugin plugin;
    private final JarexDownloader downloader;

    public JarexMediator(JarexPlugin plugin) {
        super(CLASS_NAME, new JarexHUD(plugin));
        this.plugin = plugin;
        downloader = new JarexDownloader();
    }

    @Override
    public String[] listNotificationInterests() {
        return Event.getList();
    }


    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);

        Event event = Event.getEventNamed(notification.getName());

        switch (event) {

            case OPEN_JAREX_HUD_ACTION:
                if (viewComponent.isOpen) return;
                launchJarexHUDAction();
                break;

            case OPEN_APPLICATION_ACTION:
                openApplicationAction(notification.getBody());
                break;

            case DOWNLOAD_APPLICATION_JAR_ACTION:
                downloadJarAction(notification.getBody());
                break;

        }

    }

    private void launchJarexHUDAction() {
        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(viewComponent::launch);

        service.shutdown();
    }

    private void openApplicationAction(RowElement element) {
        if (element.isAlwaysCheckUpdate()) {
            checkForUpdates(element.getLink());
            return;
        }
        launchApplication(GithubUtil.applicationJarName(element.getLink()));
    }

    private void launchApplication(String appName) {

        try {
            System.out.println("java -jar " + plugin.getAPI().getCacheDir() + File.separator + appName);
            Runtime.getRuntime().exec("java -jar " + plugin.getAPI().getCacheDir() + File.separator + appName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void checkForUpdates(String link) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {

            downloader.show(plugin.getAPI().getUIStage());
            downloader.setMessage("Checking for updates ...");
            downloader.setProgress(0);

            Capsule c = new Capsule();

            c.pluginName = GithubUtil.displayName(link);
            c.cachePath = plugin.getAPI().getCacheDir();
            c.jarPath = c.cachePath + File.separator + GithubUtil.applicationJarName(link);

            new File(c.cachePath).mkdirs();
            try {
                File target = new File(c.jarPath);
                if (!target.isFile()) target.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String data = HttpDownloadUtility.downloadToString(link);
                Json json = new Json();
                json.setIgnoreUnknownFields(true);
                c.data = json.fromJson(GithubReleaseData.class, data);

                String key = "latest_update:" + c.pluginName;

                // If:
                // * file doesnt exists
                // * plugin inbuilt record for any latest version, had it been there is null, ie, has never been downloaded before
                // * and if it exists, it's not equal to latest version
                if (!new File(c.jarPath).exists() || plugin.getStorage().get(key) == null || !plugin.getStorage().get(key).equals(c.data.tag_name)) {

                    Dialogs.showConfirmDialog(plugin.getAPI().getUIStage(),
                            "New update found!", "A new version of " + GithubUtil.displayName(link) + " has been found, would you like to download it?",
                            new String[]{"Later", "Download Now"}, new Integer[]{0, 1}, r -> {
                                if (r == 1) {
                                    plugin.facade.sendNotification(Event.DOWNLOAD_APPLICATION_JAR_ACTION.name, c);
                                } else {
                                    downloader.progressComplete();
                                    launchApplication(GithubUtil.applicationJarName(link));
                                }
                            }).padBottom(20).pack();

                } else {
                    downloader.progressComplete();
                    launchApplication(GithubUtil.applicationJarName(link));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();
    }

    private void downloadJarAction(Capsule c) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {

            downloader.setMessage("Downloading " + c.data.name + " ...");
            for (GithubReleaseData.GithubReleaseAssetData assetData : c.data.assets) {

                if (assetData.name.equals(c.pluginName) || perhapsEqual(assetData.name, c.pluginName.split("[^a-zA-Z]]"))) {

                    try {
                        HttpDownloadUtility.downloadFile(assetData.browser_download_url, c.jarPath, downloader);

                        plugin.getStorage().put("latest_update:" + c.pluginName, c.data.tag_name);

                        launchApplication(c.pluginName + ".jar");
                    } catch (IOException e) {
                        downloader.progressFailed();
                        e.printStackTrace();
                    }
                    break;
                }
            }
        });
        executor.shutdown();
        plugin.facade.sendNotification(MsgAPI.SAVE_EDITOR_CONFIG);
    }

    public static boolean perhapsEqual(String name, String... elements) {
        name = name.toLowerCase();
        for (String element : elements) if (!name.contains(element.toLowerCase())) return false;
        return true;
    }

    static class Capsule {
        String pluginName;
        String cachePath;
        String jarPath;
        GithubReleaseData data;
    }

}
