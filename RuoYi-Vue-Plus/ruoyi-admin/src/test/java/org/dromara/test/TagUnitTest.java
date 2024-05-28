package org.dromara.test;

import org.dromara.common.json.utils.JsonUtils;
import org.dromara.im.domain.vo.Content;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 标签单元测试案例
 *
 * @author Lion Li
 */
@SpringBootTest
@DisplayName("标签单元测试案例")
public class TagUnitTest {
    @Tag("deveee")
    @DisplayName("测试 @Tag dev")
    @Test
    public void testJSon(){
        String json = """
            {"video":["1","2","3"],"text":"sdfsadassfasd123123","files":[{"name":"webp_to_png_14.png","url":"http://127.0.0.1:9100/jilijili-project/2024/04/06/cf6bb9de55c748ad9ae0cfc3d4e5036c.png"}],"audio":""}
            """;
        Content content = JsonUtils.parseObject(json, Content.class);
        System.out.println(content);
    }

    @Tag("dev")
    @DisplayName("测试 @Tag dev")
    @Test
    public void testTagDev() {
        System.out.println("dev");
    }

    @Tag("prod")
    @DisplayName("测试 @Tag prod")
    @Test
    public void testTagProd() {
        System.out.println("prod");
    }

    @Tag("local")
    @DisplayName("测试 @Tag local")
    @Test
    public void testTagLocal() {
        System.out.println("local");
    }

    @Tag("exclude")
    @DisplayName("测试 @Tag exclude")
    @Test
    public void testTagExclude() {
        System.out.println("exclude");
    }

    @BeforeEach
    public void testBeforeEach() {
        System.out.println("@BeforeEach ==================");
    }

    @AfterEach
    public void testAfterEach() {
        System.out.println("@AfterEach ==================");
    }


}
