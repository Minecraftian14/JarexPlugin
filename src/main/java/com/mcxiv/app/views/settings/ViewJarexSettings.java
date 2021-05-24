package com.mcxiv.app.views.settings;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.mcxiv.app.ui.RowElement;
import com.mcxiv.app.ui.RowElementEntry;
import com.mcxiv.app.util.CUD;
import com.mcxiv.app.util.GithubUtil;
import com.mcxiv.app.valueobjects.JarexSettingsData;
import com.mcxiv.app.valueobjects.LinkData;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;
import games.rednblack.h2d.common.view.SettingsNodeValue;

public class ViewJarexSettings extends SettingsNodeValue<JarexSettingsData> {

    private H2DPluginAdapter plugin;
    private boolean loaded = false;

    private final VisLabel title;
    private final RowElementEntry entryElement;

    public ViewJarexSettings(H2DPluginAdapter _plugin) {
        super("Jarex", _plugin.facade);
        this.plugin = _plugin;

        title = new VisLabel("Registered Application Links:");

        entryElement = new RowElementEntry(element ->
                plugin.facade.sendNotification(EventSettings.ADD_NEW_ROW_ELEMENT.getName(), element)
        );
    }

    private void createUI() {
        VisTable root = getContentTable();
        root.clearChildren();

        root.add(title).pad(25).row();

        JarexSettingsData settings = getSettings();

        if (settings != null)
            CUD.forEach(settings.registeredLinks, (index, linkData) -> {
                root.add(new RowElement(index, GithubUtil.authorAndRepo(linkData.getLink()), linkData.isAlwaysUpdateCheck()))
                        .padBottom(6).padRight(20).padLeft(20)
                        .growX().row();
            });

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
        JarexSettingsData newSettings = new JarexSettingsData();

        getContentTable().getChildren().forEach(actor -> {
            if (actor instanceof RowElement && !actor.equals(entryElement)) {
                RowElement element = (RowElement) actor;
                newSettings.registeredLinks.add(new LinkData(element.getLink(), element.isAlwaysCheckUpdate()));
            }
        });

        setSettings(newSettings);

        newSettings.toStorage(plugin.getStorage());
        facade.sendNotification(MsgAPI.SAVE_EDITOR_CONFIG);
    }

    /**
     * Are the settings object and the values in UI different?
     */
    @Override
    public boolean validateSettings() {
        if (!loaded) return false;

        JarexSettingsData settings = getSettings();

        for (Actor actor : getContentTable().getChildren()) {

            if (actor instanceof RowElement && !actor.equals(entryElement)) {

                RowElement element = (RowElement) actor;

                boolean flag = true;

                for (LinkData link : settings.registeredLinks)
                    if (link.getLink().equals(element.getLink()) && link.isAlwaysUpdateCheck() == element.isAlwaysCheckUpdate())
                        flag = false;

                return flag;

            }
        }

        return false;
    }

}
