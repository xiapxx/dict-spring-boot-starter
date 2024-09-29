package io.github.xiapxx.starter.dict.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.xiapxx.starter.dict.enums.DictCodeJdbcType;
import io.github.xiapxx.starter.dict.holder.DictHolder;
import org.springframework.util.StringUtils;
import java.io.Serializable;

/**
 * @Author xiapeng
 * @Date 2024-04-15 15:47
 */
public abstract class AbstractDict<PARENT extends AbstractDict> implements Serializable {

    private String parentCode;

    private String code;

    private String name;

    private String nameEn;

    private String actualName;

    private Class<PARENT> parentDictClass;

    public PARENT parent() {
        return DictHolder.get(parentCode, parentDictClass);
    }

    @JsonIgnore
    public Long getLongParentCode(){
        if(!StringUtils.hasLength(parentCode)){
            return null;
        }
        return Long.valueOf(parentCode);
    }

    @JsonIgnore
    public Integer getIntParentCode(){
        if(!StringUtils.hasLength(parentCode)){
            return null;
        }
        return Integer.valueOf(parentCode);
    }

    @JsonIgnore
    public Integer getIntCode(){
        if(!StringUtils.hasLength(code)){
            return null;
        }
        return Integer.valueOf(code);
    }

    @JsonIgnore
    public Long getLongCode(){
        if(!StringUtils.hasLength(code)){
            return null;
        }
        return Long.valueOf(code);
    }

    public DictCodeJdbcType codeJdbcType() {
        return DictCodeJdbcType.STRING;
    }

    public void initParentDictClass(Class<PARENT> parentDictClass) {
        this.parentDictClass = parentDictClass;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        if(!DictHolder.isInit()){
            return StringUtils.hasLength(name) ? name : nameEn;
        }
        if(actualName == null){
            actualName = DictHolder.getName(name, nameEn);
        }
        return actualName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Override
    public String toString() {
        return getName();
    }

}
