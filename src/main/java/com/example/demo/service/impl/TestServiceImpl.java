package com.example.demo.service.impl;

import com.example.demo.dao.User;
import com.example.demo.jpa.a.ChangeDataSource;
import com.example.demo.jpa.b.DynamicSwitchDataSource;
import com.example.demo.repo.TestRepository;
import com.example.demo.service.TestService;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    @Resource
    private TestRepository testRepository;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private DataSourceProperties dataSourceProperties;

    @ChangeDataSource(name = "1")
    @DynamicSwitchDataSource
    public String   getDatabase(){
        List<User> list = testRepository.findAll();
        String dataName = "";
        for (User user:list
             ) {
            dataName = user.getDatabaseName();
            System.out.println(user.getDatabaseName());
        }

        return dataName;
    }

    @Override
    public void saveUser() {
        try {
            User user  = new User();
            user.setId(1L);
            user.setDatabaseName("wma");
            user.setName("yangchen");
            this.testRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DataSource getDataSource(String id){
        DataSource dataSource = new ComboPooledDataSource();
//        DataSource dataSource =  applicationContext.getBean(DataSource.class);
        // 查看配置数据源信息
        System.out.println("url :"+dataSourceProperties.getUrl());
        ((ComboPooledDataSource) dataSource).setJdbcUrl(dataSourceProperties.getUrl()+id);

        return dataSource;

    }
}