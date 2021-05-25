package com.mcxiv.app.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.function.Consumer;

public class RowElementEntry extends RowElement {

    public RowElementEntry(Consumer<RowElement> addNewElementAction) {
        super(0,"", true);

        lbl_index.setText(">");
        lbl_versionInstalled.setText("");
        fie_gitLink.setText("Enter New URL");

        for (EventListener listener : btn_log.getListeners())
            if (listener instanceof ButtonRemoveAction)
                btn_log.removeListener(listener);

        btn_log.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addNewElementAction.accept(prepare());
            }
        });

    }

    private RowElement prepare() {
        RowElement element = new RowElement(getLink(), isAlwaysUpdateCheck());
        fie_gitLink.setText("Enter New URL");
        cbx_autoUpdate.setChecked(true);
        return element;
    }

}
