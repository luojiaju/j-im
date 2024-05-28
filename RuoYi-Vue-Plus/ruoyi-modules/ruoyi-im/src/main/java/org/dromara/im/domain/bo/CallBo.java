package org.dromara.im.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.im.domain.MsgRecord;

import java.util.List;

/**
 * <p>
 * description:
 * </p>
 *
 * @author luojiaju
 * @date 07/04/2024
 */
@Data
@AutoMapper(target = CallBo.class, reverseConvertGenerate = false)
public class CallBo {
    @NotNull(message = "用户不能为空")
    @NotEmpty(message = "用户不能为空")
    private List<String> toUserId;
    @NotNull(message = "类型不能为空")
    private String type;
    @NotNull(message = "内容不能为空")
    private String content;

}
