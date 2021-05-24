package com.mcxiv.app.views.settings;

import com.mcxiv.app.GdxApp;
import com.mcxiv.app.PureMVCUtil;
import com.mcxiv.app.ui.RowElement;

import java.util.Arrays;

class MediatorJarexSettingsTest {

    public static void main(String[] args) throws InterruptedException {

        GdxApp.test(MediatorJarexSettingsTest::Test);

    }

    private static void Test() {
        MediatorJarexSettings mediator = new MediatorJarexSettings(PureMVCUtil.plugin);

        PureMVCUtil.facade.registerMediator(mediator);
        PureMVCUtil.facade.sendNotification(EventSettings.ADD_SETTINGS_MENU_ACTION.getName());
        mediator.getViewComponent().translateSettingsToView();

        GdxApp.instance.setTable(mediator.getViewComponent().getContentTable());

        Arrays.stream(
                mediator
                        .getViewComponent()
                        .getContentTable()
                        .getChildren().items
        )
                .filter(actor -> actor instanceof RowElement)
                .map(actor -> (RowElement)actor)
                .forEach(element -> System.out.println(element.getLink()));
    }

}