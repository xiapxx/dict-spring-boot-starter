package io.github.xiapxx.starter.dict.mybatis;

import io.github.xiapxx.starter.dict.interfaces.DictKeyTransfer;

/**
 * @Author xiapeng
 * @Date 2024-04-01 14:18
 */
public class DefaultDictKeyTransfer implements DictKeyTransfer {

    private static final String DELIMITER = "#";

    @Override
    public DictKey getKey(String expression) {
        if(expression == null){
            return null;
        }
        DictKey dictKey = new DictKey();
        String[] stringArray = expression.split(DELIMITER, 3);
        if(stringArray.length != 3){
            return null;
        }
        dictKey.setBusinessType(stringArray[0]);
        dictKey.setParentCode(stringArray[1]);
        dictKey.setCode(stringArray[1]);
        return dictKey;
    }

}
