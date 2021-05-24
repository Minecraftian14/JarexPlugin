package com.mcxiv.app.ui;

import games.rednblack.h2d.common.ProgressHandler;

public class IDENTITY  {

    private IDENTITY(){}

    public static final ProgressHandler progressHandler = new ProgressHandler() {
        @Override
        public void progressStarted() {
        }

        @Override
        public void progressChanged(float value) {
        }

        @Override
        public void progressComplete() {
        }

        @Override
        public void progressFailed() {
        }
    };
}
