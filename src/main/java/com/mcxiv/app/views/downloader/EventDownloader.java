package com.mcxiv.app.views.downloader;

import java.util.stream.Stream;

public enum EventDownloader {

    NULL,
    /**
     * An event sent to start a jar download.
     * Usually, the attached body is of type: LinkData.
     */
    DOWNLOAD_APPLICATION_JAR_REQUEST,

    /**
     * An event sent to start a jar download.
     * Usually, the attached body is of type: LinkData.
     */
    CHECK_FOR_UPDATES_ACTION,

    /**
     * An event sent to let user choose and download the jar file.
     * Usually, the attached body is of type: DownloadData.
     */
    OPEN_JAR_CHOOSER_MENU;

    private final String name;

    EventDownloader() {
        this.name = getClass() + ":" + name();
    }

    public static String[] getList() {
        return Stream.of(values()).map(EventDownloader::getName).toArray(String[]::new);
    }

    public static EventDownloader getEventNamed(String name) {
        return Stream.of(values()).filter(event -> event.equals(name)).findFirst().orElse(EventDownloader.NULL);
    }

    public boolean equals(String name) {
        return getName().equals(name);
    }

    public String getName() {
        return name;
    }

}
