package com.mcxiv.app.views.jarexhud;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.mcxiv.app.JarexPlugin;
import com.mcxiv.app.util.GithubUtil;
import com.mcxiv.app.valueobjects.JarexSettingsData;
import com.mcxiv.app.valueobjects.LinkData;
import games.rednblack.h2d.common.UIDraggablePanel;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;

public class ViewJarexHUD extends VisTable {

    private UIDraggablePanel parent;
    private final Array<VisTextButton> buttons;

    public ViewJarexHUD(boolean createPanel) {

        buttons = new Array<>();

        if (createPanel) {
            parent = new UIDraggablePanel("Jarex HUD");
            parent.addCloseButton();
            parent.setModal(false);
            parent.add(this).fill().expand();
        }
    }

    public void launch() {
        createUI();
        if (parent != null)
            parent.show(JarexPlugin.plugin.getAPI().getUIStage());
    }

    public void createUI() {
        clear();
        buttons.clear();

        JarexSettingsData values = new JarexSettingsData();
        values.fromStorage(JarexPlugin.plugin.getStorage());

        values.registeredLinks.forEach(element -> {
            VisTextButton button = new VisTextButton(GithubUtil.displayName(element.getLink()));
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    JarexPlugin.plugin.facade.sendNotification(EventHUD.OPEN_APPLICATION_ACTION.getName(), element);
                    if (parent != null)
                        parent.close();
                }
            });
            buttons.add(button);
        });

        add(new VisLabel("Launch Application"))
                .pad(10).center().row();

        buttons.forEach(button -> add(button)
                .padBottom(10).padRight(10).padLeft(10)
                .growX().row());

        if (parent != null)
            parent.pack();
    }

    public boolean isOpen() {
        if (parent == null)
            return false;
        return parent.isOpen;
    }
}