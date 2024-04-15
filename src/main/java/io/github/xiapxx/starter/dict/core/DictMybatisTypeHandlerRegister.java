package io.github.xiapxx.starter.dict.core;

import io.github.xiapxx.starter.dict.annotation.Dict;
import io.github.xiapxx.starter.dict.interfaces.AbstractDict;
import io.github.xiapxx.starter.dict.interfaces.DictLanguageGetter;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

/**
 * @Author xiapeng
 * @Date 2024-04-15 16:43
 */
public class DictMybatisTypeHandlerRegister<T extends AbstractDict> implements ConfigurationCustomizer {

    @Autowired
    private ObjectProvider<DictLanguageGetter> dictLanguageGetterObjectProvider;

    @Override
    public void customize(Configuration configuration) {
        DictLanguageGetter dictLanguageGetter = dictLanguageGetterObjectProvider.getIfAvailable();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        for (Map.Entry<Class<? extends AbstractDict>, Dict> entry : DictRegister.class2DictMap.entrySet()) {
            Class<T> dictClazz = ( Class<T>) entry.getKey();
            typeHandlerRegistry.register(dictClazz, new DictTypeHandler<>(dictClazz, entry.getValue(), dictLanguageGetter));
        }
    }

}
