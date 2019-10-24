package pub.dao.jdbc;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public interface JdbcTemplateAware {
    void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate);
}
