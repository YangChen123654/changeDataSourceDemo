package com.example.demo.jpa.a;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yang chen
 * @date 2019/4/17 13:23
 */
@Service
public class DynamicDataSource extends AbstractRoutingDataSource {

    // 默认数据源，也就是主库
    protected DataSource masterDataSource;
    // 保存动态创建的数据源
    private static  Map targetDataSource = new HashMap<>();

    @Override
    protected DataSource determineTargetDataSource() {
        // 根据数据库选择方案，拿到要访问的数据库
        String dataSourceName = determineCurrentLookupKey();
        if("default".equals(dataSourceName)) {
            // 访问默认主库
            return masterDataSource;
        }

        // 根据数据库名字，从已创建的数据库中获取要访问的数据库
        DataSource dataSource = (DataSource) targetDataSource.get(dataSourceName);
        if(null == dataSource) {
            // 从已创建的数据库中获取要访问的数据库，如果没有则创建一个
            dataSource = this.selectDataSource(dataSourceName);
        }
        return dataSource;
    }

    @Override
    protected String determineCurrentLookupKey() {
        String dataSourceName = DynamicDataSourceContextHolder.getDataSourceType();
        if (dataSourceName == null || dataSourceName == "default") {
            // 默认的数据源名字
            dataSourceName = "default";
        }
        return dataSourceName;
    }

    /*public void setTargetDataSource(Map targetDataSource) {
        this.targetDataSource = targetDataSource;
        super.setTargetDataSources(this.targetDataSource);
    }*/

    /*public Map getTargetDataSource() {
        return this.targetDataSource;
    }*/

    public void addTargetDataSource(String key, DataSource dataSource) {
        this.targetDataSource.put(key, dataSource);
        //setTargetDataSources(this.targetDataSource);
    }


    /**
     * 该方法为同步方法，防止并发创建两个相同的数据库
     * 使用双检锁的方式，防止并发
     * @param dbType
     * @return
     */
    private synchronized DataSource selectDataSource(String dbType) {
        // 再次从数据库中获取，双检锁
        DataSource obj = (DataSource)this.targetDataSource.get(dbType);
        if (null != obj) {
            return obj;
        }
        // 为空则创建数据库
        DataSource dataSource = this.getDataSource(dbType);
        if (null != dataSource) {
            // 将新创建的数据库保存到map中
            this.setDataSource(dbType, dataSource);

        }else {
            System.out.println("创建数据源失败！");
        }
        return dataSource;
    }

    /**
     * 查询对应数据库的信息
     * @param dbtype
     * @return
     */
    private DataSource getDataSource(String dbtype) {
        String oriType = DynamicDataSourceContextHolder.getDataSourceType();
        // 先切换回主库
        DynamicDataSourceContextHolder.setDataSourceType("dataSource");


        String url = ""+" id ";
        DataSource dataSource = new ComboPooledDataSource();
        ((ComboPooledDataSource) dataSource).setJdbcUrl(url);
        return dataSource;
    }

    //创建SQLServer数据源
    private DataSource createDataSource(String url,String userName,String password) {
        return createDataSource("com.microsoft.sqlserver.jdbc.SQLServerDriver", url, userName, password);
    }

    //创建数据源
    private DataSource createDataSource(String driverClassName, String url,
                                             String username, String password) {
        DataSource dataSource = new ComboPooledDataSource();
//        dataSource.setDriverClassName(driverClassName);
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        dataSource.setTestWhileIdle(true);

        return dataSource;
    }

    public void setDataSource(String type, DataSource dataSource) {
        this.addTargetDataSource(type, dataSource);
        DynamicDataSourceContextHolder.setDataSourceType(type);
    }

   @Override
    public void setTargetDataSources(Map targetDataSources) {
        // TODO Auto-generated method stub
        super.setTargetDataSources(targetDataSources);
        // 重点：通知container容器数据源发生了变化
        afterPropertiesSet();
    }


    /**
     * 该方法重写为空，因为AbstractRoutingDataSource类中会通过此方法将，targetDataSources变量中保存的数据源交给resolvedDefaultDataSource变量
     * 在本方案中动态创建的数据源保存在了本类的targetDataSource变量中。如果不重写该方法为空，会因为targetDataSources变量为空报错
     * 如果仍然想要使用AbstractRoutingDataSource类中的变量保存数据源，则需要在每次数据源变更时，调用此方法来为resolvedDefaultDataSource变量更新
     */
    @Override
    public void afterPropertiesSet() {
    }

    public DataSource getMasterDataSource() {
        return masterDataSource;
    }

    public void setMasterDataSource(DataSource masterDataSource) {
        this.masterDataSource = masterDataSource;
    }
}
