package org.dromara.im.service;

import org.dromara.common.core.domain.R;
import org.dromara.im.domain.MsgRecord;
import org.dromara.im.domain.vo.MsgRecordVo;
import org.dromara.im.domain.bo.MsgRecordBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 聊天消息记录Service接口
 *
 * @author luojiaju
 * @date 2024-02-20
 */
public interface IMsgRecordService {

    /**
     * 查询聊天消息记录
     */
    MsgRecordVo queryById(String id);

    /**
     * 查询聊天消息记录列表
     */
    TableDataInfo<MsgRecordVo> queryPageList(MsgRecordBo bo, PageQuery pageQuery);

    /**
     * 查询聊天消息记录列表
     */
    List<MsgRecordVo> queryList(MsgRecordBo bo);

    /**
     * 新增聊天消息记录
     */
    MsgRecordVo insertByBo(MsgRecordBo bo);

    /**
     * 修改聊天消息记录
     */
    Boolean updateByBo(MsgRecordBo bo);

    /**
     * 校验并批量删除聊天消息记录信息
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);

    boolean updateByboMsgRead(MsgRecordBo bo);


    R<Map<String, String>> startCall(List<String> toUserId, String type, String content);

    R<Void> endCall(List<String> toUserId, String type, String content);

    /**
     * 处理信息
     * @param bo
     */
    void atHandler(MsgRecordBo bo);
}
