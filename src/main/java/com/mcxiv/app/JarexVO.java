package com.mcxiv.app;

import com.mcxiv.app.ui.RowElement;

import java.util.ArrayList;
import java.util.Map;

public class JarexVO {

    /**
     * A delimiter to differentiate between different links
     */
    public static final String DELIMITER = "__rickroll__";

    public ArrayList<RowElement> registeredLinks = new ArrayList<>();

    public JarexVO() {
        registeredLinks.add(new RowElement("https://api.github.com/repos/raeleus/skin-composer/releases/latest", false));
        registeredLinks.add(new RowElement("https://api.github.com/repos/Minecraftian14/Novix/releases/latest", true));
    }

    public void fromStorage(Map<String, Object> settings) {
        String links = (String) settings.get("registeredLinks");

        if (links == null) {
            System.out.println("ERROR loading links, value received is null.");
            return;
        }

        for (String s : links.split(DELIMITER))
            registeredLinks.add(RowElement.fromString(s));
    }

    public void toStorage(Map<String, Object> settings) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < registeredLinks.size() - 1; i++)
            builder.append(registeredLinks.get(i)).append(DELIMITER);
        builder.append(registeredLinks.get(registeredLinks.size() - 1));
        settings.put("registeredLinks", builder.toString());
    }

}
