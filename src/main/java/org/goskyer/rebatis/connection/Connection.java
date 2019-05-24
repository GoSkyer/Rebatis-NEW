package org.goskyer.rebatis.connection;

import com.github.jasync.sql.db.QueryResult;

import java.util.concurrent.CompletableFuture;

public interface Connection {

    CompletableFuture<QueryResult> execute(String sql);

}
