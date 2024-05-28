package org.dromara.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.im.domain.*;
import org.dromara.im.domain.bo.GroupChatBo;
import org.dromara.im.domain.bo.MsgRecordBo;
import org.dromara.im.domain.vo.AppboxApplicationVo;
import org.dromara.im.domain.vo.Content;
import org.dromara.im.domain.vo.ImNoticeVo;
import org.dromara.im.domain.vo.MsgRecordVo;
import org.dromara.im.enums.AtType;
import org.dromara.im.enums.MsgRecordReadStatus;
import org.dromara.im.enums.MsgRecordToType;
import org.dromara.im.enums.MsgRecordType;
import org.dromara.im.mapper.*;
import org.dromara.im.service.IMsgRecordService;
import org.dromara.im.utils.ImWebSocketMsgUtils;
import org.dromara.im.utils.SysUserUtils;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.mapper.SysUserMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.dromara.im.constant.ImStringConstants.PUBLIC_NOTIFY;
import static org.dromara.im.utils.SysUserUtils.getCurrentUserIdToLong;
import static org.dromara.im.utils.SysUserUtils.getCurrentUserIdToString;

/**
 * 聊天消息记录Service业务层处理
 *
 * @author luojiaju
 * @date 2024-02-20
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class MsgRecordServiceImpl implements IMsgRecordService {

    private final MsgRecordMapper baseMapper;
    private final MsgActionMapper msgActionMapper;
    private final SysUserMapper sysUserMapper;
    private final FriendMapper friendMapper;
    private final GroupChatServiceImpl groupChatService;
    private final AppboxApplicationMapper appboxApplicationMapper;
    private final ImNoticeMapper imNoticeMapper;

    /**
     * 查询聊天消息记录
     */
    @Override
    public MsgRecordVo queryById(String id) {
        return baseMapper.selectVoByIdUser(id);

    }

    /**
     * 查询聊天消息记录列表
     */
    @Override
    public TableDataInfo<MsgRecordVo> queryPageList(MsgRecordBo bo, PageQuery pageQuery) {
        // [1] 得到消息类型
        Long toType = bo.getToType();
        if (Objects.isNull(toType)) {
            List<MsgRecordVo> msgRecordVos = this.queryList(bo);
            return TableDataInfo.build(msgRecordVos);
        }
        QueryWrapper<MsgRecord> queryWrapper = Wrappers.query();
        Page<MsgRecordVo> page = pageQuery.build();
        queryWrapper.orderByAsc("imr.create_time");
        // [2] 根据toType设置查询条件
        switch (toType.intValue()) {
            case 1:
                // [2.1] 查询用户与用户私聊,一对一
                queryWrapper
                    .eq("imr.sender_id", bo.getSenderId())
                    .eq("imr.to_id", bo.getToId())
                    .or()
                    .eq("imr.sender_id", bo.getToId())
                    .eq("imr.to_id", bo.getSenderId())
                    .eq("imr.to_type", 1);
                page = baseMapper.queryUsersOneOnOneChatHistoryWithFriends(queryWrapper, page);
                break;
            case 2:
                // [2.2] 查询用户与群聊,群聊
                queryWrapper.eq("imr.to_group_id", bo.getToGroupId())
                    .eq("imr.to_type", 2);
                page = baseMapper.queryChatRecordsOfUserGroupChats(queryWrapper, page);
                break;
            case 3:
                // [2.3] 查询用户与频道,频道
                queryWrapper.eq("imr.to_app_id", bo.getToAppId())
                    .eq("imr.to_type", 3);
                page = baseMapper.queryChatHistoryOfTheChannel(queryWrapper, page);
                break;
            default:
                // [2.4] 不支持的toType
                throw new IllegalArgumentException("不支持的toType：" + toType);
        }
        List<MsgRecordVo> records = page.getRecords();

        // [3] 根据refMsgId获取聊天的回复列表
        page.setRecords(msgRecordList(records));
        return TableDataInfo.build(page);
    }

    private static List<MsgRecordVo> msgRecordList(List<MsgRecordVo> records) {
        // 创建一个Map来索引所有消息记录，键为消息ID，值为MsgRecordVo对象
        Map<String, MsgRecordVo> recordMap = new HashMap<>();
        records.forEach(record -> {
            record.setContents(JsonUtils.parseObject(record.getContent(), Content.class));
            recordMap.put(record.getId(), record);
        });

        // 过滤出顶级消息
        List<MsgRecordVo> topList = records.stream()
            .filter(item -> item.getRefMsgId() == null)
            .collect(Collectors.toList());

        // 为每个顶级消息设置内容和回复列表
        topList.forEach(item -> {
            // 使用Map来查找回复，避免嵌套遍历
            List<MsgRecordVo> chiller = records.stream()
                .filter(record -> record.getRefMsgId() != null && record.getRefMsgId().equals(item.getId()))
                .collect(Collectors.toList());
            item.setChiller(chiller);
        });
        return topList;
    }

    /**
     * 查询聊天消息记录列表
     */
    @Override
    public List<MsgRecordVo> queryList(MsgRecordBo bo) {
        LambdaQueryWrapper<MsgRecord> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MsgRecord> buildQueryWrapper(MsgRecordBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MsgRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getSenderId() != null, MsgRecord::getSenderId, bo.getSenderId());
        lqw.eq(bo.getToId() != null, MsgRecord::getToId, bo.getToId());
        lqw.eq(bo.getToGroupId() != null, MsgRecord::getToGroupId, bo.getToGroupId());
        lqw.eq(bo.getToAppId() != null, MsgRecord::getToAppId, bo.getToAppId());
        lqw.like(StringUtils.isNotBlank(bo.getContent()), MsgRecord::getContent, bo.getContent());
        lqw.eq(bo.getMsgType() != null, MsgRecord::getMsgType, bo.getMsgType());
        lqw.eq(bo.getToType() != null, MsgRecord::getToType, bo.getToType());
        lqw.eq(bo.getStatus() != null, MsgRecord::getStatus, bo.getStatus());
        lqw.eq(bo.getUnread() != null, MsgRecord::getUnread, bo.getUnread());
        lqw.eq(bo.getRefMsgId() != null, MsgRecord::getRefMsgId, bo.getRefMsgId());
        return lqw;
    }

    /**
     * 新增聊天消息记录
     */
    @Transactional
    @Override
    public MsgRecordVo insertByBo(MsgRecordBo bo) {
        MsgRecord add = MapstructUtils.convert(bo, MsgRecord.class);
        add.setSenderId(getCurrentUserIdToString());
        // [1] 插入数据库
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());

            MsgAction action = new MsgAction();
            action.setMsgId(add.getId());
            // [2] 添加到`消息动作表`关联
            msgActionMapper.insert(action);
            MsgRecordVo msgRecordVo = baseMapper.selectVoByIdUser(add.getId());
            msgRecordVo.setContents(JsonUtils.parseObject(add.getContent(), Content.class));

            // [2] 添加到对应的附加消息
            addAnnex(msgRecordVo);

            // [3] websocket发送消息
            verifyIfTheOtherPartyIsOnline(msgRecordVo);
            return msgRecordVo;
        } else {
            return new MsgRecordVo();
        }
    }

    /**
     * 添加附加消息
     */
    private void addAnnex(MsgRecordVo record) {
        String attStr = "";
        // 内容类型: 1文本 2图片 3视频 4音频 5复合内容
        switch (record.getMsgType().intValue()) {
            case 1:
                if (record.getContents() != null && StringUtils.isNotEmpty(record.getContents().getText())) {
                    attStr = record.getContents().getText();
                }
                break;
            case 2:
                if (record.getContents() != null && StringUtils.isNotEmpty(record.getContents().getAudio())) {
                    attStr = "[语音]";
                }
                break;
            case 3:
                if (record.getContents() != null && record.getContents().getVideo().length > 0) {
                    attStr = "[视频]";
                }
                break;
            case 4:
                if (record.getContents() != null && record.getContents().getFiles().length > 0) {
                    attStr = "[文件]";
                }
                break;
            case 5:
                attStr = "";
                break;
            default:
                attStr = "暂无最新内容";
                break;

        }
        //消息类型:1用户 2群聊 3频道
        switch (record.getToType().intValue()) {
            case 1:
                String toId = record.getToId();
                String senderId = record.getSenderId();
                LambdaUpdateWrapper<Friend> luwUser = Wrappers.lambdaUpdate();
                luwUser.eq(Friend::getFriendId, toId)
                    .eq(Friend::getUserId, senderId)
                    .set(Friend::getAttachMsg, attStr);

                friendMapper.update(luwUser);

                LambdaUpdateWrapper<Friend> luwFriend = Wrappers.lambdaUpdate();
                luwFriend.eq(Friend::getUserId, toId)
                    .eq(Friend::getFriendId, senderId)
                    .set(Friend::getAttachMsg, attStr);
                friendMapper.update(luwFriend);
                break;
            case 2:
                String toGroupId = record.getToGroupId();
                LambdaUpdateWrapper<GroupChat> luw2 = Wrappers.lambdaUpdate();
                luw2.eq(GroupChat::getId, toGroupId).set(GroupChat::getRecentMessage, attStr);
                groupChatService.updateRecentMessage(luw2);
                break;
            default:
                break;
        }
    }

    /**
     * 验证对方是否在线
     */
    private void verifyIfTheOtherPartyIsOnline(MsgRecordVo msg) {
        // [1] 一对一发送 单点消息
        if (Objects.nonNull(msg.getToId())) {
            ImWebSocketMsgUtils.sendAMessageToTheUser(Long.valueOf(msg.getToId()), msg);
        } else if (Objects.nonNull(msg.getToGroupId())) {
            // [2] 群发消息
            String groupId = msg.getToGroupId();
            GroupChatBo bo = new GroupChatBo();
            bo.setId(groupId);
            PageQuery query = new PageQuery();
            query.setPageNum(0);
            query.setPageSize(4000); // 群聊配置:群聊最大用户量
            TableDataInfo<SimpleUser> simpleUserTableDataInfo = groupChatService.queryUserListThroughGroupChatId(bo, query);
            List<SimpleUser> rows = simpleUserTableDataInfo.getRows();
            rows.forEach(item -> {
                ImWebSocketMsgUtils.sendAMessageToTheUser(Long.valueOf(item.getUserId()), msg);
            });
        } else {
            // [3] 发送应用频道消息
            String appId = msg.getToAppId();
            AppboxApplicationVo appBox = appboxApplicationMapper.selectVoById(appId);
            AppboxApplication application = new AppboxApplication();
            if (appBox.getParentId().equals("0")) {
                application.setId(appId);
            } else {
                application.setId(appBox.getParentId());
            }

            // [3.1] 得到当前最顶级的频道用户列表
            PageQuery query = new PageQuery();
            query.setPageNum(0);
            query.setPageSize(4000); // 配置:应用最大用户量
            List<SimpleUser> rows = appboxApplicationMapper.selectUserListByAppBoxChildId(application, query);
            // [3.2] 应用用户群发消息
            rows.forEach(item -> {
                ImWebSocketMsgUtils.sendAMessageToTheUser(Long.valueOf(item.getUserId()), msg);
            });
        }

    }

    /**
     * 修改聊天消息记录
     */
    @Override
    public Boolean updateByBo(MsgRecordBo bo) {
        MsgRecord update = MapstructUtils.convert(bo, MsgRecord.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MsgRecord entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除聊天消息记录
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }

        // [1] 删除子记录
        LambdaQueryWrapper<MsgRecord> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.in(MsgRecord::getRefMsgId, ids);
        baseMapper.delete(lambdaQuery);
        // [2] 删除主记录
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean updateByboMsgRead(MsgRecordBo bo) {
        Long userIdToLong = getCurrentUserIdToLong();
        LambdaUpdateWrapper<MsgRecord> luw = Wrappers.lambdaUpdate();
        luw.set(true, MsgRecord::getUnread, 1)
            .eq(Objects.nonNull(bo.getId()), MsgRecord::getId, bo.getId())
            .eq(MsgRecord::getSenderId, bo.getToId())
            .eq(MsgRecord::getToId, userIdToLong)
            .eq(MsgRecord::getToType, 1);

        // 更新
        baseMapper.update(luw);
        return true;
    }


    @Override
    public R<Map<String, String>> startCall(List<String> toUserId, String type, String content) {
        int count = 0;
        Map<String, String> resultMap = new HashMap<>();

        List<SysUserVo> userList = this.sysUserMapper.selectVoBatchIds(toUserId);
        for (SysUserVo userVo : userList) {
            if (!SysUserUtils.isOnline(userVo)) {
                // 用户不在线,发送通知
                count += 1;
                log.info("用户不在线");
                resultMap.put(userVo.getNickName(), "不在线,已发送通知!!!");
                continue;
            }

            MsgRecordVo recordVo = getMsgRecordVo(type, content, userVo);
            ImWebSocketMsgUtils.sendAMessageToTheUser(userVo.getUserId(), recordVo);
        }

        return R.ok(String.valueOf(count), resultMap);
    }

    private static @NotNull MsgRecordVo getMsgRecordVo(String type, String content, SysUserVo userVo) {
        MsgRecordVo recordVo = new MsgRecordVo();
        recordVo.setToType(MsgRecordToType.USER.getValue());
        recordVo.setToId(String.valueOf(userVo.getUserId()));
        recordVo.setSenderId(getCurrentUserIdToString());
        recordVo.setContent(content);
        recordVo.setMsgType(MsgRecordType.START_CALL.getValue());
        recordVo.setUnread(MsgRecordReadStatus.UNREAD.getValue());
        recordVo.setRemark("发起通话");
        recordVo.setRtcType(type);
        return recordVo;
    }


    @Override
    public R<Void> endCall(List<String> toUserId, String type, String content) {
        toUserId.stream().forEach(item -> {
            if (item != null) {
                ImWebSocketMsgUtils.sendAMessageToTheUser(Long.valueOf(item), new MsgRecordVo());
            }
        });
        return R.ok();
    }

    /**
     * 处理信息
     */
    @Override
    public void atHandler(MsgRecordBo bo) {
        List<Long> longList = bo.getAtSelected();
        Content content = JsonUtils.parseObject(bo.getContent(), Content.class);
        String msgType = getMsgType(content);
        Long currUserId = getCurrentUserIdToLong();
        SysUserVo currUser = sysUserMapper.selectUserById(currUserId);

        AppboxApplicationVo applicationVo = appboxApplicationMapper.selectVoById(bo.getToAppId());

        // 查询这个应用下的所有用户id
        List<Long> userIds = appboxApplicationMapper.selectUserIdListByAppId(
            "0".equals(applicationVo.getParentId()) ? bo.getToAppId() : applicationVo.getParentId());

        // 查询这个应用的的管理员用户
        List<Long> adminUserIds = appboxApplicationMapper.selectAdminUserIdListByAppId(bo.getToAppId());

        String formattedString = String.format("%s 在%s@了你\n%s", currUser.getNickName(), applicationVo.getAppName(), msgType);

        // at列表，依次发送消息
        for (Long flag : longList) {
            // 构建消息
            ImNotice notice = new ImNotice();
            notice.setNoticeType(3L);
            notice.setTitle("有人@你");
            notice.setContent(formattedString);
            notice.setFromUrl("/home/" + applicationVo.getId());
            imNoticeMapper.insert(notice);
            String key = PUBLIC_NOTIFY.formatted(notice.getNoticeType(), notice.getId());
            RedisUtils.setCacheMap(key, Map.of("0", 0));
            ImNoticeVo noticeVo = MapstructUtils.convert(notice, ImNoticeVo.class);

            // 如果是特定用户
            if (flag.equals(AtType.AT_ALL.getType())) {
                // at 全体成员,给这个应用下的所有用户发送at消息
                for (Long userId : userIds) {
                    if (currUser.getUserId().equals(userId)) continue;
                    // 发送消息
                    ImWebSocketMsgUtils.sendAMessageToTheUser(userId, noticeVo);
                }
            } else if (equals(AtType.AT_MSG.getType())) {
                // at 管理员
                for (Long userId : adminUserIds) {
                    if (currUser.getUserId().equals(userId)) continue;
                    // 发送消息
                    ImWebSocketMsgUtils.sendAMessageToTheUser(userId, noticeVo);
                }
            } else if (equals(AtType.AT_AI.getType())) {
                // @at Ai
//                aiCallReply(currUser, bo.getContent());
                System.out.println("ai回复");
            } else {
                if (flag.equals(currUser.getUserId())) continue;
                // @t 普通用户
                ImWebSocketMsgUtils.sendAMessageToTheUser(flag, noticeVo);
            }

        }
    }

    public String getMsgType(Content content) {

        if (!content.getAudio().isEmpty()) {
            return "语音";
        }
        if (content.getVideo().length > 0) {
            return "视频";
        }
        if (content.getFiles().length > 0) {
            return "文件";
        }
        return content.getText();
    }
}
