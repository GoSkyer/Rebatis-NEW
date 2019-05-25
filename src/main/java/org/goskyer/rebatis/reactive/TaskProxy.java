package org.goskyer.rebatis.reactive;

import com.github.jasync.sql.db.QueryResult;
import org.goskyer.rebatis.ExecuteResult;
import org.goskyer.rebatis.annotation.Param;

import org.goskyer.rebatis.annotation.Delete;
import org.goskyer.rebatis.annotation.Insert;
import org.goskyer.rebatis.annotation.Select;
import org.goskyer.rebatis.annotation.Update;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import org.goskyer.rebatis.connection.AsyncConnection;
import org.goskyer.rebatis.connection.Config;
import org.goskyer.rebatis.connection.Connection;
import org.goskyer.rebatis.convert.ResultConvert;
import org.goskyer.rebatis.convert.RowMap;

public class TaskProxy implements InvocationHandler {

    private final static TaskProxy PROXY = new TaskProxy();

    public static TaskProxy getInstance() {
        return PROXY;
    }

    private final Map<Class, TaskType> mAnnotationMap = new HashMap<>();

    private final Connection mConnection;

    private TaskProxy() {

        Config cfg = new Config();
        cfg.setHost("localhost");
        cfg.setPort(3306);
        cfg.setDatabase("test");
        cfg.setUsername("root");
        cfg.setPassword("root");

        this.mConnection = new AsyncConnection(cfg);

        this.init();

    }

    private void init() {
        mAnnotationMap.put(Insert.class, TaskType.Insert);
        mAnnotationMap.put(Delete.class, TaskType.Delete);
        mAnnotationMap.put(Update.class, TaskType.Update);
        mAnnotationMap.put(Select.class, TaskType.Select);
    }

    public <T> T register(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Annotation[] annotations = method.getAnnotations();

        TaskType taskType = this.parseTaskType(annotations);

        return this.taskHandler(taskType, method, args);
    }

    private TaskType parseTaskType(Annotation[] annotations) throws Throwable {
        return Arrays.stream(annotations)
                .filter(annotation -> mAnnotationMap.containsKey(annotation.annotationType()))
                .findFirst()
                .map(annotation -> mAnnotationMap.get(annotation.annotationType()))
                .orElseThrow(() -> new NoSuchElementException("there is not matched annotation."));
    }

    private ExecuteResult taskHandler(TaskType taskType, Method method, Object[] args) {

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

        return new ExecuteResult(future.thenApply(ResultConvert::convert));

    }

    private String parseSQL(String sqlBeforeParse, Method method, Object[] args) {

        if (args == null || args.length == 0) {
            return sqlBeforeParse;
        }

        Parameter[] parameters = method.getParameters();

        String sqlAfterParse = sqlBeforeParse;

        for (int i = 0; i < args.length; i++) {
            Object value = args[i];
            Parameter parameter = parameters[i];
            Param paramAnnotation = parameter.getAnnotation(Param.class);
            String paramHolder = paramAnnotation.value();

            if (value instanceof String || value instanceof Character) {
                sqlAfterParse = sqlAfterParse.replace("#{" + paramHolder + "}", "'" + value.toString() + "'");
            } else {
                sqlAfterParse = sqlAfterParse.replace("#{" + paramHolder + "}", value.toString());
            }

        }

        return sqlAfterParse;

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

    private CompletableFuture<QueryResult> executeSQLHandler(String sqlBeforeParse, Method method, Object[] args) {
        String executeSQL = this.parseSQL(sqlBeforeParse, method, args);
        return this.mConnection.execute(executeSQL);
    }

}
