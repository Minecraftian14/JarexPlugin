package com.mcxiv.app.valueobjects;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Map;

public class JarexSettingsData {

    public static final String CLASS_NAME = JarexSettingsData.class.getName();

    /**
     * A delimiter to differentiate between different links
     */
    public static final String DELIMITER = "__rickroll__";

    public ArrayList<LinkData> registeredLinks = new ArrayList<>();

    public void fromStorage(Map<String, Object> settings) {
        String links = (String) settings.get(CLASS_NAME);

        if (links == null) {
            System.out.println("ERROR loading links, value received is null.");
            return;
        }

        for (String s : links.split(DELIMITER))
            registeredLinks.add(LinkData.deRow(s));
    }

    public void toStorage(Map<String, Object> settings) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < registeredLinks.size() - 1; i++)
            builder.append(registeredLinks.get(i).row()).append(DELIMITER);
        builder.append(registeredLinks.get(registeredLinks.size() - 1).row());

        settings.put(CLASS_NAME, builder.toString());
    }

    public void fromSettings(JarexSettingsData settings) {
        registeredLinks.clear();
        registeredLinks.addAll(settings.registeredLinks);
    }
}
