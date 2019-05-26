package org.goskyer.rebatis.test.dao;

import org.goskyer.rebatis.ExecuteReturn;
import org.goskyer.rebatis.annotation.*;

public interface UserMapper {

    @Select("select * from user where sex = #{sex} ")
    ExecuteReturn selectUser(@Param("name") String name, @Param("age") int age, @Param("sex") int sex);

    @Insert("insert into user values(#{name},#{age},#{sex})")
    ExecuteReturn insertUser(@Param("name") String name, @Param("age") int age, @Param("sex") int sex);

    @Delete("delete from user where name=#{name}")
    ExecuteReturn deleteUser(@Param("name") String name, @Param("age") int age, @Param("sex") int sex);

    @Update("update user set age=#{age} where name=#{name}")
    ExecuteReturn updateUser(@Param("name") String name, @Param("age") int age, @Param("sex") int sex);

}
