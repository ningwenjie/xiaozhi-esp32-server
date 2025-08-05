package xiaozhi.modules.agent.dto;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 步骤模板数据传输对象
 * 用于在服务层和控制器层之间传递步骤模板相关的数据
 */
@Data
@Schema(description = "步骤模板对象")
public class StepTemplateDTO {
    
    @Schema(description = "模板唯一标识")
    private String id;

    @Schema(description = "模板编码")
    private String templateCode;

    @Schema(description = "模板名称")
    private String templateName;

    @Schema(description = "模板类型：greeting/instruction/encouragement等")
    private String templateType;

    @Schema(description = "AI说的话模板")
    private String aiMessage;

    @Schema(description = "期望的关键词模板列表")
    private List<String> expectedKeywords;

    @Schema(description = "期望的完整短语模板列表")
    private List<String> expectedPhrases;

    @Schema(description = "替代提示模板")
    private String alternativeMessage;

    @Schema(description = "模板描述")
    private String description;

    @Schema(description = "是否默认模板")
    private Integer isDefault;

    @Schema(description = "排序权重")
    private Integer sortOrder;

    @Schema(description = "创建者ID")
    private Long creator;

    @Schema(description = "创建时间")
    private Date createdAt;
} 