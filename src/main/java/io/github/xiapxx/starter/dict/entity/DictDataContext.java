package io.github.xiapxx.starter.dict.entity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author xiapeng
 * @Date 2024-09-27 15:31
 */
public class DictDataContext {

    private Map<Class<? extends AbstractDict>, List<? extends AbstractDict>> dictClass2DictDataMap;

    public DictDataContext(Map<Class<? extends AbstractDict>, List<? extends AbstractDict>> dictClass2DictDataMap){
        this.dictClass2DictDataMap = dictClass2DictDataMap;
    }

    public <T extends AbstractDict> void put(Class<T> dictClass, List<T> dictList) {
        dictClass2DictDataMap.putIfAbsent(dictClass, dictList);
    }

    public Map<Class<? extends AbstractDict>, List<? extends AbstractDict>> getDictClass2DictDataMap(){
        return Collections.unmodifiableMap(dictClass2DictDataMap);
    }


}
