package xiaozhi.modules.agent.service;

import java.util.List;

import xiaozhi.common.service.BaseService;
import xiaozhi.modules.agent.dto.ScenarioStepDTO;
import xiaozhi.modules.agent.entity.ScenarioStepEntity;

/**
 * 场景步骤配置Service接口
 *
 * @author AI Assistant
 * @version 1.0, 2024/12/19
 * @since 1.0.0
 */
public interface ScenarioStepService extends BaseService<ScenarioStepEntity> {
    
    /**
     * 根据场景ID获取步骤列表
     *
     * @param scenarioId 场景ID
     * @return 步骤列表
     */
    List<ScenarioStepDTO> getStepsByScenarioId(String scenarioId);
    
    /**
     * 根据场景ID获取步骤列表（按顺序排序）
     *
     * @param scenarioId 场景ID
     * @return 步骤列表
     */
    List<ScenarioStepDTO> getStepsByScenarioIdOrdered(String scenarioId);
    
    /**
     * 根据场景ID和步骤类型获取步骤列表
     *
     * @param scenarioId 场景ID
     * @param stepType 步骤类型
     * @return 步骤列表
     */
    List<ScenarioStepDTO> getStepsByScenarioIdAndType(String scenarioId, String stepType);
    
    /**
     * 根据步骤编码获取步骤
     *
     * @param stepCode 步骤编码
     * @return 步骤信息
     */
    ScenarioStepDTO getStepByCode(String stepCode);
    
    /**
     * 创建步骤
     *
     * @param dto 步骤信息
     * @return 创建的步骤ID
     */
    String createStep(ScenarioStepDTO dto);
    
    /**
     * 更新步骤
     *
     * @param id 步骤ID
     * @param dto 步骤信息
     */
    void updateStep(String id, ScenarioStepDTO dto);
    
    /**
     * 删除步骤
     *
     * @param id 步骤ID
     */
    void deleteStep(String id);
    
    /**
     * 根据场景ID删除所有步骤
     *
     * @param scenarioId 场景ID
     */
    void deleteStepsByScenarioId(String scenarioId);
    
    /**
     * 更新步骤顺序
     *
     * @param id 步骤ID
     * @param stepOrder 新的顺序
     */
    void updateStepOrder(String id, Integer stepOrder);
    
    /**
     * 移动步骤位置
     *
     * @param id 步骤ID
     * @param direction 移动方向：-1向上，1向下
     */
    void moveStep(String id, Integer direction);
    
    /**
     * 批量保存步骤
     *
     * @param scenarioId 场景ID
     * @param steps 步骤列表
     */
    void batchSaveSteps(String scenarioId, List<ScenarioStepDTO> steps);
} 