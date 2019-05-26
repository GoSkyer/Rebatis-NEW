package org.goskyer.rebatis.connection;

import com.github.jasync.sql.db.QueryResult;

import java.util.concurrent.CompletableFuture;

/**
 * @author Galaxy
 * @description TODO
 * @since 2019-05-26 10:10
 */
public interface Connection {

    /**
     * 执行sql并返回CompletableFuture
     *
     * @param sql 需要执行的语句
     * @return CompletableFuture
     */
    CompletableFuture<QueryResult> execute(String sql);

}
