package io.github.xiapxx.starter.dict.interfaces;

import io.github.xiapxx.starter.dict.holder.DictItem;
import java.util.List;

/**
 * @Author xiapeng
 * @Date 2024-04-01 10:30
 */
public interface DictProvider {

    List<DictItem> getDictList();

}
