package com.mcxiv.app.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.function.Consumer;

public class RowElementEntry extends RowElement {

    public RowElementEntry(Consumer<RowElement> addNewElementAction) {
        super(0,"Enter New URL", true);

        lbl_index.setText(">");
        lbl_versionInstalled.setText("");
//        btn_log.removeListener(); // TODO
        btn_log.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addNewElementAction.accept(prepare());
            }
        });

    }

    private RowElement prepare() {
        return new RowElement(getLink(), isAlwaysCheckUpdate());
    }

}
