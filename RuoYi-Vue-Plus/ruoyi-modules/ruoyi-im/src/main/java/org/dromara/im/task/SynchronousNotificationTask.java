package org.dromara.im.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.im.mapper.ImNoticeMapper;
import org.dromara.im.service.IImNoticeService;
import org.dromara.im.service.impl.ImNoticeServiceImpl;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

import java.util.Collection;
import java.util.Map;

import static org.dromara.im.constant.ImStringConstants.PUBLIC_NOTIFY;

/**
 * <p>
 * description:
 * </p>
 *
 * @author luojiaju
 * @date 14/04/2024
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SynchronousNotificationTask implements BasicProcessor {

    private final ImNoticeMapper imNoticeMapper;

    @Override
    public ProcessResult process(TaskContext taskContext) throws Exception {
        String jobParams = taskContext.getJobParams();
        log.info("参数", jobParams);
        // [1] 得到公告 keys
        Collection<String> keys = RedisUtils.keys(PUBLIC_NOTIFY.formatted("*"));


        // [2] 得到用户id,读取状态
        for (String key : keys) {
            Map<String, Integer> cacheMap = RedisUtils.getCacheMap(key);
            String noticeId = key.split(":")[0];
            cacheMap.remove("0");
            // [2.1] 更新这条公告
            Boolean updated =  imNoticeMapper.updateByUserId(noticeId,cacheMap);
        }
        return new ProcessResult(true,"Redis公告通知数据同步到数据库成功!!!");
    }
}
