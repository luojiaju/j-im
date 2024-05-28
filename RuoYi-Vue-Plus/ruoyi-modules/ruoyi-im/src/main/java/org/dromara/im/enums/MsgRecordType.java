package org.dromara.im.enums;

import lombok.Getter;

/**
 * 消息类型
 */
@Getter
public enum MsgRecordType {
//    内容类型: 1文本 2图片 3视频 4音频 5复合内容 6发起通话

    TEXT("文本", 1L),
    IMAGE("图片", 2L),
    VIDEO("视频", 3L),
    AUDIO("音频", 4L),
    COMPOSITE("复合内容", 5L),
    START_CALL("发起通话", 6L),
    INVITE("邀请加入频道", 7L),
    RECALL("撤回消息", 8L),
    ;

    MsgRecordType(String type, Long value) {
        this.type = type;
        this.value = value;
    }

    private final String type;
    private final Long value;

}
