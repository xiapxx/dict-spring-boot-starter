package io.github.xiapxx.starter.dict.mybatis;

import java.io.Serializable;

/**
 * @Author xiapeng
 * @Date 2024-04-01 13:49
 */
public class IDictionary implements Serializable {

    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
