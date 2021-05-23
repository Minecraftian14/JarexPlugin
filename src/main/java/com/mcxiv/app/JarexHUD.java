package com.mcxiv.app;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.mcxiv.app.util.GithubUtil;
import games.rednblack.h2d.common.UIDraggablePanel;

public class JarexHUD extends UIDraggablePanel {

    private final Array<VisTextButton> buttons;

    private final JarexPlugin plugin;

    public JarexHUD(JarexPlugin _plugin) {
        super("Jarex Plugin");

        addCloseButton();

        plugin = _plugin;

        JarexVO values = plugin.getSettingsVO();

        buttons = new Array<>(values.registeredLinks.size());
        values.registeredLinks.forEach(element -> {
            VisTextButton button = new VisTextButton(GithubUtil.displayName(element.getLink()));
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    plugin.facade.sendNotification(Event.OPEN_APPLICATION_ACTION.name, element);
                    JarexHUD.this.close();
                }
            });
            buttons.add(button);
        });

        VisLabel label = new VisLabel("Launch Application");

//      setMovable(false);
        setModal(false);

        setHeight(Math.max(label.getPrefHeight() + new VisTextButton("").getPrefHeight() * buttons.size,200));
        setWidth(250);

        VisTable mainTable = new VisTable();
        add(mainTable).fill().expand();
        mainTable.add(label).pad(10).center().row();

        buttons.forEach(button -> mainTable.add(button)
                .padBottom(10).padRight(10).padLeft(10)
                .growX().row());

        pack();
    }

    public void launch() {
        show(plugin.getAPI().getUIStage());
    }

}
