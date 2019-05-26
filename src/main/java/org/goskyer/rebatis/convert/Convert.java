package org.goskyer.rebatis.convert;

import org.goskyer.rebatis.ExecuteReturn;
import org.goskyer.rebatis.Result;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Convert {

    private ExecuteReturn executeResult;

    public Convert(ExecuteReturn completableFuture) {
        this.executeResult = completableFuture;
    }

    public CompletableFuture<Integer> status() {
        return executeResult.thenApply(new Function<Result, Integer>() {

            @Override
            public Integer apply(Result result) {

                if (!result.isSucceeded()) {
                    return -1;
                }

                return result.getEffectedRows();
            }

        });
    }

    public <U> CompletableFuture<U> single(Class<U> clazz) {

        return executeResult.thenApply(new Function<Result, U>() {

            @Override
            public U apply(Result result) {

                if (result.isNon()) {
                    return null;
                }

                if (result.getRaws().size() == 0) {
                    return null;
                }

                return Convert.this.convertTo(result.getRaws(), clazz);
            }

        });
    }

    public <U> CompletableFuture<List<U>> convert(Class<U> clazz) {

        return executeResult.thenApply(new Function<Result, List<U>>() {

            @Override
            public List<U> apply(Result result) {

                if (result.isNon()) {
                    List list = new LinkedList();
                    for (int i = 0; i < result.getEffectedRows(); i++) {
                        list.add(null);
                    }
                    return list;
                }

                if (result.getRaws().size() == 0) {
                    return null;
                }

                return Convert.this.convertToList(result.getRaws(), clazz);
            }

        });
    }

    private <U> U convertTo(List<RowMap<String, Object>> rowMaps, Class<U> clazz) {

        Type type = clazz;

        if (type.equals(List.class) || (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType().equals(List.class))) {
            return (U) convertToList(rowMaps, clazz);
        } else if (type.equals(Map.class) || (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType().equals(Map.class))) {
            return (U) rowMaps.get(0).toMap();
        } else if (type instanceof Class && type.equals(Void.class)) {
            return null;
        } else {
            return convertToObject(rowMaps.get(0), clazz);
        }

    }

    private <U> U convertToObject(RowMap<String, Object> rowMap, Class<U> clazz) {
        try {

            U obj = clazz.newInstance();

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                field.set(obj, rowMap.get(field.getName()));
            }

            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private <U> List<U> convertToList(List<RowMap<String, Object>> rowMaps, Class<U> clazz) {

        List<U> list = new ArrayList<>();
        for (RowMap<String, Object> rowMap : rowMaps) {
            list.add(convertToObject(rowMap, clazz));
        }

        return list;

    }

}
