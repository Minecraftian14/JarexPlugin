package com.mcxiv.app.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;

public class CUD {

    public static boolean perhapsEqual(String name, String... elements) {
        name = name.toLowerCase();
        for (String element : elements) if (!name.contains(element.toLowerCase())) return false;
        return true;
    }

    public static <E> void forEach(Iterable<E> iterable, BiConsumer<Integer, E> action) {
        Objects.requireNonNull(action);

        int idx = 0;
        for (E e : iterable) {
            action.accept(idx, e);
            idx++;
        }
    }

    /**
     * one and two are equal in the sense that, for each element
     * is one, there is an equivalent in two, and so is vice versa.
     *
     * @param one
     * @param two
     * @return
     */
    public static boolean equalsList(Iterable<? extends EqualityCompatible> one, Iterable<? extends EqualityCompatible> two) {
        return AIsOntoToB(one, two) && AIsOntoToB(two, one);
    }

    /**
     * For every element in domain, there is an equivalent in range.
     *
     * @param domain
     * @param range
     * @return
     */
    public static boolean AIsOntoToB(Iterable<? extends EqualityCompatible> domain, Iterable<? extends EqualityCompatible> range) {
        for (EqualityCompatible domEle : domain) {

            boolean isDomEleInRange = false;

            for (EqualityCompatible ranEle : range) {
                if (domEle.equivalent(ranEle)) {
                    isDomEleInRange = true;
                    break;
                }
            }

            if(!isDomEleInRange) return false;
        }
        return true;
    }

}
