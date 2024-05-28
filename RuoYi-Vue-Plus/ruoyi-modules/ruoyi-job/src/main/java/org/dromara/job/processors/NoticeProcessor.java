package org.dromara.job.processors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

/**
 * <p>
 * description:
 * </p>
 *
 * @author luojiaju
 * @date 10/04/2024
 */
@Slf4j
@Component
public class NoticeProcessor implements BasicProcessor {

    @Override
    public ProcessResult process(TaskContext taskContext) throws Exception {
        log.info("NoticeProcessor.process=>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return new ProcessResult(true);
    }
}
