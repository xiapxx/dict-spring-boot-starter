package io.github.xiapxx.starter.dict.core;

/**
 * 1. 需保证businessType+code联合唯一
 * 2. businessType,code,name等三个字段必填, 其他字段选填
 *
 * @Author xiapeng
 * @Date 2024-04-01 10:28
 */
public class DictItem {

   private String businessType;

   private String parentCode;

   private String code;

   private String name;

   private String nameEn;


   public String getBusinessType() {
      return businessType;
   }

   public void setBusinessType(String businessType) {
      this.businessType = businessType;
   }

   public String getParentCode() {
      return parentCode;
   }

   public void setParentCode(String parentCode) {
      this.parentCode = parentCode;
   }

   public String getCode() {
      return code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getNameEn() {
      return nameEn;
   }

   public void setNameEn(String nameEn) {
      this.nameEn = nameEn;
   }

}
