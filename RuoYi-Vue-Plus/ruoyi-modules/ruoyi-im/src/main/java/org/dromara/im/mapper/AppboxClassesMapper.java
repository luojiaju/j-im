package org.dromara.im.mapper;

import org.apache.ibatis.annotations.Param;
import org.dromara.im.domain.AppboxClasses;
import org.dromara.im.domain.vo.AppboxClassesVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 应用分类Mapper接口
 *
 * @author luojiaju
 * @date 2024-01-06
 */
public interface AppboxClassesMapper extends BaseMapperPlus<AppboxClasses, AppboxClassesVo> {

    /**
     * 查询应用分类关联应用数量
     * @param ids id列表
     * @return id
     */
    Long selectAssociationClassesCount(@Param("ids") Collection<String> ids);

    /**
     * 删除应用分类关联应用
     * @param ids id列表
     * @return id
     */
    Long deleteAssociationClassesByAppId(@Param("ids") Collection<String> ids);

    /**
     * 删除应用分类关联应用
     * @param ids id列表
     * @return id
     */
    Long deleteAssociationClassesByClassesId(@Param("ids") Collection<String> ids);

    /**
     * 添加应用分类关联应用
     * @param classesId 分类id
     * @param appId 应用id
     * @return id
     */
    Long insertAssociationClassesByClassesIdAndAppId(@Param("classesId")Serializable classesId,
                                                     @Param("appId")Serializable appId);

    /**
     * 修改应用分类关联应用
     * @param classesId 分类id
     * @param appId 应用id
     * @return id
     */
    Long updateAssociationClassesByClassesIdAndAppId(@Param("classesId")Serializable classesId,
                                                     @Param("appId")Serializable appId);


}
