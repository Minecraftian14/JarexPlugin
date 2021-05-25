package com.mcxiv.app.views.settings;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mcxiv.app.ui.RowElement;
import com.mcxiv.app.valueobjects.JarexSettingsData;
import com.mcxiv.app.valueobjects.LinkData;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

public class MediatorJarexSettings extends Mediator<ViewJarexSettings> {

    public static final String CLASS_NAME = MediatorJarexSettings.class.getName();

    private final H2DPluginAdapter plugin;

    public MediatorJarexSettings(H2DPluginAdapter _plugin) {
        super(CLASS_NAME, new ViewJarexSettings(_plugin));
        plugin = _plugin;
    }

    @Override
    public String[] listNotificationInterests() {
        return EventSettings.getList();
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);

        EventSettings event = EventSettings.getEventNamed(notification.getName());

        switch (event) {

            case ADD_SETTINGS_MENU_ACTION:
                var settingsData = new JarexSettingsData();
                settingsData.fromStorage(plugin.getStorage());

                // Setting the settings in viewComponent
                viewComponent.setSettings(settingsData);

                RowElement.ButtonRemoveAction.setButtonRemoveAction(element -> facade.sendNotification(EventSettings.REMOVE_ROW_ELEMENT.getName(), element));

                facade.sendNotification(MsgAPI.ADD_PLUGIN_SETTINGS, viewComponent);
                break;

            case ADD_NEW_ROW_ELEMENT:
                RowElement element = notification.getBody();
                viewComponent.getSettings().registeredLinks.add(new LinkData(element.getLink(), element.isAlwaysUpdateCheck()));
                viewComponent.translateSettingsToView();
                break;

            case REMOVE_ROW_ELEMENT:
                RowElement elementTBR = notification.getBody();
                System.out.println(
                        viewComponent.getSettings().registeredLinks.removeValue(new LinkData(elementTBR.getLink(), elementTBR.isAlwaysUpdateCheck()), false)
                );
                viewComponent.translateSettingsToView();
                break;

        }

    }

}
