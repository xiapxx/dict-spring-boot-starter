package io.github.xiapxx.starter.dict.core;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import io.github.xiapxx.starter.dict.annotation.Dict;
import io.github.xiapxx.starter.dict.interfaces.AbstractDict;
import io.github.xiapxx.starter.dict.interfaces.DictLanguageGetter;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

/**
 * @Author xiapeng
 * @Date 2024-04-15 16:42
 */
public class DictMybatisPlusTypeHandlerRegister<T extends AbstractDict> implements ConfigurationCustomizer {

    @Autowired
    private ObjectProvider<DictLanguageGetter> dictLanguageGetterObjectProvider;

    @Override
    public void customize(MybatisConfiguration configuration) {
        DictLanguageGetter dictLanguageGetter = dictLanguageGetterObjectProvider.getIfAvailable();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        for (Map.Entry<Class<? extends AbstractDict>, Dict> entry : DictRegister.class2DictMap.entrySet()) {
            Class<T> dictClazz = (Class<T>) entry.getKey();
            typeHandlerRegistry.register(dictClazz, new DictTypeHandler<>(dictClazz, entry.getValue(), dictLanguageGetter));
        }
    }

}
