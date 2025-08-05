# AI陪伴功能模块—引导式场景对话设计方案

## 1. 项目概述

### 1.1 功能目标
- 为特殊儿童提供引导式场景对话训练
- 通过视觉识别（二维码/卡片）或语音关键词触发场景
- 支持300+种场景配置，覆盖日常生活、社交技能、情感表达等
- 大语言模型仅用于判断儿童回答正确性，对话流程完全可配置

### 1.2 核心特性
- **场景配置化**：所有对话流程通过配置实现，无需编程
- **多轮对话**：支持复杂的多轮对话流程设计
- **智能判断**：AI判断儿童回答的准确性，自动调整教学策略
- **个性化**：记忆儿童姓名，提供个性化教学体验
- **动态步骤**：支持配置不固定数量的对话步骤

## 2. 数据库设计

### 2.1 场景配置表 (ai_scenario)
```sql
CREATE TABLE `ai_scenario` (
    `id` VARCHAR(32) NOT NULL COMMENT '场景唯一标识',
    `agent_id` VARCHAR(32) NOT NULL COMMENT '关联的智能体ID',
    `scenario_code` VARCHAR(64) NOT NULL COMMENT '场景编码',
    `scenario_name` VARCHAR(128) NOT NULL COMMENT '场景名称',
    `scenario_type` VARCHAR(32) NOT NULL COMMENT '场景类型：express_needs/greeting/emotion等',
    `trigger_type` VARCHAR(32) NOT NULL COMMENT '触发方式：voice/visual/button',
    `trigger_keywords` TEXT COMMENT '语音触发关键词，JSON格式',
    `trigger_cards` TEXT COMMENT '视觉触发卡片，JSON格式',
    `description` TEXT COMMENT '场景描述',
    `difficulty_level` INT DEFAULT 1 COMMENT '难度等级：1-5',
    `target_age` VARCHAR(32) COMMENT '目标年龄：3-6/7-12等',
    `sort_order` INT DEFAULT 0 COMMENT '排序权重',
    `is_active` TINYINT DEFAULT 1 COMMENT '是否启用：0-禁用 1-启用',
    `creator` BIGINT COMMENT '创建者ID',
    `created_at` DATETIME COMMENT '创建时间',
    `updater` BIGINT COMMENT '更新者ID',
    `updated_at` DATETIME COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_agent_id` (`agent_id`),
    INDEX `idx_scenario_type` (`scenario_type`),
    INDEX `idx_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场景配置表';
```

### 2.2 对话步骤配置表 (ai_scenario_step)
```sql
CREATE TABLE `ai_scenario_step` (
    `id` VARCHAR(32) NOT NULL COMMENT '步骤唯一标识',
    `scenario_id` VARCHAR(32) NOT NULL COMMENT '关联的场景ID',
    `step_code` VARCHAR(64) NOT NULL COMMENT '步骤编码',
    `step_name` VARCHAR(128) NOT NULL COMMENT '步骤名称',
    `step_order` INT NOT NULL COMMENT '步骤顺序',
    `ai_message` TEXT NOT NULL COMMENT 'AI说的话（固定配置的语句）',
    `expected_keywords` TEXT COMMENT '期望的关键词，JSON格式',
    `expected_phrases` TEXT COMMENT '期望的完整短语，JSON格式',
    `max_attempts` INT DEFAULT 3 COMMENT '最大尝试次数',
    `timeout_seconds` INT DEFAULT 10 COMMENT '等待超时时间(秒)',
    `success_condition` VARCHAR(32) DEFAULT 'exact' COMMENT '成功条件：exact/partial/keyword',
    `next_step_id` VARCHAR(32) COMMENT '成功后的下一步ID',
    `retry_step_id` VARCHAR(32) COMMENT '失败后的重试步骤ID',
    `alternative_message` TEXT COMMENT '失败时的替代提示',
    `gesture_hint` VARCHAR(128) COMMENT '手势提示：point_mouth/point_stomach等',
    `music_effect` VARCHAR(128) COMMENT '音效文件名',
    `is_optional` TINYINT DEFAULT 0 COMMENT '是否可选步骤：0-必需 1-可选',
    `step_type` VARCHAR(32) DEFAULT 'normal' COMMENT '步骤类型：normal/start/end/branch',
    `branch_condition` TEXT COMMENT '分支条件，JSON格式',
    `created_at` DATETIME COMMENT '创建时间',
    `updated_at` DATETIME COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_scenario_id` (`scenario_id`),
    INDEX `idx_step_order` (`step_order`),
    INDEX `idx_step_type` (`step_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对话步骤配置表';
```

### 2.3 步骤模板表 (ai_step_template)
```sql
CREATE TABLE `ai_step_template` (
    `id` VARCHAR(32) NOT NULL COMMENT '模板唯一标识',
    `template_code` VARCHAR(64) NOT NULL COMMENT '模板编码',
    `template_name` VARCHAR(128) NOT NULL COMMENT '模板名称',
    `template_type` VARCHAR(32) NOT NULL COMMENT '模板类型：greeting/instruction/encouragement等',
    `ai_message` TEXT NOT NULL COMMENT 'AI说的话模板',
    `expected_keywords` TEXT COMMENT '期望的关键词模板',
    `expected_phrases` TEXT COMMENT '期望的完整短语模板',
    `alternative_message` TEXT COMMENT '替代提示模板',
    `description` TEXT COMMENT '模板描述',
    `is_default` TINYINT DEFAULT 0 COMMENT '是否默认模板',
    `sort_order` INT DEFAULT 0 COMMENT '排序权重',
    `creator` BIGINT COMMENT '创建者ID',
    `created_at` DATETIME COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='步骤模板表';
```

## 3. 前端界面设计

### 3.1 智能体配置页面增强
在现有的`roleConfig.vue`页面添加场景配置按钮：

```vue
<!-- 在角色配置页面添加场景配置按钮 -->
<el-form-item label="场景配置：">
  <el-button type="primary" @click="goToScenarioConfig">
    配置场景对话
  </el-button>
  <span class="hint-text">配置引导式场景对话，帮助儿童练习表达需求</span>
</el-form-item>
```

### 3.2 场景配置主页面 (ScenarioConfig.vue)
```vue
<template>
  <div class="scenario-config">
    <HeaderBar />
    
    <div class="operation-bar">
      <h2 class="page-title">场景配置</h2>
      <div class="right-operations">
        <el-button type="primary" @click="createScenario">新建场景</el-button>
        <el-button @click="importTemplate">导入模板</el-button>
      </div>
    </div>
    
    <div class="main-wrapper">
      <!-- 场景列表 -->
      <div class="scenario-list">
        <el-card v-for="scenario in scenarios" :key="scenario.id" class="scenario-card">
          <div class="scenario-header">
            <h3>{{ scenario.scenarioName }}</h3>
            <div class="scenario-actions">
              <el-button size="mini" @click="editScenario(scenario)">编辑</el-button>
              <el-button size="mini" @click="configureSteps(scenario)">配置步骤</el-button>
              <el-button size="mini" @click="testScenario(scenario)">测试</el-button>
              <el-switch v-model="scenario.isActive" @change="toggleScenario(scenario)" />
            </div>
          </div>
          <div class="scenario-info">
            <span class="type-tag">{{ scenario.scenarioType }}</span>
            <span class="difficulty">难度: {{ scenario.difficultyLevel }}</span>
            <span class="target-age">年龄: {{ scenario.targetAge }}</span>
            <span class="step-count">步骤数: {{ scenario.stepCount }}</span>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>
```

### 3.3 对话步骤配置页面 (ScenarioStepConfig.vue)
```vue
<template>
  <div class="scenario-step-config">
    <HeaderBar />
    
    <div class="operation-bar">
      <h2 class="page-title">对话步骤配置 - {{ scenario.scenarioName }}</h2>
      <div class="right-operations">
        <el-button type="primary" @click="addStep">添加步骤</el-button>
        <el-button @click="importStepTemplate">导入步骤模板</el-button>
        <el-button @click="saveSteps">保存配置</el-button>
      </div>
    </div>
    
    <div class="main-wrapper">
      <!-- 步骤列表 -->
      <div class="step-list">
        <el-card v-for="(step, index) in steps" :key="step.id" class="step-card">
          <div class="step-header">
            <div class="step-info">
              <span class="step-number">步骤 {{ index + 1 }}</span>
              <span class="step-name">{{ step.stepName }}</span>
              <el-tag :type="getStepTypeColor(step.stepType)">{{ step.stepType }}</el-tag>
            </div>
            <div class="step-actions">
              <el-button size="mini" @click="moveStep(index, -1)" :disabled="index === 0">上移</el-button>
              <el-button size="mini" @click="moveStep(index, 1)" :disabled="index === steps.length - 1">下移</el-button>
              <el-button size="mini" type="danger" @click="removeStep(index)">删除</el-button>
            </div>
          </div>
          
          <div class="step-content">
            <el-form :model="step" label-width="120px">
              <el-form-item label="步骤名称">
                <el-input v-model="step.stepName" placeholder="请输入步骤名称" />
              </el-form-item>
              
              <el-form-item label="AI说的话">
                <el-input type="textarea" v-model="step.aiMessage" rows="3" 
                          placeholder="请输入AI要说的固定语句" />
                <div class="hint-text">支持使用 **{childName}** 替换儿童姓名</div>
              </el-form-item>
              
              <el-form-item label="期望回答">
                <el-input v-model="step.expectedKeywords" placeholder="关键词，用逗号分隔" />
                <el-input v-model="step.expectedPhrases" placeholder="完整短语，用逗号分隔" style="margin-top: 5px;" />
              </el-form-item>
              
              <el-form-item label="成功条件">
                <el-select v-model="step.successCondition">
                  <el-option label="完全匹配" value="exact" />
                  <el-option label="部分匹配" value="partial" />
                  <el-option label="关键词匹配" value="keyword" />
                </el-select>
              </el-form-item>
              
              <el-form-item label="最大尝试次数">
                <el-input-number v-model="step.maxAttempts" :min="1" :max="10" />
              </el-form-item>
              
              <el-form-item label="失败提示">
                <el-input type="textarea" v-model="step.alternativeMessage" rows="2" 
                          placeholder="当儿童回答错误时的提示语句" />
              </el-form-item>
              
              <el-form-item label="手势提示">
                <el-select v-model="step.gestureHint" placeholder="选择手势提示">
                  <el-option label="无" value="" />
                  <el-option label="指嘴巴" value="point_mouth" />
                  <el-option label="指肚子" value="point_stomach" />
                  <el-option label="指眼睛" value="point_eyes" />
                  <el-option label="指腿" value="point_legs" />
                </el-select>
              </el-form-item>
              
              <el-form-item label="音效">
                <el-input v-model="step.musicEffect" placeholder="音效文件名" />
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </div>
      
      <!-- 步骤模板选择器 -->
      <el-dialog title="选择步骤模板" :visible.sync="templateDialogVisible" width="60%">
        <div class="template-list">
          <el-card v-for="template in stepTemplates" :key="template.id" 
                   class="template-card" @click="selectStepTemplate(template)">
            <h4>{{ template.templateName }}</h4>
            <p>{{ template.description }}</p>
            <div class="template-preview">
              <strong>AI语句：</strong>{{ template.aiMessage }}
            </div>
          </el-card>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ScenarioStepConfig',
  data() {
    return {
      scenario: {},
      steps: [],
      stepTemplates: [],
      templateDialogVisible: false
    }
  },
  methods: {
    addStep() {
      const newStep = {
        id: this.generateId(),
        stepName: `步骤${this.steps.length + 1}`,
        aiMessage: '',
        expectedKeywords: '',
        expectedPhrases: '',
        successCondition: 'partial',
        maxAttempts: 3,
        timeoutSeconds: 10,
        alternativeMessage: '',
        gestureHint: '',
        musicEffect: '',
        stepType: 'normal',
        stepOrder: this.steps.length + 1
      }
      this.steps.push(newStep)
    },
    
    removeStep(index) {
      this.steps.splice(index, 1)
      this.updateStepOrder()
    },
    
    moveStep(index, direction) {
      const newIndex = index + direction
      if (newIndex >= 0 && newIndex < this.steps.length) {
        const temp = this.steps[index]
        this.steps[index] = this.steps[newIndex]
        this.steps[newIndex] = temp
        this.updateStepOrder()
      }
    },
    
    updateStepOrder() {
      this.steps.forEach((step, index) => {
        step.stepOrder = index + 1
      })
    },
    
    selectStepTemplate(template) {
      const newStep = {
        id: this.generateId(),
        stepName: template.templateName,
        aiMessage: template.aiMessage,
        expectedKeywords: template.expectedKeywords || '',
        expectedPhrases: template.expectedPhrases || '',
        successCondition: 'partial',
        maxAttempts: 3,
        timeoutSeconds: 10,
        alternativeMessage: template.alternativeMessage || '',
        gestureHint: '',
        musicEffect: '',
        stepType: 'normal',
        stepOrder: this.steps.length + 1
      }
      this.steps.push(newStep)
      this.templateDialogVisible = false
    },
    
    saveSteps() {
      // 保存步骤配置到后端
      this.$message.success('步骤配置保存成功')
    }
  }
}
</script>
```

## 4. 核心功能实现

### 4.1 场景触发机制
```python
# 在xiaozhi-server中实现场景触发
class ScenarioTrigger:
    def __init__(self):
        self.scenario_manager = ScenarioManager()
    
    def detect_trigger(self, user_input, input_type):
        """检测场景触发"""
        if input_type == "voice":
            return self.detect_voice_trigger(user_input)
        elif input_type == "visual":
            return self.detect_visual_trigger(user_input)
        elif input_type == "button":
            return self.detect_button_trigger(user_input)
    
    def detect_voice_trigger(self, text):
        """语音触发检测"""
        scenarios = self.scenario_manager.get_active_scenarios()
        for scenario in scenarios:
            keywords = json.loads(scenario.trigger_keywords)
            if any(keyword in text for keyword in keywords):
                return scenario
        return None
```

### 4.2 对话步骤执行器
```python
class DialogueStepExecutor:
    def __init__(self, scenario_id):
        self.scenario = ScenarioManager().get_scenario(scenario_id)
        self.steps = ScenarioStepManager().get_scenario_steps(scenario_id)
        self.current_step_index = 0
        self.attempts = 0
        self.child_name = None
    
    def execute_current_step(self, user_response):
        """执行当前对话步骤"""
        if self.current_step_index >= len(self.steps):
            return {"type": "complete", "message": "恭喜你完成了这个场景！"}
        
        current_step = self.steps[self.current_step_index]
        
        # 替换儿童姓名
        ai_message = current_step.ai_message.replace("**{childName}**", self.child_name or "小朋友")
        
        # 判断用户回答
        is_correct = self.judge_response(user_response, current_step)
        
        if is_correct:
            return self.handle_success(current_step)
        else:
            return self.handle_failure(current_step)
    
    def judge_response(self, user_response, step):
        """判断用户回答是否正确"""
        expected_keywords = json.loads(step.expected_keywords) if step.expected_keywords else []
        expected_phrases = json.loads(step.expected_phrases) if step.expected_phrases else []
        
        if step.success_condition == "exact":
            return user_response in expected_phrases
        elif step.success_condition == "partial":
            return any(phrase in user_response for phrase in expected_phrases)
        elif step.success_condition == "keyword":
            return any(keyword in user_response for keyword in expected_keywords)
        
        return False
    
    def handle_success(self, step):
        """处理成功情况"""
        self.current_step_index += 1
        self.attempts = 0
        
        if self.current_step_index >= len(self.steps):
            return {"type": "complete", "message": "恭喜你完成了这个场景！"}
        else:
            next_step = self.steps[self.current_step_index]
            ai_message = next_step.ai_message.replace("**{childName}**", self.child_name or "小朋友")
            return {"type": "next", "message": ai_message}
    
    def handle_failure(self, step):
        """处理失败情况"""
        self.attempts += 1
        
        if self.attempts >= step.max_attempts:
            # 提供替代方案
            alternative_message = step.alternative_message.replace("**{childName}**", self.child_name or "小朋友")
            return {
                "type": "alternative",
                "message": alternative_message,
                "gesture": step.gesture_hint
            }
        else:
            # 重复示范
            ai_message = step.ai_message.replace("**{childName}**", self.child_name or "小朋友")
            return {
                "type": "retry",
                "message": f"让我们再试一次：{ai_message}"
            }
```

## 5. 配置示例

### 5.1 表达需求场景配置
```json
{
  "scenario": {
    "scenarioCode": "express_needs_thirsty",
    "scenarioName": "表达需求-口渴",
    "scenarioType": "express_needs",
    "triggerType": "voice",
    "triggerKeywords": ["渴了", "口渴", "喝水"],
    "difficultyLevel": 1,
    "targetAge": "3-6"
  },
  "steps": [
    {
      "stepOrder": 1,
      "stepName": "介绍表达需求",
      "aiMessage": "小朋友，今天我们一起练习如何表达需求。什么是表达需求呢，就是比如当我们渴了、饿了、累了、困了的时候，怎样让别人知道我们的想法。现在开始，**{childName}**，现在你渴了，你怎么跟妈妈说呢？",
      "expectedKeywords": "我渴了,我要喝水",
      "expectedPhrases": "我渴了,我要喝水",
      "maxAttempts": 3,
      "timeoutSeconds": 10,
      "successCondition": "partial",
      "alternativeMessage": "**{childName}**，我们也可以这样做：指指自己的嘴巴，说：喝-水（拖长音）"
    },
    {
      "stepOrder": 2,
      "stepName": "示范正确表达",
      "aiMessage": "对，我们要跟妈妈说：我渴了，我要喝水。（示范）请你跟我一起说：我-渴-了，我-要-喝-水（拖长音）",
      "expectedKeywords": "我渴了,我要喝水",
      "expectedPhrases": "我渴了,我要喝水",
      "maxAttempts": 2,
      "successCondition": "exact",
      "alternativeMessage": "你已经做的很好了，每天反复说几遍"我-渴-了，我-要-喝-水"，一定会越来越好的"
    }
  ]
}
```

## 6. 实施计划

### 开发原则
**为了避免与现有代码产生冲突，所有新功能都通过新建文件实现：**
- ✅ 新建数据库表，不修改现有表结构
- ✅ 新建后端模块，不修改现有Controller/Service
- ✅ 新建前端页面，不修改现有Vue组件
- ✅ 新建设备端模块，不修改现有ESP32代码
- ✅ 通过配置和接口集成，而不是直接修改现有代码

### 第一阶段（2周）
- 数据库设计和创建（新建表）
- 基础API开发（新建模块）
- 场景配置界面开发（新建页面）

### 第二阶段（2周）
- 对话流程执行器开发（新建Python模块）
- 学习记录功能（新建模块）
- 基础场景模板（新建配置）

### 第三阶段（1周）
- 设备端集成（新建ESP32模块）
- 测试和优化
- 文档完善

### 第四阶段（1周）
- 用户培训
- 部署上线
- 反馈收集和优化 