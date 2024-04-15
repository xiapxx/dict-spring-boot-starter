package io.github.xiapxx.starter.dict.holder;

import io.github.xiapxx.starter.dict.interfaces.DictProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典持有者
 *
 * @Author xiapeng
 * @Date 2024-04-01 09:59
 */
public class DictHolder implements InitializingBean {

    private static DictHolder INSTANCE;

    private List<DictItem> dictItemList;

    private Map<String, Map<String, DictItem>> businessType2CodeMap;

    private Map<String, Map<String, List<DictItem>>> businessType2ParentCodeMap;

    private DictProvider dictProvider;

    public DictHolder(){
        dictItemList = new ArrayList<>();
    }

    public DictHolder(DictProvider dictProvider){
        this.dictItemList = dictProvider == null ? new ArrayList<>() : dictProvider.getDictList();
    }

    public DictHolder add(String businessType, String parentCode, String code, String name, String nameEn){
        addItem(businessType, parentCode, code, name, nameEn);
        return this;
    }

    public DictHolder add(String businessType, String parentCode, String code, String name){
        addItem(businessType, parentCode, code, name, null);
        return this;
    }

    public DictHolder add(String businessType, String code, String name){
        addItem(businessType, null, code, name, null);
        return this;
    }

    public static DictItem get(String businessType, String code){
        Assert.notNull(INSTANCE, "DictHolder is uninitialized");
        if(!INSTANCE.businessType2CodeMap.containsKey(businessType)){
            return null;
        }
        Map<String, DictItem> code2DictMap = INSTANCE.businessType2CodeMap.get(businessType);
        return code2DictMap == null ? null : code2DictMap.get(code);
    }

    public static List<DictItem> getList(String businessType, String parentCode){
        Assert.notNull(INSTANCE, "DictHolder is uninitialized");
        if(!INSTANCE.businessType2ParentCodeMap.containsKey(businessType)){
            return new ArrayList<>();
        }
        Map<String, List<DictItem>> parentCode2DictListMap = INSTANCE.businessType2ParentCodeMap.get(businessType);
        return parentCode2DictListMap == null ? null : parentCode2DictListMap.get(parentCode);
    }

    private void addItem(String businessType, String parentCode, String code, String name, String nameEn){
        Assert.notNull(businessType, "businessType must not be null");
        Assert.notNull(code, "code must not be null");
        Assert.notNull(name, "name must not be null");

        DictItem dictItem = new DictItem();
        dictItem.setBusinessType(businessType);
        dictItem.setParentCode(parentCode);
        dictItem.setCode(code);
        dictItem.setName(name);
        dictItem.setNameEn(nameEn);
        dictItemList.add(dictItem);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        INSTANCE = this;
        if(dictItemList == null || dictItemList.isEmpty()){
            businessType2CodeMap = new HashMap<>();
            businessType2ParentCodeMap = new HashMap<>();
            return;
        }
        Map<String, List<DictItem>> businessType2DictList = dictItemList.stream().collect(Collectors.groupingBy(item -> item.getBusinessType()));
        businessType2CodeMap = businessType2DictList.entrySet()
                .stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> toCode2DictItem(entry.getValue()), (o, n) -> n));

        businessType2ParentCodeMap = businessType2DictList.entrySet()
                .stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> toParentCode2DictItemListMap(entry.getValue()), (o, n) -> n));
    }

    private Map<String, List<DictItem>> toParentCode2DictItemListMap(List<DictItem> dictItemList){
        return dictItemList.stream().filter(item -> StringUtils.hasText(item.getParentCode())).collect(Collectors.groupingBy(item -> item.getParentCode()));
    }

    private Map<String, DictItem> toCode2DictItem(List<DictItem> dictItemList){
        return dictItemList.stream().collect(Collectors.toMap(item -> item.getCode(), item -> item, (o, n) -> n));
    }

}
