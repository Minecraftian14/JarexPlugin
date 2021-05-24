package com.mcxiv.app.views.jarexhud;

import com.mcxiv.app.GdxApp;
import com.mcxiv.app.PureMVCUtil;

class MediatorJarexHUDTest {

    static class HudLaunchTest {

        public static void main(String[] args) throws InterruptedException {

            GdxApp.test(HudLaunchTest::Test);

        }

        private static void Test() {

            var mediator = new MediatorJarexHUD(PureMVCUtil.plugin, false);

            PureMVCUtil.facade.registerMediator(mediator);
            PureMVCUtil.facade.sendNotification(EventHUD.OPEN_JAREX_HUD_ACTION.getName());

            GdxApp.instance.setTable(mediator.getViewComponent());

        }
    }

    static class ApplicationLaunchTest {

        public static void main(String[] args) throws InterruptedException {

            GdxApp.test(ApplicationLaunchTest::Test);

        }

        private static void Test() {

            var mediator = new MediatorJarexHUD(PureMVCUtil.plugin, false);

            PureMVCUtil.facade.registerMediator(mediator);
            PureMVCUtil.facade.sendNotification(EventHUD.OPEN_JAREX_HUD_ACTION.getName());

            GdxApp.instance.setTable(mediator.getViewComponent());

        }
    }

}