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
import xiaozhi.modules.agent.dto.ScenarioDTO;
import xiaozhi.modules.agent.dto.ScenarioCreateDTO;
import xiaozhi.modules.agent.dto.ScenarioUpdateDTO;
import xiaozhi.modules.agent.service.ScenarioService;

/**
 * 场景配置Controller
 *
 * @author AI Assistant
 * @version 1.0, 2024/12/19
 * @since 1.0.0
 */
@Tag(name = "场景配置管理")
@AllArgsConstructor
@RestController
@RequestMapping("/scenario")
public class ScenarioController {
    
    private final ScenarioService scenarioService;
    
    @GetMapping("/agent/{agentId}")
    @Operation(summary = "根据智能体ID获取场景列表")
    @RequiresPermissions("sys:role:normal")
    public Result<List<ScenarioDTO>> getScenariosByAgentId(@PathVariable("agentId") String agentId) {
        List<ScenarioDTO> scenarios = scenarioService.getScenariosByAgentId(agentId);
        return new Result<List<ScenarioDTO>>().ok(scenarios);
    }
    
    @GetMapping("/type/{scenarioType}")
    @Operation(summary = "根据场景类型获取场景列表")
    @RequiresPermissions("sys:role:normal")
    public Result<List<ScenarioDTO>> getScenariosByType(@PathVariable("scenarioType") String scenarioType) {
        List<ScenarioDTO> scenarios = scenarioService.getScenariosByType(scenarioType);
        return new Result<List<ScenarioDTO>>().ok(scenarios);
    }
    
    @GetMapping("/active")
    @Operation(summary = "获取启用的场景列表")
    @RequiresPermissions("sys:role:normal")
    public Result<List<ScenarioDTO>> getActiveScenarios() {
        List<ScenarioDTO> scenarios = scenarioService.getActiveScenarios();
        return new Result<List<ScenarioDTO>>().ok(scenarios);
    }
    
    @GetMapping("/code/{scenarioCode}")
    @Operation(summary = "根据场景编码获取场景")
    @RequiresPermissions("sys:role:normal")
    public Result<ScenarioDTO> getScenarioByCode(@PathVariable("scenarioCode") String scenarioCode) {
        ScenarioDTO scenario = scenarioService.getScenarioByCode(scenarioCode);
        return new Result<ScenarioDTO>().ok(scenario);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取场景详情")
    @RequiresPermissions("sys:role:normal")
    public Result<ScenarioDTO> getScenarioById(@PathVariable("id") String id) {
        ScenarioDTO scenario = scenarioService.getScenarioById(id);
        return new Result<ScenarioDTO>().ok(scenario);
    }
    
    @PostMapping
    @Operation(summary = "创建场景")
    @RequiresPermissions("sys:role:normal")
    public Result<String> createScenario(@RequestBody @Valid ScenarioCreateDTO dto) {
        String scenarioId = scenarioService.createScenario(dto);
        return new Result<String>().ok(scenarioId);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新场景")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> updateScenario(@PathVariable("id") String id, @RequestBody @Valid ScenarioUpdateDTO dto) {
        scenarioService.updateScenario(id, dto);
        return new Result<Void>().ok();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除场景")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> deleteScenario(@PathVariable("id") String id) {
        scenarioService.deleteScenario(id);
        return new Result<Void>().ok();
    }
    
    @PutMapping("/{id}/active")
    @Operation(summary = "启用/禁用场景")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> toggleScenarioActive(
            @PathVariable("id") String id,
            @RequestParam("isActive") Integer isActive) {
        scenarioService.toggleScenarioActive(id, isActive);
        return new Result<Void>().ok();
    }
    
    @GetMapping("/agent/{agentId}/type/{scenarioType}")
    @Operation(summary = "根据智能体ID和场景类型获取场景列表")
    @RequiresPermissions("sys:role:normal")
    public Result<List<ScenarioDTO>> getScenariosByAgentIdAndType(
            @PathVariable("agentId") String agentId,
            @PathVariable("scenarioType") String scenarioType) {
        List<ScenarioDTO> scenarios = scenarioService.getScenariosByAgentIdAndType(agentId, scenarioType);
        return new Result<List<ScenarioDTO>>().ok(scenarios);
    }
}