package com.mipo.db.config;

public enum DsTypeEnum {

  READ("只读数据源"),
  WRITE("读写数据源");

  private String desc;

  DsTypeEnum(String desc) {
    this.desc = desc;
  }

}
