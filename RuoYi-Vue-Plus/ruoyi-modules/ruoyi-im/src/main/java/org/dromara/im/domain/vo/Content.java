package org.dromara.im.domain.vo;

/**
 * <p>
 * description:
 * </p>
 *
 * @author luojiaju
 * @date 06/04/2024
 */

import lombok.Data;

@Data
public class Content {
    private File[] files;
    private String text;
    private String audio;
    private String[] video;

    @Data
    public static class File {
        private String name;
        private String url;
    }


}

