package xiaozhi.modules.agent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 对话步骤配置实体类
 *
 * @author AI Assistant
 * @version 1.0, 2024/12/19
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ai_scenario_step")
public class ScenarioStepEntity {

    /**
     * 步骤唯一标识
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 关联的场景ID
     */
    private String scenarioId;

    /**
     * 步骤编码
     */
    private String stepCode;

    /**
     * 步骤名称
     */
    private String stepName;

    /**
     * 步骤顺序
     */
    private Integer stepOrder;

    /**
     * AI说的话（固定配置的语句）
     */
    private String aiMessage;

    /**
     * 期望的关键词，JSON格式
     */
    private String expectedKeywords;

    /**
     * 期望的完整短语，JSON格式
     */
    private String expectedPhrases;

    /**
     * 最大尝试次数
     */
    private Integer maxAttempts;

    /**
     * 等待超时时间(秒)
     */
    private Integer timeoutSeconds;

    /**
     * 成功条件：exact/partial/keyword
     */
    private String successCondition;

    /**
     * 成功后的下一步ID
     */
    private String nextStepId;

    /**
     * 失败后的重试步骤ID
     */
    private String retryStepId;

    /**
     * 失败时的替代提示
     */
    private String alternativeMessage;

    /**
     * 手势提示：point_mouth/point_stomach等
     */
    private String gestureHint;

    /**
     * 音效文件名
     */
    private String musicEffect;

    /**
     * 是否可选步骤：0-必需 1-可选
     */
    private Integer isOptional;

    /**
     * 步骤类型：normal/start/end/branch
     */
    private String stepType;

    /**
     * 分支条件，JSON格式
     */
    private String branchCondition;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}