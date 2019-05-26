package org.goskyer.rebatis.annotation;

import org.goskyer.rebatis.reactive.TaskType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-05-26 10:44
 **/
public class AnnotationParser {

    public final static Map<Class, TaskType> ANNOTATIONS = new HashMap<>();

    static {
        ANNOTATIONS.put(Insert.class, TaskType.Insert);
        ANNOTATIONS.put(Delete.class, TaskType.Delete);
        ANNOTATIONS.put(Update.class, TaskType.Update);
        ANNOTATIONS.put(Select.class, TaskType.Select);
    }

    public static TaskType parseTaskType(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .filter(annotation -> ANNOTATIONS.containsKey(annotation.annotationType()))
                .findFirst()
                .map(annotation -> ANNOTATIONS.get(annotation.annotationType()))
                .orElseThrow(() -> new NoSuchElementException("there is not matched annotation."));
    }

    /**
     * 将SQL中的参数进行替换，生成可执行的SQL
     *
     * @param sqlBeforeParse 带参数SQL
     * @param method         方法
     * @param args           参数值
     * @return 可执行的SQL
     */
    public static String parseSQL(String sqlBeforeParse, Method method, Object[] args) {

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

}
