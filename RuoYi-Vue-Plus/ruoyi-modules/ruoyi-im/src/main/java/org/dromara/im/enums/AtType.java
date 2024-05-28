package org.dromara.im.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum AtType {

    /**
     * @all 全体成员
     */
    AT_ALL(0L),
    /**
     * @msg 管理员
     */
    AT_MSG(1L),
    /**
     * @ai ai
     */
    AT_AI(2L),
    ;

    private final Long type;

    AtType(Long type) {
        this.type = type;
    }
}
