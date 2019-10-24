package com.gpdi.miniprogram.config;

import com.jolbox.bonecp.BoneCPDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import pub.dao.GeneralDao2;
import pub.dao.mybatis.GeneralDaoImpl;
import pub.dao.mybatis.GeneralDaoImpl2;
import pub.dao.mybatis.support.MySqlSessionFactoryBean;
import javax.sql.DataSource;

@Configuration
public class Bonecp2Configuration {
    @Value("${jdbc2.url}")
    private String jdbcUrl;
    @Value("${jdbc2.username}")
    private String username;
    @Value("${jdbc2.password}")
    private String password;
    @Value("${jdbc2.driverClass}")
    private String driverClass;
    private boolean lazyInit = true;
    private Integer partitionCount = 1;
    private Integer minConnectionsPerPartition = 1;
    private Integer maxConnectionsPerPartition = 32;
    private Integer poolAvailabilityThreshold = 0;
    private Integer acquireIncrement = 1;
    private boolean transactionRecoveryEnabled = false;
    private Integer idleConnectionTestPeriodInMinutes = 10;
    private String connectionTestStatement = "/* ping */ select 1";

    // 数据库连接池
    @Bean(name = "dataSource2")
    public DataSource dataSource2(){
        BoneCPDataSource dataSource2 = new BoneCPDataSource();
        try {
            dataSource2.setJdbcUrl(jdbcUrl);
            dataSource2.setUsername(username);
            dataSource2.setPassword(password);
            dataSource2.setDriverClass(driverClass);
            dataSource2.setLazyInit(lazyInit);
            dataSource2.setPartitionCount(partitionCount);
            dataSource2.setMinConnectionsPerPartition(minConnectionsPerPartition);
            dataSource2.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
            dataSource2.setPoolAvailabilityThreshold(poolAvailabilityThreshold);
            dataSource2.setPoolAvailabilityThreshold(poolAvailabilityThreshold);
            dataSource2.setAcquireIncrement(acquireIncrement);
            dataSource2.setTransactionRecoveryEnabled(transactionRecoveryEnabled);
            dataSource2.setIdleConnectionTestPeriod(idleConnectionTestPeriodInMinutes);
            dataSource2.setConnectionTestStatement(connectionTestStatement);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return dataSource2;
    }

    @Bean(name = "sqlSessionFactory2")
    public MySqlSessionFactoryBean sqlSessionFactory2(DataSource dataSource2){
        MySqlSessionFactoryBean sqlSessionFactory = new MySqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource2);
        // Mapper、entity路径配置
        try{
            PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            sqlSessionFactory.setMapperLocations(resourcePatternResolver.getResources("classpath*:/sql/**/*.xml"));
            sqlSessionFactory.setEntityPackages(new String[]{"com.example.web.entity.**.*"});
        }catch (Exception e){
            e.printStackTrace();
        }

        return sqlSessionFactory;
    }

    @Bean(name = "sqlSessionTemplate2")
    public SqlSessionTemplate sqlSessionTemplate2(SqlSessionFactory sqlSessionFactory2){
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory2);
        return sqlSessionTemplate;
    }

    @Bean(name = "namedParameterJdbcTemplate2")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate2(DataSource dataSource2){
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource2);
        return jdbcTemplate;
    }

    @Bean(name = "generalDao2")
    public GeneralDao2 generalDao2(DataSource dataSource2, SqlSessionTemplate sqlSessionTemplate2, NamedParameterJdbcTemplate namedParameterJdbcTemplate2){
        GeneralDaoImpl2 generalDao2 = new GeneralDaoImpl2();
        generalDao2.setSqlSessionTemplate(sqlSessionTemplate2);
        generalDao2.setDataSource(dataSource2);
        generalDao2.setJdbcTemplate(namedParameterJdbcTemplate2);
        return generalDao2;
    }
}
