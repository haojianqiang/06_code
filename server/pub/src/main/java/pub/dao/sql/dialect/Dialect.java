package pub.dao.sql.dialect;

public interface Dialect {
    boolean supportsTop();
    boolean supportsLimit();
    boolean supportsOffset();
}
