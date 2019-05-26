package org.goskyer.rebatis;

import org.goskyer.rebatis.connection.Configuration;
import org.goskyer.rebatis.convert.Convert;
import org.goskyer.rebatis.reactive.TaskProxy;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Galaxy
 * @description TODO
 * @since 2019-05-26 10:10
 */
public class Rebatis {

    private final TaskProxy mProxy;

    public Rebatis(Configuration cfg) {
        mProxy = new TaskProxy(cfg);
    }

    public <T> T register(Class<T> clazz) {
        return mProxy.register(clazz);
    }

    public <T> CompletableFuture<List<T>> execute(ExecuteReturn task, Class<T> clazz) {
        return new Convert(task).convert(clazz);
    }

    public Convert execute(ExecuteReturn task) {
        return new Convert(task);
    }

}
