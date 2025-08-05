import { getServiceUrl } from '../api';
import RequestService from '../httpRequest';

export default {
    // 根据场景ID获取步骤列表
    getStepsByScenarioId(scenarioId, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario-step/scenario/${scenarioId}`)
            .method('GET')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.getStepsByScenarioId(scenarioId, callback);
                });
            }).send();
    },

    // 根据场景ID获取步骤列表（按顺序排序）
    getStepsByScenarioIdOrdered(scenarioId, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario-step/scenario/${scenarioId}/ordered`)
            .method('GET')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.getStepsByScenarioIdOrdered(scenarioId, callback);
                });
            }).send();
    },

    // 根据场景ID和步骤类型获取步骤列表
    getStepsByScenarioIdAndType(scenarioId, stepType, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario-step/scenario/${scenarioId}/type/${stepType}`)
            .method('GET')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.getStepsByScenarioIdAndType(scenarioId, stepType, callback);
                });
            }).send();
    },

    // 根据步骤编码获取步骤
    getStepByCode(stepCode, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario-step/code/${stepCode}`)
            .method('GET')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.getStepByCode(stepCode, callback);
                });
            }).send();
    },

    // 创建步骤
    createStep(stepData, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario-step`)
            .method('POST')
            .data(stepData)
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.createStep(stepData, callback);
                });
            }).send();
    },

    // 更新步骤
    updateStep(id, stepData, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario-step/${id}`)
            .method('PUT')
            .data(stepData)
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.updateStep(id, stepData, callback);
                });
            }).send();
    },

    // 删除步骤
    deleteStep(id, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario-step/${id}`)
            .method('DELETE')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.deleteStep(id, callback);
                });
            }).send();
    },

    // 根据场景ID删除所有步骤
    deleteStepsByScenarioId(scenarioId, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario-step/scenario/${scenarioId}`)
            .method('DELETE')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.deleteStepsByScenarioId(scenarioId, callback);
                });
            }).send();
    },

    // 更新步骤顺序
    updateStepOrder(id, stepOrder, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario-step/${id}/order`)
            .method('PUT')
            .params({ stepOrder })
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.updateStepOrder(id, stepOrder, callback);
                });
            }).send();
    },

    // 移动步骤位置
    moveStep(id, direction, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario-step/${id}/move`)
            .method('PUT')
            .params({ direction })
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.moveStep(id, direction, callback);
                });
            }).send();
    },

    // 批量保存步骤
    batchSaveSteps(scenarioId, steps, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/scenario-step/scenario/${scenarioId}/batch`)
            .method('POST')
            .data(steps)
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.batchSaveSteps(scenarioId, steps, callback);
                });
            }).send();
    }
}; 