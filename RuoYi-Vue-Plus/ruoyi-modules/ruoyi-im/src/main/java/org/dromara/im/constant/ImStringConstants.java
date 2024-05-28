package org.dromara.im.constant;

import static org.dromara.common.core.constant.GlobalConstants.GLOBAL_REDIS_KEY;

public interface ImStringConstants {


    /**
     * github
     */
    String GITHUB_TOKEN_URL = "https://github.com/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s";

    /**
     * gitee
     */
    String GITHUB_GET_USER = "https://api.github.com/user";

    /**
     * gitee
     */
    String GITEE_TOKEN_URL = "https://gitee.com/oauth/token?" +
        "grant_type=authorization_code" +
        "&code=%s" +
        "&client_id=%s" +
        "&client_secret=%s" +
        "&redirect_uri=%s";

    /**
     * gitee
     */
    String GITEE_GET_USER = "https://gitee.com/api/v5/user";

    /**
     * Redis: key 公告通知
     */
    String PUBLIC_NOTIFY = GLOBAL_REDIS_KEY + "public_notify:%s:%s";

    /**
     * Redis: key pv增量控制
     */
    String PV_INCREMENT_CONTROL = GLOBAL_REDIS_KEY + "pv_increment_control:%s";



}
