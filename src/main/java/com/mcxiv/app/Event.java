package com.mcxiv.app;

import java.util.stream.Stream;

public enum Event {

    NULL("NULL"),
    OPEN_JAREX_HUD_ACTION("OPEN_JAREX_HUD"),
    /**
     * An event sent to start a jar executable.
     * Usually, the attached body is of type: RowElement.
     */
    OPEN_APPLICATION_ACTION("OPEN_APPLICATION_ACTION"),
    /**
     * An event sent to start a jar download.
     * Usually, the attached body is of type: JarexMediator$Capsule.
     */
    DOWNLOAD_APPLICATION_JAR_ACTION("DOWNLOAD_APPLICATION_JAR_ACTION");

    String name;

    Event(String name) {
        this.name = getClass().getName() + "." + name;
    }

    public static String[] getList() {
        return Stream.of(Event.values()).map(event -> event.name).toArray(String[]::new);
    }

    public static Event getEventNamed(String name) {
        return Stream.of(Event.values()).filter(event -> event.name.equals(name)).findFirst().orElse(Event.NULL);
    }

    @Override
    public String toString() {
        return name;
    }

}
