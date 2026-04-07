package com.shenmei.data.sse.dataservice.ruledata;


import com.alibaba.fastjson2.JSON;
import com.shenmei.data.sse.domain.SseOrderInfo;
import com.shenmei.data.sse.domain.SseOrderStats;
import com.shenmei.data.sse.service.ISseOrderInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderStatsCalService {

    private static final Logger log = LoggerFactory.getLogger(OrderStatsCalService.class);
    @Resource
    private ISseOrderInfoService sseOrderInfoService;

    public SseOrderStats calOrderStats(long ruleId){
        SseOrderStats sseOrderStats = new SseOrderStats();
        sseOrderStats.setRuleId(ruleId);
        sseOrderStats.setTotalOrders(calTotalOrders(ruleId));
        sseOrderStats.setLoginPbuOrders(calLoginPbuOrders(ruleId));
        sseOrderStats.setBizPbuOrders(calBizPbuOrders(ruleId));
        sseOrderStats.setSetsOrders(calSetOrders(ruleId));
        sseOrderStats.setMatchingTypeOrders(calMatchingOrders(ruleId));


        return sseOrderStats;
    }

    private long calTotalOrders(long ruleId){

        long orders = sseOrderInfoService.countTotalOrderNumberByRuleId(ruleId);
        return orders;
    }

    /**
     * 计算登陆PBU的订单数
     * @param ruleId
     * @return
     */
    private String calLoginPbuOrders(long ruleId){
        List<String> orderTableList = sseOrderInfoService.selectOrderTableListByRuleId(ruleId);
        SseOrderInfo sseOrderInfo = new SseOrderInfo();
        sseOrderInfo.setRuleId(ruleId);
        Map<String,Long> loginPbuOrders = new HashMap<>();
        for(String orderTable : orderTableList){
            sseOrderInfo.setOrderTable(orderTable);
            loginPbuOrders.put(orderTable.substring(orderTable.length()-5),sseOrderInfoService.countOrderNumbersBySseOrderInfo(sseOrderInfo));
        }
        return JSON.toJSONString(loginPbuOrders);
    }

    private String calBizPbuOrders(long ruleId){
        List<String> bizPbuList = sseOrderInfoService.selectStatsListByRuleId("biz_pbu", ruleId);
        log.info("bizPbuList:{}",bizPbuList);
        SseOrderInfo sseOrderInfo = new SseOrderInfo();
        sseOrderInfo.setRuleId(ruleId);
        Map<String,Long> bizPbuOrders = new HashMap<>();
        for(String bizPbu : bizPbuList){
            sseOrderInfo.setBizPbu(bizPbu);
            bizPbuOrders.put(bizPbu,sseOrderInfoService.countOrderNumbersBySseOrderInfo(sseOrderInfo));
        }
        return JSON.toJSONString(bizPbuOrders);
    }

    private String calSetOrders(long ruleId) {
        List<String> setList = sseOrderInfoService.selectStatsListByRuleId("order_set", ruleId);
        SseOrderInfo sseOrderInfo = new SseOrderInfo();
        sseOrderInfo.setRuleId(ruleId);
        Map<String, Long> setOrders = new HashMap<>();
        for (String set : setList) {
            sseOrderInfo.setOrderSet(set);
            setOrders.put(set, sseOrderInfoService.countOrderNumbersBySseOrderInfo(sseOrderInfo));
        }
        return JSON.toJSONString(setOrders);
    }

    private String calMatchingOrders(long ruleId) {
        List<String> matchingList = sseOrderInfoService.selectStatsListByRuleId("order_matching", ruleId);
        SseOrderInfo sseOrderInfo = new SseOrderInfo();
        sseOrderInfo.setRuleId(ruleId);
        Map<String, Long> matchingOrders = new HashMap<>();
        for (String matching : matchingList) {
            sseOrderInfo.setOrderMatching(matching);
            matchingOrders.put(matching, sseOrderInfoService.countOrderNumbersBySseOrderInfo(sseOrderInfo));
        }
        return JSON.toJSONString(matchingOrders);
    }

}
