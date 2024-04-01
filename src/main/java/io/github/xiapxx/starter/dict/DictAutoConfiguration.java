package io.github.xiapxx.starter.dict;

import io.github.xiapxx.starter.dict.holder.DictHolder;
import io.github.xiapxx.starter.dict.interfaces.DictProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @Author xiapeng
 * @Date 2024-04-01 10:27
 */
public class DictAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DictHolder.class)
    @ConditionalOnBean(DictProvider.class)
    public DictHolder dictHolder(DictProvider dictProvider){
        return new DictHolder(dictProvider);
    }

}
