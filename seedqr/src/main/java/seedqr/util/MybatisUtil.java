/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.util;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.jndi.JndiDataSourceFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

public class MybatisUtil {

    private static SqlSessionFactory sessionFactory;
    static {
        try {
            Properties properties = new Properties();
            properties.put(JndiDataSourceFactory.DATA_SOURCE, "java:/ds/seedqr");
            JndiDataSourceFactory dsFactory = new JndiDataSourceFactory();
            dsFactory.setProperties(properties);
            DataSource dataSource = dsFactory.getDataSource();
            TransactionFactory transactionFactory = new ManagedTransactionFactory();
            Environment environment = new Environment("development", transactionFactory, dataSource);
            Configuration configuration = new Configuration(environment);
            configuration.addMappers("seedqr.mapper");
            sessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SqlSessionFactory getInstance() {
        return sessionFactory;
    }
}
