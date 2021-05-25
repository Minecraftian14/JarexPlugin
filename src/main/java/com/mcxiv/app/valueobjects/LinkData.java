package com.mcxiv.app.valueobjects;

import com.mcxiv.app.ui.RowElement;
import com.mcxiv.app.util.EqualityCompatible;

import java.util.Objects;

public class LinkData implements EqualityCompatible {

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

    @Override
    public boolean equivalent(EqualityCompatible object) {
        if(equals(object)) return true;
        if(object instanceof RowElement) {
            RowElement element = ((RowElement) object);
            return element.getLink().equals(getLink()) && element.isAlwaysUpdateCheck()==isAlwaysUpdateCheck();
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkData linkData = (LinkData) o;
        return isAlwaysUpdateCheck == linkData.isAlwaysUpdateCheck && Objects.equals(link, linkData.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, isAlwaysUpdateCheck);
    }
}
