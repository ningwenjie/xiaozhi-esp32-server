-- AI陪伴功能模块数据库表创建
-- 创建时间：2024-12-19
-- 作者：AI Assistant

-- 1. 场景配置表
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
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` BIGINT COMMENT '更新者ID',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_scenario_code` (`scenario_code`),
    INDEX `idx_agent_id` (`agent_id`),
    INDEX `idx_scenario_type` (`scenario_type`),
    INDEX `idx_is_active` (`is_active`),
    INDEX `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场景配置表';

-- 2. 对话步骤配置表
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
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_step_code` (`step_code`),
    INDEX `idx_scenario_id` (`scenario_id`),
    INDEX `idx_step_order` (`step_order`),
    INDEX `idx_step_type` (`step_type`),
    FOREIGN KEY (`scenario_id`) REFERENCES `ai_scenario` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对话步骤配置表';

-- 3. 步骤模板表
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
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_template_code` (`template_code`),
    INDEX `idx_template_type` (`template_type`),
    INDEX `idx_is_default` (`is_default`),
    INDEX `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='步骤模板表';

-- 4. 儿童学习记录表
CREATE TABLE `ai_child_learning_record` (
    `id` VARCHAR(32) NOT NULL COMMENT '记录唯一标识',
    `agent_id` VARCHAR(32) NOT NULL COMMENT '关联的智能体ID',
    `scenario_id` VARCHAR(32) NOT NULL COMMENT '关联的场景ID',
    `child_name` VARCHAR(64) COMMENT '儿童姓名',
    `session_id` VARCHAR(64) NOT NULL COMMENT '学习会话ID',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME COMMENT '结束时间',
    `total_steps` INT DEFAULT 0 COMMENT '总步骤数',
    `completed_steps` INT DEFAULT 0 COMMENT '完成步骤数',
    `total_attempts` INT DEFAULT 0 COMMENT '总尝试次数',
    `success_attempts` INT DEFAULT 0 COMMENT '成功尝试次数',
    `success_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '成功率',
    `difficulty_level` INT DEFAULT 1 COMMENT '难度等级',
    `learning_duration` INT DEFAULT 0 COMMENT '学习时长(秒)',
    `status` VARCHAR(32) DEFAULT 'in_progress' COMMENT '状态：in_progress/completed/abandoned',
    `notes` TEXT COMMENT '备注信息',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_agent_id` (`agent_id`),
    INDEX `idx_scenario_id` (`scenario_id`),
    INDEX `idx_session_id` (`session_id`),
    INDEX `idx_start_time` (`start_time`),
    INDEX `idx_status` (`status`),
    FOREIGN KEY (`scenario_id`) REFERENCES `ai_scenario` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='儿童学习记录表';

-- 5. 学习步骤记录表
CREATE TABLE `ai_learning_step_record` (
    `id` VARCHAR(32) NOT NULL COMMENT '记录唯一标识',
    `learning_record_id` VARCHAR(32) NOT NULL COMMENT '关联的学习记录ID',
    `step_id` VARCHAR(32) NOT NULL COMMENT '关联的步骤ID',
    `step_order` INT NOT NULL COMMENT '步骤顺序',
    `ai_message` TEXT NOT NULL COMMENT 'AI说的话',
    `user_response` TEXT COMMENT '用户回答',
    `is_correct` TINYINT DEFAULT 0 COMMENT '是否正确：0-错误 1-正确',
    `attempt_count` INT DEFAULT 1 COMMENT '尝试次数',
    `response_time` INT DEFAULT 0 COMMENT '响应时间(秒)',
    `gesture_used` VARCHAR(128) COMMENT '使用的手势',
    `music_played` VARCHAR(128) COMMENT '播放的音效',
    `step_status` VARCHAR(32) DEFAULT 'pending' COMMENT '步骤状态：pending/completed/skipped',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_learning_record_id` (`learning_record_id`),
    INDEX `idx_step_id` (`step_id`),
    INDEX `idx_step_order` (`step_order`),
    INDEX `idx_is_correct` (`is_correct`),
    FOREIGN KEY (`learning_record_id`) REFERENCES `ai_child_learning_record` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`step_id`) REFERENCES `ai_scenario_step` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习步骤记录表';

-- 插入默认的步骤模板数据
INSERT INTO `ai_step_template` (`id`, `template_code`, `template_name`, `template_type`, `ai_message`, `expected_keywords`, `expected_phrases`, `alternative_message`, `description`, `is_default`, `sort_order`) VALUES
('TPL001', 'greeting_basic', '基础问候模板', 'greeting', '你好，**{childName}**！今天我们一起练习问候。请跟我说：你好！', '["你好", "您好"]', '["你好", "您好"]', '**{childName}**，我们一起说：你-好（拖长音）', '基础问候练习模板', 1, 1),
('TPL002', 'express_needs_thirsty', '表达需求-口渴模板', 'express_needs', '**{childName}**，现在你渴了，你怎么跟妈妈说呢？', '["我渴了", "我要喝水", "喝水"]', '["我渴了，我要喝水", "我要喝水"]', '**{childName}**，我们也可以这样做：指指自己的嘴巴，说：喝-水（拖长音）', '表达口渴需求的模板', 1, 2),
('TPL003', 'express_needs_hungry', '表达需求-饥饿模板', 'express_needs', '**{childName}**，现在你饿了，你怎么跟妈妈说呢？', '["我饿了", "我要吃饭", "吃饭"]', '["我饿了，我要吃饭", "我要吃饭"]', '**{childName}**，我们也可以这样做：指指自己的肚子，说：我-饿-了（拖长音）', '表达饥饿需求的模板', 1, 3),
('TPL004', 'emotion_happy', '情感表达-开心模板', 'emotion', '**{childName}**，当你很开心的时候，你会怎么说呢？', '["我很开心", "我很高兴", "我很快乐"]', '["我很开心", "我很高兴", "我很快乐"]', '**{childName}**，我们可以这样说：我-很-开-心（拖长音）', '表达开心情感的模板', 1, 4),
('TPL005', 'instruction_simple', '简单指令模板', 'instruction', '**{childName}**，请跟我一起做：拍拍手。', '["拍拍手", "拍手"]', '["拍拍手", "拍手"]', '**{childName}**，我们一起做：拍-拍-手（边说边做动作）', '简单指令练习模板', 1, 5); 