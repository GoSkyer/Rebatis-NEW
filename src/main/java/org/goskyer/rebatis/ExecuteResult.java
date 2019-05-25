package org.goskyer.rebatis;

import org.goskyer.rebatis.convert.RowMap;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ExecuteResult {

    public CompletableFuture<List<RowMap<String, Object>>> FUTURE;

    public ExecuteResult(CompletableFuture<List<RowMap<String, Object>>> completableFuture) {
        this.FUTURE = completableFuture;
    }

    public <U> CompletableFuture<U> thenApply(Function<List<RowMap<String, Object>>, ? extends U> fn) {
        return this.FUTURE.thenApply(fn);
    }

}
