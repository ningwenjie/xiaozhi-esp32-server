-- 添加第一句话字段到智能体表
ALTER TABLE ai_agent ADD COLUMN first_message VARCHAR(500) COMMENT '第一句话输出';

-- 添加第一句话字段到智能体模板表
ALTER TABLE ai_agent_template ADD COLUMN first_message VARCHAR(500) COMMENT '第一句话输出';

-- 创建智能体场景配置表
CREATE TABLE `ai_agent_scene` (
    `id` VARCHAR(32) NOT NULL COMMENT '场景唯一标识',
    `agent_id` VARCHAR(32) NOT NULL COMMENT '智能体ID',
    `scene_name` VARCHAR(64) COMMENT '场景名称',
    `scene_config` TEXT COMMENT '场景配置(JSON格式)',
    `is_default` TINYINT(1) DEFAULT 0 COMMENT '是否默认场景(0否/1是)',
    `sort` INT UNSIGNED DEFAULT 0 COMMENT '排序权重',
    `creator` BIGINT COMMENT '创建者ID',
    `created_at` DATETIME COMMENT '创建时间',
    `updater` BIGINT COMMENT '更新者ID',
    `updated_at` DATETIME COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_ai_agent_scene_agent_id` (`agent_id`) COMMENT '智能体ID索引',
    INDEX `idx_ai_agent_scene_default` (`agent_id`, `is_default`) COMMENT '默认场景索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能体场景配置表'; 