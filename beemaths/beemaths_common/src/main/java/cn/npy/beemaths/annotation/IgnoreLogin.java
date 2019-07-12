package cn.npy.beemaths.annotation;

import java.lang.annotation.*;

/**
 * 排除登陆验证
 *
 * @author wangshichang
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreLogin {

    boolean value() default true;

}
