package com.mcxiv.app.views.jarexhud;

import com.mcxiv.app.JarexPlugin;
import com.mcxiv.app.util.CUD;
import com.mcxiv.app.util.GithubUtil;
import com.mcxiv.app.util.ThreadUtil;
import com.mcxiv.app.valueobjects.LinkData;
import com.mcxiv.app.views.downloader.EventDownloader;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;


public class MediatorJarexHUD extends Mediator<ViewJarexHUD> {

    public static final String CLASS_NAME = MediatorJarexHUD.class.getName();

    public MediatorJarexHUD(boolean createPanel) {
        super(CLASS_NAME, new ViewJarexHUD(createPanel));
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
            CUD.event(EventDownloader.CHECK_FOR_UPDATES_ACTION.getName(), data);
            return;
        }
        ThreadUtil.launch(() -> launchApplication(data));
    }

    private void launchApplication(LinkData data) {
        try {
            String appName = GithubUtil.applicationJarName(data.getLink());

            String command = String.format("java -jar \"%s%s%s\"", JarexPlugin.plugin.getAPI().getCacheDir(), File.separator, appName);

            System.out.println("Command Ran: " + command);
            Process process = Runtime.getRuntime().exec(command);

            ThreadUtil.launchForOnly(() -> ThreadUtil.readStream(process.getInputStream()), 20);
            ThreadUtil.launchForOnly(() -> ThreadUtil.readStream(process.getErrorStream()), 5);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
