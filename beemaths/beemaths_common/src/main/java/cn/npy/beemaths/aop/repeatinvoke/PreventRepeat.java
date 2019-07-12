package cn.npy.beemaths.aop.repeatinvoke;

import java.lang.annotation.*;

/**
 * Created by wangshichang on 2017/8/30.
 * 防止重复提交
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreventRepeat {
   int storeSeconds() default 60;
}
