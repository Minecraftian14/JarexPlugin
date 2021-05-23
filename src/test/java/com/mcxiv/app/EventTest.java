package com.mcxiv.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void testRName() {
        assertEquals(Event.OPEN_APPLICATION_ACTION, Event.getEventNamed("com.mcxiv.app.Event.OPEN_APPLICATION_ACTION"));
    }

    @Test
    void testInName() {
        assertEquals(Event.OPEN_APPLICATION_ACTION, Event.getEventNamed(Event.OPEN_APPLICATION_ACTION.name));
    }
}