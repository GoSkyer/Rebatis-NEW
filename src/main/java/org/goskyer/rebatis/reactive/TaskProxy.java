package org.goskyer.rebatis.reactive;

import com.github.jasync.sql.db.QueryResult;
import org.goskyer.rebatis.ExecuteReturn;
import org.goskyer.rebatis.annotation.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import org.goskyer.rebatis.connection.AsyncConnection;
import org.goskyer.rebatis.connection.Configuration;
import org.goskyer.rebatis.connection.Connection;
import org.goskyer.rebatis.convert.ResultConvert;

/**
 * @author Galaxy
 * @description TODO
 * @since 2019-05-26 10:10
 */
public class TaskProxy implements InvocationHandler {

    private final Connection mConnection;

    public TaskProxy(Configuration cfg) {
        this.mConnection = new AsyncConnection(cfg);
    }

    public <T> T register(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        TaskType taskType = AnnotationParser.parseTaskType(method.getAnnotations());

        return this.taskHandler(taskType, method, args);
    }

    private ExecuteReturn taskHandler(TaskType taskType, Method method, Object[] args) {

        CompletableFuture<QueryResult> future;

        switch (taskType) {
            case Insert:
                future = insertHandler(method, args);
                break;
            case Delete:
                future = deleteHandler(method, args);
                break;
            case Update:
                future = updateHandler(method, args);
                break;
            case Select:
                future = selectHandler(method, args);
                break;
            default:
                return null;
        }

        return new ExecuteReturn(future.thenApply(ResultConvert::convert));

    }

    private CompletableFuture<QueryResult> insertHandler(Method method, Object[] args) {

        Insert annotation = method.getAnnotation(Insert.class);

        String sqlBeforeParse = Optional.ofNullable(annotation).map(Insert::value).get();

        return this.executeSQLHandler(sqlBeforeParse, method, args);
    }

    private CompletableFuture<QueryResult> deleteHandler(Method method, Object[] args) {

        Delete annotation = method.getAnnotation(Delete.class);

        String sqlBeforeParse = Optional.ofNullable(annotation).map(Delete::value).get();

        return this.executeSQLHandler(sqlBeforeParse, method, args);
    }

    private CompletableFuture<QueryResult> updateHandler(Method method, Object[] args) {

        Update annotation = method.getAnnotation(Update.class);

        String sqlBeforeParse = Optional.ofNullable(annotation).map(Update::value).get();

        return this.executeSQLHandler(sqlBeforeParse, method, args);
    }

    private CompletableFuture<QueryResult> selectHandler(Method method, Object[] args) {

        Select annotation = method.getAnnotation(Select.class);

        String sqlBeforeParse = Optional.ofNullable(annotation).map(Select::value).get();

        return this.executeSQLHandler(sqlBeforeParse, method, args);
    }

    /**
     * 解析sql中的参数，使用方法传入的值进行替换，并交由连接池发到数据库执行。
     *
     * @param sqlBeforeParse 未解析参数的sql
     * @param method         需要执行的方法，获取参数名
     * @param args           方法的参数值
     * @return 数据库执行结果
     */
    private CompletableFuture<QueryResult> executeSQLHandler(String sqlBeforeParse, Method method, Object[] args) {
        String executeSQL = AnnotationParser.parseSQL(sqlBeforeParse, method, args);

        System.out.println("Execute SQL -> " + executeSQL);

        return this.mConnection.execute(executeSQL);
    }

}
