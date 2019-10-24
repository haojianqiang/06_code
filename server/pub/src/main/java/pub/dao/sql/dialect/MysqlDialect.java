package pub.dao.sql.dialect;

public class MysqlDialect implements Dialect{
    @Override
    public boolean supportsTop() {
        return false;
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public boolean supportsOffset() {
        return true;
    }
}
