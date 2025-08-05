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
      <!-- 搜索和筛选 -->
      <div class="search-panel">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="智能体：">
            <el-select v-model="searchForm.agentId" placeholder="请选择智能体" @change="loadScenarios">
              <el-option v-for="agent in agentList" :key="agent.id" :label="agent.agentName" :value="agent.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="场景类型：">
            <el-select v-model="searchForm.scenarioType" placeholder="请选择场景类型" @change="loadScenarios">
              <el-option label="全部" value="" />
              <el-option label="表达需求" value="express_needs" />
              <el-option label="问候" value="greeting" />
              <el-option label="情感表达" value="emotion" />
              <el-option label="指令执行" value="instruction" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态：">
            <el-select v-model="searchForm.isActive" placeholder="请选择状态" @change="loadScenarios">
              <el-option label="全部" value="" />
              <el-option label="启用" :value="1" />
              <el-option label="禁用" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadScenarios">搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

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
            <span class="type-tag">{{ getScenarioTypeName(scenario.scenarioType) }}</span>
            <span class="difficulty">难度: {{ scenario.difficultyLevel }}</span>
            <span class="target-age">年龄: {{ scenario.targetAge }}</span>
            <span class="step-count">步骤数: {{ scenario.stepCount || 0 }}</span>
          </div>
          <div class="scenario-description">
            {{ scenario.description || '暂无描述' }}
          </div>
          <div class="scenario-trigger">
            <span class="trigger-label">触发方式：</span>
            <span class="trigger-value">{{ getTriggerTypeName(scenario.triggerType) }}</span>
          </div>
        </el-card>
      </div>

      <!-- 分页 -->
      <div class="pagination-wrapper" v-if="total > 0">
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </div>

    <!-- 场景编辑对话框 -->
    <el-dialog title="编辑场景" :visible.sync="editDialogVisible" width="60%">
      <el-form :model="editForm" label-width="120px">
        <el-form-item label="场景名称" required>
          <el-input v-model="editForm.scenarioName" placeholder="请输入场景名称" />
        </el-form-item>
        <el-form-item label="场景类型" required>
          <el-select v-model="editForm.scenarioType" placeholder="请选择场景类型">
            <el-option label="表达需求" value="express_needs" />
            <el-option label="问候" value="greeting" />
            <el-option label="情感表达" value="emotion" />
            <el-option label="指令执行" value="instruction" />
          </el-select>
        </el-form-item>
        <el-form-item label="触发方式" required>
          <el-select v-model="editForm.triggerType" placeholder="请选择触发方式">
            <el-option label="语音触发" value="voice" />
            <el-option label="视觉触发" value="visual" />
            <el-option label="按键触发" value="button" />
          </el-select>
        </el-form-item>
        <el-form-item label="难度等级">
          <el-rate v-model="editForm.difficultyLevel" :max="5" />
        </el-form-item>
        <el-form-item label="目标年龄">
          <el-select v-model="editForm.targetAge" placeholder="请选择目标年龄">
            <el-option label="3-6岁" value="3-6" />
            <el-option label="7-12岁" value="7-12" />
            <el-option label="13-18岁" value="13-18" />
          </el-select>
        </el-form-item>
        <el-form-item label="场景描述">
          <el-input type="textarea" v-model="editForm.description" rows="3" placeholder="请输入场景描述" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveScenario">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import HeaderBar from '@/components/HeaderBar.vue';
import scenarioApi from '@/apis/module/scenario.js';
import agentApi from '@/apis/module/agent.js';

export default {
  name: 'ScenarioConfig',
  components: {
    HeaderBar
  },
  data() {
    return {
      searchForm: {
        agentId: '',
        scenarioType: '',
        isActive: ''
      },
      scenarios: [],
      agentList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      editDialogVisible: false,
      editForm: {
        id: '',
        scenarioName: '',
        scenarioType: '',
        triggerType: '',
        difficultyLevel: 1,
        targetAge: '',
        description: ''
      }
    };
  },
  mounted() {
    this.loadAgentList();
  },
  methods: {
    // 加载智能体列表
    loadAgentList() {
      agentApi.getAgentList((res) => {
        if (res.code === 0) {
          this.agentList = res.data;
          if (this.agentList.length > 0) {
            this.searchForm.agentId = this.agentList[0].id;
            this.loadScenarios();
          }
        }
      });
    },

    // 加载场景列表
    loadScenarios() {
      if (!this.searchForm.agentId) {
        this.$message.warning('请先选择智能体');
        return;
      }

      const params = {
        page: this.currentPage,
        limit: this.pageSize,
        agentId: this.searchForm.agentId
      };

      if (this.searchForm.scenarioType) {
        params.scenarioType = this.searchForm.scenarioType;
      }

      if (this.searchForm.isActive !== '') {
        params.isActive = this.searchForm.isActive;
      }

      scenarioApi.getScenarioPage(params, (res) => {
        if (res.code === 0) {
          this.scenarios = res.data.list;
          this.total = res.data.total;
        }
      });
    },

    // 重置搜索
    resetSearch() {
      this.searchForm = {
        agentId: this.searchForm.agentId,
        scenarioType: '',
        isActive: ''
      };
      this.currentPage = 1;
      this.loadScenarios();
    },

    // 创建场景
    createScenario() {
      this.editForm = {
        id: '',
        scenarioName: '',
        scenarioType: '',
        triggerType: '',
        difficultyLevel: 1,
        targetAge: '',
        description: ''
      };
      this.editDialogVisible = true;
    },

    // 编辑场景
    editScenario(scenario) {
      this.editForm = { ...scenario };
      this.editDialogVisible = true;
    },

    // 保存场景
    saveScenario() {
      if (!this.editForm.scenarioName) {
        this.$message.warning('请输入场景名称');
        return;
      }

      const callback = (res) => {
        if (res.code === 0) {
          this.$message.success('保存成功');
          this.editDialogVisible = false;
          this.loadScenarios();
        } else {
          this.$message.error(res.msg || '保存失败');
        }
      };

      if (this.editForm.id) {
        // 更新场景
        scenarioApi.updateScenario(this.editForm.id, this.editForm, callback);
      } else {
        // 创建场景
        this.editForm.agentId = this.searchForm.agentId;
        scenarioApi.createScenario(this.editForm, callback);
      }
    },

    // 配置步骤
    configureSteps(scenario) {
      this.$router.push({
        name: 'ScenarioStepConfig',
        params: { scenarioId: scenario.id },
        query: { scenarioName: scenario.scenarioName }
      });
    },

    // 测试场景
    testScenario(scenario) {
      this.$message.info('测试功能开发中...');
    },

    // 切换场景启用状态
    toggleScenario(scenario) {
      scenarioApi.updateScenarioActiveStatus(scenario.id, scenario.isActive, (res) => {
        if (res.code === 0) {
          this.$message.success(scenario.isActive ? '启用成功' : '禁用成功');
        } else {
          this.$message.error(res.msg || '操作失败');
          scenario.isActive = !scenario.isActive; // 恢复原状态
        }
      });
    },

    // 导入模板
    importTemplate() {
      this.$message.info('导入模板功能开发中...');
    },

    // 分页处理
    handleCurrentChange(page) {
      this.currentPage = page;
      this.loadScenarios();
    },

    // 获取场景类型名称
    getScenarioTypeName(type) {
      const typeMap = {
        'express_needs': '表达需求',
        'greeting': '问候',
        'emotion': '情感表达',
        'instruction': '指令执行'
      };
      return typeMap[type] || type;
    },

    // 获取触发方式名称
    getTriggerTypeName(type) {
      const triggerMap = {
        'voice': '语音触发',
        'visual': '视觉触发',
        'button': '按键触发'
      };
      return triggerMap[type] || type;
    }
  }
};
</script>

<style scoped>
.scenario-config {
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

.search-panel {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.search-form {
  margin: 0;
}

.scenario-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.scenario-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;
}

.scenario-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.scenario-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.scenario-header h3 {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.scenario-actions {
  display: flex;
  gap: 8px;
  align-items: center;
}

.scenario-info {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 15px;
}

.type-tag {
  background-color: #e3f2fd;
  color: #1976d2;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.difficulty, .target-age, .step-count {
  color: #666;
  font-size: 14px;
}

.scenario-description {
  color: #666;
  font-size: 14px;
  line-height: 1.5;
  margin-bottom: 15px;
}

.scenario-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
}

.trigger-label {
  color: #666;
  font-size: 14px;
}

.trigger-value {
  color: #333;
  font-size: 14px;
  font-weight: 500;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.dialog-footer {
  text-align: right;
}
</style> 