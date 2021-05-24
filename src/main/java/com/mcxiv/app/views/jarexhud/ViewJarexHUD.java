package com.mcxiv.app.views.jarexhud;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.mcxiv.app.util.GithubUtil;
import com.mcxiv.app.valueobjects.JarexSettingsData;
import com.mcxiv.app.valueobjects.LinkData;
import games.rednblack.h2d.common.UIDraggablePanel;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;

public class ViewJarexHUD extends VisTable {

    private final H2DPluginAdapter plugin;
    private UIDraggablePanel parent;
    private final Array<VisTextButton> buttons;

    public ViewJarexHUD(H2DPluginAdapter plugin, boolean createPanel) {
        this.plugin = plugin;

        buttons = new Array<>();

        if (createPanel) {
            parent = new UIDraggablePanel("Jarex HUD");
            parent.addCloseButton();
            parent.setModal(false);

            parent.add(this).fill().expand();
        }
    }

    public void launch() {
        if (weHaveNewValuesToBeUpdated()) {
            clearChildren();
            createUI();
        }
        if (parent != null)
            parent.show(plugin.getAPI().getUIStage());
    }

    public void createUI() {
        JarexSettingsData values = new JarexSettingsData();
        values.fromStorage(plugin.getStorage());

        clear();
        buttons.clear();

        values.registeredLinks.forEach(element -> {
            VisTextButton button = new VisTextButton(GithubUtil.displayName(element.getLink()));
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    plugin.facade.sendNotification(EventHUD.OPEN_APPLICATION_ACTION.getName(), new LinkData(element.getLink(), element.isAlwaysUpdateCheck()));
                    if (parent != null)
                        parent.close();
                }
            });
            buttons.add(button);
        });

        VisLabel label = new VisLabel("Launch Application");

        add(label).pad(10).center().row();

        buttons.forEach(button -> add(button)
                .padBottom(10).padRight(10).padLeft(10)
                .growX().row());

        if (parent != null)
            parent.pack();
    }

    private boolean weHaveNewValuesToBeUpdated() { // TODO
        return true;
    }

    public boolean isOpen() {
        if (parent == null)
            return false;
        return parent.isOpen;
    }
}