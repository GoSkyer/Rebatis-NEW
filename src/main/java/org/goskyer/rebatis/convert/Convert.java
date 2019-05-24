package org.goskyer.rebatis.convert;

import com.github.jasync.sql.db.RowData;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Convert {

    private CompletableFuture completableFuture;

    public Convert(CompletableFuture completableFuture) {
        this.completableFuture = completableFuture;
    }

    public <U> CompletableFuture<U> convert(Class<U> clazz) {

        return completableFuture.thenApply(new Function<List<RowMap<String, Object>>, U>() {

            @Override
            public U apply(List<RowMap<String, Object>> rowMaps) {

                if (rowMaps.size() == 0) return null;

                return Convert.this.convertTo(rowMaps, clazz);
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

            Field[] fields = clazz.getFields();

            for (Field field : fields) {
                field.set(obj, rowMap.get(field.getName()));
            }

            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <U> List<U> convertToList(List<RowMap<String, Object>> rowMaps, Class<U> clazz) {

        Type dataType = ((ParameterizedType) (Type) clazz).getActualTypeArguments()[0];

        List<U> list = new ArrayList<>();
        for (RowMap<String, Object> rowMap : rowMaps) {
            list.add(convertToObject(rowMap, (Class<U>) dataType));
        }

        return list;

    }

}
