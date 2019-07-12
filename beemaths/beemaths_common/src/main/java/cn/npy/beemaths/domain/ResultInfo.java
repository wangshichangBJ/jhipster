package cn.npy.beemaths.domain;

import cn.npy.beemaths.web.rest.errors.ServiceRetConstError;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by wangshichang on 2016/9/21.
 */

@Data
public class ResultInfo<T> implements Serializable {
    private int errno;
    private String errmsg;
    private T data;

    private static ResultInfo nullBeanListDtoResultInfo = new ResultInfo();
    private static ResultInfo nullObjectResultInfo = new ResultInfo();
    private static ResultInfo nullListResultInfo = new ResultInfo();

    static {
        // 创建空的BeanListDto类的resultInfo
        BeanListDto beanListDto = new BeanListDto();
        beanListDto.setTotal(0);
        beanListDto.setTList(Lists.newArrayList());
        nullBeanListDtoResultInfo.setData(beanListDto);

        // 创建空的对象的resultInfo
        nullObjectResultInfo.setData(null);

        // 创建空的List的resultInfo
        nullListResultInfo.setData(Lists.newArrayList());
    }

    public ResultInfo() {
        this.errno = 0;
        data = null;
    }

    public ResultInfo(int errno, T data) {
        this.errno = errno;
        this.data = data;
        if (errno != 0) {
            this.errmsg = data.toString();
        }
    }

    /**
     * fail信息设置方法
     *
     * @param error 错误码
     * @author cuishilei
     * @date 2018/9/4
     */
    public void fail(ServiceRetConstError error, String... parameters) {
        this.errno = error.errno();
        String errmsg = error.errmsg();
        for (int i = 0; i < parameters.length; i++) {
            errmsg = errmsg.replace(String.format("{%d}", i), parameters[i]);
        }
        this.errmsg = errmsg;
    }

    /**
     * 判断是否成功方法
     *
     * @return boolean
     * @author cuishilei
     * @date 2018/9/4
     */
    public boolean judgeIsSuccess() {
        return this.errno == ServiceRetConstError.SUCCESS.errno();
    }

    /**
     * 创建空的BeanListDto的resultInfo
     *
     * @return
     */
    public static ResultInfo getNullBeanListDtoResultInfo() {
        return nullBeanListDtoResultInfo;
    }

    /**
     * 创建空的对象的resultInfo
     *
     * @return
     */
    public static ResultInfo getNullObjectResultInfo() {
        return nullObjectResultInfo;
    }

    /**
     * 创建空的list的resultInfo
     *
     * @return
     */
    public static ResultInfo getNullListResultInfo() {
        return nullListResultInfo;
    }
}
