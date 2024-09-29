package io.github.xiapxx.starter.dict.webserializer;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.xiapxx.starter.dict.entity.AbstractDict;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import java.util.Set;

/**
 * @Author xiapeng
 * @Date 2024-04-17 09:49
 */
public class DictDeserializerRegister<T extends AbstractDict> implements BeanPostProcessor {

    private Set<Class<? extends AbstractDict>> dictClassSet;

    public DictDeserializerRegister(Set<Class<? extends AbstractDict>> dictClassSet){
        this.dictClassSet = dictClassSet;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof RequestMappingHandlerAdapter) {
            RequestMappingHandlerAdapter requestMappingHandlerAdapter = (RequestMappingHandlerAdapter) bean;
            for (HttpMessageConverter<?> messageConverter : requestMappingHandlerAdapter.getMessageConverters()) {
                if(!(messageConverter instanceof MappingJackson2HttpMessageConverter)){
                    continue;
                }
                MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) messageConverter;
                mappingJackson2HttpMessageConverter.getObjectMapper().registerModule(newSimpleModule());
            }
        }
        return bean;
    }

    private SimpleModule newSimpleModule(){
        SimpleModule simpleModule = new SimpleModule();
        for (Class<? extends AbstractDict> dictClass : dictClassSet) {
            Class<T> itemDictClass = (Class<T>) dictClass;
            simpleModule.addDeserializer(itemDictClass, new DictDeserializer<>(itemDictClass));
        }

        return simpleModule;
    }

}
