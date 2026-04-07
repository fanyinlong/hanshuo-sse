package com.shenmei.data.sse.dataservice.ruledata;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shenmei.data.sse.domain.SseDataRule;
import com.shenmei.data.sse.domain.SseDataRuleInit;
import com.shenmei.data.sse.service.ISseDataRuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单撮合和成交比计算
 */

@Service
public class OrderMatchInitService {

    private static final BigDecimal orderNum = new BigDecimal("100000");
    @Resource
    private ISseDataRuleService sseDataRuleService;

    /**
     * 输出JSON字符串格式如下
     * {"matching1v10Seller":4660.000000,
     * "kshMatchOrderBuyer":2800.000,
     * "kshCancelOrder":950.000,
     * "matching1v1Seller":28528.000000,
     * "kshMatchOrderSeller":2800.000,
     * "ashNoMatchOrder":5000.00,
     * "ashInvalidOrder":9000.00,
     * "kshNoMatchOrder":300.000,
     * "ashCancelOrderOri":7500.00,
     * "matching1v1Buyer":28528.000000,
     * "kshInvalidOrder":1000.00,
     * "ashCancelOrder":7500.00,
     * "kshCancelOrderOri":950.000,
     * "matching1v10Buyer":466.000000,
     * "matching1v2Seller":12.000000,
     * "matching1v2Buyer":6.000000}
     */

    /**
     * 初始化订单撮合和成交比
     */
    public SseDataRuleInit initOrderMatchRatio(SseDataRuleInit sseDataRuleInit) {
        // 计算订单撮合和成交比
        SseDataRule sseDataRule = sseDataRuleService.selectSseDataRuleByPkId(sseDataRuleInit.getRuleId());
        Map<String, BigDecimal> orderMatching = new HashMap<>();
        orderMatching = setMatchingOrderDetails(sseDataRule.getMatchMethod(), orderNum, sseDataRule.getTradeRatio());
        String orderMatchingJson = JSON.toJSONString(orderMatching);
        sseDataRuleInit.setMatchOrderList(orderMatchingJson);
        return sseDataRuleInit;
    }
    /**
     * 返回所有撮合方式细分的订单数
     */
    private Map<String, BigDecimal> setMatchingOrderDetails(String matchMethod, BigDecimal orderNumber, BigDecimal tradeRatio){
        JSONObject matchMethodJson = JSON.parseObject(matchMethod);
        BigDecimal p =new BigDecimal("0.01");
        tradeRatio = p.multiply(tradeRatio);
        BigDecimal ashMatchRatio = p.multiply(matchMethodJson.getBigDecimal("01"));
        BigDecimal ashCancelRatio = p.multiply(matchMethodJson.getBigDecimal("02"));
        BigDecimal ashInvalidRatio = p.multiply(matchMethodJson.getBigDecimal("03"));
        BigDecimal ashNoMatchRatio = p.multiply(matchMethodJson.getBigDecimal("04"));
        BigDecimal kshMatchRatio = p.multiply(matchMethodJson.getBigDecimal("05"));
        BigDecimal kshCancelRatio = p.multiply(matchMethodJson.getBigDecimal("06"));
        BigDecimal kshInvalidRatio = p.multiply(matchMethodJson.getBigDecimal("07"));
        BigDecimal kshNoMatchRatio = p.multiply(matchMethodJson.getBigDecimal("08"));
        //主板撮合订单
        BigDecimal ashMatchOrderNumber = ashMatchRatio.multiply(orderNumber);
        //主板撤单
        BigDecimal ashCancelOrderNumber = ashCancelRatio.multiply(orderNumber);
        //主板废单
        BigDecimal ashInvalidOrderNumber = ashInvalidRatio.multiply(orderNumber);
        //主板不撮合订单
        BigDecimal ashNoMatchOrderNumber = ashNoMatchRatio.multiply(orderNumber);
        //科创板撮合订单
        BigDecimal kshMatchOrderNumber = kshMatchRatio.multiply(orderNumber);
        //科创板撤单
        BigDecimal kshCancelOrderNumber = kshCancelRatio.multiply(orderNumber);
        //科创板废单
        BigDecimal kshInvalidOrderNumber = kshInvalidRatio.multiply(orderNumber);
        //科创板不撮合订单
        BigDecimal kshNoMatchOrderNumber = kshNoMatchRatio.multiply(orderNumber);


        Map<String, BigDecimal> matchingOrderDetails = new HashMap<>();
        matchingOrderDetails = calMatchRatio(orderNumber,ashMatchRatio.add(kshMatchRatio),tradeRatio,ashInvalidRatio.add(kshInvalidRatio));
        matchingOrderDetails.put("kshMatchOrderBuyer",kshMatchOrderNumber.divideAndRemainder(BigDecimal.valueOf(2))[0]);
        matchingOrderDetails.put("kshMatchOrderSeller",kshMatchOrderNumber.divideAndRemainder(BigDecimal.valueOf(2))[0]);
        matchingOrderDetails.put("ashInvalidOrder",ashInvalidOrderNumber);
        matchingOrderDetails.put("kshInvalidOrder",kshInvalidOrderNumber);
        matchingOrderDetails.put("ashCancelOrder",ashCancelOrderNumber.divideAndRemainder(BigDecimal.valueOf(2))[0]);
        matchingOrderDetails.put("ashCancelOrderOri",ashCancelOrderNumber.divideAndRemainder(BigDecimal.valueOf(2))[0]);
        matchingOrderDetails.put("kshCancelOrder",kshCancelOrderNumber.divideAndRemainder(BigDecimal.valueOf(2))[0]);
        matchingOrderDetails.put("kshCancelOrderOri",kshCancelOrderNumber.divideAndRemainder(BigDecimal.valueOf(2))[0]);
        matchingOrderDetails.put("ashNoMatchOrder",ashNoMatchOrderNumber);
        matchingOrderDetails.put("kshNoMatchOrder",kshNoMatchOrderNumber);
        matchingOrderDetails.put("matching1v1Buyer",matchingOrderDetails.get("matching1v1Buyer").subtract(matchingOrderDetails.get("kshMatchOrderBuyer")));
        matchingOrderDetails.put("matching1v1Seller",matchingOrderDetails.get("matching1v1Seller").subtract(matchingOrderDetails.get("kshMatchOrderSeller")));


        return matchingOrderDetails;

    }

    private Map<String,BigDecimal> calMatchRatio(BigDecimal orderNumber, BigDecimal matchRatio, BigDecimal tradeRatio, BigDecimal invalidRatio) {
        // 计算成交撮合订单数
        BigDecimal matchOrderNumber = orderNumber.multiply(matchRatio);
        // 计算成交笔数
        BigDecimal matchTradeNumber = BigDecimal.ONE.subtract(invalidRatio).multiply(orderNumber).multiply(tradeRatio);
        BigDecimal tradeMinusMatchOrderNumber = matchTradeNumber.subtract(matchOrderNumber);
        BigDecimal matching1v1Buyer= new BigDecimal("0");
        BigDecimal matching1v1Seller= new BigDecimal("0");
        BigDecimal matching1v10Buyer= new BigDecimal("0");
        BigDecimal matching1v10Seller= new BigDecimal("0");
        BigDecimal matching1v2Buyer= new BigDecimal("0");
        BigDecimal matching1v2Seller= new BigDecimal("0");

        if(tradeMinusMatchOrderNumber.compareTo(BigDecimal.ZERO)>=0) {
            matching1v10Buyer = tradeMinusMatchOrderNumber.divideAndRemainder(BigDecimal.valueOf(9))[0];
            matching1v10Seller = matching1v10Buyer.multiply(BigDecimal.valueOf(10));
            matching1v2Buyer = tradeMinusMatchOrderNumber.divideAndRemainder(BigDecimal.valueOf(9))[1];
            matching1v2Seller = matching1v2Buyer.multiply(BigDecimal.valueOf(2));
            BigDecimal matching1v10=matching1v10Buyer.add(matching1v10Seller);
            BigDecimal matching1v2=matching1v2Buyer.add(matching1v2Seller);
            BigDecimal matching1v1=matchOrderNumber.subtract(matching1v10.add(matching1v2));
            if(matching1v1.divideAndRemainder(BigDecimal.valueOf(2))[1].compareTo(BigDecimal.ZERO)==0) {
                matching1v1Buyer = matching1v1.divideAndRemainder(BigDecimal.valueOf(2))[0];
                matching1v1Seller = matching1v1.divideAndRemainder(BigDecimal.valueOf(2))[0];
            } else {
                matching1v10Buyer = matching1v10Buyer.subtract(BigDecimal.valueOf(1));
                matching1v10Seller = matching1v10Buyer.multiply(BigDecimal.valueOf(10));
                matching1v2Buyer = matching1v2Buyer.add(BigDecimal.valueOf(9));
                matching1v2Seller = matching1v2Buyer.multiply(BigDecimal.valueOf(2));
                matching1v1=matchOrderNumber.subtract(matching1v10Buyer.subtract(matching1v2Buyer.subtract(matching1v10Seller.subtract(matching1v2Seller))));
                matching1v1Buyer = matching1v1.divideAndRemainder(BigDecimal.valueOf(2))[0];
                matching1v1Seller = matching1v1.divideAndRemainder(BigDecimal.valueOf(2))[0];
            }

            Map<String,BigDecimal> MatchOrderMap = new HashMap<String,BigDecimal>();
            MatchOrderMap.put("matching1v1Buyer",matching1v1Buyer);
            MatchOrderMap.put("matching1v1Seller",matching1v1Seller);
            MatchOrderMap.put("matching1v10Buyer",matching1v10Buyer);
            MatchOrderMap.put("matching1v10Seller",matching1v10Seller);
            MatchOrderMap.put("matching1v2Buyer",matching1v2Buyer);
            MatchOrderMap.put("matching1v2Seller",matching1v2Seller);
            return MatchOrderMap;
        }else{
            return null;
        }
    }



}
