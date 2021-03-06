package com.mcxiv.app.util;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.*;

public class ThreadUtil {

    public static void launch(Runnable runnable) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(runnable);
        service.shutdown();
    }

    public static void launchForOnly(Runnable runnable, int seconds) {
        CUD.Try(() -> CompletableFuture.runAsync(runnable).get(seconds, TimeUnit.SECONDS));
    }

    public static void readStream(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        while (scanner.hasNext()) System.out.println(scanner.nextLine());
    }

    public static void launchAfter(Runnable runnable, int seconds) {
        launch(() -> {
            try {
                Thread.sleep(seconds * 1000);
                runnable.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
