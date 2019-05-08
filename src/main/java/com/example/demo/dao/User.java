package com.example.demo.dao;

import javax.persistence.*;

/**
 * @author yang chen
 * @date 2019/4/17 17:00
 */
@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "database_name")
    private String databaseName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
