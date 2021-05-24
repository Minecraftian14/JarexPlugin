package com.mcxiv.app.valueobjects;

public class LinkData {

    public static final String DELIMITER = "&";

    private final String link;
    private final boolean isAlwaysUpdateCheck;

    public LinkData(String link, boolean isAlwaysUpdateCheck) {
        this.link = link;
        this.isAlwaysUpdateCheck = isAlwaysUpdateCheck;
    }

    public String getLink() {
        return link;
    }

    public boolean isAlwaysUpdateCheck() {
        return isAlwaysUpdateCheck;
    }

    @Override
    public String toString() {
        return String.format("{LinkData:%s}", row());
    }

    public String row() {
        return String.format("%s%s%s", link, DELIMITER, isAlwaysUpdateCheck);
    }

    public static LinkData deRow(String row) {
        String[] ele = row.split(DELIMITER);
        return new LinkData(ele[0], Boolean.parseBoolean(ele[1]));
    }

}
