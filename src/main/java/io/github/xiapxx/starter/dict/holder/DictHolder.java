package io.github.xiapxx.starter.dict.holder;

import io.github.xiapxx.starter.dict.entity.AbstractDict;
import io.github.xiapxx.starter.dict.interfaces.DictLanguageGetter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Map;

/**
 * 字典持有者
 *
 * @Author xiapeng
 * @Date 2024-04-01 09:59
 */
public class DictHolder {

    static DictLanguageGetter dictLanguageGetter;

    static boolean init = false;

    static String initFailMessage;

    static Map<Class<? extends AbstractDict>, Map<String, ? extends AbstractDict>> dictClass2Code2DataMap;

    static Map<Class<? extends AbstractDict>,  Map<String, ? extends List<? extends AbstractDict>>> dictClass2ParentCode2DataListMap;

    static Map<Class<? extends AbstractDict>,  Map<String, ? extends List<? extends AbstractDict>>> dictClass2Name2DataListMap;

    private DictHolder(){}

    /**
     * 根据字典编码获取字典对象
     *
     * @param code code
     * @param dictClass dictClass
     * @return 字典对象
     * @param <T>
     */
    public static <T extends AbstractDict> T get(Long code, Class<T> dictClass) {
        if(code == null){
            return null;
        }
        return get(code.toString(), dictClass);
    }

    /**
     * 根据字典编码获取字典对象
     *
     * @param code code
     * @param dictClass dictClass
     * @return 字典对象
     * @param <T>
     */
    public static <T extends AbstractDict> T get(Integer code, Class<T> dictClass) {
       if(code == null){
           return null;
       }
       return get(code.toString(), dictClass);
    }

    /**
     * 根据字典编码获取字典对象
     *
     * @param code code
     * @param dictClass dictClass
     * @return 字典对象
     * @param <T>
     */
    public static <T extends AbstractDict> T get(String code, Class<T> dictClass) {
        if(!StringUtils.hasLength(code) || dictClass == null || dictClass == AbstractDict.class){
            return null;
        }
        Assert.isTrue(init, initFailMessage);
        Map<String, ? extends AbstractDict> code2DataMap = dictClass2Code2DataMap.get(dictClass);
        if(code2DataMap == null){
            return null;
        }
        return (T) code2DataMap.get(code);
    }

    /**
     * 根据名称获取字典数据
     *
     * @param name name
     * @param dictClass dictClass
     * @return 字典集合
     * @param <T>
     */
    public static <T extends AbstractDict> List<T> getByName(String name, Class<T> dictClass){
        if(!StringUtils.hasLength(name) || dictClass == null || dictClass == AbstractDict.class){
            return null;
        }
        Assert.isTrue(init, initFailMessage);
        Map<String, ? extends List<? extends AbstractDict>> name2DataList = dictClass2Name2DataListMap.get(dictClass);
        if(name2DataList == null){
            return null;
        }
        return (List<T>) name2DataList.get(name);
    }

    /**
     * 根据父编码获取字典集合
     *
     * @param parentCode parentCode
     * @param dictClass dictClass
     * @return 字典集合
     * @param <T>
     */
    public static <T extends AbstractDict> List<T> getList(Long parentCode, Class<T> dictClass) {
        if(parentCode == null){
            return null;
        }
        return getList(parentCode.toString(), dictClass);
    }

    /**
     * 根据父编码获取字典集合
     *
     * @param parentCode parentCode
     * @param dictClass dictClass
     * @return 字典集合
     * @param <T>
     */
    public static <T extends AbstractDict> List<T> getList(Integer parentCode, Class<T> dictClass) {
       if(parentCode == null){
           return null;
       }
       return getList(parentCode.toString(), dictClass);
    }

    /**
     * 根据父编码获取字典集合
     *
     * @param parentCode parentCode
     * @param dictClass dictClass
     * @return 字典集合
     * @param <T>
     */
    public static <T extends AbstractDict> List<T> getList(String parentCode, Class<T> dictClass) {
        if(!StringUtils.hasLength(parentCode) || dictClass == null || dictClass == AbstractDict.class){
            return null;
        }
        Assert.isTrue(init, initFailMessage);
        Map<String, ? extends List<? extends AbstractDict>> parentCode2DataListMap = dictClass2ParentCode2DataListMap.get(dictClass);
        if(parentCode2DataListMap == null){
            return null;
        }
        return (List<T>) parentCode2DataListMap.get(parentCode);
    }

    /**
     * 获取字典名称
     *
     * @param name 中文名称
     * @param nameEn 英文名称
     * @return 字典名称
     */
    public static String getName(String name, String nameEn) {
        if(!StringUtils.hasLength(name) && !StringUtils.hasLength(nameEn)) {
            return null;
        }
        Assert.isTrue(init, initFailMessage);
        return dictLanguageGetter == null || dictLanguageGetter.isChinese() ? name : nameEn;
    }

    /**
     * 是否初始化
     *
     * @return true/false
     */
    public static boolean isInit(){
        return init;
    }

}
