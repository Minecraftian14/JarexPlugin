package com.mcxiv.app.ui;

import com.mcxiv.app.util.GithubUtil;
import com.mcxiv.app.valueobjects.LinkData;
import games.rednblack.h2d.common.ProgressHandler;

public class IDENTITY  {

    public static final LinkData link = new LinkData(GithubUtil.link("null/null"), true);
    public static Runnable doNothing = () -> {};

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
