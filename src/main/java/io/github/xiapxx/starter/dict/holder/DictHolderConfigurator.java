package io.github.xiapxx.starter.dict.holder;

import io.github.xiapxx.starter.dict.entity.AbstractDict;
import io.github.xiapxx.starter.dict.entity.DictDataContext;
import io.github.xiapxx.starter.dict.interfaces.DictAfterLoader;
import io.github.xiapxx.starter.dict.interfaces.DictDataProvider;
import io.github.xiapxx.starter.dict.interfaces.DictLanguageGetter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * dict holder配置器
 *
 * @Author xiapeng
 * @Date 2024-09-28 11:45
 */
public class DictHolderConfigurator implements SmartInitializingSingleton, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        boolean loadDictDataSuccess = loadDictData();
        if(!loadDictDataSuccess){
            return;
        }
        ObjectProvider<DictLanguageGetter> dictLanguageGetterObjectProvider = applicationContext.getBeanProvider(DictLanguageGetter.class);
        DictHolder.dictLanguageGetter = dictLanguageGetterObjectProvider.getIfAvailable();
        DictHolder.init = true;
        dictAfterLoad();
    }

    /**
     * 字典成功加载后的行为
     */
    private void dictAfterLoad(){
        Map<String, DictAfterLoader> dictAfterLoadMap = applicationContext.getBeansOfType(DictAfterLoader.class);
        if(dictAfterLoadMap == null || dictAfterLoadMap.isEmpty()){
            return;
        }
        for (DictAfterLoader dictAfterLoader : dictAfterLoadMap.values()) {
            dictAfterLoader.afterDictLoad();
        }
    }

    /**
     * 加载字典数据
     *
     * @return 是否加载成功
     */
    private boolean loadDictData() {
        Map<String, DictDataProvider> dictDataProviderMap = applicationContext.getBeansOfType(DictDataProvider.class);
        if(dictDataProviderMap == null || dictDataProviderMap.isEmpty()){
            DictHolder.initFailMessage = "请实现DictDataProvider接口, 提供字典数据";
            return false;
        }
        Map<Class<? extends AbstractDict>, List<? extends AbstractDict>> dictClass2DictDataMap = new HashMap<>();
        DictDataContext dictDataContext = new DictDataContext(dictClass2DictDataMap);
        for (DictDataProvider dictDataProvider : dictDataProviderMap.values()) {
            dictDataProvider.addDictData(dictDataContext);
        }

        if(dictClass2DictDataMap == null || dictClass2DictDataMap.isEmpty()){
            DictHolder.initFailMessage = "请检查实现了DictDataProvider接口的类, 这些类未提供任何字典数据";
            return false;
        }

        doLoadDictData(dictClass2DictDataMap);
        return true;
    }

    /**
     * 开始加载字典数据
     *
     * @param dictClass2DictDataMap dictClass2DictDataMap
     */
    private void doLoadDictData(Map<Class<? extends AbstractDict>, List<? extends AbstractDict>> dictClass2DictDataMap){
        Map<Class<? extends AbstractDict>, Map<String, ? extends AbstractDict>> dictClass2Code2DataMap = new HashMap<>();
        Map<Class<? extends AbstractDict>,  Map<String, ? extends List<? extends AbstractDict>>> dictClass2ParentCode2DataListMap = new HashMap<>();
        Map<Class<? extends AbstractDict>,  Map<String, ? extends List<? extends AbstractDict>>> dictClass2Name2DataListMap = new HashMap<>();
        for (Map.Entry<Class<? extends AbstractDict>, List<? extends AbstractDict>> dictClass2DictList : dictClass2DictDataMap.entrySet()) {
            Class<? extends AbstractDict> dictClass = dictClass2DictList.getKey();
            List<? extends AbstractDict> dictList = dictClass2DictList.getValue();
            initParentDictClass(dictClass, dictList);

            Map<String, ? extends AbstractDict> code2DictMap = dictList
                    .stream()
                    .collect(Collectors.toMap(item -> item.getCode(), item -> item, (o, n) -> n));
            dictClass2Code2DataMap.put(dictClass, code2DictMap);

            Map<String, ? extends List<? extends AbstractDict>> parentCode2DataListMap = dictList
                    .stream()
                    .collect(Collectors.groupingBy(item -> item.getParentCode()));

            dictClass2ParentCode2DataListMap.put(dictClass, parentCode2DataListMap);

            Map<String, ? extends List<? extends AbstractDict>> name2DataListMap = dictList
                    .stream()
                    .collect(Collectors.groupingBy(item -> item.getName()));
            dictClass2Name2DataListMap.put(dictClass, name2DataListMap);
        }
        DictHolder.dictClass2Code2DataMap = dictClass2Code2DataMap;
        DictHolder.dictClass2ParentCode2DataListMap = dictClass2ParentCode2DataListMap;
        DictHolder.dictClass2Name2DataListMap = dictClass2Name2DataListMap;
    }

    /***
     * 初始化父字典类
     *
     * @param dictClass dictClass
     * @param dictList dictList
     */
    private void initParentDictClass(Class<? extends AbstractDict> dictClass, List<? extends AbstractDict> dictList){
        ResolvableType resolvableType = ResolvableType.forClass(dictClass).as(AbstractDict.class).getGeneric();
        if(resolvableType == null){
            return;
        }

        Class<?> parentDictClass = resolvableType.resolve();
        if(parentDictClass == null || AbstractDict.class == parentDictClass){
            return;
        }

        for (AbstractDict abstractDict : dictList) {
            abstractDict.initParentDictClass(parentDictClass);
        }
    }

}
