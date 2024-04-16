使用先决条件
    1. 配置类上标注@DictScanner注解:
       例如：
       @Configuration
       @DictScanner(basePackages="com.xxx.biz")   // 开启字典功能
       class XXXconfig {...}
   
    2. 加载字典数据
        @Service
        class XXServiceImpl implements DictProvider {

            public List<DictItem> getDictList() {
                 List<DictItem> list = new ArrayList<>();
                 DictItem item0 = new DictItem();
                 item0.setBusinessType("SEX_CODE");
                 item0.setCode("1");
                 item0.setBusinessType("男");
                 ...
                 list.add(item0)
                 return list;
            }
        }
        
       
    
使用方法
    1. 通过DictHolder获取字典:
        DictItem dictItem = DictHolderget("SEX_CODE", "1");
        ...
    
    2. 如果项目依赖了mybatis或mybatisplus: 
    
        @Dict("SEX_CODE")
        class XXXDict extends AbstractDict {...}

        class XXEntity {
            XXXDict sex; // 数据库中的 1或0将直接转换为XXXDict对象(包含code, name等信息)
        }
        
        List<XXEntity> list = mapper.selectXXX(..);
    
    
