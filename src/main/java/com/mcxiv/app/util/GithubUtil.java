package com.mcxiv.app.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class GithubUtil {

    // link example
    // https://api.github.com/repos/raeleus/skin-composer/releases/latest

    static Pattern pat_displayName = Pattern.compile("(?:.*[\\/]){5}(.*)(?:[\\/].*){2}");

    public static String displayName(String link) {
        Matcher match = pat_displayName.matcher(link);
        if (match.find()) return sentenceConversion(match.group(1));
        return link;
    }

    public static String applicationJarName(String link) {
        return displayName(link) + ".jar";
    }

    private static String sentenceConversion(String inputString) {
        if (inputString == null) return "Null";

        if (inputString.isEmpty()) return inputString;

        if (inputString.length() == 1) return inputString.toUpperCase();

        inputString = inputString.replaceAll("[^a-zA-Z0-9]", " ")
                .replaceAll("[^a-zA-Z ]", "")
                .replaceAll("[ ]{2,}", "");

        StringBuffer resultPlaceHolder = new StringBuffer(inputString.length());

        Stream.of(inputString.split(" ")).forEach(stringPart ->
        {
            if (stringPart.length() > 1)
                resultPlaceHolder.append(stringPart.substring(0, 1)
                        .toUpperCase())
                        .append(stringPart.substring(1)
                                .toLowerCase());
            else
                resultPlaceHolder.append(stringPart.toUpperCase());

            resultPlaceHolder.append(" ");
        });
        return resultPlaceHolder.toString().trim();
    }
}
