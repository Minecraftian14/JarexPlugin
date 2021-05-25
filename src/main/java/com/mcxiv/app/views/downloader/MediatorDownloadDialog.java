package com.mcxiv.app.views.downloader;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.mcxiv.app.util.CUD;
import com.mcxiv.app.util.HttpDownloadUtility;
import com.mcxiv.app.util.ThreadUtil;
import com.mcxiv.app.valueobjects.DownloadData;
import com.mcxiv.app.valueobjects.LinkData;
import com.mcxiv.app.views.jarexhud.EventHUD;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.common.network.model.GithubReleaseData;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

import java.io.File;
import java.io.IOException;

public class MediatorDownloadDialog extends Mediator<ViewDownloadDialog> {

    public static final String CLASS_NAME = MediatorDownloadDialog.class.getName();

    private final H2DPluginAdapter plugin;

    public MediatorDownloadDialog(H2DPluginAdapter _plugin, boolean createPanel) {
        super(CLASS_NAME, new ViewDownloadDialog(createPanel));
        this.plugin = _plugin;
    }

    @Override
    public String[] listNotificationInterests() {
        return EventDownloader.getList();
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);

        EventDownloader event = EventDownloader.getEventNamed(notification.getName());

        switch (event) {

            case CHECK_FOR_UPDATES_ACTION:
                ThreadUtil.launch(() -> checkForUpdates(notification.getBody()));
                break;

            case DOWNLOAD_APPLICATION_JAR_REQUEST:
                ThreadUtil.launch(() -> downloadJarAction(notification.getBody()));
                plugin.facade.sendNotification(MsgAPI.SAVE_EDITOR_CONFIG); // TODO: Is it required?
                break;

        }
    }

    private void checkForUpdates(LinkData link) {

        viewComponent.show(plugin.getAPI().getUIStage());
        viewComponent.setMessage("Checking for updates ...");
        viewComponent.setProgress(0);

        DownloadData data = new DownloadData(link.getLink(), plugin.getAPI().getCacheDir());

        new File(data.getCachePath()).mkdirs();
        try {
            File target = new File(data.getJarPath());
            if (!target.isFile()) target.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Json json = new Json();
            json.setIgnoreUnknownFields(true);
            data.setData(json.fromJson(GithubReleaseData.class, HttpDownloadUtility.downloadToString(link.getLink())));

            if (data.getData() == null) return;

            // If:
            // * file doesn't exists at all! ie, application not yet installed
            // then ask user to download it.
            if (!new File(data.getJarPath()).exists()) {
                VisDialog dialog = Dialogs.showOKDialog(plugin.getAPI().getUIStage(),
                        data.getPluginName() + " not yet installed!",
                        "Would you like to download the latest release? " + data.getData().tag_name);
                dialog.padBottom(20).pack();
                dialog.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        facade.sendNotification(EventDownloader.DOWNLOAD_APPLICATION_JAR_REQUEST.getName(), data);
                    }
                });
                return;
            }

            // If:
            // * plugin inbuilt record for any latest version, had it been there is null, ie, has never been downloaded before
            // * and if it exists, it's not equal to latest version
            // then ask user if they want it installed.
            Object currentVersion = plugin.getStorage().get(data.getLatestUpdateKey());
            if (currentVersion == null ||
                    !currentVersion.equals(data.getData().tag_name)
            ) {

                Dialogs.showConfirmDialog(plugin.getAPI().getUIStage(),
                        "New update found!", "A new version of " + data.getPluginName() + " has been found, would you like to download it? \nInstalled:" + currentVersion + "  Available:" + data.getData().tag_name,
                        new String[]{"Later...", "Now!!!"}, new Integer[]{0, 1}, r -> {
                            if (r == 1) {
                                plugin.facade.sendNotification(EventDownloader.DOWNLOAD_APPLICATION_JAR_REQUEST.getName(), data);
                            } else {
                                viewComponent.progressComplete();
                                facade.sendNotification(EventHUD.OPEN_APPLICATION_ACTION_NO_CHECK.getName(), link);
                            }
                        }).padBottom(20).pack();

            } else {
                viewComponent.progressComplete();
                facade.sendNotification(EventHUD.OPEN_APPLICATION_ACTION_NO_CHECK.getName(), link);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void downloadJarAction(DownloadData data) {
        viewComponent.setMessage("Downloading " + data.getPluginName() + " ...");

        for (GithubReleaseData.GithubReleaseAssetData assetData : data.getData().assets) {

            if (assetData.name.equals(data.getPluginName()) || CUD.perhapsEqual(assetData.name, (data.getPluginName() + ".jar").split("[^a-zA-Z]"))) {

                try {
                    HttpDownloadUtility.downloadFile(assetData.browser_download_url, data.getJarPath(), viewComponent);

                    plugin.getStorage().put(data.getLatestUpdateKey(), data.getData().tag_name);

                    facade.sendNotification(EventHUD.OPEN_APPLICATION_ACTION_NO_CHECK.getName(), new LinkData(data.getLink(), false));
                } catch (IOException e) {
                    viewComponent.progressFailed();
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
