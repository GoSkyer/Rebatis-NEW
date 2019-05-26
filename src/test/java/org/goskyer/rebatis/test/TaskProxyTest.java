package org.goskyer.rebatis.test;


import org.goskyer.rebatis.ExecuteReturn;
import org.goskyer.rebatis.connection.Configuration;
import org.goskyer.rebatis.test.dao.UserMapper;
import org.goskyer.rebatis.convert.RowMap;
import org.goskyer.rebatis.reactive.TaskProxy;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.function.BiConsumer;

public class TaskProxyTest {

    private Configuration cfg;

    @Before
    public void beforeMain() {
        cfg = new Configuration();
        cfg.setHost("localhost");
        cfg.setPort(3306);
        cfg.setDatabase("test");
        cfg.setUsername("root");
        cfg.setPassword("root");
    }

    @Test
    public void testMain() {

        TaskProxy taskProxy = new TaskProxy(cfg);

        UserMapper mapper = taskProxy.register(UserMapper.class);

        ExecuteReturn future = mapper.selectUser("Tom", 18, 1);

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
