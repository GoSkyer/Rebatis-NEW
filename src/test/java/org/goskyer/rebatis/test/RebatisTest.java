package org.goskyer.rebatis.test;

import org.goskyer.rebatis.Rebatis;
import org.goskyer.rebatis.connection.Configuration;
import org.goskyer.rebatis.test.entity.User;
import org.goskyer.rebatis.test.dao.UserMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RebatisTest {

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
    public void testMain() throws ExecutionException, InterruptedException {

        Rebatis rebatis = new Rebatis(cfg);

        UserMapper mapper = rebatis.register(UserMapper.class);

        int status1 = rebatis.execute(mapper.insertUser("Jack", 17, 1)).status().get();
        System.out.println(status1);

        int status2 = rebatis.execute(mapper.updateUser("Jack", 18, 1)).status().get();
        System.out.println(status2);

        int status3 = rebatis.execute(mapper.deleteUser("Jack", 17, 1)).status().get();
        System.out.println(status3);

        int status4 = rebatis.execute(mapper.selectUser("Jack", 17, 1)).status().get();
        System.out.println(status4);

        User user2 = rebatis.execute(mapper.selectUser("Tom", 18, 1)).single(User.class).get();
        System.out.println(user2);

        List<User> user3 = rebatis.execute(mapper.selectUser("Tom", 18, 1)).convert(User.class).get();
        System.out.println(user3);

    }

}
