package xiaozhi.modules.agent.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 学习记录数据传输对象
 * 用于在服务层和控制器层之间传递学习记录相关的数据
 */
@Data
@Schema(description = "学习记录对象")
public class LearningRecordDTO {
    
    @Schema(description = "记录唯一标识")
    private String id;

    @Schema(description = "儿童姓名")
    private String childName;

    @Schema(description = "关联的场景ID")
    private String scenarioId;

    @Schema(description = "关联的步骤ID")
    private String stepId;

    @Schema(description = "会话ID")
    private String sessionId;

    @Schema(description = "尝试次数")
    private Integer attemptCount;

    @Schema(description = "是否成功：0-失败 1-成功")
    private Integer isSuccess;

    @Schema(description = "用户回答内容")
    private String userResponse;

    @Schema(description = "回答耗时(秒)")
    private Integer responseTime;

    @Schema(description = "AI反馈内容")
    private String aiFeedback;

    @Schema(description = "使用的手势提示")
    private String gestureUsed;

    @Schema(description = "播放的音效")
    private String musicPlayed;

    @Schema(description = "学习日期")
    private Date learningDate;

    @Schema(description = "创建时间")
    private Date createdAt;

    // 关联信息
    @Schema(description = "场景名称")
    private String scenarioName;

    @Schema(description = "步骤名称")
    private String stepName;
} 