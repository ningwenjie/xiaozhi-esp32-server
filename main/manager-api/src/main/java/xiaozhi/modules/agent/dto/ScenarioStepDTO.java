package xiaozhi.modules.agent.dto;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 场景步骤数据传输对象
 * 用于在服务层和控制器层之间传递场景步骤相关的数据
 */
@Data
@Schema(description = "场景步骤对象")
public class ScenarioStepDTO {
    
    @Schema(description = "步骤唯一标识")
    private String id;

    @Schema(description = "关联的场景ID")
    private String scenarioId;

    @Schema(description = "步骤编码")
    private String stepCode;

    @Schema(description = "步骤名称")
    private String stepName;

    @Schema(description = "步骤顺序")
    private Integer stepOrder;

    @Schema(description = "AI说的话（固定配置的语句）")
    private String aiMessage;

    @Schema(description = "期望的关键词列表")
    private List<String> expectedKeywords;

    @Schema(description = "期望的完整短语列表")
    private List<String> expectedPhrases;

    @Schema(description = "最大尝试次数")
    private Integer maxAttempts;

    @Schema(description = "等待超时时间(秒)")
    private Integer timeoutSeconds;

    @Schema(description = "成功条件：exact/partial/keyword")
    private String successCondition;

    @Schema(description = "成功后的下一步ID")
    private String nextStepId;

    @Schema(description = "失败后的重试步骤ID")
    private String retryStepId;

    @Schema(description = "失败时的替代提示")
    private String alternativeMessage;

    @Schema(description = "手势提示：point_mouth/point_stomach等")
    private String gestureHint;

    @Schema(description = "音效文件名")
    private String musicEffect;

    @Schema(description = "是否可选步骤：0-必需 1-可选")
    private Integer isOptional;

    @Schema(description = "步骤类型：normal/start/end/branch")
    private String stepType;

    @Schema(description = "分支条件，JSON格式")
    private String branchCondition;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "更新时间")
    private Date updatedAt;
} 