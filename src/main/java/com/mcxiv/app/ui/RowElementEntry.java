package com.mcxiv.app.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mcxiv.app.JarexPlugin;
import com.mcxiv.app.valueobjects.LinkData;
import com.mcxiv.app.views.settings.EventSettings;

public class RowElementEntry extends RowElement {

    public RowElementEntry() {
        super(0, IDENTITY.link);

        lbl_index.setText(">");
        lbl_versionInstalled.setText("");
        fie_gitLink.setText("Enter New URL");

        for (EventListener listener : btn_action.getListeners())
            if (listener instanceof ButtonRemoveAction)
                btn_action.removeListener(listener);

        btn_action.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                RowElement element = new RowElement(new LinkData(getLink(), isAlwaysUpdateCheck()));
                fie_gitLink.setText("Enter New URL");
                cbx_autoUpdate.setChecked(true);
                JarexPlugin.plugin.facade
                        .sendNotification(EventSettings.ADD_NEW_ROW_ELEMENT.getName(), element);

            }
        });


    }

}
