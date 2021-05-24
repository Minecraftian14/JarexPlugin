package com.mcxiv.app.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {

    public static void launch(Runnable runnable) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(runnable);
        service.shutdown();
    }

}
