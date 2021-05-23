package com.mcxiv.app;

import games.rednblack.h2d.common.MenuAPI;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;
import net.mountainblade.modular.annotations.Implementation;

@Implementation(authors = "Minecraftian14", version = "0.1")
public class JarexPlugin extends H2DPluginAdapter {

    public static final String CLASS_NAME = JarexPlugin.class.getName();

    private final JarexMediator mediator;
    private JarexVO vo = new JarexVO();

    public JarexPlugin() {
        super(CLASS_NAME);

        mediator = new JarexMediator(this);
    }

    @Override
    public void initPlugin() {
        facade.registerMediator(mediator);
        pluginAPI.addMenuItem(MenuAPI.WINDOW_MENU, "Jarex", Event.OPEN_JAREX_HUD_ACTION.name);
        JarexSettings settings = new JarexSettings(facade, this);

        vo.fromStorage(getStorage());
        settings.setSettings(vo);
        facade.sendNotification(MsgAPI.ADD_PLUGIN_SETTINGS, settings);
    }

    public JarexVO getSettingsVO() {
        return vo;
    }
}
