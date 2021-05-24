package com.mcxiv.app;

import com.mcxiv.app.valueobjects.JarexSettingsData;
import com.mcxiv.app.views.downloader.MediatorDownloadDialog;
import com.mcxiv.app.views.jarexhud.EventHUD;
import com.mcxiv.app.views.jarexhud.MediatorJarexHUD;
import com.mcxiv.app.views.settings.EventSettings;
import com.mcxiv.app.views.settings.MediatorJarexSettings;
import games.rednblack.h2d.common.MenuAPI;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;
import net.mountainblade.modular.annotations.Implementation;

@Implementation(authors = "Minecraftian14", version = "0.1")
public class JarexPlugin extends H2DPluginAdapter {

    public static final String CLASS_NAME = JarexPlugin.class.getName();

    public JarexPlugin() {
        super(CLASS_NAME);
    }

    private final JarexSettingsData settingsData = new JarexSettingsData();

    @Override
    public void initPlugin() {
        facade.registerMediator(new MediatorJarexSettings(this));
        facade.registerMediator(new MediatorJarexHUD(this, true));
        facade.registerMediator(new MediatorDownloadDialog(this, true));

        pluginAPI.addMenuItem(MenuAPI.WINDOW_MENU, "~ Jarex ~", EventHUD.OPEN_JAREX_HUD_ACTION.getName());

        facade.sendNotification(EventSettings.ADD_SETTINGS_MENU_ACTION.getName());
    }

    public JarexSettingsData getSettingsData() {
        return settingsData;
    }

    public JarexSettingsData updateAndGetSettingsData() {
        settingsData.fromStorage(getStorage());
        return settingsData;
    }


}
