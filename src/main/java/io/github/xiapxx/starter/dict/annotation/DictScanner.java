package io.github.xiapxx.starter.dict.annotation;

import io.github.xiapxx.starter.dict.DictRegister;
import org.springframework.context.annotation.Import;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author xiapeng
 * @Date 2024-04-15 16:22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DictRegister.class)
public @interface DictScanner {

    String[] basePackages();

}
