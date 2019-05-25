package org.goskyer.rebatis.test.dao;

import org.goskyer.rebatis.ExecuteResult;
import org.goskyer.rebatis.annotation.Mapper;
import org.goskyer.rebatis.annotation.Param;
import org.goskyer.rebatis.annotation.Select;

import java.util.concurrent.CompletableFuture;

@Mapper
public interface UserMapper {

    @Select("select * from user where sex = #{sex} ")
    ExecuteResult selectUser(@Param("name") String name, @Param("age") int age, @Param("sex") int sex);

}
