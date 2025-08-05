package xiaozhi.modules.agent.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("ai_child_learning_record")
@Schema(description = "儿童学习记录信息")
public class LearningRecordEntity {

    @TableId(type = IdType.ASSIGN_UUID)
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
} 