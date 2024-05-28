package org.dromara.im.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 发送到具体任务、位置、对象
 */
@Getter
public enum MsgRecordToType {
    USER("用户", 1L),
    GROUP("群聊", 2L),
    CHANNEL("频道", 3L),
    CHOICE_NOTICE("精选", 4L),
    OTHER_NOTICE("其他", 5L),
    MENTION_NOTICE("提及", 6L);

    private final String description;
    private final Long value;

    MsgRecordToType(String description, Long value) {
        this.description = description;
        this.value = value;
    }

    /**
     * 根据值获取枚举
     * @param value
     * @return
     */
    public static MsgRecordToType fromValue(Long value) {
        for (MsgRecordToType type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的值: " + value);
    }

    public static List<MsgRecordToType> getAllTypes() {
        return Arrays.asList(values());
    }
}
