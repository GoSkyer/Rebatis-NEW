package org.goskyer.rebatis;

import org.goskyer.rebatis.convert.Convert;
import org.goskyer.rebatis.convert.RowMap;
import org.goskyer.rebatis.reactive.TaskProxy;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Rebatis {

    private final static TaskProxy PROXY = TaskProxy.getInstance();

    private final static ThreadLocal<CompletableFuture<List<RowMap<String, Object>>>> threadLocal = new ThreadLocal<>();

    public static <T> T register(Class<T> clazz) {
        return PROXY.register(clazz);
    }

    public static <T> CompletableFuture<T> execute(CompletableFuture task, Class<T> clazz) {
        return new Convert(task).convert(clazz);
    }

    public static Convert execute(CompletableFuture task) {
        return new Convert(task);
    }

}
