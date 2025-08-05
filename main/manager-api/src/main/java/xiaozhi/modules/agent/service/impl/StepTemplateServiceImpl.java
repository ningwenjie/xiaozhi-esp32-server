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
import xiaozhi.modules.agent.dao.StepTemplateDao;
import xiaozhi.modules.agent.dto.StepTemplateDTO;
import xiaozhi.modules.agent.entity.StepTemplateEntity;
import xiaozhi.modules.agent.service.StepTemplateService;
import xiaozhi.modules.security.user.SecurityUser;

/**
 * 步骤模板Service实现类
 *
 * @author AI Assistant
 * @version 1.0, 2024/12/19
 * @since 1.0.0
 */
@Service
@AllArgsConstructor
public class StepTemplateServiceImpl extends BaseServiceImpl<StepTemplateDao, StepTemplateEntity> implements StepTemplateService {
    
    private final StepTemplateDao stepTemplateDao;
    
    @Override
    public List<StepTemplateDTO> getTemplatesByType(String templateType) {
        List<StepTemplateEntity> entities = stepTemplateDao.selectByTemplateType(templateType);
        return ConvertUtils.sourceToTarget(entities, StepTemplateDTO.class);
    }
    
    @Override
    public List<StepTemplateDTO> getDefaultTemplates() {
        List<StepTemplateEntity> entities = stepTemplateDao.selectDefaultTemplates();
        return ConvertUtils.sourceToTarget(entities, StepTemplateDTO.class);
    }
    
    @Override
    public StepTemplateDTO getTemplateByCode(String templateCode) {
        StepTemplateEntity entity = stepTemplateDao.selectByTemplateCode(templateCode);
        return ConvertUtils.sourceToTarget(entity, StepTemplateDTO.class);
    }
    
    @Override
    public List<StepTemplateDTO> getTemplatesByTypeAndDefault(String templateType, Integer isDefault) {
        List<StepTemplateEntity> entities = stepTemplateDao.selectByTypeAndDefault(templateType, isDefault);
        return ConvertUtils.sourceToTarget(entities, StepTemplateDTO.class);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createTemplate(StepTemplateDTO dto) {
        // 检查模板编码是否已存在
        StepTemplateEntity existingTemplate = stepTemplateDao.selectByTemplateCode(dto.getTemplateCode());
        if (existingTemplate != null) {
            throw new RenException("模板编码已存在");
        }
        
        StepTemplateEntity entity = ConvertUtils.sourceToTarget(dto, StepTemplateEntity.class);
        
        // 设置ID
        if (StringUtils.isBlank(entity.getId())) {
            entity.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        
        // 设置创建时间和创建者
        UserDetail user = SecurityUser.getUser();
        entity.setCreatedAt(new Date());
        entity.setCreator(user.getId());
        
        // 转换JSON字段
        if (dto.getExpectedKeywords() != null) {
            entity.setExpectedKeywords(JsonUtils.toJsonString(dto.getExpectedKeywords()));
        }
        if (dto.getExpectedPhrases() != null) {
            entity.setExpectedPhrases(JsonUtils.toJsonString(dto.getExpectedPhrases()));
        }
        
        // 设置默认值
        if (entity.getIsDefault() == null) {
            entity.setIsDefault(0);
        }
        if (entity.getSortOrder() == null) {
            entity.setSortOrder(0);
        }
        
        stepTemplateDao.insert(entity);
        return entity.getId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplate(String id, StepTemplateDTO dto) {
        StepTemplateEntity entity = stepTemplateDao.selectById(id);
        if (entity == null) {
            throw new RenException("模板不存在");
        }
        
        // 更新字段
        if (StringUtils.isNotBlank(dto.getTemplateCode())) {
            // 检查模板编码是否已存在（排除自己）
            StepTemplateEntity existingTemplate = stepTemplateDao.selectByTemplateCode(dto.getTemplateCode());
            if (existingTemplate != null && !existingTemplate.getId().equals(id)) {
                throw new RenException("模板编码已存在");
            }
            entity.setTemplateCode(dto.getTemplateCode());
        }
        if (StringUtils.isNotBlank(dto.getTemplateName())) {
            entity.setTemplateName(dto.getTemplateName());
        }
        if (StringUtils.isNotBlank(dto.getTemplateType())) {
            entity.setTemplateType(dto.getTemplateType());
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
        if (StringUtils.isNotBlank(dto.getAlternativeMessage())) {
            entity.setAlternativeMessage(dto.getAlternativeMessage());
        }
        if (StringUtils.isNotBlank(dto.getDescription())) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getIsDefault() != null) {
            entity.setIsDefault(dto.getIsDefault());
        }
        if (dto.getSortOrder() != null) {
            entity.setSortOrder(dto.getSortOrder());
        }
        
        stepTemplateDao.updateById(entity);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplate(String id) {
        StepTemplateEntity entity = stepTemplateDao.selectById(id);
        if (entity == null) {
            throw new RenException("模板不存在");
        }
        
        stepTemplateDao.deleteById(id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultTemplate(String id, Integer isDefault) {
        StepTemplateEntity entity = stepTemplateDao.selectById(id);
        if (entity == null) {
            throw new RenException("模板不存在");
        }
        
        entity.setIsDefault(isDefault);
        stepTemplateDao.updateById(entity);
    }
} 