package pub.dao;

import java.io.Serializable;
import java.util.List;

public interface Dao<T> {
    T get(Serializable id);

    List<T> getAll();
    List<T> getAll(String orderBy);

    Serializable save(T t);

    void delete(T t);
    void delete(Serializable id);
    void delete(Serializable[] ids);
}
