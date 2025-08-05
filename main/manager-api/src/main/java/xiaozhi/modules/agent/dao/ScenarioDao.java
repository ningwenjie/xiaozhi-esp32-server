package xiaozhi.modules.agent.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import xiaozhi.common.dao.BaseDao;
import xiaozhi.modules.agent.entity.ScenarioEntity;

@Mapper
public interface ScenarioDao extends BaseDao<ScenarioEntity> {
    
    /**
     * 根据智能体ID查询场景列表
     * 
     * @param agentId 智能体ID
     * @return 场景列表
     */
    List<ScenarioEntity> selectByAgentId(@Param("agentId") String agentId);
    
    /**
     * 根据智能体ID和场景类型查询场景列表
     * 
     * @param agentId 智能体ID
     * @param scenarioType 场景类型
     * @return 场景列表
     */
    List<ScenarioEntity> selectByAgentIdAndType(@Param("agentId") String agentId, @Param("scenarioType") String scenarioType);
    
    /**
     * 根据智能体ID查询启用的场景列表
     * 
     * @param agentId 智能体ID
     * @return 启用的场景列表
     */
    List<ScenarioEntity> selectActiveByAgentId(@Param("agentId") String agentId);
    
    /**
     * 根据场景编码查询场景
     * 
     * @param scenarioCode 场景编码
     * @return 场景信息
     */
    ScenarioEntity selectByScenarioCode(@Param("scenarioCode") String scenarioCode);
    
    /**
     * 根据触发关键词查询场景
     * 
     * @param keyword 触发关键词
     * @param agentId 智能体ID
     * @return 场景列表
     */
    List<ScenarioEntity> selectByTriggerKeyword(@Param("keyword") String keyword, @Param("agentId") String agentId);
    
    /**
     * 根据触发卡片查询场景
     * 
     * @param cardCode 触发卡片编码
     * @param agentId 智能体ID
     * @return 场景列表
     */
    List<ScenarioEntity> selectByTriggerCard(@Param("cardCode") String cardCode, @Param("agentId") String agentId);
    
    /**
     * 更新场景启用状态
     * 
     * @param id 场景ID
     * @param isActive 启用状态
     * @return 更新行数
     */
    int updateActiveStatus(@Param("id") String id, @Param("isActive") Integer isActive);
} 