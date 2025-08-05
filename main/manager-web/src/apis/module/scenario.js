import { getServiceUrl } from '../api';
import RequestService from '../httpRequest';

export default {
    // 获取场景分页列表
    getScenarioPage(params, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario/list`)
            .method('GET')
            .params(params)
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.getScenarioPage(params, callback);
                });
            }).send();
    },

    // 根据智能体ID获取场景列表
    getScenariosByAgentId(agentId, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario/agent/${agentId}`)
            .method('GET')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.getScenariosByAgentId(agentId, callback);
                });
            }).send();
    },

    // 根据智能体ID和场景类型获取场景列表
    getScenariosByAgentIdAndType(agentId, scenarioType, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario/agent/${agentId}/type/${scenarioType}`)
            .method('GET')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.getScenariosByAgentIdAndType(agentId, scenarioType, callback);
                });
            }).send();
    },

    // 根据智能体ID获取启用的场景列表
    getActiveScenariosByAgentId(agentId, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario/agent/${agentId}/active`)
            .method('GET')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.getActiveScenariosByAgentId(agentId, callback);
                });
            }).send();
    },

    // 根据场景编码获取场景
    getScenarioByCode(scenarioCode, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario/code/${scenarioCode}`)
            .method('GET')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.getScenarioByCode(scenarioCode, callback);
                });
            }).send();
    },

    // 根据触发关键词获取场景
    getScenariosByTriggerKeyword(keyword, agentId, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario/trigger/keyword`)
            .method('GET')
            .params({ keyword, agentId })
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.getScenariosByTriggerKeyword(keyword, agentId, callback);
                });
            }).send();
    },

    // 根据触发卡片获取场景
    getScenariosByTriggerCard(cardCode, agentId, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario/trigger/card`)
            .method('GET')
            .params({ cardCode, agentId })
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.getScenariosByTriggerCard(cardCode, agentId, callback);
                });
            }).send();
    },

    // 创建场景
    createScenario(scenarioData, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario`)
            .method('POST')
            .data(scenarioData)
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.createScenario(scenarioData, callback);
                });
            }).send();
    },

    // 更新场景
    updateScenario(id, scenarioData, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario/${id}`)
            .method('PUT')
            .data(scenarioData)
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.updateScenario(id, scenarioData, callback);
                });
            }).send();
    },

    // 更新场景启用状态
    updateScenarioActiveStatus(id, isActive, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario/${id}/active`)
            .method('PUT')
            .params({ isActive })
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.updateScenarioActiveStatus(id, isActive, callback);
                });
            }).send();
    },

    // 删除场景
    deleteScenario(id, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario/${id}`)
            .method('DELETE')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.deleteScenario(id, callback);
                });
            }).send();
    }
}; 