package io.github.xiapxx.starter.dict.enums;

/**
 * 字典code类型;
 * 设置jdbc的参数时, 如果不严格按照code类型设置jdbc参数, 会导致索引失效
 *
 * @Author xiapeng
 * @Date 2024-09-28 12:35
 */
public enum DictCodeJdbcType {
    STRING, INT, LONG;

}
