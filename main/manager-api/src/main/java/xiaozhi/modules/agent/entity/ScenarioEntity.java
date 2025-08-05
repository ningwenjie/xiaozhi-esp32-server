package xiaozhi.modules.agent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 场景配置实体类
 *
 * @author AI Assistant
 * @version 1.0, 2024/12/19
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ai_scenario")
public class ScenarioEntity {

    /**
     * 场景唯一标识
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 关联的智能体ID
     */
    private String agentId;

    /**
     * 场景编码
     */
    private String scenarioCode;

    /**
     * 场景名称
     */
    private String scenarioName;

    /**
     * 场景类型：express_needs/greeting/emotion等
     */
    private String scenarioType;

    /**
     * 触发方式：voice/visual/button
     */
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
}