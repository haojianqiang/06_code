package pub.dao.mybatis;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import pub.dao.DataSourceAware;
import pub.dao.GeneralDao;
import pub.dao.jdbc.JdbcTemplateAware;
import pub.types.Executable;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

public class GeneralDaoImpl extends MyBatisDao<Object> implements GeneralDao{

    private DataSource dataSource; // 数据源
    private NamedParameterJdbcTemplate jdbcTemplate; // Spring JDBC

    public GeneralDaoImpl(){
        super(Object.class);
    }

    public void execute(Executable executable) {
        if(executable instanceof SqlSessionAware) {
            ((SqlSessionAware)executable).setSqlSession(getSqlSession());
        }
        if(dataSource != null && executable instanceof DataSourceAware) {
            ((DataSourceAware) executable).setDataSource(dataSource);
        }
        else if(jdbcTemplate != null && executable instanceof JdbcTemplateAware) {
            ((JdbcTemplateAware)executable).setJdbcTemplate(jdbcTemplate);
        }
        executable.execute();
    }

    public Date getSysDate() {
        return null;
    }

    public void flushOperations() {

    }

    public void evict(List entities) {

    }

    public void evict(Object entity) {

    }

    @SuppressWarnings("unchecked")
    public <T> T queryValue(Class<T> clazz, String sql, Object... args) {
        try {
            if(clazz.getName().startsWith("java")) {
                return jdbcTemplate.getJdbcOperations().queryForObject(sql, args, clazz);
            }else {
                RowMapper rowMapper = new BeanPropertyRowMapper(clazz);
                return (T) jdbcTemplate.getJdbcOperations().queryForObject(sql, args, rowMapper);
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            if(e.getActualSize() == 0) {
                return null;
            }else {
                throw e;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> queryList(Class<T> clazz, String sql, Object... args) {
        if (clazz.getName().startsWith("java")){
            return jdbcTemplate.getJdbcOperations().queryForList(sql, args, clazz);
        }else{
            RowMapper rowMapper = new BeanPropertyRowMapper(clazz);
            return jdbcTemplate.getJdbcOperations().query(sql, args, rowMapper);
        }
    }

    public <T> int[] executeBatch(String sql, List<T> param) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(param.toArray());
        return jdbcTemplate.batchUpdate(sql,batch);
    }

    public int execute(String sql, Object... args) {
        return jdbcTemplate.getJdbcOperations().update(sql,args);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
