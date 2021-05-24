package com.mcxiv.app.views.downloader;

import com.mcxiv.app.GdxApp;
import com.mcxiv.app.PureMVCUtil;
import com.mcxiv.app.valueobjects.LinkData;

class MediatorDownloadDialogTest {

    public static void main(String[] args) throws InterruptedException {

        GdxApp.test(MediatorDownloadDialogTest::Test);

    }

    private static void Test() {

        var mediator = new MediatorDownloadDialog(PureMVCUtil.plugin, false);

        PureMVCUtil.facade.registerMediator(mediator);
        PureMVCUtil.facade.sendNotification(EventDownloader.CHECK_FOR_UPDATES_ACTION.getName(),
                new LinkData("https://api.github.com/repos/Minecraftian14/Novix/releases/latest", true));

        GdxApp.instance.setTable(mediator.getViewComponent());

    }

}