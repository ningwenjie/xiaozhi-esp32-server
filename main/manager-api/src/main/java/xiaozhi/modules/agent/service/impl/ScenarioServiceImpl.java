package xiaozhi.modules.agent.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import lombok.AllArgsConstructor;
import xiaozhi.common.exception.RenException;
import xiaozhi.common.page.PageData;
import xiaozhi.common.service.impl.BaseServiceImpl;
import xiaozhi.common.user.UserDetail;
import xiaozhi.common.utils.ConvertUtils;
import xiaozhi.common.utils.JsonUtils;
import xiaozhi.modules.agent.dao.ScenarioDao;
import xiaozhi.modules.agent.dto.ScenarioCreateDTO;
import xiaozhi.modules.agent.dto.ScenarioDTO;
import xiaozhi.modules.agent.dto.ScenarioUpdateDTO;
import xiaozhi.modules.agent.entity.ScenarioEntity;
import xiaozhi.modules.agent.service.ScenarioService;
import xiaozhi.modules.security.user.SecurityUser;

/**
 * 场景配置Service实现类
 *
 * @author AI Assistant
 * @version 1.0, 2024/12/19
 * @since 1.0.0
 */
@Service
@AllArgsConstructor
public class ScenarioServiceImpl extends BaseServiceImpl<ScenarioDao, ScenarioEntity> implements ScenarioService {
    
    private final ScenarioDao scenarioDao;
    
    @Override
    public PageData<ScenarioEntity> getScenarioPage(Map<String, Object> params) {
        IPage<ScenarioEntity> page = scenarioDao.selectPage(
                getPage(params, "created_at", false),
                new QueryWrapper<>());
        return new PageData<>(page.getRecords(), page.getTotal());
    }
    
    @Override
    public List<ScenarioDTO> getScenariosByAgentId(String agentId) {
        List<ScenarioEntity> entities = scenarioDao.selectByAgentId(agentId);
        return ConvertUtils.sourceToTarget(entities, ScenarioDTO.class);
    }
    
    @Override
    public List<ScenarioDTO> getScenariosByAgentIdAndType(String agentId, String scenarioType) {
        List<ScenarioEntity> entities = scenarioDao.selectByAgentIdAndType(agentId, scenarioType);
        return ConvertUtils.sourceToTarget(entities, ScenarioDTO.class);
    }
    
    @Override
    public List<ScenarioDTO> getActiveScenariosByAgentId(String agentId) {
        List<ScenarioEntity> entities = scenarioDao.selectActiveByAgentId(agentId);
        return ConvertUtils.sourceToTarget(entities, ScenarioDTO.class);
    }
    
    @Override
    public ScenarioDTO getScenarioByCode(String scenarioCode) {
        ScenarioEntity entity = scenarioDao.selectByScenarioCode(scenarioCode);
        return ConvertUtils.sourceToTarget(entity, ScenarioDTO.class);
    }
    
    @Override
    public List<ScenarioDTO> getScenariosByTriggerKeyword(String keyword, String agentId) {
        List<ScenarioEntity> entities = scenarioDao.selectByTriggerKeyword(keyword, agentId);
        return ConvertUtils.sourceToTarget(entities, ScenarioDTO.class);
    }
    
    @Override
    public List<ScenarioDTO> getScenariosByTriggerCard(String cardCode, String agentId) {
        List<ScenarioEntity> entities = scenarioDao.selectByTriggerCard(cardCode, agentId);
        return ConvertUtils.sourceToTarget(entities, ScenarioDTO.class);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createScenario(ScenarioCreateDTO dto) {
        // 检查场景编码是否已存在
        ScenarioEntity existingScenario = scenarioDao.selectByScenarioCode(dto.getScenarioCode());
        if (existingScenario != null) {
            throw new RenException("场景编码已存在");
        }
        
        ScenarioEntity entity = ConvertUtils.sourceToTarget(dto, ScenarioEntity.class);
        
        // 设置ID
        if (StringUtils.isBlank(entity.getId())) {
            entity.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        
        // 设置创建时间和创建者
        UserDetail user = SecurityUser.getUser();
        entity.setCreatedAt(new Date());
        entity.setCreator(user.getId());
        
        // 转换JSON字段
        if (dto.getTriggerKeywords() != null) {
            entity.setTriggerKeywords(JsonUtils.toJsonString(dto.getTriggerKeywords()));
        }
        if (dto.getTriggerCards() != null) {
            entity.setTriggerCards(JsonUtils.toJsonString(dto.getTriggerCards()));
        }
        
        // 设置默认值
        if (entity.getIsActive() == null) {
            entity.setIsActive(1);
        }
        if (entity.getSortOrder() == null) {
            entity.setSortOrder(0);
        }
        if (entity.getDifficultyLevel() == null) {
            entity.setDifficultyLevel(1);
        }
        
        scenarioDao.insert(entity);
        return entity.getId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateScenario(String id, ScenarioUpdateDTO dto) {
        ScenarioEntity entity = scenarioDao.selectById(id);
        if (entity == null) {
            throw new RenException("场景不存在");
        }
        
        // 检查权限
        UserDetail user = SecurityUser.getUser();
        if (!checkScenarioPermission(id, user.getId())) {
            throw new RenException("无权限操作此场景");
        }
        
        // 更新字段
        if (StringUtils.isNotBlank(dto.getScenarioCode())) {
            // 检查场景编码是否已存在（排除自己）
            ScenarioEntity existingScenario = scenarioDao.selectByScenarioCode(dto.getScenarioCode());
            if (existingScenario != null && !existingScenario.getId().equals(id)) {
                throw new RenException("场景编码已存在");
            }
            entity.setScenarioCode(dto.getScenarioCode());
        }
        if (StringUtils.isNotBlank(dto.getScenarioName())) {
            entity.setScenarioName(dto.getScenarioName());
        }
        if (StringUtils.isNotBlank(dto.getScenarioType())) {
            entity.setScenarioType(dto.getScenarioType());
        }
        if (StringUtils.isNotBlank(dto.getTriggerType())) {
            entity.setTriggerType(dto.getTriggerType());
        }
        if (dto.getTriggerKeywords() != null) {
            entity.setTriggerKeywords(JsonUtils.toJsonString(dto.getTriggerKeywords()));
        }
        if (dto.getTriggerCards() != null) {
            entity.setTriggerCards(JsonUtils.toJsonString(dto.getTriggerCards()));
        }
        if (StringUtils.isNotBlank(dto.getDescription())) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getDifficultyLevel() != null) {
            entity.setDifficultyLevel(dto.getDifficultyLevel());
        }
        if (StringUtils.isNotBlank(dto.getTargetAge())) {
            entity.setTargetAge(dto.getTargetAge());
        }
        if (dto.getSortOrder() != null) {
            entity.setSortOrder(dto.getSortOrder());
        }
        if (dto.getIsActive() != null) {
            entity.setIsActive(dto.getIsActive());
        }
        
        // 设置更新时间
        entity.setUpdatedAt(new Date());
        entity.setUpdater(user.getId());
        
        scenarioDao.updateById(entity);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateScenarioActiveStatus(String id, Integer isActive) {
        ScenarioEntity entity = scenarioDao.selectById(id);
        if (entity == null) {
            throw new RenException("场景不存在");
        }
        
        // 检查权限
        UserDetail user = SecurityUser.getUser();
        if (!checkScenarioPermission(id, user.getId())) {
            throw new RenException("无权限操作此场景");
        }
        
        scenarioDao.updateActiveStatus(id, isActive);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteScenario(String id) {
        ScenarioEntity entity = scenarioDao.selectById(id);
        if (entity == null) {
            throw new RenException("场景不存在");
        }
        
        // 检查权限
        UserDetail user = SecurityUser.getUser();
        if (!checkScenarioPermission(id, user.getId())) {
            throw new RenException("无权限操作此场景");
        }
        
        scenarioDao.deleteById(id);
    }
    
    @Override
    public boolean checkScenarioPermission(String scenarioId, Long userId) {
        // 这里可以根据实际需求实现权限检查逻辑
        // 暂时返回true，表示有权限
        return true;
    }
} 