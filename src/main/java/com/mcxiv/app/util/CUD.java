package com.mcxiv.app.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

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

            if (!isDomEleInRange) return false;
        }
        return true;
    }

    public static String sentenceConversion(String inputString) {
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

    public static ChangeListener wrap(Runnable runnable) {
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runnable.run();
            }
        };
    }

    public static void Try(HazardousRunnable thrower) {
        LazyTry(thrower).Catch(Throwable::printStackTrace);
    }

    public static ExceptionConsumerConsumer LazyTry(HazardousRunnable thrower) {
        return consumer -> {
            try {
                thrower.run();
            } catch (Exception e) {
                consumer.accept(e);
            }
        };
    }

    // ARunnableWhichMayThrowAnException
    public interface HazardousRunnable {
        void run() throws Exception;
    }

    // Something To Take In An Action To Perform In Case An Exception Is Thrown
    public interface ExceptionConsumerConsumer {
        void Catch(Consumer<Exception> consumer);
    }

    public static <T> DefaultSupplierConsumer<T> Try(HazardousSupplier<T> thrower) {
        return LazyTry(thrower).Catch(Throwable::printStackTrace);
    }

    public static <T> ExceptionConsumerConsumerAndDefaultSupplierConsumerSupplier<T> LazyTry(HazardousSupplier<T> thrower) {
        return consumer -> {
            return supplier -> {
                try {
                    return thrower.get();
                } catch (Exception e) {
                    consumer.accept(e);
                    return supplier.get();
                }
            };
        };
    }


    // A Supplier Which May Throw An Exception
    public interface HazardousSupplier<T> {
        T get() throws Exception;
    }

    // Something To Take In An Action To Perform In Case An Exception
    // Is Thrown And Also Returning Something To Define A Default Case
    public interface ExceptionConsumerConsumerAndDefaultSupplierConsumerSupplier<T> {
        DefaultSupplierConsumer<T> Catch(Consumer<Exception> consumer);
    }

    // Something To Define The Default Case
    public interface DefaultSupplierConsumer<T> {
        T Default(Supplier<T> supplier);
    }

}
