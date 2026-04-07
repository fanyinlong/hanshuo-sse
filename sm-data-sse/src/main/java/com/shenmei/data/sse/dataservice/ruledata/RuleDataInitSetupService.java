package com.shenmei.data.sse.dataservice.ruledata;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.sse.domain.SseDataRuleInit;
import com.shenmei.data.sse.service.ISseDataRuleInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : teabot
 * @description : 规则初始化服务
 * @date : 2025-08-03
 */
@Service
public class RuleDataInitSetupService {

    @Resource
    SecuritySetRuleService securitySetRuleService;

    @Resource
    ISseDataRuleInitService sseDataRuleInitService;

    @Resource
    PbuAndAccountRuleService pbuAccountRuleService;
    @Autowired
    private PbuAndAccountRuleService pbuAndAccountRuleService;
    @Autowired
    private OrderMatchInitService orderMatchInitService;

    public AjaxResult setUp(Long pkId) {
        List<String> errorList = new java.util.ArrayList<>();
        //查找是否已存在规则初始化记录，如果已存在，则更新初，不存在则新增
        SseDataRuleInit sseDataRuleInit = sseDataRuleInitService.selectSseDataRuleInitByRuleId(pkId);
        if (sseDataRuleInit != null) {
            //初始化set分配
            sseDataRuleInit = securitySetRuleService.setSecuritySetup(sseDataRuleInit, errorList);
            if (errorList.size() > 0) {
                return AjaxResult.selfError(errorList.toString());
            }
            //初始化pbu和账户分配
            try {
                sseDataRuleInit = pbuAndAccountRuleService.setPbuAccountByRule(sseDataRuleInit);
            } catch (JsonProcessingException e) {
                errorList.add("初始化pbu和账户分配失败:" + e.getMessage());
                return AjaxResult.error(errorList.toString());
            }
            //初始化撮合方式满足成交比
            sseDataRuleInit = orderMatchInitService.initOrderMatchRatio(sseDataRuleInit);
            sseDataRuleInitService.updateSseDataRuleInit(sseDataRuleInit);
        } else {
            sseDataRuleInit = new SseDataRuleInit();
            sseDataRuleInit.setRuleId(pkId);
            //初始化set分配
            sseDataRuleInit = securitySetRuleService.setSecuritySetup(sseDataRuleInit, errorList);

            if (errorList.size() > 0) {
                return AjaxResult.selfError(errorList.toString());
            }
            //初始化pbu和账户分配
            try {
                sseDataRuleInit = pbuAndAccountRuleService.setPbuAccountByRule(sseDataRuleInit);
            } catch (JsonProcessingException e) {
                errorList.add("初始化pbu和账户分配失败:" + e.getMessage());
            }
            if (errorList.size() > 0) {
                return AjaxResult.error(errorList.toString());
            }
            //初始化撮合方式满足成交比
            sseDataRuleInit = orderMatchInitService.initOrderMatchRatio(sseDataRuleInit);
            sseDataRuleInitService.insertSseDataRuleInit(sseDataRuleInit);
        }

        if (errorList.size() > 0) {
            return AjaxResult.selfError(errorList.toString());
        }
        return AjaxResult.success("初始化数据成功！");

    }


}
