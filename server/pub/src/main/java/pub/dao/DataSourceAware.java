package pub.dao;

import javax.sql.DataSource;

public interface DataSourceAware {
    void setDataSource(DataSource dataSource);
}
