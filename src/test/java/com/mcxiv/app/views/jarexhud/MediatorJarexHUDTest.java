package com.mcxiv.app.views.jarexhud;

import com.mcxiv.app.JarexPlugin;
import com.mcxiv.app.PluginTester;
import com.mcxiv.app.valueobjects.JarexSettingsData;
import com.mcxiv.app.valueobjects.LinkData;
import games.rednblack.h2d.common.vo.EditorConfigVO;
import org.puremvc.java.interfaces.IFacade;

import java.util.HashMap;

class MediatorJarexHUDTest {

    static class HudLaunchTest {

        public static void main(String[] args) throws InterruptedException {
            PluginTester.getAPI().setEditorConfigVO(new EditorConfigVO() {{
                new JarexSettingsData() {{
                    registeredLinks.add(
                            new LinkData("https://api.github.com/repos/Minecraftian14/Novix/releases/latest", true),
                            new LinkData("https://api.github.com/repos/raeleus/skin-composer/releases/latest", true)
                    );
                }}.toStorage(this.pluginStorage.computeIfAbsent(JarexPlugin.CLASS_NAME, k -> new HashMap<>()));
            }});

            PluginTester.launchTest(null, HudLaunchTest::Test);

        }

        private static void Test() {

            JarexPlugin plugin = new JarexPlugin();
            plugin.debugMode(true);

            PluginTester.setPlugin(plugin);

            MediatorJarexHUD mediator = plugin.facade.retrieveMediator(MediatorJarexHUD.CLASS_NAME);

            plugin.facade.sendNotification(EventHUD.OPEN_JAREX_HUD_ACTION.getName());

            PluginTester.setTable(mediator.getViewComponent());

        }
    }

    static class ApplicationLaunchTest {

        public static void main(String[] args) throws InterruptedException {

            PluginTester.getAPI().setEditorConfigVO(new EditorConfigVO() {{
                new JarexSettingsData() {{
                    registeredLinks.add(
                            new LinkData("https://api.github.com/repos/Minecraftian14/Novix/releases/latest", true),
                            new LinkData("https://api.github.com/repos/raelus/skin-composer/releases/latest", true)
                    );
                }}.toStorage(this.pluginStorage.computeIfAbsent(JarexPlugin.CLASS_NAME, k -> new HashMap<>()));
            }});

            PluginTester.launchTest(null, ApplicationLaunchTest::Test);

        }

        private static void Test() {

            JarexPlugin plugin = new JarexPlugin();
            plugin.debugMode(true);

            MediatorJarexHUD mediator = plugin.facade.retrieveMediator(MediatorJarexHUD.CLASS_NAME);

            plugin.facade.sendNotification(EventHUD.OPEN_JAREX_HUD_ACTION.getName());

            PluginTester.setTable(mediator.getViewComponent());

        }
    }

}