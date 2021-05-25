package com.mcxiv.app.ui;

import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.kotcrab.vis.ui.widget.*;
import com.mcxiv.app.util.EqualityCompatible;
import com.mcxiv.app.util.GithubUtil;
import com.mcxiv.app.valueobjects.LinkData;

import java.util.Objects;

public class RowElement extends VisTable implements EqualityCompatible {

    /**
     * A delimiter to separate github links and auto update setting
     */
    public static final String DELIMITER = "==";

    // index | link | version installed [red:new update available|grn:latest] | auto update | log
    protected final VisLabel lbl_index;
    protected final VisTextField fie_gitLink;
    protected final VisLabel lbl_versionInstalled;
    protected final VisCheckBox cbx_autoUpdate;
    protected final VisImageButton btn_log;

    public RowElement(String gitLink, boolean autoUpdate) {
        this(0, gitLink, autoUpdate);
    }

    public RowElement(int _index, String gitLink, boolean autoUpdate) {

        gitLink = GithubUtil.authorAndRepo(gitLink);

        add(this.lbl_index = new VisLabel("" + (_index + 1)))
                .minWidth(10).padRight(10);
        add(this.fie_gitLink = new VisTextField(gitLink))
                .growX();
        add(this.lbl_versionInstalled = new VisLabel("TODO"))
                .minWidth(70).padLeft(10).padRight(10); // TODO
        add(this.cbx_autoUpdate = new VisCheckBox("", autoUpdate))
                .padRight(10);
        add(this.btn_log = new VisImageButton(new BaseDrawable()))
                .minWidth(20); // TODO
    }

    public RowElement(int index, LinkData linkData) {
        this(index, linkData.getLink(), linkData.isAlwaysUpdateCheck());
    }

    @Override
    public String toString() {
        return String.format("{%s%s%s}", getLink(), DELIMITER, isAlwaysUpdateCheck());
    }

    public static RowElement fromString(String s) {
        if (s.length() < 2) return null;
        s = s.substring(1, s.length() - 1);
        String[] s2 = s.split(DELIMITER);
        return new RowElement(s2[0], Boolean.parseBoolean(s2[1]));
    }

    public String getLink() {
        return GithubUtil.link(fie_gitLink.getText());
    }

    public boolean isAlwaysUpdateCheck() {
        return cbx_autoUpdate.isChecked();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RowElement element = (RowElement) o;
        return Objects.equals(getLink(), element.getLink()) && Objects.equals(isAlwaysUpdateCheck(), element.isAlwaysUpdateCheck());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLink(), isAlwaysUpdateCheck());
    }

    @Override
    public boolean equivalent(EqualityCompatible object) {
        if (equals(object)) return true;
        if (object instanceof LinkData) {
            LinkData data = ((LinkData) object);
            return data.getLink().equals(getLink()) && data.isAlwaysUpdateCheck() == isAlwaysUpdateCheck();
        }
        return false;
    }
}
