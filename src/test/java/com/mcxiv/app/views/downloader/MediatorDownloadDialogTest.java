package com.mcxiv.app.views.downloader;

import com.mcxiv.app.JarexPlugin;
import com.mcxiv.app.PluginTester;
import com.mcxiv.app.util.CUD;
import com.mcxiv.app.util.GithubUtil;
import com.mcxiv.app.util.ThreadUtil;
import com.mcxiv.app.valueobjects.JarexSettingsData;
import com.mcxiv.app.valueobjects.LinkData;
import games.rednblack.h2d.common.vo.EditorConfigVO;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

class MediatorDownloadDialogTest {

    public static void main(String[] args) throws InterruptedException {
        PluginTester.getAPI().setEditorConfigVO(new EditorConfigVO() {{
            new JarexSettingsData() {{
                registeredLinks.add(new LinkData("https://api.github.com/repos/Minecraftian14/Novix/releases/latest", true));
                registeredLinks.add(new LinkData("https://api.github.com/repos/raeleus/skin-composer/releases/latest", true));
            }}.toStorage(this.pluginStorage.computeIfAbsent(JarexPlugin.CLASS_NAME, k -> new HashMap<>()));
        }});

        PluginTester.launchTest(null, MediatorDownloadDialogTest::TestStarter);
    }

    private static void TestStarter() {

        JarexPlugin plugin = new JarexPlugin();
        plugin.debugMode(true);
        PluginTester.setPlugin(plugin);

        MediatorDownloadDialog mediator = plugin.facade.retrieveMediator(MediatorDownloadDialog.CLASS_NAME);
        PluginTester.setTable(mediator.getViewComponent());

//        Test_InvalidLinkData();
//        Test_ValidLinkDataNotDownloadedEvenOnce();
//        Test_ValidLinkOldVersionInstalled();
//        Test_NoSuitableJarFile();
//        Test_NoJarFileFound();
        Test_DoVersionsActuallyGetUpdated();
    }

    private static void Test_InvalidLinkData() {
        JarexPlugin.plugin.facade
                .sendNotification(EventDownloader.CHECK_FOR_UPDATES_ACTION.getName(),
                        new LinkData(GithubUtil.link("NoOne/There"), true));
    }

    private static void Test_ValidLinkDataNotDownloadedEvenOnce() {
        File t = new File("D:\\AppData\\Roaming\\.hyperlap2d\\cache\\Novix.jar");
        t.delete();

//        JarexPlugin.plugin.facade
//                .sendNotification(EventDownloader.CHECK_FOR_UPDATES_ACTION.getName(),
//                        new LinkData(GithubUtil.link("Minecraftian14/Novix"), true));

        JarexPlugin.plugin.facade
                .sendNotification(EventDownloader.CHECK_FOR_UPDATES_ACTION.getName(),
                        new LinkData(GithubUtil.link("lbalazscs/Pixelitor"), true));
    }

    private static void Test_ValidLinkOldVersionInstalled() {
        File t = new File("D:\\AppData\\Roaming\\.hyperlap2d\\cache\\Novix.jar");
        if (!t.exists() || !t.isFile())
            CUD.Try(() -> new FileOutputStream(t).write("OoOF".getBytes(StandardCharsets.UTF_8)));

        JarexPlugin.plugin.facade
                .sendNotification(EventDownloader.CHECK_FOR_UPDATES_ACTION.getName(),
                        new LinkData(GithubUtil.link("Minecraftian14/Novix"), "0.0.1", true));
    }

    /**
     * How it works?
     * Well, the specific repository "pubnub/java" (I dont know what it is)
     * in {@link GithubUtil} will be interpreted as if the name of the
     * application is "java". Therefore, in the release list, {@link CUD}
     * perhapsEqual wont be able to identify the file named "pubnub-gson-5.1.0-all.jar".
     * <p>
     * This way i can check if it's able to "not" identify the "incorrect"
     * files (most cases; i'll add author's name check too if required).
     * <p>
     * Plus also checking if Selection menu works, where user can select the correct jar file.
     */
    private static void Test_NoSuitableJarFile() {
        JarexPlugin.plugin.facade
                .sendNotification(EventDownloader.CHECK_FOR_UPDATES_ACTION.getName(),
                        new LinkData(GithubUtil.link("pubnub/java"), true));
    }

    private static void Test_NoJarFileFound() {
        JarexPlugin.plugin.facade
                .sendNotification(EventDownloader.CHECK_FOR_UPDATES_ACTION.getName(),
                        new LinkData(GithubUtil.link("grpc/grpc"), true));
    }

    private static void Test_DoVersionsActuallyGetUpdated() {

        assert new JarexSettingsData() {{
            fromStorage(JarexPlugin.plugin.getStorage());
        }}.registeredLinks.stream().anyMatch(linkData -> new LinkData(GithubUtil.link("Minecraftian14/Novix"), true).equivalent(linkData));
        System.out.println("Yes MCXIV/Novix exists");

        JarexPlugin.plugin.facade
                .sendNotification(EventDownloader.CHECK_FOR_UPDATES_ACTION.getName(),
                        new LinkData(GithubUtil.link("Minecraftian14/Novix"), "0.0.1", true));

        ThreadUtil.launchAfter(() -> {
            System.out.println("version: " + new JarexSettingsData() {{
                fromStorage(JarexPlugin.plugin.getStorage());
            }}.registeredLinks.stream().filter(linkData -> new LinkData(GithubUtil.link("Minecraftian14/Novix"), true).equivalent(linkData)).findFirst().get().getVersion());
        }, 10);

    }

}