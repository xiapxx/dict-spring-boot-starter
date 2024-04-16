package io.github.xiapxx.starter.dict.core;

import io.github.xiapxx.starter.dict.annotation.Dict;
import io.github.xiapxx.starter.dict.annotation.DictScanner;
import io.github.xiapxx.starter.dict.interfaces.AbstractDict;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author xiapeng
 * @Date 2024-04-15 16:27
 */
public class DictRegister implements ImportBeanDefinitionRegistrar {

    static final Map<Class<? extends AbstractDict>, Dict> class2DictMap = new HashMap<>();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importMetadata, BeanDefinitionRegistry registry) {
        String dictHolderBeanName = registerDictHolder(registry);

        loadClass2DictMap(importMetadata);

        registerTypeHandlerRegister(registry, dictHolderBeanName);
    }

    /**
     * 注册type handler的注册对象
     *
     * @param registry registry
     * @param dictHolderBeanName dictHolderBeanName
     */
    private void registerTypeHandlerRegister(BeanDefinitionRegistry registry, String dictHolderBeanName){
        if(class2DictMap.isEmpty()){
            return;
        }
        Class typeHandlerRegisterClass = getTypeHandlerRegister();
        if(typeHandlerRegisterClass == null){
            return;
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(typeHandlerRegisterClass).addDependsOn(dictHolderBeanName);
        registry.registerBeanDefinition(typeHandlerRegisterClass.getName(), beanDefinitionBuilder.getBeanDefinition());
    }

    /**
     * 加载class2DictMap
     *
     * @param importMetadata importMetadata
     */
    private void loadClass2DictMap(AnnotationMetadata importMetadata){
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
    }

    /**
     * 注册DictHolder
     *
     * @param registry registry
     * @return DictHolder的beanName
     */
    private String registerDictHolder(BeanDefinitionRegistry registry){
        String dictHolderBeanName = DictHolder.class.getName();
        registry.registerBeanDefinition(dictHolderBeanName, BeanDefinitionBuilder.genericBeanDefinition(DictHolder.class, () -> DictHolder.newInstance()).getBeanDefinition());
        return dictHolderBeanName;
    }

    /**
     * 获取type handler的注册类对象
     *
     * @return type handler的注册类对象
     */
    private Class getTypeHandlerRegister() {
        Class typeHandlerRegisterClass = null;
        try {
            Class.forName("com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer");
            typeHandlerRegisterClass = DictMybatisPlusTypeHandlerRegister.class;
        } catch (ClassNotFoundException e) {
        }

        if(typeHandlerRegisterClass == null){
            try {
                Class.forName("org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer");
                typeHandlerRegisterClass = DictMybatisTypeHandlerRegister.class;
            } catch (ClassNotFoundException e) {
            }
        }
        return typeHandlerRegisterClass;
    }
}
