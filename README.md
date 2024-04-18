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
        DictItem dictItem = DictHolder.get("SEX_CODE", "1");
        ...
    
    2. 如果项目依赖了mybatis或mybatisplus: 
    
        @Dict("SEX_CODE")
        class XXXDict extends AbstractDict {...}

        class XXEntity {
            XXXDict sex; // 数据库中的 1或0将直接转换为XXXDict对象(包含code, name等信息)
        }
        
        List<XXEntity> list = mapper.selectXXX(..);

    3. 如果项目依赖了spring web:  将支持前端传入的code转换成字典对象;
                                将支持字典对象转换为json供前端使用;
                                将支持通过实现DictLanguageGetter接口决定输出字典name是中文还是英文
       另外, 如果需自定义com.fasterxml.jackson.databind.ObjectMapper对象, 请务必Jackson2ObjectMapperBuilder来实例化ObjectMapper对象, 例如:
       
       @Bean
       public com.fasterxml.jackson.databind.ObjectMapper objectMapper(org.springframework.http.converter.json.Jackson2ObjectMapperBuilder builder){
            ObjectMapper objectMapper = builder.build();
            ...
            return objectMapper;
       }

    
