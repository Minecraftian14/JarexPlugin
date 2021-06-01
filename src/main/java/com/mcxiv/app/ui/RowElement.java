package com.mcxiv.app.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.*;
import com.mcxiv.app.JarexPlugin;
import com.mcxiv.app.util.CUD;
import com.mcxiv.app.util.EqualityCompatible;
import com.mcxiv.app.util.GithubUtil;
import com.mcxiv.app.valueobjects.LinkData;
import com.mcxiv.app.views.settings.EventSettings;

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
    protected final VisImageButton btn_action;

    public RowElement(LinkData link) {
        this(0, link);
    }

    public RowElement(int _index, LinkData link) {

        add(this.lbl_index = new VisLabel("" + (_index + 1)))
                .minWidth(10).padRight(10);
        add(this.fie_gitLink = new VisTextField(GithubUtil.authorAndRepo(link.getLink())))
                .growX();
        add(this.lbl_versionInstalled = new VisLabel(link.getVersion()))
                .minWidth(70).padLeft(10).padRight(10);
        add(this.cbx_autoUpdate = new VisCheckBox("", link.isAlwaysUpdateCheck()))
                .padRight(10);
        add(this.btn_action = new VisImageButton(new BaseDrawable()))
                .minWidth(20);

        btn_action.addListener(new ButtonRemoveAction(this));
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
        return Objects.equals(getLink(), element.getLink());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLink());
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

    public static class ButtonRemoveAction extends ChangeListener {

        private final RowElement element;

        public ButtonRemoveAction(RowElement element) {
            this.element = element;
        }

        @Override
        public void changed(ChangeEvent event, Actor actor) {
            CUD.event(EventSettings.REMOVE_ELEMENT.getName(), new LinkData(element.getLink(), element.isAlwaysUpdateCheck()));
        }
    }


}
