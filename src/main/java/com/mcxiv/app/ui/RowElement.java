package com.mcxiv.app.ui;

import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.kotcrab.vis.ui.widget.*;

public class RowElement extends VisTable {

    /**
     * A delimiter to separate github links and auto update setting
     */
    public static final String DELIMITER = "==";

    protected static int counter = 0;

    protected final int index;

    // index | link | version installed [red:new update available|grn:latest] | auto update | log
    protected final VisLabel lbl_index;
    protected final VisTextField fie_gitLink;
    protected final VisLabel lbl_versionInstalled;
    protected final VisCheckBox cbx_autoUpdate;
    protected final VisImageButton btn_log;

    public RowElement(String gitLink, boolean autoUpdate) {
        index = counter;

        add(this.lbl_index = new VisLabel("" + (index + 1)))
                .padRight(10);
        add(this.fie_gitLink = new VisTextField(gitLink))
                .growX();
        add(this.lbl_versionInstalled = new VisLabel("TODO"))
                .minWidth(70).padLeft(10).padRight(10); // TODO
        add(this.cbx_autoUpdate = new VisCheckBox("", autoUpdate))
                .padRight(10);
        add(this.btn_log = new VisImageButton(new BaseDrawable()))
                .minWidth(20); // TODO

        counter++;

    }

    @Override
    public String toString() {
        return String.format("{%s%s%s}", getLink(), DELIMITER, isAlwaysCheckUpdate());
    }

    public static RowElement fromString(String s) {
        if (s.length() < 2) return null;
        s = s.substring(1, s.length() - 1);
        String[] s2 = s.split(DELIMITER);
        return new RowElement(s2[0], Boolean.parseBoolean(s2[1]));
    }

    public int getIndex() {
        return index;
    }

    public String getLink() {
        return fie_gitLink.getText();
    }

    public boolean isAlwaysCheckUpdate() {
        return cbx_autoUpdate.isChecked();
    }
}
