package org.goskyer.rebatis.test;

import com.github.jasync.sql.db.QueryResult;
import org.goskyer.rebatis.connection.AsyncConnection;
import org.goskyer.rebatis.connection.Configuration;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ConnectionTest {

    @Test
    public void testMain() {

        Configuration cfg = new Configuration();
        cfg.setHost("localhost");
        cfg.setPort(3306);
        cfg.setDatabase("test");
        cfg.setUsername("root");
        cfg.setPassword("Aa761349852.");

        AsyncConnection connection = new AsyncConnection(cfg);

        CompletableFuture<QueryResult> future = connection.execute("select * from user");

        future.thenAccept(new Consumer<QueryResult>() {

            @Override
            public void accept(QueryResult queryResult) {
                System.out.println(queryResult.getRows().get(0).getString("name"));
            }

        });

        while (true) {

        }

    }

}
