package com.handu.apollo.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.handu.apollo.base.Page;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by markerking on 14-4-9.
 */
@Repository
public class Dao extends SqlSessionDaoSupport {

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    public void insert(String prefix, String key, Object object) {
        getSqlSession().insert(prefix + key, object);
    }

    public void update(String prefix, String key, Object object) {
        getSqlSession().update(prefix + key, object);
    }

    public void delete(String prefix, String key, Serializable id) {
        getSqlSession().delete(prefix + key, id);
    }

    public void delete(String key, Serializable id) {
        getSqlSession().delete(key, id);
    }

    public void delete(String prefix, String key, Object object) {
        getSqlSession().delete(prefix + key, object);
    }

    public <T> T get(String prefix, String key, Object params) {
        return getSqlSession().selectOne(prefix + key, params);
    }

    public <T> List<T> getList(String prefix, String key) {
        return getSqlSession().selectList(prefix + key);
    }

    public <T> List<T> getList(String prefix, String key, Object params) {
        return getSqlSession().selectList(prefix + key, params);
    }

    public Integer count(String prefix, String key, Object params) {
        return getSqlSession().selectOne(prefix + key, params);
    }

    public Object[] page(String prefix, String pageKey, String countKey, Object params, int offset, int limit) {
        return new Object[]{
                getSqlSession().selectList(prefix + pageKey, params, new RowBounds(offset, limit)),
                getSqlSession().selectOne(prefix + countKey, params)
        };
    }

    public Page page(String prefix, String pageKey, String countKey, Object params, Page pager) {

        pager.setList(getSqlSession().selectList(prefix + pageKey, params, new RowBounds(pager.getStartIndex(), pager.getPagesize())),
                (Integer) getSqlSession().selectOne(prefix + countKey, params));
        return pager;
    }

    public boolean executeSql(String sql) {
        try {
            return getSqlSession().getConnection().prepareStatement(sql).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Map> querySql(String sql) {
        List<Map> list = Lists.newArrayList();
        try {
            ResultSet rs = getSqlSession().getConnection().prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE).executeQuery();
            try {
                ResultSetMetaData rsm = rs.getMetaData(); //获得列集
                int col = rsm.getColumnCount(); //获得列的个数
                String colName[] = new String[col];
                //取结果集中的表头名称, 放在colName数组中
                for (int i = 0; i < col; i++) {
                    colName[i] = rsm.getColumnName(i + 1);
                }
                rs.beforeFirst();
                while (rs.next()) {
                    Map<String, String> map = Maps.newHashMap();
                    for (String aColName : colName) {
                        map.put(aColName, rs.getString(aColName));
                    }
                    list.add(map);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
