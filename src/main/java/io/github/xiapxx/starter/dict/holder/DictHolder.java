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

    private static final String DEFAULT_BUSINESS_TYPE = "_default_type";

    private static final String DEFAULT_PARENT_CODE = "_default_parent_code";

    private List<DictItem> dictItemList;

    private Map<String, Map<String, Map<String, DictItem>>> dictMap;

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

    public DictHolder add(String parentKey, String code, String name){
        addItem(null, parentKey, code, name, null);
        return this;
    }

    public DictHolder add(String code, String name){
        addItem(null, null, code, name, null);
        return this;
    }


    public DictItem getItem(String businessType, String parentCode, String code){
        String type = StringUtils.hasText(businessType) ? DEFAULT_BUSINESS_TYPE : businessType;
        String parent = StringUtils.hasText(parentCode) ? DEFAULT_PARENT_CODE : parentCode;
        if(!dictMap.containsKey(type) || !dictMap.get(businessType).containsKey(parent)){
            return null;
        }
        return dictMap.get(type).get(parent).get(code);
    }

    private void addItem(String businessType, String parentCode, String code, String name, String nameEn){
        Assert.notNull(code, "code must not be null");
        Assert.notNull(name, "name must not be null");

        DictItem dictItem = new DictItem();
        dictItem.setBusinessType(businessType == null ? DEFAULT_BUSINESS_TYPE : businessType);
        dictItem.setParentCode(parentCode == null ? DEFAULT_PARENT_CODE : parentCode);
        dictItem.setCode(code);
        dictItem.setName(name);
        dictItem.setNameEn(nameEn);
        dictItemList.add(dictItem);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(dictItemList == null || dictItemList.isEmpty()){
            dictMap = new HashMap<>();
            return;
        }
        Map<String, List<DictItem>> businessType2DictList = dictItemList.stream().collect(Collectors.groupingBy(item -> item.getBusinessType()));
        dictMap = businessType2DictList.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> toParentCode2CodeMap(entry.getValue()), (o, n) -> n));
    }


    private Map<String, Map<String, DictItem>> toParentCode2CodeMap(List<DictItem> dictItemList){
        Map<String, List<DictItem>> parentCode2DictList = dictItemList.stream().collect(Collectors.groupingBy(item -> item.getParentCode()));
        return parentCode2DictList.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> toCode2DictItem(entry.getValue()), (o, n) -> n));
    }

    private Map<String, DictItem> toCode2DictItem(List<DictItem> dictItemList){
        return dictItemList.stream().collect(Collectors.toMap(item -> item.getCode(), item -> item, (o, n) -> n));
    }

}
