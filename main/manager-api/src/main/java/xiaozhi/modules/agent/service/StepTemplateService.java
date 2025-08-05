package xiaozhi.modules.agent.service;

import java.util.List;

import xiaozhi.common.service.BaseService;
import xiaozhi.modules.agent.dto.StepTemplateDTO;
import xiaozhi.modules.agent.entity.StepTemplateEntity;

/**
 * 步骤模板Service接口
 *
 * @author AI Assistant
 * @version 1.0, 2024/12/19
 * @since 1.0.0
 */
public interface StepTemplateService extends BaseService<StepTemplateEntity> {
    
    /**
     * 根据模板类型获取模板列表
     *
     * @param templateType 模板类型
     * @return 模板列表
     */
    List<StepTemplateDTO> getTemplatesByType(String templateType);
    
    /**
     * 获取默认模板列表
     *
     * @return 默认模板列表
     */
    List<StepTemplateDTO> getDefaultTemplates();
    
    /**
     * 根据模板编码获取模板
     *
     * @param templateCode 模板编码
     * @return 模板信息
     */
    StepTemplateDTO getTemplateByCode(String templateCode);
    
    /**
     * 根据模板类型和是否默认获取模板列表
     *
     * @param templateType 模板类型
     * @param isDefault 是否默认
     * @return 模板列表
     */
    List<StepTemplateDTO> getTemplatesByTypeAndDefault(String templateType, Integer isDefault);
    
    /**
     * 创建模板
     *
     * @param dto 模板信息
     * @return 创建的模板ID
     */
    String createTemplate(StepTemplateDTO dto);
    
    /**
     * 更新模板
     *
     * @param id 模板ID
     * @param dto 模板信息
     */
    void updateTemplate(String id, StepTemplateDTO dto);
    
    /**
     * 删除模板
     *
     * @param id 模板ID
     */
    void deleteTemplate(String id);
    
    /**
     * 设置默认模板
     *
     * @param id 模板ID
     * @param isDefault 是否默认
     */
    void setDefaultTemplate(String id, Integer isDefault);
} 