package org.dromara.test;

import org.dromara.common.redis.utils.RedisUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * description:
 * </p>
 *
 * @author luojiaju
 * @date 10/04/2024
 */
@SpringBootTest
@DisplayName("标签单元测试案例")
public class RedisUtilsTest {

    @Tag("deveee")
    @DisplayName("测试 @Tag dev")
    @Test
    public void setObjectTest() {
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put("0",0);
        RedisUtils.setCacheMap("global:test", stringIntegerHashMap);
    }
}
