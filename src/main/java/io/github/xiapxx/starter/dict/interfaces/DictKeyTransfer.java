package io.github.xiapxx.starter.dict.interfaces;

import io.github.xiapxx.starter.dict.mybatis.DictKey;

/**
 * @Author xiapeng
 * @Date 2024-04-01 14:02
 */
@FunctionalInterface
public interface DictKeyTransfer {

    DictKey getKey(String expression);

}
