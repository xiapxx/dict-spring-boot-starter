package io.github.xiapxx.starter.dict.mybatistypehandler;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import io.github.xiapxx.starter.dict.entity.AbstractDict;
import org.apache.ibatis.type.TypeHandlerRegistry;
import java.util.Set;

/**
 * @Author xiapeng
 * @Date 2024-04-15 16:42
 */
public class MybatisPlusTypeHandlerRegister<T extends AbstractDict> implements ConfigurationCustomizer {

    private Set<Class<? extends AbstractDict>> dictClassSet;

    public MybatisPlusTypeHandlerRegister(Set<Class<? extends AbstractDict>> dictClassSet){
        this.dictClassSet = dictClassSet;
    }

    @Override
    public void customize(MybatisConfiguration configuration) {
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        for (Class<? extends AbstractDict> dictClass : dictClassSet) {
            Class<T> itemDictClass = (Class<T>) dictClass;
            typeHandlerRegistry.register(itemDictClass, new MybatisTypeHandler<>(itemDictClass));
        }
    }

}
