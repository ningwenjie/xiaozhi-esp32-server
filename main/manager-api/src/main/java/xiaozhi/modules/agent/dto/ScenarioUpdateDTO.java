package xiaozhi.modules.agent.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 场景更新数据传输对象
 * 用于更新场景时的数据传输
 */
@Data
@Schema(description = "场景更新对象")
public class ScenarioUpdateDTO {
    
    @Schema(description = "场景唯一标识", required = true)
    private String id;

    @Schema(description = "场景编码")
    private String scenarioCode;

    @Schema(description = "场景名称")
    private String scenarioName;

    @Schema(description = "场景类型：express_needs/greeting/emotion等")
    private String scenarioType;

    @Schema(description = "触发方式：voice/visual/button")
    private String triggerType;

    @Schema(description = "语音触发关键词列表")
    private List<String> triggerKeywords;

    @Schema(description = "视觉触发卡片列表")
    private List<String> triggerCards;

    @Schema(description = "场景描述")
    private String description;

    @Schema(description = "难度等级：1-5")
    private Integer difficultyLevel;

    @Schema(description = "目标年龄：3-6/7-12等")
    private String targetAge;

    @Schema(description = "排序权重")
    private Integer sortOrder;

    @Schema(description = "是否启用：0-禁用 1-启用")
    private Integer isActive;
} 