package io.github.xiapxx.starter.dict.core;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.xiapxx.starter.dict.annotation.Dict;
import io.github.xiapxx.starter.dict.interfaces.AbstractDict;
import io.github.xiapxx.starter.dict.interfaces.DictLanguageGetter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import java.util.Map;

/**
 * @Author xiapeng
 * @Date 2024-04-17 09:49
 */
public class DictSerializerRegister implements BeanPostProcessor {

    @Autowired
    private ObjectProvider<DictLanguageGetter> dictLanguageGetterObjectProvider;

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
        DictLanguageGetter dictLanguageGetter = dictLanguageGetterObjectProvider.getIfAvailable();
        for (Map.Entry<Class<? extends AbstractDict>, Dict> entry : DictRegister.class2DictMap.entrySet()) {
            Class dictClass = entry.getKey();
            simpleModule.addDeserializer(dictClass, new DictDeserializer<>(entry.getValue(), dictClass, dictLanguageGetter));
        }
        return simpleModule;
    }

}
