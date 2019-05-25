package org.goskyer.rebatis.convert;

import com.github.jasync.sql.db.RowData;
import org.goskyer.rebatis.ExecuteResult;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Convert {

    private ExecuteResult completableFuture;

    public Convert(ExecuteResult completableFuture) {
        this.completableFuture = completableFuture;
    }

    public <U> CompletableFuture<U> convert(Class<U> clazz) {

        return completableFuture.thenApply(new Function<List<RowMap<String, Object>>, U>() {

            @Override
            public U apply(List<RowMap<String, Object>> rowMaps) {

                if (rowMaps.size() == 0) {
                    return null;
                }

                return Convert.this.convertTo(rowMaps, clazz);
            }

        });
    }

    public <U> CompletableFuture<List<U>> convertToList(Class<U> clazz) {

        return completableFuture.thenApply(new Function<List<RowMap<String, Object>>, List<U>>() {

            @Override
            public List<U> apply(List<RowMap<String, Object>> rowMaps) {

                if (rowMaps.size() == 0) {
                    return null;
                }

                return Convert.this.convertToList(rowMaps, clazz);
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
