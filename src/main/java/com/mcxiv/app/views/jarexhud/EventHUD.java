package com.mcxiv.app.views.jarexhud;

import java.util.stream.Stream;

public enum EventHUD {

    NULL,
    OPEN_JAREX_HUD_ACTION,
    /**
     * An event sent to start a jar executable.
     * Usually, the attached body is of type: LinkData.
     */
    OPEN_APPLICATION_ACTION,
    /**
     * An event sent to start a jar executable.
     * Usually, the attached body is of type: LinkData.
     */
    OPEN_APPLICATION_ACTION_NO_CHECK;

    private final String name;

    EventHUD() {
        this.name = getClass() + ":" + name();
    }

    public static String[] getList() {
        return Stream.of(values()).map(EventHUD::getName).toArray(String[]::new);
    }

    public static EventHUD getEventNamed(String name) {
        return Stream.of(values()).filter(event -> event.equals(name)).findFirst().orElse(NULL);
    }

    public boolean equals(String name) {
        return getName().equals(name);
    }

    public String getName() {
        return name;
    }

}
