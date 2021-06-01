package com.mcxiv.app.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkDataTest {

    @Test
    void testRowDerow() {

        LinkData data = new LinkData("https://api.github.com/repos/Minecraftian14/Novix/releases/latest", true);

        System.out.println(data);
        System.out.println(data.row());

        System.out.println(LinkData.deRow(data.row()));

    }
}