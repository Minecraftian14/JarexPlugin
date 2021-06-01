package com.mcxiv.app;

import com.mcxiv.app.util.CUD;
import com.mcxiv.app.views.downloader.MediatorDownloadDialog;
import com.mcxiv.app.views.jarexhud.EventHUD;
import com.mcxiv.app.views.jarexhud.MediatorJarexHUD;
import com.mcxiv.app.views.settings.EventSettings;
import com.mcxiv.app.views.settings.MediatorJarexSettings;
import games.rednblack.h2d.common.MenuAPI;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;
import net.mountainblade.modular.annotations.Implementation;
import org.puremvc.java.interfaces.IFacade;

@Implementation(authors = "Minecraftian14", version = "0.1")
public class JarexPlugin extends H2DPluginAdapter {

    public static final String CLASS_NAME = JarexPlugin.class.getName();
    public static H2DPluginAdapter plugin;

    private boolean debugMode = false;

    public JarexPlugin() {
        super(CLASS_NAME);
        plugin = this;
    }

    public void debugMode(boolean b) {
        debugMode = b;
    }

    @Override
    public void initPlugin() {
        facade.registerMediator(new MediatorJarexSettings());
        facade.registerMediator(new MediatorJarexHUD(!debugMode));
        facade.registerMediator(new MediatorDownloadDialog(!debugMode));

        pluginAPI.addMenuItem(MenuAPI.WINDOW_MENU, "~ Jarex ~", EventHUD.OPEN_JAREX_HUD_ACTION.getName());

        CUD.event(EventSettings.ADD_SETTINGS_MENU_ACTION.getName());
    }

}
