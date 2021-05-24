package com.mcxiv.app.views.downloader;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisTable;
import games.rednblack.h2d.common.ProgressHandler;
import games.rednblack.h2d.common.UIDraggablePanel;

public class ViewDownloadDialog extends VisTable implements ProgressHandler {

    private UIDraggablePanel parent;
    private final VisLabel downloadingLabel;
    private final VisProgressBar progressBar;

    public ViewDownloadDialog(boolean createPanel) {

        if (createPanel) {
            parent = new UIDraggablePanel("Skin Composer Plugin");
            parent.setMovable(false);
            parent.setModal(false);

            parent.setHeight(100);
            parent.setWidth(250);

            parent.add(this).fill().expand();
        }

        downloadingLabel = new VisLabel("Checking for updates ...");
        add(downloadingLabel).left();
        row().padBottom(5);

        progressBar = new VisProgressBar(0, 100, 1, false);
        add(progressBar).fillX().pad(5).width(240);
        row().padBottom(5);

        if (createPanel)
            parent.pack();
    }

    public void setMessage(String message) {
        downloadingLabel.setText(message);
    }

    public void setProgress(float value) {
        progressBar.setValue(value);
    }

    @Override
    public void progressStarted() {

    }

    @Override
    public void progressChanged(float value) {
        progressBar.setValue(value);
    }

    @Override
    public void progressComplete() {
        progressBar.setValue(100);
        if (parent != null)
            parent.close();
    }

    @Override
    public void progressFailed() {
        downloadingLabel.setText("Download failed!");
        if (parent != null)
            parent.addCloseButton();
    }

    public void show(Stage uiStage) {
        if(parent!=null)
            parent.show(uiStage);
    }
}

