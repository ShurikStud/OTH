package com.omegaauto.shurik.mobilesklad.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnoProperty {
    String apiName() default (""); // имя реквизита в json-ответе от API сервера. Если пустая строка - значит используется имя поля класса Container
}
