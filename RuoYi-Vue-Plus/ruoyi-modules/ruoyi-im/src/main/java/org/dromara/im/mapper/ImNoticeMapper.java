package org.dromara.im.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.im.domain.ImNotice;
import org.dromara.im.domain.vo.ImNoticeVo;

import java.util.Map;

/**
 * 用户通知Mapper接口
 *
 * @author luojiaju
 * @date 2024-01-05
 */
public interface ImNoticeMapper extends BaseMapperPlus<ImNotice, ImNoticeVo> {

    /**
     * 更新用户通知
     */
    Boolean updateByUserId(String noticeId,
                           Map<String, Integer> cacheMap);
}
