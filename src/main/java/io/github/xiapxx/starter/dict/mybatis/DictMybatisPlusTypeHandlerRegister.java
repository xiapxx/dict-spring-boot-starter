package io.github.xiapxx.starter.dict.mybatis;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import io.github.xiapxx.starter.dict.holder.DictHolder;
import io.github.xiapxx.starter.dict.interfaces.DictKeyTransfer;
import io.github.xiapxx.starter.dict.interfaces.DictLanguageGetter;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author xiapeng
 * @Date 2024-04-01 14:51
 */
public class DictMybatisPlusTypeHandlerRegister implements ConfigurationCustomizer {

    @Autowired
    private ObjectProvider<DictKeyTransfer> dictKeyTransferObjectProvider;

    @Autowired
    private ObjectProvider<DictLanguageGetter> dictLanguageGetterObjectProvider;

    @Autowired
    private DictHolder dictHolder;

    @Override
    public void customize(MybatisConfiguration configuration) {
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        IDictionaryTypeHandler dictionaryTypeHandler = new IDictionaryTypeHandler(dictKeyTransferObjectProvider.getIfAvailable(),
                dictLanguageGetterObjectProvider.getIfAvailable(), dictHolder);
        typeHandlerRegistry.register(IDictionary.class, dictionaryTypeHandler);
    }

}
