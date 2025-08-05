import { getServiceUrl } from '../api';
import RequestService from '../httpRequest';

export default {
    // 根据模板类型获取模板列表
    getTemplatesByType(templateType, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/step-template/type/${templateType}`)
            .method('GET')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.getTemplatesByType(templateType, callback);
                });
            }).send();
    },

    // 获取默认模板列表
    getDefaultTemplates(callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/step-template/default`)
            .method('GET')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.getDefaultTemplates(callback);
                });
            }).send();
    },

    // 根据模板编码获取模板
    getTemplateByCode(templateCode, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/step-template/code/${templateCode}`)
            .method('GET')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.getTemplateByCode(templateCode, callback);
                });
            }).send();
    },

    // 根据模板类型和是否默认获取模板列表
    getTemplatesByTypeAndDefault(templateType, isDefault, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/step-template/type/${templateType}/default/${isDefault}`)
            .method('GET')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.getTemplatesByTypeAndDefault(templateType, isDefault, callback);
                });
            }).send();
    },

    // 创建模板
    createTemplate(templateData, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/step-template`)
            .method('POST')
            .data(templateData)
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.createTemplate(templateData, callback);
                });
            }).send();
    },

    // 更新模板
    updateTemplate(id, templateData, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/step-template/${id}`)
            .method('PUT')
            .data(templateData)
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.updateTemplate(id, templateData, callback);
                });
            }).send();
    },

    // 删除模板
    deleteTemplate(id, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/step-template/${id}`)
            .method('DELETE')
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.deleteTemplate(id, callback);
                });
            }).send();
    },

    // 设置默认模板
    setDefaultTemplate(id, isDefault, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/step-template/${id}/default`)
            .method('PUT')
            .params({ isDefault })
            .success((res) => {
                RequestService.clearRequestTime();
                callback(res);
            })
            .networkFail(() => {
                RequestService.reAjaxFun(() => {
                    this.setDefaultTemplate(id, isDefault, callback);
                });
            }).send();
    }
}; 