package org.goskyer.rebatis.test;


import org.goskyer.rebatis.ExecuteResult;
import org.goskyer.rebatis.test.dao.UserMapper;
import org.goskyer.rebatis.convert.RowMap;
import org.goskyer.rebatis.reactive.TaskProxy;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class TaskProxyTest {

    @Test
    public void testMain() {

        UserMapper mapper = TaskProxy.getInstance().register(UserMapper.class);

        ExecuteResult future = mapper.selectUser("Tom", 18, 1);

        future.FUTURE.whenComplete(new BiConsumer<List<RowMap<String, Object>>, Throwable>() {
            @Override
            public void accept(List<RowMap<String, Object>> rowMaps, Throwable throwable) {
                System.out.println(rowMaps);
                System.out.println(throwable.toString());
            }
        });

        while (true) {

        }

    }

}
