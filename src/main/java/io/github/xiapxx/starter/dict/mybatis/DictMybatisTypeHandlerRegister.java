package io.github.xiapxx.starter.dict.mybatis;

import io.github.xiapxx.starter.dict.IDictionary;
import io.github.xiapxx.starter.dict.holder.DictHolder;
import io.github.xiapxx.starter.dict.interfaces.DictKeyTransfer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author xiapeng
 * @Date 2024-04-01 14:51
 */
public class DictMybatisTypeHandlerRegister implements ConfigurationCustomizer {

    @Autowired
    private ObjectProvider<DictKeyTransfer> dictKeyTransferObjectProvider;

    @Autowired
    private DictHolder dictHolder;

    @Override
    public void customize(Configuration configuration) {
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        IDictionaryTypeHandler dictionaryTypeHandler = new IDictionaryTypeHandler(dictKeyTransferObjectProvider.getIfAvailable(), dictHolder);
        typeHandlerRegistry.register(IDictionary.class, dictionaryTypeHandler);
    }

}
