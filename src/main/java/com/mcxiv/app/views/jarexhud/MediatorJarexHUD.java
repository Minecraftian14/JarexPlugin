package com.mcxiv.app.views.jarexhud;

import com.mcxiv.app.util.GithubUtil;
import com.mcxiv.app.util.ThreadUtil;
import com.mcxiv.app.valueobjects.LinkData;
import com.mcxiv.app.views.downloader.EventDownloader;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

import java.io.File;
import java.io.IOException;


public class MediatorJarexHUD extends Mediator<ViewJarexHUD> {

    public static final String CLASS_NAME = MediatorJarexHUD.class.getName();

    private final H2DPluginAdapter plugin;

    public MediatorJarexHUD(H2DPluginAdapter _plugin, boolean createPanel) {
        super(CLASS_NAME, new ViewJarexHUD(_plugin, createPanel));
        this.plugin = _plugin;
    }

    @Override
    public String[] listNotificationInterests() {
        return EventHUD.getList();
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);

        EventHUD event = EventHUD.getEventNamed(notification.getName());

        switch (event) {

            case OPEN_JAREX_HUD_ACTION:
                if (viewComponent.isOpen()) return;
                ThreadUtil.launch(viewComponent::launch);
                break;

            case OPEN_APPLICATION_ACTION:
                openApplicationAction(notification.getBody(), false);
                break;

            case OPEN_APPLICATION_ACTION_NO_CHECK:
                openApplicationAction(notification.getBody(), true);
                break;

        }
    }

    private void openApplicationAction(LinkData data, boolean ignoreUpdates) {
        if (!ignoreUpdates && data.isAlwaysUpdateCheck()) {
            facade.sendNotification(EventDownloader.CHECK_FOR_UPDATES_ACTION.getName(), data);
            return;
        }
        ThreadUtil.launch(() -> launchApplication(GithubUtil.applicationJarName(data.getLink())));
    }

    private void launchApplication(String appName) {
        try {
            System.out.println("java -jar " + plugin.getAPI().getCacheDir() + File.separator + appName);
            Runtime.getRuntime().exec("java -jar " + plugin.getAPI().getCacheDir() + File.separator + appName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
