package org.goskyer.rebatis;

import org.goskyer.rebatis.convert.Convert;
import org.goskyer.rebatis.convert.RowMap;
import org.goskyer.rebatis.reactive.TaskProxy;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Rebatis {

    private final static TaskProxy PROXY = TaskProxy.getInstance();

    public static <T> T register(Class<T> clazz) {
        return PROXY.register(clazz);
    }

    public static <T> CompletableFuture<T> execute(ExecuteResult task, Class<T> clazz) {
        return new Convert(task).convert(clazz);
    }

    public static Convert execute(ExecuteResult task) {
        return new Convert(task);
    }

}
