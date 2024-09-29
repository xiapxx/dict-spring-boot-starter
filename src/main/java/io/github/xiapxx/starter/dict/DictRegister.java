package io.github.xiapxx.starter.dict;

import io.github.xiapxx.starter.dict.annotation.DictScanner;
import io.github.xiapxx.starter.dict.holder.DictHolderConfigurator;
import io.github.xiapxx.starter.dict.mybatistypehandler.MybatisPlusTypeHandlerRegister;
import io.github.xiapxx.starter.dict.mybatistypehandler.MybatisTypeHandlerRegister;
import io.github.xiapxx.starter.dict.webserializer.DictDeserializerRegister;
import io.github.xiapxx.starter.dict.entity.AbstractDict;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import java.util.Set;

/**
 * 字典注册服务类
 */
public class DictRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importMetadata, BeanDefinitionRegistry registry) {
        Set<Class<? extends AbstractDict>> dictClassSet = scanDictClass(importMetadata);
        if(dictClassSet == null || dictClassSet.isEmpty()){
            return;
        }
        registerMybatisTypeHandlerRegister(registry, dictClassSet);
        registerWebDeserializerRegister(registry, dictClassSet);
        registerDictHolderConfigurator(registry);
    }

    /**
     * 注册字典持有者的配置器
     *
     * @param registry registry
     */
    private void registerDictHolderConfigurator(BeanDefinitionRegistry registry){
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DictHolderConfigurator.class);
        registry.registerBeanDefinition(DictHolderConfigurator.class.getName(), beanDefinitionBuilder.getBeanDefinition());
    }

    /**
     * 如果项目依赖了spring-boot-starter-web, 那么将支持前端传入的code转换为字典对象
     *
     * @param registry registry
     * @param dictClassSet 字典类
     */
    private void registerWebDeserializerRegister(BeanDefinitionRegistry registry, Set<Class<? extends AbstractDict>> dictClassSet){
        try {
            Class.forName("org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter");
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DictDeserializerRegister.class);
            beanDefinitionBuilder.addConstructorArgValue(dictClassSet);
            registry.registerBeanDefinition(DictDeserializerRegister.class.getName(), beanDefinitionBuilder.getBeanDefinition());
        } catch (ClassNotFoundException e) {
        }
    }

    /**
     * 如果项目依赖了mybatis或mybatis plus, 那么将支持数据库中查询出的code转换为字典对象
     *
     * @param registry registry
     * @param dictClassSet 字典类
     */
    private void registerMybatisTypeHandlerRegister(BeanDefinitionRegistry registry, Set<Class<? extends AbstractDict>> dictClassSet){
        Class typeHandlerRegisterClass = getTypeHandlerRegister();
        if(typeHandlerRegisterClass == null){
            return;
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(typeHandlerRegisterClass);
        beanDefinitionBuilder.addConstructorArgValue(dictClassSet);
        registry.registerBeanDefinition(typeHandlerRegisterClass.getName(), beanDefinitionBuilder.getBeanDefinition());
    }

    /**
     * 扫描字典类
     *
     * @param importMetadata importMetadata
     * @return 字典类
     */
    private Set<Class<? extends AbstractDict>> scanDictClass(AnnotationMetadata importMetadata){
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(DictScanner.class.getName()));
        String[] basePackages = annoAttrs.getStringArray("basePackages");
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(basePackages));
        return reflections.getSubTypesOf(AbstractDict.class);
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
            typeHandlerRegisterClass = MybatisPlusTypeHandlerRegister.class;
        } catch (ClassNotFoundException e) {
        }

        if(typeHandlerRegisterClass == null){
            try {
                Class.forName("org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer");
                typeHandlerRegisterClass = MybatisTypeHandlerRegister.class;
            } catch (ClassNotFoundException e) {
            }
        }
        return typeHandlerRegisterClass;
    }
}
