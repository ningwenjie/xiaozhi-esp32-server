package xiaozhi.modules.agent.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import xiaozhi.common.constant.Constant;
import xiaozhi.common.page.PageData;
import xiaozhi.common.utils.Result;
import xiaozhi.modules.agent.dto.LearningRecordDTO;
import xiaozhi.modules.agent.entity.LearningRecordEntity;
import xiaozhi.modules.agent.service.LearningRecordService;

/**
 * 学习记录Controller
 *
 * @author AI Assistant
 * @version 1.0, 2024/12/19
 * @since 1.0.0
 */
@Tag(name = "学习记录管理")
@AllArgsConstructor
@RestController
@RequestMapping("/learning-record")
public class LearningRecordController {
    
    private final LearningRecordService learningRecordService;
    
    @GetMapping("/list")
    @Operation(summary = "获取学习记录分页列表")
    @RequiresPermissions("sys:role:normal")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "当前页码，从1开始", required = true),
            @Parameter(name = Constant.LIMIT, description = "每页显示记录数", required = true),
    })
    public Result<PageData<LearningRecordEntity>> getLearningRecordPage(
            @Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        PageData<LearningRecordEntity> page = learningRecordService.getLearningRecordPage(params);
        return new Result<PageData<LearningRecordEntity>>().ok(page);
    }
    
    @GetMapping("/child/{childName}")
    @Operation(summary = "根据儿童姓名获取学习记录")
    @RequiresPermissions("sys:role:normal")
    public Result<List<LearningRecordDTO>> getRecordsByChildName(@PathVariable("childName") String childName) {
        List<LearningRecordDTO> records = learningRecordService.getRecordsByChildName(childName);
        return new Result<List<LearningRecordDTO>>().ok(records);
    }
    
    @GetMapping("/scenario/{scenarioId}")
    @Operation(summary = "根据场景ID获取学习记录")
    @RequiresPermissions("sys:role:normal")
    public Result<List<LearningRecordDTO>> getRecordsByScenarioId(@PathVariable("scenarioId") String scenarioId) {
        List<LearningRecordDTO> records = learningRecordService.getRecordsByScenarioId(scenarioId);
        return new Result<List<LearningRecordDTO>>().ok(records);
    }
    
    @GetMapping("/session/{sessionId}")
    @Operation(summary = "根据会话ID获取学习记录")
    @RequiresPermissions("sys:role:normal")
    public Result<List<LearningRecordDTO>> getRecordsBySessionId(@PathVariable("sessionId") String sessionId) {
        List<LearningRecordDTO> records = learningRecordService.getRecordsBySessionId(sessionId);
        return new Result<List<LearningRecordDTO>>().ok(records);
    }
    
    @GetMapping("/child/{childName}/date-range")
    @Operation(summary = "根据儿童姓名和日期范围获取学习记录")
    @RequiresPermissions("sys:role:normal")
    public Result<List<LearningRecordDTO>> getRecordsByChildNameAndDateRange(
            @PathVariable("childName") String childName,
            @RequestParam("startDate") Date startDate,
            @RequestParam("endDate") Date endDate) {
        List<LearningRecordDTO> records = learningRecordService.getRecordsByChildNameAndDateRange(childName, startDate, endDate);
        return new Result<List<LearningRecordDTO>>().ok(records);
    }
    
    @GetMapping("/scenario/{scenarioId}/child/{childName}")
    @Operation(summary = "根据场景ID和儿童姓名获取学习记录")
    @RequiresPermissions("sys:role:normal")
    public Result<List<LearningRecordDTO>> getRecordsByScenarioIdAndChildName(
            @PathVariable("scenarioId") String scenarioId,
            @PathVariable("childName") String childName) {
        List<LearningRecordDTO> records = learningRecordService.getRecordsByScenarioIdAndChildName(scenarioId, childName);
        return new Result<List<LearningRecordDTO>>().ok(records);
    }
    
    @PostMapping
    @Operation(summary = "创建学习记录")
    @RequiresPermissions("sys:role:normal")
    public Result<String> createLearningRecord(@RequestBody @Valid LearningRecordDTO dto) {
        String recordId = learningRecordService.createLearningRecord(dto);
        return new Result<String>().ok(recordId);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新学习记录")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> updateLearningRecord(@PathVariable("id") String id, @RequestBody @Valid LearningRecordDTO dto) {
        learningRecordService.updateLearningRecord(id, dto);
        return new Result<Void>().ok();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除学习记录")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> deleteLearningRecord(@PathVariable("id") String id) {
        learningRecordService.deleteLearningRecord(id);
        return new Result<Void>().ok();
    }
    
    @GetMapping("/child/{childName}/success-rate")
    @Operation(summary = "统计儿童的学习成功率")
    @RequiresPermissions("sys:role:normal")
    public Result<Double> getSuccessRate(
            @PathVariable("childName") String childName,
            @RequestParam(value = "scenarioId", required = false) String scenarioId) {
        Double successRate = learningRecordService.getSuccessRate(childName, scenarioId);
        return new Result<Double>().ok(successRate);
    }
    
    @GetMapping("/child/{childName}/learning-count")
    @Operation(summary = "统计儿童的学习次数")
    @RequiresPermissions("sys:role:normal")
    public Result<Integer> getLearningCount(
            @PathVariable("childName") String childName,
            @RequestParam(value = "scenarioId", required = false) String scenarioId) {
        Integer learningCount = learningRecordService.getLearningCount(childName, scenarioId);
        return new Result<Integer>().ok(learningCount);
    }
    
    @GetMapping("/child/{childName}/stats")
    @Operation(summary = "获取儿童学习统计信息")
    @RequiresPermissions("sys:role:normal")
    public Result<Map<String, Object>> getChildLearningStats(@PathVariable("childName") String childName) {
        Map<String, Object> stats = learningRecordService.getChildLearningStats(childName);
        return new Result<Map<String, Object>>().ok(stats);
    }
} 