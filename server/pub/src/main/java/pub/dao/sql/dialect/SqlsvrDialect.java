package pub.dao.sql.dialect;

public class SqlsvrDialect implements Dialect{
    @Override
    public boolean supportsTop() {
        return true;
    }

    @Override
    public boolean supportsLimit() {
        return false;
    }

    @Override
    public boolean supportsOffset() {
        return false;
    }
}
