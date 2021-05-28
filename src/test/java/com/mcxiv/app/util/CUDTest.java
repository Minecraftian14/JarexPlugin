package com.mcxiv.app.util;

import com.badlogic.gdx.utils.Array;
import com.mcxiv.app.PluginTester;
import com.mcxiv.app.ui.RowElement;
import com.mcxiv.app.valueobjects.LinkData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CUDTest {

    @Test
    void testPerhapsEqual() {

        String name = "SkinComposer.jar";
        String ds = "Skin Composer";

        assertTrue(CUD.perhapsEqual(name, (ds + ".jar").split("[^a-zA-Z]")));

        name = "particle-park.jar";
        ds = GithubUtil.displayName(GithubUtil.link("raeleus/Particle-Park"));

        assertTrue(CUD.perhapsEqual(name, (ds + ".jar").split("[^a-zA-Z]")));

    }

    public static void main(String[] args) throws InterruptedException {

        // No exception, especially "org.opentest4j.AssertionFailedError" means, test successful.

        PluginTester.launchTest(null, CUDTest::TestEqualLists);
    }

    static void TestEqualLists() {

        ArrayList<LinkData> datas = new ArrayList<>();
        datas.add(new LinkData(GithubUtil.link("c/a"), true));
        datas.add(new LinkData(GithubUtil.link("b/b"), true));
        datas.add(new LinkData(GithubUtil.link("d/c"), true));
        datas.add(new LinkData(GithubUtil.link("a/d"), true));


        Array<RowElement> elements = new Array<>();
        elements.addAll(
                new RowElement(new LinkData(GithubUtil.link("a/d"), true)),
                new RowElement(new LinkData(GithubUtil.link("b/b"), true)),
                new RowElement(new LinkData(GithubUtil.link("c/a"), true)),
                new RowElement(new LinkData(GithubUtil.link("d/c"), true))
        );

        assertTrue(CUD.equalsList(datas, elements));

        datas.add(new LinkData(GithubUtil.link("e/e"), true));

        assertFalse(CUD.equalsList(datas, elements));

        assertTrue(CUD.AIsOntoToB(elements, datas));

        assertFalse(CUD.AIsOntoToB(datas, elements));

    }


    @Test
    void testOntoFunction() {

        ArrayList<Vehical> vehicals = new ArrayList<>();
        vehicals.add(new Vehical(4, 4));
        vehicals.add(new Vehical(6, 6));
        vehicals.add(new Vehical(8, 3));
        vehicals.add(new Vehical(10, 4));


        Array<Car> cars = new Array<>();
        cars.addAll(
                new Car(6),
                new Car(3),
                new Car(4),
                new Car(4)
        );

        assertTrue(CUD.equalsList(vehicals, cars));

        vehicals.add(new Vehical(11, 2));

        assertFalse(CUD.equalsList(vehicals, cars));

        assertTrue(CUD.AIsOntoToB(cars, vehicals));

        assertFalse(CUD.AIsOntoToB(vehicals, cars));

    }

    @Test
    void testingDemoVehicalAndCarObject() {

        assertTrue(new Vehical(10, 4).equivalent(new Car(4)));
        assertTrue(new Car(4).equivalent(new Car(4)));
        assertTrue(new Car(4).equivalent(new Vehical(10, 4)));
        assertTrue(new Vehical(10, 4).equivalent(new Vehical(12, 4)));


        assertFalse(new Vehical(10, 6).equivalent(new Car(4)));
        assertFalse(new Car(3).equivalent(new Car(4)));
        assertFalse(new Car(4).equivalent(new Vehical(10, 6)));
        assertFalse(new Vehical(10, 6).equivalent(new Vehical(12, 4)));
    }

    static class Vehical implements EqualityCompatible {

        // Less important
        final int doors;

        // More important
        final int wheels;

        public Vehical(int doors, int wheels) {
            this.doors = doors;
            this.wheels = wheels;
        }

        @Override
        public boolean equivalent(EqualityCompatible object) {
            if (equals(object)) return true;
            if (object instanceof Car) return ((Car) object).wheels == wheels;
            if (object instanceof Vehical) return ((Vehical) object).wheels == wheels;
            return false;
        }

        // Just to exist and demonstrate
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vehical vehical = (Vehical) o;
            return doors == vehical.doors && wheels == vehical.wheels;
        }

        @Override
        public int hashCode() {
            return Objects.hash(doors, wheels);
        }
    }

    static class Car implements EqualityCompatible {

        final int wheels;

        public Car(int wheels) {
            this.wheels = wheels;
        }

        @Override
        public boolean equivalent(EqualityCompatible object) {
            if (equals(object)) return true;
            if (object instanceof Car) return ((Car) object).wheels == wheels;
            if (object instanceof Vehical) return ((Vehical) object).wheels == wheels;
            return false;
        }

        // Just to exist and demonstrate

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Car car = (Car) o;
            return wheels == car.wheels;
        }

        @Override
        public int hashCode() {
            return Objects.hash(wheels);
        }
    }


    @Test
    void TestTryAndLazyTry() {

        CUD.Try(() -> {
            int i = 1 / 1;
            System.out.println("A:" + i);
        });

        CUD.Try(() -> {
            int i = 1 / 0;
            System.out.println("B:" + i);
        });

        int i = CUD.Try(() -> 1 / 1).Default(() -> 0);
        System.out.println("C:" + i);
        i = CUD.Try(() -> 1 / 0).Default(() -> 0);
        System.out.println("D:" + i);


        //


        CUD.LazyTry(() -> {
            int k = 1 / 1;
            System.out.println("E:" + k);
        }).Catch(e -> System.out.println("Oh, an error?"));

        CUD.LazyTry(() -> {
            int k = 1 / 0;
            System.out.println("F:" + k);
        }).Catch(e -> System.out.println("Oh, an error?"));

        i = CUD.LazyTry(() -> 1 / 1).Catch(e -> System.out.println("Oh, an error?")).Default(() -> 0);
        System.out.println("G:" + i);
        i = CUD.LazyTry(() -> 1 / 0).Catch(e -> System.out.println("Oh, an error?")).Default(() -> 0);
        System.out.println("H:" + i);

    }
}