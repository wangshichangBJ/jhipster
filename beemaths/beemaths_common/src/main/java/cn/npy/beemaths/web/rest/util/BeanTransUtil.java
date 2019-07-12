package cn.npy.beemaths.web.rest.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;

import java.util.Map;

/**
 * @author wangshichang
 * @date 2019-01-22 10:43
 * @description
 */
@Slf4j
public class BeanTransUtil {
    /**
     * 将对象装换为map
     * @param bean
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将map装换为javabean对象
     * @param map
     * @param bean
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map,T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

}
