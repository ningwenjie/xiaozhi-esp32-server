package xiaozhi.modules.agent.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 场景配置DTO
 *
 * @author AI Assistant
 * @version 1.0, 2024/12/19
 * @since 1.0.0
 */
@Data
public class ScenarioDTO {

    /**
     * 场景唯一标识
     */
    private String id;

    /**
     * 关联的智能体ID
     */
    @NotBlank(message = "智能体ID不能为空")
    private String agentId;

    /**
     * 场景编码
     */
    @NotBlank(message = "场景编码不能为空")
    private String scenarioCode;

    /**
     * 场景名称
     */
    @NotBlank(message = "场景名称不能为空")
    private String scenarioName;

    /**
     * 场景类型：express_needs/greeting/emotion等
     */
    @NotBlank(message = "场景类型不能为空")
    private String scenarioType;

    /**
     * 触发方式：voice/visual/button
     */
    @NotBlank(message = "触发方式不能为空")
    private String triggerType;

    /**
     * 语音触发关键词，JSON格式
     */
    private String triggerKeywords;

    /**
     * 视觉触发卡片，JSON格式
     */
    private String triggerCards;

    /**
     * 场景描述
     */
    private String description;

    /**
     * 难度等级：1-5
     */
    @NotNull(message = "难度等级不能为空")
    private Integer difficultyLevel;

    /**
     * 目标年龄：3-6/7-12等
     */
    private String targetAge;

    /**
     * 排序权重
     */
    private Integer sortOrder;

    /**
     * 是否启用：0-禁用 1-启用
     */
    @NotNull(message = "启用状态不能为空")
    private Integer isActive;

    /**
     * 创建者ID
     */
    private Long creator;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新者ID
     */
    private Long updater;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 步骤数量
     */
    private Integer stepCount;

    /**
     * 步骤列表
     */
    private List<ScenarioStepDTO> steps;
}