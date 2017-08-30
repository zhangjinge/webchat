package com.amayadream.webchat.dao;

import com.amayadream.webchat.utils.DBCPUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by dell on 2017/5/2.
 */
public class TsQueryRunner extends QueryRunner{
    /**
     * 1. 得到连接
     * 2. 执行父类方法，传递连接对象
     * 3. 释放连接
     * 4. 返回值
     */
    @Override
    public int[] batch(String sql, Object[][] params) throws SQLException {
        Connection con = DBCPUtils.getConnection();
        int[] result = super.batch(con, sql, params);
        DBCPUtils.releaseConnection(con);
        return result;
    }

    @Override
    public <T> T query(String sql, Object param, ResultSetHandler<T> rsh)
            throws SQLException {
        Connection con = DBCPUtils.getConnection();
        T result = super.query(con, sql, param, rsh);
        DBCPUtils.releaseConnection(con);
        return result;
    }

    @Override
    public <T> T query(String sql, Object[] params, ResultSetHandler<T> rsh)
            throws SQLException {
        Connection con = DBCPUtils.getConnection();
        T result = super.query(con, sql, params, rsh);
        DBCPUtils.releaseConnection(con);
        return result;
    }

    @Override
    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params)
            throws SQLException {
        Connection con = DBCPUtils.getConnection();
        T result = super.query(con, sql, rsh, params);
        DBCPUtils.releaseConnection(con);
        return result;
    }

    @Override
    public <T> T query(String sql, ResultSetHandler<T> rsh) throws SQLException {
        Connection con = DBCPUtils.getConnection();
        T result = super.query(con, sql, rsh);
        DBCPUtils.releaseConnection(con);
        return result;
    }

    @Override
    public int update(String sql) throws SQLException {
        Connection con = DBCPUtils.getConnection();
        int result = super.update(con, sql);
        DBCPUtils.releaseConnection(con);
        return result;
    }

    @Override
    public int update(String sql, Object param) throws SQLException {
        Connection con = DBCPUtils.getConnection();
        int result = super.update(con, sql, param);
        DBCPUtils.releaseConnection(con);
        return result;
    }

    @Override
    public int update(String sql, Object... params) throws SQLException {
        Connection con = DBCPUtils.getConnection();
        int result = super.update(con, sql, params);
        DBCPUtils.releaseConnection(con);
        return result;
    }
}
