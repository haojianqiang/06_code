package pub.dao.mybatis.support;

import org.apache.ibatis.session.*;
import org.springframework.util.ReflectionUtils;
import pub.types.Action;
import pub.types.Builder;
import pub.types.ReloadTrigger;
import pub.types.Reloadable;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Set;

@SuppressWarnings("unchecked")
public class ReloadableSqlSessionFactory implements SqlSessionFactory,Reloadable {

    private Builder<SqlSessionFactory> ssfBuilder;
    private SqlSessionFactory ssf;

    public ReloadableSqlSessionFactory(Builder<SqlSessionFactory> ssfBuilder) {
        this(ssfBuilder, null);
    }

    public ReloadableSqlSessionFactory(Builder<SqlSessionFactory> ssfBuilder, ReloadTrigger trigger) {
        this.ssfBuilder = ssfBuilder;
        buildSsf();

        Configuration configuration = this.ssf.getConfiguration();
        Field field = ReflectionUtils.findField(configuration.getClass(), "loadedResources");
        ReflectionUtils.makeAccessible(field);
        Set<String> resources = (Set<String>) ReflectionUtils.getField(field, configuration);
        System.out.println("?");

        if (trigger != null) {
            trigger.setTriggerAction(new Action() {
                public void execute(Object param) {
                    buildSsf();
                }
            });
        }
    }

    private synchronized void buildSsf() {
        try {
            this.ssf = ssfBuilder.build();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public SqlSession openSession() {
        return ssf.openSession();
    }

    public SqlSession openSession(boolean autoCommit) {
        return ssf.openSession(autoCommit);
    }

    public SqlSession openSession(Connection connection) {
        return ssf.openSession(connection);
    }

    public SqlSession openSession(TransactionIsolationLevel level) {
        return ssf.openSession(level);
    }

    public SqlSession openSession(ExecutorType execType) {
        return ssf.openSession(execType);
    }

    public SqlSession openSession(ExecutorType execType, boolean autoCommit) {
        return ssf.openSession(execType, autoCommit);
    }

    public SqlSession openSession(ExecutorType execType, TransactionIsolationLevel level) {
        return ssf.openSession(execType, level);
    }

    public SqlSession openSession(ExecutorType execType, Connection connection) {
        return ssf.openSession(execType, connection);
    }

    public Configuration getConfiguration() {
        return ssf.getConfiguration();
    }

    public void reload() {
        buildSsf();
    }
}
