package org.dromara.im.enums;

import lombok.Getter;

/**
 * <p>
 * description:
 * </p>
 *
 * @author luojiaju
 * @date 25/03/2024
 */
@Getter
public enum MsgRecordReadStatus {

//    读取状态:0未读 已读
    UNREAD(0L, "未读"),
    READ(1L, "已读");

    private final Long value;
    private final String desc;

    MsgRecordReadStatus(Long value, String desc) {
        this.value = value;
        this.desc = desc;
    }


}
