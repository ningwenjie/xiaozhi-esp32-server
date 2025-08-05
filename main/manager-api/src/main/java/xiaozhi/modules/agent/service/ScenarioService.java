package xiaozhi.modules.agent.service;

import java.util.List;
import java.util.Map;

import xiaozhi.common.page.PageData;
import xiaozhi.common.service.BaseService;
import xiaozhi.modules.agent.dto.ScenarioCreateDTO;
import xiaozhi.modules.agent.dto.ScenarioDTO;
import xiaozhi.modules.agent.dto.ScenarioUpdateDTO;
import xiaozhi.modules.agent.entity.ScenarioEntity;

/**
 * 场景配置Service接口
 *
 * @author AI Assistant
 * @version 1.0, 2024/12/19
 * @since 1.0.0
 */
public interface ScenarioService extends BaseService<ScenarioEntity> {
    
    /**
     * 获取场景分页列表
     *
     * @param params 查询参数
     * @return 分页数据
     */
    PageData<ScenarioEntity> getScenarioPage(Map<String, Object> params);
    
    /**
     * 根据智能体ID获取场景列表
     *
     * @param agentId 智能体ID
     * @return 场景列表
     */
    List<ScenarioDTO> getScenariosByAgentId(String agentId);
    
    /**
     * 根据智能体ID和场景类型获取场景列表
     *
     * @param agentId 智能体ID
     * @param scenarioType 场景类型
     * @return 场景列表
     */
    List<ScenarioDTO> getScenariosByAgentIdAndType(String agentId, String scenarioType);
    
    /**
     * 根据智能体ID获取启用的场景列表
     *
     * @param agentId 智能体ID
     * @return 启用的场景列表
     */
    List<ScenarioDTO> getActiveScenariosByAgentId(String agentId);
    
    /**
     * 根据场景编码获取场景
     *
     * @param scenarioCode 场景编码
     * @return 场景信息
     */
    ScenarioDTO getScenarioByCode(String scenarioCode);
    
    /**
     * 根据触发关键词获取场景
     *
     * @param keyword 触发关键词
     * @param agentId 智能体ID
     * @return 场景列表
     */
    List<ScenarioDTO> getScenariosByTriggerKeyword(String keyword, String agentId);
    
    /**
     * 根据触发卡片获取场景
     *
     * @param cardCode 触发卡片编码
     * @param agentId 智能体ID
     * @return 场景列表
     */
    List<ScenarioDTO> getScenariosByTriggerCard(String cardCode, String agentId);
    
    /**
     * 创建场景
     *
     * @param dto 创建场景所需的信息
     * @return 创建的场景ID
     */
    String createScenario(ScenarioCreateDTO dto);
    
    /**
     * 更新场景
     *
     * @param id 场景ID
     * @param dto 更新场景所需的信息
     */
    void updateScenario(String id, ScenarioUpdateDTO dto);
    
    /**
     * 更新场景启用状态
     *
     * @param id 场景ID
     * @param isActive 启用状态
     */
    void updateScenarioActiveStatus(String id, Integer isActive);
    
    /**
     * 删除场景
     *
     * @param id 场景ID
     */
    void deleteScenario(String id);
    
    /**
     * 检查用户是否有权限访问场景
     *
     * @param scenarioId 场景ID
     * @param userId 用户ID
     * @return 是否有权限
     */
    boolean checkScenarioPermission(String scenarioId, Long userId);
} 