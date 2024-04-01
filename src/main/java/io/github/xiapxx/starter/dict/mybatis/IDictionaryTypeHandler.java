package io.github.xiapxx.starter.dict.mybatis;

import io.github.xiapxx.starter.dict.IDictionary;
import io.github.xiapxx.starter.dict.holder.DictHolder;
import io.github.xiapxx.starter.dict.interfaces.DictKeyTransfer;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author xiapeng
 * @Date 2024-04-01 14:56
 */
public class IDictionaryTypeHandler extends BaseTypeHandler<IDictionary> {

    private DictKeyTransfer dictKeyTransfer;

    private DictHolder dictHolder;

    public IDictionaryTypeHandler(DictKeyTransfer dictKeyTransfer, DictHolder dictHolder){
        this.dictKeyTransfer = dictKeyTransfer == null ? new DefaultDictKeyTransfer() : dictKeyTransfer;
        this.dictHolder = dictHolder;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, IDictionary parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public IDictionary getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String expression = rs.getString(columnName);
        return getDictionary(expression);
    }

    @Override
    public IDictionary getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String expression = rs.getString(columnIndex);
        return getDictionary(expression);
    }

    @Override
    public IDictionary getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String expression = cs.getString(columnIndex);
        return getDictionary(expression);
    }

    public IDictionary getDictionary(String expression){
        if(!StringUtils.hasText(expression)){
            return null;
        }

        DictKey dictKey = dictKeyTransfer.getKey(expression);
        if(dictKey == null || !StringUtils.hasText(dictKey.getCode())){
            return null;
        }
        return dictHolder.getDictionary(dictKey.getBusinessType(), dictKey.getParentCode(), dictKey.getCode());
    }

}
