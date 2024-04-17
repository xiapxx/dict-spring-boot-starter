package io.github.xiapxx.starter.dict.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.xiapxx.starter.dict.annotation.Dict;
import io.github.xiapxx.starter.dict.interfaces.AbstractDict;
import io.github.xiapxx.starter.dict.interfaces.DictLanguageGetter;
import java.io.IOException;
import java.lang.reflect.Constructor;

/**
 * @Author xiapeng
 * @Date 2024-04-17 09:51
 */
public class DictDeserializer<T extends AbstractDict> extends JsonDeserializer<T> {

    private Dict dictConfig;

    private Constructor<T> dictConstructor;

    private DictLanguageGetter dictLanguageGetter;

    public DictDeserializer(Dict dictConfig, Class<T> dictClass, DictLanguageGetter dictLanguageGetter){
        this.dictConfig = dictConfig;
        this.dictLanguageGetter = dictLanguageGetter;
        try {
            this.dictConstructor = dictClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if(p.hasTokenId(JsonTokenId.ID_NULL)){
            return null;
        }
        DictItem dictItem = DictHolder.get(dictConfig.value(), p.getValueAsString());
        return newDict(dictItem);
    }

    private T newDict(DictItem dictItem){
        if(dictItem == null){
            return null;
        }
        try {
            T dictInstance = dictConstructor.newInstance();
            dictInstance.init(dictItem, dictLanguageGetter);
            return dictInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
