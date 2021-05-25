package com.mcxiv.app.views.downloader;

import com.mcxiv.app.JarexPlugin;
import com.mcxiv.app.PluginTester;
import com.mcxiv.app.valueobjects.JarexSettingsData;
import com.mcxiv.app.valueobjects.LinkData;
import games.rednblack.h2d.common.vo.EditorConfigVO;

import java.util.HashMap;

class MediatorDownloadDialogTest {

    public static void main(String[] args) throws InterruptedException {
        PluginTester.getAPI().setEditorConfigVO(new EditorConfigVO() {{
            new JarexSettingsData() {{
                registeredLinks.add(
                        new LinkData("https://api.github.com/repos/Minecraftian14/Novix/releases/latest", true),
                        new LinkData("https://api.github.com/repos/raeleus/skin-composer/releases/latest", true)
                );
            }}.toStorage(this.pluginStorage.computeIfAbsent(JarexPlugin.CLASS_NAME, k -> new HashMap<>()));
        }});

        PluginTester.launchTest(null, MediatorDownloadDialogTest::Test);

    }

    private static void Test() {

        JarexPlugin plugin = new JarexPlugin();
        plugin.debugMode(true);
        PluginTester.setPlugin(plugin);

        plugin.facade.sendNotification(EventDownloader.CHECK_FOR_UPDATES_ACTION.getName(),
                new LinkData("https://api.github.com/repos/Minecraftian14/Novix/releases/latest", true));

        MediatorDownloadDialog mediator = plugin.facade.retrieveMediator(MediatorDownloadDialog.CLASS_NAME);

        PluginTester.setTable(mediator.getViewComponent());

    }

}