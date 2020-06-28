package project.common.DbUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)//口、类、枚举、注解
//运行时 会存在与class字节码中 运行时可以通过反射获取
@Retention(RetentionPolicy.RUNTIME)
public @interface DbFile {
    String value();
}
