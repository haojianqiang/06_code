package pub.dao.mybatis;

import org.apache.ibatis.session.SqlSession;

public interface SqlSessionAware {
    void setSqlSession(SqlSession sqlSession);
}
