# AI陪伴功能部署指南

## 概述

本文档提供AI陪伴功能模块的完整部署指南，包括环境准备、配置、部署和测试。

## 环境要求

### 系统要求
- **操作系统**: Linux/Windows/macOS
- **内存**: 最低2GB，推荐4GB+
- **存储**: 最低10GB可用空间
- **网络**: 稳定的互联网连接

### 软件要求
- **Docker**: 20.10+
- **Docker Compose**: 2.0+
- **Java**: 11+ (如果源码部署)
- **Node.js**: 16+ (如果源码部署)
- **Python**: 3.8+ (如果源码部署)

## 部署方式

### 方式一：Docker部署（推荐）

#### 1. 克隆项目
```bash
git clone https://github.com/your-repo/xiaozhi-esp32-server2.git
cd xiaozhi-esp32-server2
```

#### 2. 配置环境变量
```bash
# 复制环境变量模板
cp .env.example .env

# 编辑环境变量
vim .env
```

主要配置项：
```bash
# 数据库配置
MYSQL_ROOT_PASSWORD=your_password
MYSQL_DATABASE=xiaozhi
MYSQL_USER=xiaozhi
MYSQL_PASSWORD=your_password

# Redis配置
REDIS_PASSWORD=your_redis_password

# API配置
MANAGE_API_URL=http://localhost:8080
XIAOZHI_SERVER_URL=http://localhost:8000

# AI服务配置
OPENAI_API_KEY=your_openai_key
ALIYUN_ACCESS_KEY=your_aliyun_key
ALIYUN_ACCESS_SECRET=your_aliyun_secret
```

#### 3. 启动服务
```bash
# 启动所有服务
docker-compose -f docker-compose_all.yml up -d

# 查看服务状态
docker-compose -f docker-compose_all.yml ps

# 查看日志
docker-compose -f docker-compose_all.yml logs -f
```

#### 4. 初始化数据库
```bash
# 执行数据库迁移
docker-compose -f docker-compose_all.yml exec manager-api java -jar /app.jar --spring.profiles.active=dev
```

### 方式二：源码部署

#### 1. 后端部署
```bash
cd main/manager-api

# 编译项目
mvn clean package -DskipTests

# 运行应用
java -jar target/manager-api-1.0.0.jar --spring.profiles.active=dev
```

#### 2. 前端部署
```bash
cd main/manager-web

# 安装依赖
npm install

# 开发模式运行
npm run serve

# 生产构建
npm run build
```

#### 3. 服务端部署
```bash
cd main/xiaozhi-server

# 安装依赖
pip install -r requirements.txt

# 运行服务
python app.py
```

## 配置说明

### 1. AI陪伴功能配置

编辑 `main/xiaozhi-server/config/ai_companion_config.yaml`:

```yaml
# AI陪伴功能配置
ai_companion:
  enabled: true  # 启用AI陪伴功能
  
  # 场景触发配置
  scenario_trigger:
    enabled: true
    voice_trigger: true      # 启用语音触发
    visual_trigger: true     # 启用视觉触发
    button_trigger: true     # 启用按键触发
    
  # 对话执行配置
  dialogue_executor:
    enabled: true
    max_attempts: 3         # 最大尝试次数
    timeout_seconds: 10     # 等待超时时间
    child_name_default: "小朋友"  # 默认儿童姓名
```

### 2. 数据库配置

确保数据库连接配置正确：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/xiaozhi?useUnicode=true&characterEncoding=utf8&useSSL=false
    username: xiaozhi
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 3. API配置

配置管理API地址：

```yaml
# 在xiaozhi-server的config.yaml中
manage_api:
  url: http://localhost:8080
  timeout: 5
```

## 测试验证

### 1. 服务健康检查

```bash
# 检查后端API
curl http://localhost:8080/actuator/health

# 检查前端服务
curl http://localhost:8081

# 检查xiaozhi-server
curl http://localhost:8000/health
```

### 2. 功能测试

#### 2.1 登录管理后台
1. 访问 `http://localhost:8081`
2. 使用管理员账号登录
3. 进入"角色配置"页面
4. 点击"配置场景对话"按钮

#### 2.2 创建测试场景
1. 点击"新建场景"
2. 填写场景信息：
   - 场景名称：测试场景
   - 场景类型：表达需求
   - 触发方式：语音触发
   - 触发关键词：["测试", "口渴"]
3. 保存场景

#### 2.3 配置对话步骤
1. 点击"配置步骤"
2. 添加步骤：
   - 步骤名称：介绍表达需求
   - AI说的话：你好，{childName}，口渴了要怎么说呢？
   - 期望回答：["口渴", "想喝水", "喝水"]
   - 成功条件：关键词匹配
3. 保存步骤

#### 2.4 测试场景
1. 启用场景
2. 通过语音触发测试
3. 验证对话流程

### 3. 设备端测试

#### 3.1 ESP32固件更新
```bash
# 编译固件
cd firmware
make flash
```

#### 3.2 设备连接测试
1. 确保ESP32连接到网络
2. 检查设备状态
3. 测试语音识别功能

## 故障排除

### 常见问题

#### 1. 数据库连接失败
```bash
# 检查MySQL服务状态
docker-compose -f docker-compose_all.yml ps mysql

# 查看MySQL日志
docker-compose -f docker-compose_all.yml logs mysql
```

#### 2. API服务无法访问
```bash
# 检查端口占用
netstat -tulpn | grep :8080

# 检查服务日志
docker-compose -f docker-compose_all.yml logs manager-api
```

#### 3. 前端页面无法加载
```bash
# 检查前端服务
docker-compose -f docker-compose_all.yml logs manager-web

# 检查网络连接
curl http://localhost:8081
```

### 日志查看

```bash
# 查看所有服务日志
docker-compose -f docker-compose_all.yml logs

# 查看特定服务日志
docker-compose -f docker-compose_all.yml logs manager-api
docker-compose -f docker-compose_all.yml logs xiaozhi-server
```

## 性能优化

### 1. 数据库优化
```sql
-- 添加索引
CREATE INDEX idx_scenario_agent_id ON ai_scenario(agent_id);
CREATE INDEX idx_scenario_type ON ai_scenario(scenario_type);
CREATE INDEX idx_scenario_active ON ai_scenario(is_active);
```

### 2. 缓存配置
```yaml
# Redis缓存配置
spring:
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
    database: 0
```

### 3. 并发配置
```yaml
# 线程池配置
server:
  tomcat:
    threads:
      max: 200
      min-spare: 10
```

## 监控和维护

### 1. 系统监控
- 使用Prometheus + Grafana监控系统性能
- 设置告警规则
- 定期检查日志

### 2. 数据备份
```bash
# 数据库备份
docker-compose -f docker-compose_all.yml exec mysql mysqldump -u root -p xiaozhi > backup.sql

# 配置文件备份
tar -czf config_backup.tar.gz main/xiaozhi-server/config/
```

### 3. 版本更新
```bash
# 拉取最新代码
git pull origin main

# 重新构建镜像
docker-compose -f docker-compose_all.yml build

# 重启服务
docker-compose -f docker-compose_all.yml up -d
```

## 安全建议

### 1. 网络安全
- 使用HTTPS协议
- 配置防火墙规则
- 定期更新SSL证书

### 2. 数据安全
- 加密敏感数据
- 定期备份数据
- 限制数据库访问权限

### 3. 应用安全
- 定期更新依赖包
- 使用强密码策略
- 启用日志审计

---

*最后更新时间：2024年12月19日*
*版本：v1.0.0* 