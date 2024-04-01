package io.github.xiapxx.starter.dict.mybatis;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

/**
 * @Author xiapeng
 * @Date 2024-04-01 14:42
 */
public class DictTypeHandlerRegister implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        String typeHandlerRegister = getTypeHandlerRegister();
        if(!StringUtils.hasText(typeHandlerRegister)){
            return null;
        }
        return new String[]{typeHandlerRegister};
    }

    public String getTypeHandlerRegister() {
        String typeHandlerRegister = null;
        try {
            Class.forName("com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer");
            typeHandlerRegister = DictMybatisPlusTypeHandlerRegister.class.getName();
        } catch (ClassNotFoundException e) {
        }

        if(typeHandlerRegister == null){
            try {
                Class.forName("org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer");
                typeHandlerRegister = DictMybatisTypeHandlerRegister.class.getName();
            } catch (ClassNotFoundException e) {
            }
        }
        return typeHandlerRegister;
    }
}
