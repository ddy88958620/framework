package com.handu.apollo.data;

import com.handu.apollo.utils.Log;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.util.Properties;
import java.util.regex.Pattern;


@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class}))
public class PaginationInterceptor implements Interceptor {
    private static final Log LOG = Log.getLog(PaginationInterceptor.class);

    private final static String SQL_SELECT_REGEX = "(?is)^\\s*SELECT.*$";
    private final static String SQL_COUNT_REGEX = "(?is)^\\s*SELECT\\s+COUNT\\s*\\(\\s*(?:\\*|\\w+)\\s*\\).*$";

    //@Override
    public Object intercept(Invocation inv) throws Throwable {
        StatementHandler target = (StatementHandler) inv.getTarget();
        BoundSql boundSql = target.getBoundSql();
        String sql = boundSql.getSql();
        if (StringUtils.isBlank(sql)) {
            return inv.proceed();
        }

        LOG.trace("原始SQL语句 >>>>>> \n" + sql);
        // 只有为select查询语句时才进行下一步
        if (sql.matches(SQL_SELECT_REGEX)
                && !Pattern.matches(SQL_COUNT_REGEX, sql)) {
            Object obj = FieldUtils.readField(target, "delegate", true);
            // 反射获取 RowBounds 对象。
            RowBounds rowBounds = (RowBounds) FieldUtils.readField(obj, "rowBounds", true);
            // 分页参数存在且不为默认值时进行分页SQL构造
            if (rowBounds != null && rowBounds != RowBounds.DEFAULT) {

                // 重写sql
                sql = sql + " LIMIT " + rowBounds.getLimit() + " OFFSET " + rowBounds.getOffset();

                LOG.trace("转换后SQL语句 >>>>>> \n" + sql);

                FieldUtils.writeField(boundSql, "sql", sql, true);

                // 一定要还原否则将无法得到下一组数据(第一次的数据被缓存了)
                FieldUtils.writeField(rowBounds, "offset", RowBounds.NO_ROW_OFFSET, true);
                FieldUtils.writeField(rowBounds, "limit", RowBounds.NO_ROW_LIMIT, true);
            }
        }
        return inv.proceed();
    }

    //@Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    //@Override
    public void setProperties(Properties arg0) {
    }

}