package org.dromara.im.utils;

import cn.hutool.json.JSONUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.im.domain.vo.Message;
import org.dromara.im.handler.ws.WebSocketSessionHolder;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * <p>
 * description:
 * </p>
 *
 * @author luojiaju
 * @date 16/01/2024
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImWebSocketMsgUtils {

    /**
     * @param targetId 回复者 id
     * @param msg      消息
     */
    public static void sendAMessageToTheUser(Long targetId, Message msg) {
        WebSocketSession sessions = WebSocketSessionHolder.getSessions(targetId);
        if (sessions != null) {
            try {
                R<Message> ok = R.ok(msg);
                sessions.sendMessage(new TextMessage(JSONUtil.toJsonStr(ok)));

            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            WebSocketSessionHolder.removeSession(targetId);
            log.warn("用户不在线,消息丢失:{}", msg);
        }
    }


    /**
     * 发送所有在线用户
     */
    public static void sendAllOnlineUser(Message msg) {
        for (Long userId : WebSocketSessionHolder.getSessionsAll()) {
            sendAMessageToTheUser(userId, msg);
        }
    }

    /**
     * 发送心跳
     *
     * @param session
     */
    private static void sendPongMessage(WebSocketSession session) {
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new PongMessage());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            log.info("会话关闭");
        }


    }

    /**
     * 发送心跳
     */
    private static void sendPingMessage(WebSocketSession session) {
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new PingMessage());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        log.error("会话关闭");


    }

}
