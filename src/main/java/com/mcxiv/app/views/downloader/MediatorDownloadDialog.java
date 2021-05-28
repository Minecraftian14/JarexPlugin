package com.mcxiv.app.views.downloader;

import com.mcxiv.app.JarexPlugin;
import com.mcxiv.app.ui.DialogMan;
import com.mcxiv.app.ui.DialogMan.ButtonMeta;
import com.mcxiv.app.ui.IDENTITY;
import com.mcxiv.app.util.CUD;
import com.mcxiv.app.util.GithubUtil;
import com.mcxiv.app.util.HttpDownloadUtility;
import com.mcxiv.app.util.ThreadUtil;
import com.mcxiv.app.valueobjects.DownloadData;
import com.mcxiv.app.valueobjects.LinkData;
import com.mcxiv.app.views.jarexhud.EventHUD;
import com.mcxiv.app.views.settings.EventSettings;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.common.network.model.GithubReleaseData;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

import java.io.File;
import java.io.IOException;

public class MediatorDownloadDialog extends Mediator<ViewDownloadDialog> {

    public static final String CLASS_NAME = MediatorDownloadDialog.class.getName();

    public MediatorDownloadDialog(boolean createPanel) {
        super(CLASS_NAME, new ViewDownloadDialog(createPanel));
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
                ThreadUtil.launch(() -> downloadJarRequestProcessor(notification.getBody()));
                JarexPlugin.plugin.facade.sendNotification(MsgAPI.SAVE_EDITOR_CONFIG); // TODO: Is it required?
                break;

            case OPEN_JAR_CHOOSER_MENU:
                openJarChooserMenu(notification.getBody());

        }
    }

    private void checkForUpdates(LinkData link) {

        final H2DPluginAdapter plugin = JarexPlugin.plugin;

        viewComponent.show(plugin.getAPI().getUIStage());
        viewComponent.setMessage("Checking for updates ...");
        viewComponent.setProgress(0);

        DownloadData data = new DownloadData(link, plugin.getAPI().getCacheDir());

        // Release Data == null === Release/Repo not found.
        if (data.getReleaseData() == null) {
            DialogMan.showOkDialog("Oops!", "There was some issues getting the release\n" +
                    "data for " + GithubUtil.authorAndRepo(link.getLink()) + ".");
            return;
        }

        // To check if the application file has been downloaded even once!
        // The only difference being the message shown to the user.
        File jarFile = new File(data.getJarPath());
        if (!jarFile.isFile() || !jarFile.exists()) {

            new File(data.getCachePath()).mkdirs();
            CUD.Try(jarFile::createNewFile);

            DialogMan.showOkDialog(
                    data.getPluginName() + " has not been downloaded even once!",
                    "Would you like to download the latest release? " + data.getReleaseData().tag_name,
                    () -> facade.sendNotification(EventDownloader.DOWNLOAD_APPLICATION_JAR_REQUEST.getName(), data));
            return;
        }
        // else, a "version" is already downloaded.


        // If:
        // * There is no data in LinkData available for version, ie, version equals "null" in text
        // * Or if there is a version stored, but it is different from the latest release.
        // then ask user if they want it installed, or continue with the current one.
        String currentVersion = data.getLink().getVersion();

        if (currentVersion.equals("null") || !currentVersion.equals(data.getReleaseData().tag_name)) {

            DialogMan.showChoiceDialog(
                    "New update found!",
                    String.format("A new version for %s has been found!\nWould you like to download it now?\nCurrent: %s\nLatest: %s", data.getPluginName(), currentVersion, data.getReleaseData().tag_name),
                    new ButtonMeta("Maybe Later...", () -> facade.sendNotification(EventHUD.OPEN_APPLICATION_ACTION_NO_CHECK.getName(), link)),
                    new ButtonMeta("Yeah Sure!!!", () -> facade.sendNotification(EventDownloader.DOWNLOAD_APPLICATION_JAR_REQUEST.getName(), data))
            );

        } else {
            viewComponent.progressComplete();
            facade.sendNotification(EventHUD.OPEN_APPLICATION_ACTION_NO_CHECK.getName(), link);
        }

    }

    private void downloadJarRequestProcessor(DownloadData data) {

        viewComponent.setMessage("Downloading " + data.getPluginName() + "...");
        boolean flagWasJarfileFound = false;

        for (var assetData : data.getReleaseData().assets) {

            if (assetData.name.endsWith(".jar")) flagWasJarfileFound = true;

            if (assetData.name.equals(data.getPluginName()) || CUD.perhapsEqual(assetData.name, (data.getPluginName() + ".jar").split("[^a-zA-Z]"))) {

                CUD.Try(() -> downloadJarAction(data, assetData));

                return;
            }
        }

        if (!flagWasJarfileFound) {
            DialogMan.showOkDialog("No jar file found!", "Ask the author, " + GithubUtil.author(data.getLink().getLink()) + " to release a compiled jar file in the assets!");
            viewComponent.progressFailed();
            return;
        }

        // If downloaded successfully, method will return that instant only.
        // So reaching this point itself means that download was unsuccessful.

        DialogMan.showChoiceDialog("Application file not found!",
                "Jarex was not able to identify the jar file to download\n" +
                        "Would you like to try choosing the file yourself?",
                new ButtonMeta("Let's See...", () -> facade.sendNotification(EventDownloader.OPEN_JAR_CHOOSER_MENU.getName(), data)),
                new ButtonMeta("Nah, Leave It..", IDENTITY.doNothing)
        );
    }

    private void openJarChooserMenu(DownloadData data) {

        ButtonMeta[] metas = new ButtonMeta[data.getReleaseData().assets.size];

        int idx = 0;
        for (GithubReleaseData.GithubReleaseAssetData asset : data.getReleaseData().assets)
            if (asset.name.endsWith(".jar"))
                metas[idx++] = new ButtonMeta(asset.name, () -> CUD.Try(() -> downloadJarAction(data, asset)));

        DialogMan.showChoiceDialog("Choose The Jar File:", "", metas);

    }

    private void downloadJarAction(DownloadData data, GithubReleaseData.GithubReleaseAssetData assetData) throws IOException {

        HttpDownloadUtility.downloadFile(assetData.browser_download_url, data.getJarPath(), viewComponent);

        facade.sendNotification(EventSettings.REMOVE_ROW_ELEMENT.getName(), data.getLink());
        LinkData link = new LinkData(data.getLink().getLink(), data.getReleaseData().tag_name, data.getLink().isAlwaysUpdateCheck());
        facade.sendNotification(EventSettings.ADD_NEW_ROW_ELEMENT.getName(), link);

        facade.sendNotification(EventHUD.OPEN_APPLICATION_ACTION_NO_CHECK.getName(), data.getLink());
    }
}
