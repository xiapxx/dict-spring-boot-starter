package io.github.xiapxx.starter.dict.core;

import io.github.xiapxx.starter.dict.annotation.Dict;
import io.github.xiapxx.starter.dict.annotation.DictScanner;
import io.github.xiapxx.starter.dict.interfaces.AbstractDict;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author xiapeng
 * @Date 2024-04-15 16:27
 */
public class DictRegister implements ImportSelector {

    static final Map<Class<? extends AbstractDict>, Dict> class2DictMap = new HashMap<>();

    @Override
    public String[] selectImports(AnnotationMetadata importMetadata) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(DictScanner.class.getName()));
        String[] basePackages = annoAttrs.getStringArray("basePackages");
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(basePackages));
        Set<Class<? extends AbstractDict>> dictClassSets = reflections.getSubTypesOf(AbstractDict.class);
        for (Class<? extends AbstractDict> dictClass : dictClassSets) {
            if(!dictClass.isAnnotationPresent(Dict.class)){
                continue;
            }
            Dict dict = dictClass.getAnnotation(Dict.class);
            class2DictMap.put(dictClass, dict);
        }

        if(class2DictMap.isEmpty()){
            return null;
        }
        String typeHandlerRegister = getTypeHandlerRegister();
        return typeHandlerRegister == null ? null : new String[]{typeHandlerRegister};
    }

    public String getTypeHandlerRegister() {
        String typeHandlerRegister = null;
        try {
            Class.forName("com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer");
            typeHandlerRegister = DictMybatisPlusTypeHandlerRegister.class.getName();
        } catch (ClassNotFoundException e) {
        }

        if(typeHandlerRegister == null){
            try {
                Class.forName("org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer");
                typeHandlerRegister = DictMybatisTypeHandlerRegister.class.getName();
            } catch (ClassNotFoundException e) {
            }
        }
        return typeHandlerRegister;
    }
}
