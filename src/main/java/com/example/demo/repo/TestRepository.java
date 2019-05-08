package com.example.demo.repo;

import com.example.demo.dao.User;
import com.example.demo.jpa.a.ChangeDataSource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author yang chen
 * @date 2019/4/18 13:06
 */
@Repository
public interface TestRepository extends BaseRepository<User,Long>{

    @Query("select a from #{#entityName} a where a.databaseName=:name")
    public User findUserByName(@Param("name") String name);

}
