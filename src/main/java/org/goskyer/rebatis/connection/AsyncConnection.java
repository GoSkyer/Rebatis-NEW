package org.goskyer.rebatis.connection;

import com.github.jasync.sql.db.Configuration;
import com.github.jasync.sql.db.ConnectionPoolConfiguration;
import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.mysql.MySQLConnection;
import com.github.jasync.sql.db.mysql.pool.MySQLConnectionFactory;
import com.github.jasync.sql.db.pool.ConnectionPool;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AsyncConnection implements Connection {

    private ConnectionPool<MySQLConnection> mConnectionPool;

    public AsyncConnection(Config cfg) {
        this(cfg.getHost(), cfg.getPort(), cfg.getDatabase(), cfg.getUsername(), cfg.getPassword());
    }

    public AsyncConnection(String host, int port, String database, String username, String password) {
        mConnectionPool = this.createConnectionPool(host, port, database, username, password);
    }

    private ConnectionPool<MySQLConnection> createConnectionPool(String host, int port, String database, String username, String password) {
        Configuration cfg = new Configuration(username, host, port, password, database);
        return new ConnectionPool<>(new MySQLConnectionFactory(cfg), new ConnectionPoolConfiguration(host, port, database, username, password));
    }

    public CompletableFuture<QueryResult> execute(String sql) {
        return mConnectionPool.sendPreparedStatement(sql);
    }

}
