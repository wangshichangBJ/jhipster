package cn.npy.beemaths.domain;

import lombok.Data;

import java.util.List;

/**
 * Created by wangshichang on 2018/11/19.
 */
@Data
public class BeanListDto<T> {

    private List<T> tList;

    private Integer total;

}
