package cn.npy.beemaths.mapper;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author wangshichang
 * @date 2018/11/13
 * @desc mapstruct基础接口
 */
public interface BaseBeanMapper<Bean, DTO> {

    DTO bean2Dto(Bean s);

    Bean dto2Bean(DTO t);

    default List<DTO> beanList2DtoList(List<Bean> sList){

        if (sList == null || sList.size() == 0) {
            return Lists.newArrayList();
        }

        List<DTO> tList = Lists.newArrayList();
        for (Bean s : sList) {
            tList.add(bean2Dto(s));
        }

        return tList;
    }

    default List<Bean> dtoList2BeanList(List<DTO> tList){

        if (tList == null || tList.size() == 0) {
            return Lists.newArrayList();
        }

        List<Bean> sList = Lists.newArrayList();
        for (DTO t : tList) {
            sList.add(dto2Bean(t));
        }

        return sList;
    }
}
