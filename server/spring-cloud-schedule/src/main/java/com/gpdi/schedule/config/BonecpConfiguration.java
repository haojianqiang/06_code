package com.gpdi.schedule.config;

import com.jolbox.bonecp.BoneCPDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import pub.dao.GeneralDao;
import pub.dao.mybatis.GeneralDaoImpl;
import pub.dao.mybatis.support.MySqlSessionFactoryBean;

import javax.sql.DataSource;

@Configuration
public class BonecpConfiguration {
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    @Value("${jdbc.driverClass}")
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
    @Bean(name = "dataSource")
    public DataSource dataSource(){
        BoneCPDataSource dataSource = new BoneCPDataSource();
        try {
            dataSource.setJdbcUrl(jdbcUrl);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSource.setDriverClass(driverClass);
            dataSource.setLazyInit(lazyInit);
            dataSource.setPartitionCount(partitionCount);
            dataSource.setMinConnectionsPerPartition(minConnectionsPerPartition);
            dataSource.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
            dataSource.setPoolAvailabilityThreshold(poolAvailabilityThreshold);
            dataSource.setPoolAvailabilityThreshold(poolAvailabilityThreshold);
            dataSource.setAcquireIncrement(acquireIncrement);
            dataSource.setTransactionRecoveryEnabled(transactionRecoveryEnabled);
            dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriodInMinutes);
            dataSource.setConnectionTestStatement(connectionTestStatement);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return dataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public MySqlSessionFactoryBean sqlSessionFactory(DataSource dataSource){
        MySqlSessionFactoryBean sqlSessionFactory = new MySqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        // Mapper、entity路径配置
        try{
            PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            sqlSessionFactory.setMapperLocations(resourcePatternResolver.getResources("classpath*:/sql/*.xml"));
            sqlSessionFactory.setEntityPackages(new String[]{"com.gpdi.schedule.entity.**.*"});
        }catch (Exception e){
            e.printStackTrace();
        }

        return sqlSessionFactory;
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
        return sqlSessionTemplate;
    }

    @Bean(name = "namedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource){
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        return jdbcTemplate;
    }

    @Bean(name = "generalDao")
    public GeneralDao generalDao(DataSource dataSource, SqlSessionTemplate sqlSessionTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        GeneralDaoImpl generalDao = new GeneralDaoImpl();
        generalDao.setSqlSessionTemplate(sqlSessionTemplate);
        generalDao.setDataSource(dataSource);
        generalDao.setJdbcTemplate(namedParameterJdbcTemplate);
        return generalDao;
    }

}
