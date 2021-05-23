package com.mcxiv.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JarexMediatorTest {

    @Test
    void test() {
        assertTrue(JarexMediator.perhapsEqual("hello World sys", "ello", "Wor", "wor"));
        assertFalse(JarexMediator.perhapsEqual("hello World sys", "rmx", "Wor", "meta"));
    }
}