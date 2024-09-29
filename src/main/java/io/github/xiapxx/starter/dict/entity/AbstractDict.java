package io.github.xiapxx.starter.dict.entity;

import io.github.xiapxx.starter.dict.enums.DictCodeType;
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

    public DictCodeType codeType() {
        return DictCodeType.STRING;
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
