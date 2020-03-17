package cn.banban.common.desensitization;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要脱敏的字段
 *
 * @author : banxiaohua
 * @date : 2020/3/17 10:56 AM
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Desensitization {

    /**
     * 脱敏方法
     * @return DesensitizationEnum
     */
    DesensitizationEnum value();
}
