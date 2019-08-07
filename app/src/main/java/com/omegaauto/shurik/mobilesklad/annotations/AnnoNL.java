package com.omegaauto.shurik.mobilesklad.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnoNL {
    char separator() default (','); // символ-разделитель который необходимо заменять на перевод строки
}
