package com.mcxiv.app.ui;

import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.mcxiv.app.JarexPlugin;
import com.mcxiv.app.util.CUD;
import com.mcxiv.app.views.downloader.EventDownloader;
import com.mcxiv.app.views.jarexhud.EventHUD;

import java.util.concurrent.CompletableFuture;

public class DialogMan {

    public static void showOkDialog(String title, String text, Runnable affirmativeAction) {
        VisDialog dialog = Dialogs.showOKDialog(JarexPlugin.plugin.getAPI().getUIStage(), title, text);
        dialog.padBottom(20).pack();
        dialog.addListener(CUD.wrap(affirmativeAction::run));
    }

    public static void showOkDialog(String title, String text) {
        VisDialog dialog = Dialogs.showOKDialog(JarexPlugin.plugin.getAPI().getUIStage(), title, text);
        dialog.padBottom(20).pack();
    }

    public static void showChoiceDialog(String title, String text, ButtonMeta... metas) {
        String[] names = new String[metas.length];
        Integer[] indices = new Integer[metas.length];

        for (int i = 0; i < metas.length; i++) {
            names[i] = metas[i].name;
            indices[i] = i;
        }

        Dialogs.showConfirmDialog(JarexPlugin.plugin.getAPI().getUIStage(),
                title, text,
                names, indices, r -> metas[r].action.run()
        ).padBottom(20).pack();
    }

    public static class ButtonMeta {
        String name;
        Runnable action;

        public ButtonMeta(String name, Runnable action) {
            this.name = name;
            this.action = action;
        }
    }

}
