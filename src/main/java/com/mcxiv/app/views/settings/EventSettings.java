package com.mcxiv.app.views.settings;

import java.util.stream.Stream;

public enum EventSettings {

    NULL,
    ADD_SETTINGS_MENU_ACTION,
    /**
     * Object sent along is of type RowElement
     */
    ADD_NEW_ROW_ELEMENT;

    private final String name;

    EventSettings() {
        this.name = getClass() + ":" + name();
    }

    public static String[] getList() {
        return Stream.of(values()).map(EventSettings::getName).toArray(String[]::new);
    }

    public static EventSettings getEventNamed(String name) {
        return Stream.of(values()).filter(event -> event.equals(name)).findFirst().orElse(NULL);
    }

    public boolean equals(String name) {
        return getName().equals(name);
    }

    public String getName() {
        return name;
    }

}
