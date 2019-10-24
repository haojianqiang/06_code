package pub.dao.sql.dialect;

public class OracleDialect implements Dialect{
    @Override
    public boolean supportsTop() {
        return false;
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
