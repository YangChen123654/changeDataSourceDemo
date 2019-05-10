package com.example.demo.repo;

import com.example.demo.dao.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author yang chen
 * @date 2019/5/9 17:01
 */
@Repository
public interface UserRepository extends BaseRepository <User, Long> {

    @Query("select a from #{#entityName} a where a.databaseName=:name")
    public User findUserByName(@Param("name") String name);
}
