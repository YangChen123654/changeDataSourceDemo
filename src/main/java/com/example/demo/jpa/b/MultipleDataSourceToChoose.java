package com.example.demo.jpa.b;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

/**
 * @author yang chen
 * @date 2019/4/18 11:02
 */

public class MultipleDataSourceToChoose extends AbstractRoutingDataSource {

    protected Object determineCurrentLookupKey() {
        return HandlerDataSource.getDataSource();
    }

}
