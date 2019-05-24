package org.goskyer.rebatis.test;

import org.goskyer.rebatis.Rebatis;
import org.goskyer.rebatis.test.entity.User;
import org.goskyer.rebatis.test.dao.UserMapper;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RebatisTest {

    @Test
    public void testMain() throws ExecutionException, InterruptedException {

        UserMapper mapper = Rebatis.register(UserMapper.class);

//        User user1 = Rebatis.execute(mapper.selectUser("Tom", 18, 0), User.class).get();
//        System.out.println(user1);

        User user2 = Rebatis.execute(mapper.selectUser("Tom", 18, 0)).convert(User.class).get();
        System.out.println(user2);

    }

}
