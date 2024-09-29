package io.github.xiapxx.starter.dict.webserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.xiapxx.starter.dict.holder.DictHolder;
import io.github.xiapxx.starter.dict.entity.AbstractDict;
import java.io.IOException;

/**
 * 前端传入的编码转换为字典对象
 *
 * @Author xiapeng
 * @Date 2024-04-17 09:51
 */
public class DictDeserializer<T extends AbstractDict> extends JsonDeserializer<T> {

    private Class<T> dictClass;

    public DictDeserializer(Class<T> dictClass) {
       this.dictClass = dictClass;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if(p.hasTokenId(JsonTokenId.ID_NULL)){
            return null;
        }
        String code = p.getValueAsString();
        return DictHolder.get(code, dictClass);
    }

}
