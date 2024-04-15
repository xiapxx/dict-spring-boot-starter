package io.github.xiapxx.starter.dict.core;

import io.github.xiapxx.starter.dict.annotation.Dict;
import io.github.xiapxx.starter.dict.holder.DictHolder;
import io.github.xiapxx.starter.dict.holder.DictItem;
import io.github.xiapxx.starter.dict.interfaces.AbstractDict;
import io.github.xiapxx.starter.dict.interfaces.DictLanguageGetter;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.lang.reflect.Constructor;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author xiapeng
 * @Date 2024-04-01 14:56
 */
public class DictTypeHandler<T extends AbstractDict> extends BaseTypeHandler<T> {

    private Dict dictConfig;

    private Constructor<T> dictConstructor;

    private DictLanguageGetter dictLanguageGetter;

    public DictTypeHandler(Class<T> dictClass, Dict dictConfig, DictLanguageGetter dictLanguageGetter){
        this.dictConfig = dictConfig;
        this.dictLanguageGetter = dictLanguageGetter;
        try {
            this.dictConstructor = dictClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        String code = parameter.getCode();
        ps.setString(i, code);
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        DictItem dictItem = DictHolder.get(dictConfig.value(), code);
        return newDict(dictItem);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        DictItem dictItem = DictHolder.get(dictConfig.value(), code);
        return newDict(dictItem);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        DictItem dictItem = DictHolder.get(dictConfig.value(), code);
        return newDict(dictItem);
    }

    private T newDict(DictItem dictItem){
        if(dictItem == null){
            return null;
        }
        try {
            T dictInstance = dictConstructor.newInstance();
            dictInstance.init(dictItem, dictLanguageGetter);
            return dictInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
