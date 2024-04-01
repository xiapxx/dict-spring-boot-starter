前提条件
    初始化字典数据: 实现DictProvider接口， 并放入spring容器  或者 将DictHolder放入spring容器

使用方法
    1. 通过DictHolder来获取字典
    2. 如果项目依赖了mybatis或mybatis plus
    
       sql:
       select 'businessType#100#1' dict from ...

       实体:
       class XXEntity {
          private IDictionary dict;
       }
    
