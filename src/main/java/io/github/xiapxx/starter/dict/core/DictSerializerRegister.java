package io.github.xiapxx.starter.dict.core;

import io.github.xiapxx.starter.dict.annotation.Dict;
import io.github.xiapxx.starter.dict.interfaces.AbstractDict;
import io.github.xiapxx.starter.dict.interfaces.DictLanguageGetter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import java.util.Map;

/**
 * @Author xiapeng
 * @Date 2024-04-17 09:49
 */
public class DictSerializerRegister implements Jackson2ObjectMapperBuilderCustomizer {

    @Autowired
    private ObjectProvider<DictLanguageGetter> dictLanguageGetterObjectProvider;

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        DictLanguageGetter dictLanguageGetter = dictLanguageGetterObjectProvider.getIfAvailable();
        for (Map.Entry<Class<? extends AbstractDict>, Dict> entry : DictRegister.class2DictMap.entrySet()) {
            builder.deserializerByType(entry.getKey(), new DictDeserializer<>(entry.getValue(), entry.getKey(), dictLanguageGetter));
        }
    }

}
