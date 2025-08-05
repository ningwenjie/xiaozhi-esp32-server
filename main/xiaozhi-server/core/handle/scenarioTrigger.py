#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
场景触发处理模块
用于检测和处理AI陪伴功能的场景触发
"""

import json
import logging
import requests
from typing import Dict, List, Optional, Any
from datetime import datetime

logger = logging.getLogger(__name__)


class ScenarioTrigger:
    """场景触发检测器"""
    
    def __init__(self, config):
        self.config = config
        self.api_base_url = config.get('manage_api_url', 'http://localhost:8080')
        self.session = requests.Session()
        
    def detect_trigger(self, user_input: str, input_type: str, agent_id: str) -> Optional[Dict[str, Any]]:
        """
        检测场景触发
        
        Args:
            user_input: 用户输入（语音文本或视觉识别结果）
            input_type: 输入类型（voice/visual/button）
            agent_id: 智能体ID
            
        Returns:
            触发的场景信息，如果没有触发则返回None
        """
        try:
            if input_type == "voice":
                return self._detect_voice_trigger(user_input, agent_id)
            elif input_type == "visual":
                return self._detect_visual_trigger(user_input, agent_id)
            elif input_type == "button":
                return self._detect_button_trigger(user_input, agent_id)
            else:
                logger.warning(f"未知的输入类型: {input_type}")
                return None
        except Exception as e:
            logger.error(f"场景触发检测失败: {e}")
            return None
    
    def _detect_voice_trigger(self, text: str, agent_id: str) -> Optional[Dict[str, Any]]:
        """语音触发检测"""
        try:
            # 获取该智能体的所有启用场景
            scenarios = self._get_active_scenarios(agent_id)
            
            for scenario in scenarios:
                if scenario.get('triggerType') == 'voice':
                    keywords = json.loads(scenario.get('triggerKeywords', '[]'))
                    if any(keyword.lower() in text.lower() for keyword in keywords):
                        logger.info(f"检测到语音触发场景: {scenario['scenarioName']}")
                        return scenario
            
            return None
        except Exception as e:
            logger.error(f"语音触发检测失败: {e}")
            return None
    
    def _detect_visual_trigger(self, card_code: str, agent_id: str) -> Optional[Dict[str, Any]]:
        """视觉触发检测"""
        try:
            # 获取该智能体的所有启用场景
            scenarios = self._get_active_scenarios(agent_id)
            
            for scenario in scenarios:
                if scenario.get('triggerType') == 'visual':
                    cards = json.loads(scenario.get('triggerCards', '[]'))
                    if card_code in cards:
                        logger.info(f"检测到视觉触发场景: {scenario['scenarioName']}")
                        return scenario
            
            return None
        except Exception as e:
            logger.error(f"视觉触发检测失败: {e}")
            return None
    
    def _detect_button_trigger(self, button_code: str, agent_id: str) -> Optional[Dict[str, Any]]:
        """按键触发检测"""
        try:
            # 获取该智能体的所有启用场景
            scenarios = self._get_active_scenarios(agent_id)
            
            for scenario in scenarios:
                if scenario.get('triggerType') == 'button':
                    # 按键触发通常使用场景编码作为按钮代码
                    if scenario.get('scenarioCode') == button_code:
                        logger.info(f"检测到按键触发场景: {scenario['scenarioName']}")
                        return scenario
            
            return None
        except Exception as e:
            logger.error(f"按键触发检测失败: {e}")
            return None
    
    def _get_active_scenarios(self, agent_id: str) -> List[Dict[str, Any]]:
        """获取智能体的启用场景列表"""
        try:
            url = f"{self.api_base_url}/scenario/agent/{agent_id}"
            response = self.session.get(url, timeout=5)
            
            if response.status_code == 200:
                data = response.json()
                if data.get('code') == 0:
                    scenarios = data.get('data', [])
                    # 过滤出启用的场景
                    return [s for s in scenarios if s.get('isActive') == 1]
                else:
                    logger.error(f"获取场景列表失败: {data.get('msg')}")
                    return []
            else:
                logger.error(f"获取场景列表HTTP错误: {response.status_code}")
                return []
        except Exception as e:
            logger.error(f"获取场景列表异常: {e}")
            return []


class DialogueStepExecutor:
    """对话步骤执行器"""
    
    def __init__(self, config):
        self.config = config
        self.api_base_url = config.get('manage_api_url', 'http://localhost:8080')
        self.session = requests.Session()
        self.current_scenario = None
        self.current_step_index = 0
        self.attempts = 0
        self.child_name = None
        self.session_id = None
        
    def start_scenario(self, scenario: Dict[str, Any], child_name: str = None) -> Dict[str, Any]:
        """
        开始场景对话
        
        Args:
            scenario: 场景信息
            child_name: 儿童姓名
            
        Returns:
            初始步骤信息
        """
        self.current_scenario = scenario
        self.current_step_index = 0
        self.attempts = 0
        self.child_name = child_name
        self.session_id = f"session_{datetime.now().strftime('%Y%m%d_%H%M%S')}"
        
        # 获取场景步骤
        steps = self._get_scenario_steps(scenario['id'])
        if not steps:
            return {
                "type": "error",
                "message": "场景步骤配置为空"
            }
        
        # 开始学习记录
        self._start_learning_record(scenario, steps)
        
        # 执行第一个步骤
        return self._execute_current_step("", steps)
    
    def execute_user_response(self, user_response: str) -> Dict[str, Any]:
        """
        处理用户回答
        
        Args:
            user_response: 用户回答
            
        Returns:
            处理结果
        """
        if not self.current_scenario:
            return {
                "type": "error",
                "message": "没有正在进行的场景"
            }
        
        steps = self._get_scenario_steps(self.current_scenario['id'])
        if not steps:
            return {
                "type": "error",
                "message": "场景步骤配置为空"
            }
        
        return self._execute_current_step(user_response, steps)
    
    def _execute_current_step(self, user_response: str, steps: List[Dict[str, Any]]) -> Dict[str, Any]:
        """执行当前对话步骤"""
        if self.current_step_index >= len(steps):
            # 场景完成
            self._complete_learning_record()
            return {
                "type": "complete",
                "message": "恭喜你完成了这个场景！"
            }
        
        current_step = steps[self.current_step_index]
        
        # 替换儿童姓名
        ai_message = current_step['aiMessage'].replace("**{childName}**", self.child_name or "小朋友")
        
        if not user_response:
            # 初始步骤，直接返回AI消息
            return {
                "type": "step",
                "message": ai_message,
                "gesture": current_step.get('gestureHint'),
                "music": current_step.get('musicEffect'),
                "stepIndex": self.current_step_index,
                "totalSteps": len(steps)
            }
        
        # 判断用户回答
        is_correct = self._judge_response(user_response, current_step)
        
        # 记录步骤尝试
        self._record_step_attempt(current_step, user_response, is_correct)
        
        if is_correct:
            return self._handle_success(current_step, steps)
        else:
            return self._handle_failure(current_step, steps)
    
    def _judge_response(self, user_response: str, step: Dict[str, Any]) -> bool:
        """判断用户回答是否正确"""
        try:
            expected_keywords = json.loads(step.get('expectedKeywords', '[]'))
            expected_phrases = json.loads(step.get('expectedPhrases', '[]'))
            
            success_condition = step.get('successCondition', 'partial')
            
            if success_condition == "exact":
                return user_response in expected_phrases
            elif success_condition == "partial":
                return any(phrase.lower() in user_response.lower() for phrase in expected_phrases)
            elif success_condition == "keyword":
                return any(keyword.lower() in user_response.lower() for keyword in expected_keywords)
            
            return False
        except Exception as e:
            logger.error(f"判断回答失败: {e}")
            return False
    
    def _handle_success(self, step: Dict[str, Any], steps: List[Dict[str, Any]]) -> Dict[str, Any]:
        """处理成功情况"""
        self.current_step_index += 1
        self.attempts = 0
        
        if self.current_step_index >= len(steps):
            # 场景完成
            self._complete_learning_record()
            return {
                "type": "complete",
                "message": "恭喜你完成了这个场景！"
            }
        else:
            # 进入下一步
            next_step = steps[self.current_step_index]
            ai_message = next_step['aiMessage'].replace("**{childName}**", self.child_name or "小朋友")
            return {
                "type": "step",
                "message": ai_message,
                "gesture": next_step.get('gestureHint'),
                "music": next_step.get('musicEffect'),
                "stepIndex": self.current_step_index,
                "totalSteps": len(steps)
            }
    
    def _handle_failure(self, step: Dict[str, Any], steps: List[Dict[str, Any]]) -> Dict[str, Any]:
        """处理失败情况"""
        self.attempts += 1
        
        if self.attempts >= step.get('maxAttempts', 3):
            # 提供替代方案
            alternative_message = step.get('alternativeMessage', '').replace("**{childName}**", self.child_name or "小朋友")
            return {
                "type": "alternative",
                "message": alternative_message,
                "gesture": step.get('gestureHint'),
                "music": step.get('musicEffect')
            }
        else:
            # 重复示范
            ai_message = step['aiMessage'].replace("**{childName}**", self.child_name or "小朋友")
            return {
                "type": "retry",
                "message": f"让我们再试一次：{ai_message}",
                "gesture": step.get('gestureHint'),
                "music": step.get('musicEffect')
            }
    
    def _get_scenario_steps(self, scenario_id: str) -> List[Dict[str, Any]]:
        """获取场景步骤列表"""
        try:
            url = f"{self.api_base_url}/scenario-step/scenario/{scenario_id}"
            response = self.session.get(url, timeout=5)
            
            if response.status_code == 200:
                data = response.json()
                if data.get('code') == 0:
                    steps = data.get('data', [])
                    # 按步骤顺序排序
                    return sorted(steps, key=lambda x: x.get('stepOrder', 0))
                else:
                    logger.error(f"获取场景步骤失败: {data.get('msg')}")
                    return []
            else:
                logger.error(f"获取场景步骤HTTP错误: {response.status_code}")
                return []
        except Exception as e:
            logger.error(f"获取场景步骤异常: {e}")
            return []
    
    def _start_learning_record(self, scenario: Dict[str, Any], steps: List[Dict[str, Any]]):
        """开始学习记录"""
        try:
            record_data = {
                "agentId": scenario['agentId'],
                "scenarioId": scenario['id'],
                "childName": self.child_name,
                "sessionId": self.session_id,
                "startTime": datetime.now().isoformat(),
                "totalSteps": len(steps),
                "status": "in_progress"
            }
            
            url = f"{self.api_base_url}/learning-record"
            response = self.session.post(url, json=record_data, timeout=5)
            
            if response.status_code == 200:
                data = response.json()
                if data.get('code') == 0:
                    logger.info(f"学习记录已创建: {data.get('data')}")
                else:
                    logger.error(f"创建学习记录失败: {data.get('msg')}")
            else:
                logger.error(f"创建学习记录HTTP错误: {response.status_code}")
        except Exception as e:
            logger.error(f"创建学习记录异常: {e}")
    
    def _record_step_attempt(self, step: Dict[str, Any], user_response: str, is_correct: bool):
        """记录步骤尝试"""
        try:
            record_data = {
                "learningRecordId": self.session_id,  # 简化处理
                "stepId": step['id'],
                "stepOrder": step['stepOrder'],
                "aiMessage": step['aiMessage'],
                "userResponse": user_response,
                "isCorrect": 1 if is_correct else 0,
                "attemptCount": self.attempts,
                "stepStatus": "completed"
            }
            
            url = f"{self.api_base_url}/learning-step-record"
            response = self.session.post(url, json=record_data, timeout=5)
            
            if response.status_code != 200:
                logger.error(f"记录步骤尝试HTTP错误: {response.status_code}")
        except Exception as e:
            logger.error(f"记录步骤尝试异常: {e}")
    
    def _complete_learning_record(self):
        """完成学习记录"""
        try:
            update_data = {
                "endTime": datetime.now().isoformat(),
                "status": "completed"
            }
            
            url = f"{self.api_base_url}/learning-record/{self.session_id}"
            response = self.session.put(url, json=update_data, timeout=5)
            
            if response.status_code == 200:
                data = response.json()
                if data.get('code') == 0:
                    logger.info("学习记录已完成")
                else:
                    logger.error(f"完成学习记录失败: {data.get('msg')}")
            else:
                logger.error(f"完成学习记录HTTP错误: {response.status_code}")
        except Exception as e:
            logger.error(f"完成学习记录异常: {e}")


# 全局实例
scenario_trigger = None
dialogue_executor = None


def init_scenario_trigger(config):
    """初始化场景触发模块"""
    global scenario_trigger, dialogue_executor
    scenario_trigger = ScenarioTrigger(config)
    dialogue_executor = DialogueStepExecutor(config)
    logger.info("场景触发模块初始化完成")


def detect_scenario_trigger(user_input: str, input_type: str, agent_id: str) -> Optional[Dict[str, Any]]:
    """检测场景触发（全局函数）"""
    if scenario_trigger:
        return scenario_trigger.detect_trigger(user_input, input_type, agent_id)
    return None


def start_scenario_dialogue(scenario: Dict[str, Any], child_name: str = None) -> Dict[str, Any]:
    """开始场景对话（全局函数）"""
    if dialogue_executor:
        return dialogue_executor.start_scenario(scenario, child_name)
    return {"type": "error", "message": "对话执行器未初始化"}


def execute_user_response(user_response: str) -> Dict[str, Any]:
    """处理用户回答（全局函数）"""
    if dialogue_executor:
        return dialogue_executor.execute_user_response(user_response)
    return {"type": "error", "message": "对话执行器未初始化"}