package com.mcxiv.app.util;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CUD {

    public static boolean perhapsEqual(String name, String... elements) {
        name = name.toLowerCase();
        for (String element : elements) if (!name.contains(element.toLowerCase())) return false;
        return true;
    }

    public static  <E>  void forEach(Iterable<E> iterable, BiConsumer<Integer, E> action) {
        Objects.requireNonNull(action);

        int idx = 0;
        for (E e : iterable) {
            action.accept(idx, e);
            idx++;
        }
    }

}
