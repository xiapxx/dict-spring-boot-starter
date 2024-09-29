package io.github.xiapxx.starter.dict.mybatistypehandler;

import io.github.xiapxx.starter.dict.entity.AbstractDict;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import java.util.Set;

/**
 * @Author xiapeng
 * @Date 2024-04-15 16:43
 */
public class MybatisTypeHandlerRegister<T extends AbstractDict> implements ConfigurationCustomizer {

    private Set<Class<? extends AbstractDict>> dictClassSet;

    public MybatisTypeHandlerRegister(Set<Class<? extends AbstractDict>> dictClassSet){
        this.dictClassSet = dictClassSet;
    }

    @Override
    public void customize(Configuration configuration) {
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        for (Class<? extends AbstractDict> dictClass : dictClassSet) {
            Class<T> itemDictClass = (Class<T>) dictClass;
            typeHandlerRegistry.register(itemDictClass, new MybatisTypeHandler<>(itemDictClass));
        }
    }

}
