package pub.dao;

import pub.types.Executable;

import java.util.Date;
import java.util.List;

public interface GeneralDao {
    void execute(Executable executable);
    Date getSysDate();
    void flushOperations();

    void evict(List entities);
    void evict(Object entity);

    //
    <T> T queryValue(Class<T> clazz, String sql, Object... args);
    <T> List<T> queryList(Class<T> clazz, String sql, Object... args);

    <T> int[] executeBatch(String sql, List<T> param);

    int execute(String sql, Object... args);
}
