package io.github.xiapxx.starter.dict.interfaces;

import io.github.xiapxx.starter.dict.holder.DictItem;

/**
 * @Author xiapeng
 * @Date 2024-04-15 15:47
 */
public abstract class AbstractDict {

    private DictItem dictItem;

    private String name;

    private DictLanguageGetter dictLanguageGetter;

    public final void init(DictItem dictItem, DictLanguageGetter dictLanguageGetter){
        this.dictItem = dictItem;
        this.dictLanguageGetter = dictLanguageGetter;
    }

    public final String getBusinessType() {
        return dictItem.getBusinessType();
    }

    public final String getParentCode(){
        return dictItem.getParentCode();
    }

    public final String getCode(){
        return dictItem.getCode();
    }

    public final String getName(){
        if(this.name == null){
            this.name = dictLanguageGetter == null || dictLanguageGetter.isChinese() ? dictItem.getName() : dictItem.getNameEn();
        }
        return this.name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
