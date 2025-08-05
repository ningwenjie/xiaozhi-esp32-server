package xiaozhi.modules.agent.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import xiaozhi.common.exception.RenException;
import xiaozhi.common.service.impl.BaseServiceImpl;
import xiaozhi.common.user.UserDetail;
import xiaozhi.common.utils.ConvertUtils;
import xiaozhi.common.utils.JsonUtils;
import xiaozhi.modules.agent.dao.ScenarioStepDao;
import xiaozhi.modules.agent.dto.ScenarioStepDTO;
import xiaozhi.modules.agent.entity.ScenarioStepEntity;
import xiaozhi.modules.agent.service.ScenarioStepService;
import xiaozhi.modules.security.user.SecurityUser;

/**
 * 场景步骤配置Service实现类
 *
 * @author AI Assistant
 * @version 1.0, 2024/12/19
 * @since 1.0.0
 */
@Service
@AllArgsConstructor
public class ScenarioStepServiceImpl extends BaseServiceImpl<ScenarioStepDao, ScenarioStepEntity> implements ScenarioStepService {
    
    private final ScenarioStepDao scenarioStepDao;
    
    @Override
    public List<ScenarioStepDTO> getStepsByScenarioId(String scenarioId) {
        List<ScenarioStepEntity> entities = scenarioStepDao.selectByScenarioId(scenarioId);
        return ConvertUtils.sourceToTarget(entities, ScenarioStepDTO.class);
    }
    
    @Override
    public List<ScenarioStepDTO> getStepsByScenarioIdOrdered(String scenarioId) {
        List<ScenarioStepEntity> entities = scenarioStepDao.selectByScenarioIdOrdered(scenarioId);
        return ConvertUtils.sourceToTarget(entities, ScenarioStepDTO.class);
    }
    
    @Override
    public List<ScenarioStepDTO> getStepsByScenarioIdAndType(String scenarioId, String stepType) {
        List<ScenarioStepEntity> entities = scenarioStepDao.selectByScenarioIdAndType(scenarioId, stepType);
        return ConvertUtils.sourceToTarget(entities, ScenarioStepDTO.class);
    }
    
    @Override
    public ScenarioStepDTO getStepByCode(String stepCode) {
        ScenarioStepEntity entity = scenarioStepDao.selectByStepCode(stepCode);
        return ConvertUtils.sourceToTarget(entity, ScenarioStepDTO.class);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createStep(ScenarioStepDTO dto) {
        // 检查步骤编码是否已存在
        ScenarioStepEntity existingStep = scenarioStepDao.selectByStepCode(dto.getStepCode());
        if (existingStep != null) {
            throw new RenException("步骤编码已存在");
        }
        
        ScenarioStepEntity entity = ConvertUtils.sourceToTarget(dto, ScenarioStepEntity.class);
        
        // 设置ID
        if (StringUtils.isBlank(entity.getId())) {
            entity.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        
        // 设置创建时间
        entity.setCreatedAt(new Date());
        
        // 转换JSON字段
        if (dto.getExpectedKeywords() != null) {
            entity.setExpectedKeywords(JsonUtils.toJsonString(dto.getExpectedKeywords()));
        }
        if (dto.getExpectedPhrases() != null) {
            entity.setExpectedPhrases(JsonUtils.toJsonString(dto.getExpectedPhrases()));
        }
        
        // 设置默认值
        if (entity.getMaxAttempts() == null) {
            entity.setMaxAttempts(3);
        }
        if (entity.getTimeoutSeconds() == null) {
            entity.setTimeoutSeconds(10);
        }
        if (StringUtils.isBlank(entity.getSuccessCondition())) {
            entity.setSuccessCondition("partial");
        }
        if (entity.getIsOptional() == null) {
            entity.setIsOptional(0);
        }
        if (StringUtils.isBlank(entity.getStepType())) {
            entity.setStepType("normal");
        }
        
        // 如果没有设置步骤顺序，自动设置
        if (entity.getStepOrder() == null) {
            Integer maxOrder = scenarioStepDao.getMaxStepOrder(entity.getScenarioId());
            entity.setStepOrder(maxOrder != null ? maxOrder + 1 : 1);
        }
        
        scenarioStepDao.insert(entity);
        return entity.getId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStep(String id, ScenarioStepDTO dto) {
        ScenarioStepEntity entity = scenarioStepDao.selectById(id);
        if (entity == null) {
            throw new RenException("步骤不存在");
        }
        
        // 更新字段
        if (StringUtils.isNotBlank(dto.getStepCode())) {
            // 检查步骤编码是否已存在（排除自己）
            ScenarioStepEntity existingStep = scenarioStepDao.selectByStepCode(dto.getStepCode());
            if (existingStep != null && !existingStep.getId().equals(id)) {
                throw new RenException("步骤编码已存在");
            }
            entity.setStepCode(dto.getStepCode());
        }
        if (StringUtils.isNotBlank(dto.getStepName())) {
            entity.setStepName(dto.getStepName());
        }
        if (dto.getStepOrder() != null) {
            entity.setStepOrder(dto.getStepOrder());
        }
        if (StringUtils.isNotBlank(dto.getAiMessage())) {
            entity.setAiMessage(dto.getAiMessage());
        }
        if (dto.getExpectedKeywords() != null) {
            entity.setExpectedKeywords(JsonUtils.toJsonString(dto.getExpectedKeywords()));
        }
        if (dto.getExpectedPhrases() != null) {
            entity.setExpectedPhrases(JsonUtils.toJsonString(dto.getExpectedPhrases()));
        }
        if (dto.getMaxAttempts() != null) {
            entity.setMaxAttempts(dto.getMaxAttempts());
        }
        if (dto.getTimeoutSeconds() != null) {
            entity.setTimeoutSeconds(dto.getTimeoutSeconds());
        }
        if (StringUtils.isNotBlank(dto.getSuccessCondition())) {
            entity.setSuccessCondition(dto.getSuccessCondition());
        }
        if (StringUtils.isNotBlank(dto.getNextStepId())) {
            entity.setNextStepId(dto.getNextStepId());
        }
        if (StringUtils.isNotBlank(dto.getRetryStepId())) {
            entity.setRetryStepId(dto.getRetryStepId());
        }
        if (StringUtils.isNotBlank(dto.getAlternativeMessage())) {
            entity.setAlternativeMessage(dto.getAlternativeMessage());
        }
        if (StringUtils.isNotBlank(dto.getGestureHint())) {
            entity.setGestureHint(dto.getGestureHint());
        }
        if (StringUtils.isNotBlank(dto.getMusicEffect())) {
            entity.setMusicEffect(dto.getMusicEffect());
        }
        if (dto.getIsOptional() != null) {
            entity.setIsOptional(dto.getIsOptional());
        }
        if (StringUtils.isNotBlank(dto.getStepType())) {
            entity.setStepType(dto.getStepType());
        }
        if (StringUtils.isNotBlank(dto.getBranchCondition())) {
            entity.setBranchCondition(dto.getBranchCondition());
        }
        
        // 设置更新时间
        entity.setUpdatedAt(new Date());
        
        scenarioStepDao.updateById(entity);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStep(String id) {
        ScenarioStepEntity entity = scenarioStepDao.selectById(id);
        if (entity == null) {
            throw new RenException("步骤不存在");
        }
        
        scenarioStepDao.deleteById(id);
        
        // 重新排序剩余步骤
        reorderSteps(entity.getScenarioId());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStepsByScenarioId(String scenarioId) {
        scenarioStepDao.deleteByScenarioId(scenarioId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStepOrder(String id, Integer stepOrder) {
        ScenarioStepEntity entity = scenarioStepDao.selectById(id);
        if (entity == null) {
            throw new RenException("步骤不存在");
        }
        
        scenarioStepDao.updateStepOrder(id, stepOrder);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveStep(String id, Integer direction) {
        ScenarioStepEntity entity = scenarioStepDao.selectById(id);
        if (entity == null) {
            throw new RenException("步骤不存在");
        }
        
        List<ScenarioStepEntity> steps = scenarioStepDao.selectByScenarioIdOrdered(entity.getScenarioId());
        int currentIndex = -1;
        
        // 找到当前步骤的索引
        for (int i = 0; i < steps.size(); i++) {
            if (steps.get(i).getId().equals(id)) {
                currentIndex = i;
                break;
            }
        }
        
        if (currentIndex == -1) {
            throw new RenException("步骤不存在");
        }
        
        // 计算目标索引
        int targetIndex = currentIndex + direction;
        if (targetIndex < 0 || targetIndex >= steps.size()) {
            throw new RenException("无法移动到该位置");
        }
        
        // 交换步骤顺序
        ScenarioStepEntity currentStep = steps.get(currentIndex);
        ScenarioStepEntity targetStep = steps.get(targetIndex);
        
        int tempOrder = currentStep.getStepOrder();
        currentStep.setStepOrder(targetStep.getStepOrder());
        targetStep.setStepOrder(tempOrder);
        
        // 更新数据库
        scenarioStepDao.updateById(currentStep);
        scenarioStepDao.updateById(targetStep);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveSteps(String scenarioId, List<ScenarioStepDTO> steps) {
        // 删除现有步骤
        scenarioStepDao.deleteByScenarioId(scenarioId);
        
        // 批量插入新步骤
        for (int i = 0; i < steps.size(); i++) {
            ScenarioStepDTO stepDto = steps.get(i);
            stepDto.setScenarioId(scenarioId);
            stepDto.setStepOrder(i + 1);
            createStep(stepDto);
        }
    }
    
    /**
     * 重新排序步骤
     */
    private void reorderSteps(String scenarioId) {
        List<ScenarioStepEntity> steps = scenarioStepDao.selectByScenarioIdOrdered(scenarioId);
        for (int i = 0; i < steps.size(); i++) {
            ScenarioStepEntity step = steps.get(i);
            if (!step.getStepOrder().equals(i + 1)) {
                step.setStepOrder(i + 1);
                scenarioStepDao.updateById(step);
            }
        }
    }
} 