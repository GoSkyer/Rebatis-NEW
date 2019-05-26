package org.goskyer.rebatis;

import org.goskyer.rebatis.convert.RowMap;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

/**
 * @author Galaxy
 */
public class ExecuteReturn<T> {

    public CompletableFuture<List<RowMap<String, Object>>> FUTURE;

    public ExecuteReturn(CompletableFuture<List<RowMap<String, Object>>> completableFuture) {
        this.FUTURE = completableFuture;
    }

    public <U> CompletableFuture<U> thenApply(Function<List<RowMap<String, Object>>, ? extends U> fn) {
        return this.FUTURE.thenApply(fn);
    }

    public <U> CompletableFuture<U> thenApplyAsync(Function<List<RowMap<String, Object>>, ? extends U> fn) {
        return this.FUTURE.thenApplyAsync(fn);
    }

    public <U> CompletableFuture<U> thenApplyAsync(Function<List<RowMap<String, Object>>, ? extends U> fn, Executor executor) {
        return this.FUTURE.thenApplyAsync(fn, executor);
    }



}
