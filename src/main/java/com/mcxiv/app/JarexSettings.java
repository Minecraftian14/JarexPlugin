package com.mcxiv.app;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.mcxiv.app.ui.RowElement;
import com.mcxiv.app.ui.RowElementEntry;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.common.view.SettingsNodeValue;
import org.puremvc.java.interfaces.IFacade;

public class JarexSettings extends SettingsNodeValue<JarexVO> {

    private final JarexPlugin plugin;
    private boolean loaded = false;

    private final VisLabel title;
    private final RowElementEntry entryElement;

    public JarexSettings(IFacade facade, JarexPlugin plugin) {
        super("Jarex", facade);
        this.plugin = plugin;

        title = new VisLabel("Registered Application Links:");

        entryElement = new RowElementEntry(ele -> {
            getSettings().registeredLinks.add(ele);
            createUI();
        });

        createUI();
    }

    private void createUI() {
        VisTable root = getContentTable();
        root.clearChildren();

        root.add(title).pad(25).row();

        if (getSettings() != null)
            getSettings().registeredLinks.forEach(element -> root.add(element)
                    .padBottom(6).padRight(20).padLeft(20)
                    .growX().row());

        root.add(entryElement)
                .padBottom(6).padRight(20).padLeft(20)
                .growX().row();


    }

    @Override
    public void translateSettingsToView() {
        loaded = true;
        createUI();
    }

    @Override
    public void translateViewToSettings() {
        JarexVO newSettings = new JarexVO();

        getContentTable().getChildren().forEach(actor -> {
            if (actor instanceof RowElement && !actor.equals(entryElement)) {
                newSettings.registeredLinks.add((RowElement) actor);
                System.out.println(actor.getClass().getName());
            }
        });

        setSettings(newSettings);

        newSettings.toStorage(plugin.getStorage());
        facade.sendNotification(MsgAPI.SAVE_EDITOR_CONFIG);
    }

    @Override
    public boolean validateSettings() {
        if (!loaded) return false;

        JarexVO settings = getSettings();

        for (Actor actor : getContentTable().getChildren()) {
            if (actor instanceof RowElement && !actor.equals(entryElement)) {
                try {
                    RowElement element = (RowElement) actor;
                    RowElement optional = settings.registeredLinks.get(element.getIndex());
                    if (!element.equals(optional)) return true;
                } catch (IndexOutOfBoundsException e) {
                    return true;
                }
            }
        }

        return false;
    }
}
