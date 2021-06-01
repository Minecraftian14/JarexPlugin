package com.mcxiv.app.views.settings;

import com.mcxiv.app.JarexPlugin;
import com.mcxiv.app.PluginTester;
import com.mcxiv.app.ui.RowElement;
import com.mcxiv.app.ui.RowElementEntry;
import com.mcxiv.app.valueobjects.JarexSettingsData;
import com.mcxiv.app.valueobjects.LinkData;
import games.rednblack.h2d.common.vo.EditorConfigVO;

import java.util.Arrays;
import java.util.HashMap;

class MediatorJarexSettingsTest {

    public static void main(String[] args) throws InterruptedException {
        PluginTester.getAPI().setEditorConfigVO(new EditorConfigVO() {{
            new JarexSettingsData() {{
                registeredLinks.add(new LinkData("https://api.github.com/repos/Minecraftian14/Novix/releases/latest", true));
                registeredLinks.add(new LinkData("https://api.github.com/repos/raeleus/skin-composer/releases/latest", true));
            }}.toStorage(this.pluginStorage.computeIfAbsent(JarexPlugin.CLASS_NAME, k -> new HashMap<>()));
        }});

        PluginTester.launchTest(null, MediatorJarexSettingsTest::Test);
    }

    private static void Test() {

        JarexPlugin plugin = new JarexPlugin();

        plugin.debugMode(true);

        PluginTester.setPlugin(plugin);

        MediatorJarexSettings mediator = plugin.facade.retrieveMediator(MediatorJarexSettings.CLASS_NAME);

        mediator.getViewComponent().translateSettingsToView();
        PluginTester.setTable(mediator.getViewComponent().getContentTable());

        Arrays.stream(
                mediator
                        .getViewComponent()
                        .getContentTable()
                        .getChildren().items
        )
                .filter(actor -> actor instanceof RowElement)
                .filter(actor -> !(actor instanceof RowElementEntry))
                .map(actor -> (RowElement) actor)
                .forEach(element -> System.out.println("MediatorJarexSettingsTest> " + element.getLink()));
    }

}