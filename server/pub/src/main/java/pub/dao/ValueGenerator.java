package pub.dao;

import java.beans.PropertyDescriptor;
import java.sql.Connection;

public interface ValueGenerator {
    Object generate(Connection cn, PropertyDescriptor fieldDesc);
}
