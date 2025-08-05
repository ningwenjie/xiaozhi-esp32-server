<template>
  <div class="scenario-step-config">
    <HeaderBar />

    <div class="operation-bar">
      <h2 class="page-title">对话步骤配置 - {{ scenarioName }}</h2>
      <div class="right-operations">
        <el-button type="primary" @click="addStep">添加步骤</el-button>
        <el-button @click="importStepTemplate">导入步骤模板</el-button>
        <el-button @click="saveSteps">保存配置</el-button>
        <el-button @click="goBack">返回</el-button>
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
              <el-tag :type="getStepTypeColor(step.stepType)">{{ getStepTypeName(step.stepType) }}</el-tag>
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
import HeaderBar from '@/components/HeaderBar.vue';
import stepApi from '@/apis/module/step.js';
import templateApi from '@/apis/module/template.js';

export default {
  name: 'ScenarioStepConfig',
  components: {
    HeaderBar
  },
  data() {
    return {
      scenarioId: '',
      scenarioName: '',
      steps: [],
      stepTemplates: [],
      templateDialogVisible: false
    };
  },
  mounted() {
    this.scenarioId = this.$route.params.scenarioId;
    this.scenarioName = this.$route.query.scenarioName || '场景';
    this.loadSteps();
  },
  methods: {
    // 加载步骤列表
    loadSteps() {
      stepApi.getStepsByScenarioIdOrdered(this.scenarioId, (res) => {
        if (res.code === 0) {
          this.steps = res.data || [];
        } else {
          this.$message.error(res.msg || '加载步骤失败');
        }
      });
    },

    // 添加步骤
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
      };
      this.steps.push(newStep);
    },

    // 删除步骤
    removeStep(index) {
      this.$confirm('确定要删除这个步骤吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.steps.splice(index, 1);
        this.updateStepOrder();
      }).catch(() => {});
    },

    // 移动步骤
    moveStep(index, direction) {
      const newIndex = index + direction;
      if (newIndex >= 0 && newIndex < this.steps.length) {
        const temp = this.steps[index];
        this.steps[index] = this.steps[newIndex];
        this.steps[newIndex] = temp;
        this.updateStepOrder();
      }
    },

    // 更新步骤顺序
    updateStepOrder() {
      this.steps.forEach((step, index) => {
        step.stepOrder = index + 1;
      });
    },

    // 导入步骤模板
    importStepTemplate() {
      templateApi.getDefaultTemplates((res) => {
        if (res.code === 0) {
          this.stepTemplates = res.data || [];
          this.templateDialogVisible = true;
        } else {
          this.$message.error(res.msg || '加载模板失败');
        }
      });
    },

    // 选择步骤模板
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
      };
      this.steps.push(newStep);
      this.templateDialogVisible = false;
    },

    // 保存步骤配置
    saveSteps() {
      if (this.steps.length === 0) {
        this.$message.warning('请至少添加一个步骤');
        return;
      }

      // 验证步骤数据
      for (let i = 0; i < this.steps.length; i++) {
        const step = this.steps[i];
        if (!step.stepName) {
          this.$message.warning(`请填写步骤${i + 1}的名称`);
          return;
        }
        if (!step.aiMessage) {
          this.$message.warning(`请填写步骤${i + 1}的AI语句`);
          return;
        }
      }

      stepApi.batchSaveSteps(this.scenarioId, this.steps, (res) => {
        if (res.code === 0) {
          this.$message.success('步骤配置保存成功');
        } else {
          this.$message.error(res.msg || '保存失败');
        }
      });
    },

    // 返回上一页
    goBack() {
      this.$router.go(-1);
    },

    // 生成ID
    generateId() {
      return 'step_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
    },

    // 获取步骤类型颜色
    getStepTypeColor(type) {
      const colorMap = {
        'normal': '',
        'start': 'success',
        'end': 'danger',
        'branch': 'warning'
      };
      return colorMap[type] || '';
    },

    // 获取步骤类型名称
    getStepTypeName(type) {
      const nameMap = {
        'normal': '普通步骤',
        'start': '开始步骤',
        'end': '结束步骤',
        'branch': '分支步骤'
      };
      return nameMap[type] || type;
    }
  }
};
</script>

<style scoped>
.scenario-step-config {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background-color: white;
  border-bottom: 1px solid #e0e0e0;
}

.page-title {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.right-operations {
  display: flex;
  gap: 10px;
}

.main-wrapper {
  padding: 20px;
}

.step-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.step-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;
}

.step-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.step-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e0e0e0;
}

.step-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.step-number {
  background-color: #409EFF;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
}

.step-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.step-actions {
  display: flex;
  gap: 8px;
}

.step-content {
  padding: 0;
}

.hint-text {
  color: #909399;
  font-size: 12px;
  margin-top: 4px;
}

.template-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 15px;
}

.template-card {
  cursor: pointer;
  transition: all 0.3s ease;
}

.template-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.template-card h4 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 16px;
}

.template-card p {
  margin: 0 0 10px 0;
  color: #666;
  font-size: 14px;
}

.template-preview {
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  font-size: 12px;
  color: #666;
}
</style> 