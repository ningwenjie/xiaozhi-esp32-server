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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import xiaozhi.common.utils.Result;
import xiaozhi.modules.agent.dto.ScenarioStepDTO;
import xiaozhi.modules.agent.service.ScenarioStepService;

/**
 * 对话步骤配置Controller
 *
 * @author AI Assistant
 * @version 1.0, 2024/12/19
 * @since 1.0.0
 */
@Tag(name = "对话步骤配置管理")
@AllArgsConstructor
@RestController
@RequestMapping("/scenario-step")
public class ScenarioStepController {
    
    private final ScenarioStepService scenarioStepService;
    
    @GetMapping("/scenario/{scenarioId}")
    @Operation(summary = "根据场景ID获取步骤列表")
    @RequiresPermissions("sys:role:normal")
    public Result<List<ScenarioStepDTO>> getStepsByScenarioId(@PathVariable("scenarioId") String scenarioId) {
        List<ScenarioStepDTO> steps = scenarioStepService.getStepsByScenarioId(scenarioId);
        return new Result<List<ScenarioStepDTO>>().ok(steps);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取步骤详情")
    @RequiresPermissions("sys:role:normal")
    public Result<ScenarioStepDTO> getStepById(@PathVariable("id") String id) {
        ScenarioStepDTO step = scenarioStepService.getStepById(id);
        return new Result<ScenarioStepDTO>().ok(step);
    }
    
    @PostMapping
    @Operation(summary = "创建步骤")
    @RequiresPermissions("sys:role:normal")
    public Result<String> createStep(@RequestBody @Valid ScenarioStepDTO dto) {
        String stepId = scenarioStepService.createStep(dto);
        return new Result<String>().ok(stepId);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新步骤")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> updateStep(@PathVariable("id") String id, @RequestBody @Valid ScenarioStepDTO dto) {
        scenarioStepService.updateStep(id, dto);
        return new Result<Void>().ok();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除步骤")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> deleteStep(@PathVariable("id") String id) {
        scenarioStepService.deleteStep(id);
        return new Result<Void>().ok();
    }
    
    @PostMapping("/scenario/{scenarioId}/batch")
    @Operation(summary = "批量保存步骤")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> batchSaveSteps(@PathVariable("scenarioId") String scenarioId, 
                                      @RequestBody List<ScenarioStepDTO> steps) {
        scenarioStepService.batchSaveSteps(scenarioId, steps);
        return new Result<Void>().ok();
    }
    
    @PutMapping("/{id}/order")
    @Operation(summary = "更新步骤顺序")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> updateStepOrder(@PathVariable("id") String id, 
                                       @RequestBody Integer newOrder) {
        scenarioStepService.updateStepOrder(id, newOrder);
        return new Result<Void>().ok();
    }
    
    @GetMapping("/scenario/{scenarioId}/type/{stepType}")
    @Operation(summary = "根据场景ID和步骤类型获取步骤列表")
    @RequiresPermissions("sys:role:normal")
    public Result<List<ScenarioStepDTO>> getStepsByScenarioIdAndType(
            @PathVariable("scenarioId") String scenarioId,
            @PathVariable("stepType") String stepType) {
        List<ScenarioStepDTO> steps = scenarioStepService.getStepsByScenarioIdAndType(scenarioId, stepType);
        return new Result<List<ScenarioStepDTO>>().ok(steps);
    }
}