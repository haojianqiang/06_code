package pub.dao.mybatis;

import org.apache.ibatis.session.SqlSession;
import pub.dao.query.Query;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;
import pub.dao.sql.model.Columns;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大神未实现完整,UnsupportedOperationException
 */
@SuppressWarnings("unchecked")
public class NamedQuery<T> implements Query<T>, SqlSessionAware{
    private String name;
    private Map<String, Object> _params;
    private Object[] params;

    private transient SqlSession sqlSession;


    public NamedQuery() {
        _params = new HashMap<String, Object>();
    }

    public NamedQuery(String name) {
        this();
        this.name = name;
    }

    public NamedQuery(String name, Object... params) {
        this(name);
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public Query select(String select) {
        throw new UnsupportedOperationException();
    }

    public Query outerSelect(String outerSelect) {
        throw new UnsupportedOperationException();
    }

    public Query innerSelect(String innerSelect) {
        throw new UnsupportedOperationException();
    }

    public Query from(String from) {
        throw new UnsupportedOperationException();
    }

    public Query where(Object where) {
        throw new UnsupportedOperationException();
    }

    public Query distinct(boolean distinct) {
        throw new UnsupportedOperationException();
    }

    public Query orderBy(String orderBy) {
        throw new UnsupportedOperationException();
    }

    public Query groupBy(String groupBy) {
        throw new UnsupportedOperationException();
    }

    public NamedQuery put(String paramName, Object paramValue) {
        assert params == null || params.length == 0;
        _params.put(paramName, paramValue);
        return this;
    }

    public Query setSql(String sql) {
        throw new UnsupportedOperationException();
    }

    public QueryResult<T> getResult() {
        QueryResult queryResult = new QueryResult();
        queryResult.setRows(resultList);
        return queryResult;
    }

    public QuerySettings getSettings() {
        throw new UnsupportedOperationException();
    }

    public void applySettings(QuerySettings settings) {
        throw new UnsupportedOperationException();
    }

    public Query select(Columns columns) {
        throw new UnsupportedOperationException();
    }

    public void setQuietLog() {

    }

    private List resultList = null;

    public void execute() {
        Object param;
        if (params == null || params.length == 0) {
            param = _params;
        } else if (params.length == 1) {
            param = params[0];
        } else {
            param = Arrays.asList(params);
        }
        resultList = sqlSession.selectList(name, param);
    }


}
