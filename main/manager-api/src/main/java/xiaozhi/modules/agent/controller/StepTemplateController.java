package xiaozhi.modules.agent.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import xiaozhi.common.utils.Result;
import xiaozhi.modules.agent.dto.StepTemplateDTO;
import xiaozhi.modules.agent.service.StepTemplateService;

/**
 * 步骤模板Controller
 *
 * @author AI Assistant
 * @version 1.0, 2024/12/19
 * @since 1.0.0
 */
@Tag(name = "步骤模板管理")
@AllArgsConstructor
@RestController
@RequestMapping("/step-template")
public class StepTemplateController {
    
    private final StepTemplateService stepTemplateService;
    
    @GetMapping("/type/{templateType}")
    @Operation(summary = "根据模板类型获取模板列表")
    @RequiresPermissions("sys:role:normal")
    public Result<List<StepTemplateDTO>> getTemplatesByType(@PathVariable("templateType") String templateType) {
        List<StepTemplateDTO> templates = stepTemplateService.getTemplatesByType(templateType);
        return new Result<List<StepTemplateDTO>>().ok(templates);
    }
    
    @GetMapping("/default")
    @Operation(summary = "获取默认模板列表")
    @RequiresPermissions("sys:role:normal")
    public Result<List<StepTemplateDTO>> getDefaultTemplates() {
        List<StepTemplateDTO> templates = stepTemplateService.getDefaultTemplates();
        return new Result<List<StepTemplateDTO>>().ok(templates);
    }
    
    @GetMapping("/code/{templateCode}")
    @Operation(summary = "根据模板编码获取模板")
    @RequiresPermissions("sys:role:normal")
    public Result<StepTemplateDTO> getTemplateByCode(@PathVariable("templateCode") String templateCode) {
        StepTemplateDTO template = stepTemplateService.getTemplateByCode(templateCode);
        return new Result<StepTemplateDTO>().ok(template);
    }
    
    @GetMapping("/type/{templateType}/default/{isDefault}")
    @Operation(summary = "根据模板类型和是否默认获取模板列表")
    @RequiresPermissions("sys:role:normal")
    public Result<List<StepTemplateDTO>> getTemplatesByTypeAndDefault(
            @PathVariable("templateType") String templateType,
            @PathVariable("isDefault") Integer isDefault) {
        List<StepTemplateDTO> templates = stepTemplateService.getTemplatesByTypeAndDefault(templateType, isDefault);
        return new Result<List<StepTemplateDTO>>().ok(templates);
    }
    
    @PostMapping
    @Operation(summary = "创建模板")
    @RequiresPermissions("sys:role:normal")
    public Result<String> createTemplate(@RequestBody @Valid StepTemplateDTO dto) {
        String templateId = stepTemplateService.createTemplate(dto);
        return new Result<String>().ok(templateId);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新模板")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> updateTemplate(@PathVariable("id") String id, @RequestBody @Valid StepTemplateDTO dto) {
        stepTemplateService.updateTemplate(id, dto);
        return new Result<Void>().ok();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除模板")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> deleteTemplate(@PathVariable("id") String id) {
        stepTemplateService.deleteTemplate(id);
        return new Result<Void>().ok();
    }
    
    @PutMapping("/{id}/default")
    @Operation(summary = "设置默认模板")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> setDefaultTemplate(
            @PathVariable("id") String id,
            @RequestParam("isDefault") Integer isDefault) {
        stepTemplateService.setDefaultTemplate(id, isDefault);
        return new Result<Void>().ok();
    }
} 