package com.mcxiv.app.views.settings;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.mcxiv.app.JarexPlugin;
import com.mcxiv.app.ui.RowElement;
import com.mcxiv.app.ui.RowElementEntry;
import com.mcxiv.app.util.CUD;
import com.mcxiv.app.valueobjects.JarexSettingsData;
import com.mcxiv.app.valueobjects.LinkData;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.common.plugins.H2DPluginAdapter;
import games.rednblack.h2d.common.view.SettingsNodeValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ViewJarexSettings extends SettingsNodeValue<JarexSettingsData> {

    private boolean loaded = false;

    private final VisLabel title;
    private final RowElementEntry entryElement;

    private JarexSettingsData localSettings = new JarexSettingsData();

    public ViewJarexSettings() {
        super("Jarex", JarexPlugin.plugin.facade);
        title = new VisLabel("Registered Application Links:");
        entryElement = new RowElementEntry();
    }

    @Override
    public void setSettings(JarexSettingsData settings) {
        localSettings.fromSettings(settings);
    }

    @Override
    public JarexSettingsData getSettings() {
        return localSettings;
    }

    private void createUI() {
        VisTable root = getContentTable();
        root.clearChildren();

        root.add(title).pad(25).row();

        if (localSettings != null)
            CUD.forEach(localSettings.registeredLinks, (index, linkData) -> {
                root.add(new RowElement(index, linkData))
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
        getSettings().toStorage(JarexPlugin.plugin.getStorage());
        CUD.event(MsgAPI.SAVE_EDITOR_CONFIG);
    }

    /**
     * Are the settings object and the values in UI different?
     */
    @Override
    public boolean validateSettings() {
        if (!loaded) return false;

        var originalSettings = new JarexSettingsData();
        originalSettings.fromStorage(JarexPlugin.plugin.getStorage());

        List<RowElement> rowElements = Arrays.stream(getContentTable().getChildren().items)
                .filter(actor -> actor instanceof RowElement && actor != entryElement)
                .map(actor -> (RowElement) actor).collect(Collectors.toList());

        // If there's any RowElement whose data is not available in
        // originalSettings, then we need to translate View To Setting.
        // For that, this method should return true.


        return !CUD.equalsList(
                rowElements,
                originalSettings.registeredLinks
        );
    }
}
