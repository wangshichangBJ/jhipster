package cn.npy.beemaths.aop.repeatinvoke;

import cn.npy.beemaths.domain.ResultInfo;
import cn.npy.beemaths.web.rest.errors.ServiceRetConstError;
import cn.npy.beemaths.web.rest.util.MyDesUtil;
import cn.npy.beemaths.web.rest.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class RepeatInvokeAspect {

    @Autowired
    private RedisUtil redis;

    @Pointcut(value = "@annotation(preventRepeat)",argNames = "preventRepeat")
    private void repeatInvokePointcut(PreventRepeat preventRepeat) {

    }

    @Around(value = "repeatInvokePointcut(preventRepeat)", argNames = "jp,preventRepeat")
    public Object preventRepeatInvoke(ProceedingJoinPoint jp, PreventRepeat preventRepeat) throws Throwable {

        // 获取重复提交注解中的参数
        int second = preventRepeat.storeSeconds();

        // 获取方法签名对象
        Signature signature = jp.getSignature();

        // 获取类名，方法名，参数列表
        String methodName = signature.getName();
        String className = signature.getDeclaringTypeName();
        String args = Arrays.toString(jp.getArgs());

        String invokeInfo = className + methodName + args;
        String key = MyDesUtil.md5(invokeInfo);

        log.info("prevent repeat key:{},second:{}",key,second);

        Object obj = null;
        if(redis.hasKey(key)){
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.fail(ServiceRetConstError.REPEAT_INVOKE_ERROR,String.valueOf(second));
            obj = resultInfo;

        }else{
            redis.set(key,true,second);
            obj = jp.proceed(jp.getArgs());
        }

        return obj;
    }
}
