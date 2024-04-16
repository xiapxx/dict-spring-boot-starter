package io.github.xiapxx.starter.dict.core;

import io.github.xiapxx.starter.dict.interfaces.DictProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典持有者
 *
 * @Author xiapeng
 * @Date 2024-04-01 09:59
 */
public class DictHolder implements SmartInitializingSingleton, ApplicationContextAware {

    private static DictHolder INSTANCE;

    private volatile boolean init = false;

    private Map<String, Map<String, DictItem>> businessType2CodeMap = new HashMap<>();

    private Map<String, Map<String, List<DictItem>>> businessType2ParentCodeMap = new HashMap<>();

    private ApplicationContext applicationContext;

    private DictHolder(){}

    static DictHolder newInstance(){
        DictHolder dictHolder = new DictHolder();
        DictHolder.INSTANCE = dictHolder;
        return dictHolder;
    }

    public static DictItem get(String businessType, String code){
        INSTANCE.afterSingletonsInstantiated();
        if(!INSTANCE.businessType2CodeMap.containsKey(businessType)){
            return null;
        }
        Map<String, DictItem> code2DictMap = INSTANCE.businessType2CodeMap.get(businessType);
        return code2DictMap == null ? null : code2DictMap.get(code);
    }

    public static List<DictItem> getList(String businessType, String parentCode){
        INSTANCE.afterSingletonsInstantiated();
        if(!INSTANCE.businessType2ParentCodeMap.containsKey(businessType)){
            return new ArrayList<>();
        }
        Map<String, List<DictItem>> parentCode2DictListMap = INSTANCE.businessType2ParentCodeMap.get(businessType);
        return parentCode2DictListMap == null ? null : parentCode2DictListMap.get(parentCode);
    }

    private Map<String, List<DictItem>> toParentCode2DictItemListMap(List<DictItem> dictItemList){
        return dictItemList.stream().filter(item -> StringUtils.hasText(item.getParentCode())).collect(Collectors.groupingBy(item -> item.getParentCode()));
    }

    private Map<String, DictItem> toCode2DictItem(List<DictItem> dictItemList){
        return dictItemList.stream().collect(Collectors.toMap(item -> item.getCode(), item -> item, (o, n) -> n));
    }

    @Override
    public void afterSingletonsInstantiated() {
        if(init){
            return;
        }
        synchronized (DictHolder.class) {
            if(init){
                return;
            }
            doInit();
        }
    }

    private void doInit(){
        try {
            Map<String, DictProvider> dictProviderMap = applicationContext.getBeansOfType(DictProvider.class);
            if(dictProviderMap == null || dictProviderMap.isEmpty()){
                return;
            }

            List<DictItem> dictItemList = new LinkedList<>();
            for (DictProvider dictProvider : dictProviderMap.values()) {
                dictItemList.addAll(dictProvider.getDictList());
            }
            loadBusinessTypeMap(dictItemList);
        } catch (Throwable e) {
        } finally {
            init = true;
        }
    }

    private void loadBusinessTypeMap(List<DictItem> dictItemList){
        Map<String, List<DictItem>> businessType2DictList = dictItemList.stream().collect(Collectors.groupingBy(item -> item.getBusinessType()));
        businessType2CodeMap = businessType2DictList.entrySet()
                .stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> toCode2DictItem(entry.getValue()), (o, n) -> n));

        businessType2ParentCodeMap = businessType2DictList.entrySet()
                .stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> toParentCode2DictItemListMap(entry.getValue()), (o, n) -> n));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
