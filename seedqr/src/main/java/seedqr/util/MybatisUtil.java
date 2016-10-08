/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.util;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.jndi.JndiDataSourceFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.transaction.TransactionFactory;
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
            TransactionFactory transactionFactory = new JdbcTransactionFactory();
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

    public static <M> M getMapper(Class<M> mapperType) {
        return sessionFactory.openSession().getMapper(mapperType);
    }

    public static <M> void run(Class<M> mapperType, Consumer<M> task) {
        call(mapperType, mapper -> {
            task.accept(mapper);
            return null;
        });
    }

    public static <M1, M2> void run(Class<M1> mapperType1, 
            Class<M2> mapperType2, BiConsumer<M1, M2> task) {
        call(mapperType1, mapperType2, (mapper1, mapper2) -> {
            task.accept(mapper1, mapper2);
            return null;
        });
    }

    public static <M, R> R call(Class<M> mapperType, Function<M, R> task) {
        SqlSession sqlSession = sessionFactory.openSession(false);
        try {
            R result = task.apply(sqlSession.getMapper(mapperType));
            sqlSession.commit();
            return result;
        } catch (Exception ex) {
            sqlSession.rollback();
            ex.printStackTrace();
            return null;
        } finally {
            sqlSession.close();
        }
    }

    public static <M1, M2, R> R call(Class<M1> mapperType1, 
            Class<M2> mapperType2, BiFunction<M1, M2, R> task) {
        SqlSession sqlSession = sessionFactory.openSession(false);
        try {
            R result = task.apply(sqlSession.getMapper(mapperType1),
                    sqlSession.getMapper(mapperType2));
            sqlSession.commit();
            return result;
        } catch (Exception ex) {
            sqlSession.rollback();
            ex.printStackTrace();
            return null;
        } finally {
            sqlSession.close();
        }
    }
}
