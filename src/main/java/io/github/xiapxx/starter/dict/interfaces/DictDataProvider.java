package io.github.xiapxx.starter.dict.interfaces;

import io.github.xiapxx.starter.dict.entity.DictDataContext;

/**
 * 字典数据提供者
 *
 * @Author xiapeng
 * @Date 2024-09-28 11:34
 */
public interface DictDataProvider {

    /**
     * 添加字典数据
     *
     * @param dictDataContext dictDataContext
     */
    void addDictData(DictDataContext dictDataContext);

}
