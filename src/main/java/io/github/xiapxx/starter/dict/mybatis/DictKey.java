package io.github.xiapxx.starter.dict.mybatis;

/**
 * @Author xiapeng
 * @Date 2024-04-01 13:53
 */
public class DictKey {

    private String businessType;

    private String parentCode;

    private String code;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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
}
