package com.example.common.DbUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)//字段
//运行时 会存在与class字节码中 运行时可以通过反射获取
@Retention(RetentionPolicy.RUNTIME)
public @interface DbPrimary {

}
