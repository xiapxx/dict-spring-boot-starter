package io.github.xiapxx.starter.dict.mybatistypehandler;

import io.github.xiapxx.starter.dict.enums.DictCodeJdbcType;
import io.github.xiapxx.starter.dict.holder.DictHolder;
import io.github.xiapxx.starter.dict.entity.AbstractDict;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mybatis的入参和出参都支持字典对象
 *
 * @Author xiapeng
 * @Date 2024-04-01 14:56
 */
public class MybatisTypeHandler<T extends AbstractDict> extends BaseTypeHandler<T> {

   private Class<T> dictClass;

    public MybatisTypeHandler(Class<T> dictClass) {
        this.dictClass = dictClass;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T dict, JdbcType jdbcType) throws SQLException {
        DictCodeJdbcType dictCodeType = dict.codeJdbcType();
        switch (dictCodeType) {
            case INT:
                ps.setInt(i, dict.getIntCode());
                break;
            case LONG:
                ps.setLong(i, dict.getLongCode());
                break;
            case STRING:
                ps.setString(i, dict.getCode());
                break;
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return DictHolder.get(code, dictClass);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return DictHolder.get(code, dictClass);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return DictHolder.get(code, dictClass);
    }

}
