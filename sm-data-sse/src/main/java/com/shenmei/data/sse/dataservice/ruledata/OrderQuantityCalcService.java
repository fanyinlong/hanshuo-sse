package com.shenmei.data.sse.dataservice.ruledata;

import com.alibaba.fastjson2.JSONObject;
import com.shenmei.data.sse.constant.MatchTypeEnum;
import com.shenmei.data.sse.dto.OrderQuantityResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 订单数量计算器
 * 用于封装各类订单的数量计算逻辑
 */
@Slf4j
@Service
public class OrderQuantityCalcService {


    private static final int QUANTITY_4 = 4; // 特殊分配数量：4
    private static final int QUANTITY_3 = 3; // 特殊分配数量：3
    private static final int QUANTITY_2 = 2; // 特殊分配数量：2
    private static final int QUANTITY_1 = 1; // 特殊分配数量：1
    private static final int ALLOCATION_2 = 2; // 分配数量：2
    private static final int ALLOCATION_1 = 1; // 分配数量：1


    /**
     * 计算订单数量
     *
     * @param marketType      市场类型
     * @param setOrderNumber  SET订单总数
     * @param matchMethodJson 撮合方式比例配置
     * @param setId           SET编号
     * @return 订单数量计算结果
     */
    public OrderQuantityResult calculateOrderQuantities(MatchTypeEnum marketType,
                                                        BigDecimal setOrderNumber,
                                                        JSONObject matchMethodJson,
                                                        String setId) {


        // 原始比例
        BigDecimal invalidOrderRationOri = new BigDecimal(matchMethodJson.getString(marketType.getInvalidKey()))
                .divide(new BigDecimal(100), 8, RoundingMode.HALF_UP);

        BigDecimal noMatchOrderRationOri = new BigDecimal(matchMethodJson.getString(marketType.getNoMatchKey()))
                .divide(new BigDecimal(100), 8, RoundingMode.HALF_UP);
        BigDecimal cancelOrderRationOri = new BigDecimal(matchMethodJson.getString(marketType.getCancelKey()))
                .divide(new BigDecimal(100), 8, RoundingMode.HALF_UP);
        BigDecimal matchOrderRationOri = new BigDecimal(matchMethodJson.getString(marketType.getMatchKey()))
                .divide(new BigDecimal(100), 8, RoundingMode.HALF_UP);

        // 调整后比例（基于有效订单类型）
        BigDecimal totalRatio = invalidOrderRationOri.add(noMatchOrderRationOri)
                .add(cancelOrderRationOri).add(matchOrderRationOri);

        BigDecimal invalidOrderRation = invalidOrderRationOri.divide(totalRatio, 8, RoundingMode.UP);

        BigDecimal noMatchOrderRation = noMatchOrderRationOri.divide(totalRatio, 8, RoundingMode.UP);

        BigDecimal cancelOrderRation = cancelOrderRationOri.divide(totalRatio, 8, RoundingMode.UP);

        BigDecimal matchOrderRation = matchOrderRationOri.divide(totalRatio, 8, RoundingMode.UP);


        // 计算各类订单数量
        BigDecimal invalidOrderNumberDecimal = setOrderNumber.multiply(invalidOrderRation).setScale(0, RoundingMode.HALF_UP);
        BigDecimal noMatchOrderNumberDecimal = setOrderNumber.multiply(noMatchOrderRation).setScale(0, RoundingMode.HALF_UP);
        BigDecimal cancelOrderNumberDecimal = setOrderNumber.multiply(cancelOrderRation).setScale(0, RoundingMode.HALF_UP);
        BigDecimal matchOrderNumberDecimal = setOrderNumber.multiply(matchOrderRation).setScale(0, RoundingMode.HALF_UP);

        int invalidOrderNumber = invalidOrderNumberDecimal.intValue();
        int noMatchOrderNumber = noMatchOrderNumberDecimal.intValue();
        int cancelOrderNumber = cancelOrderNumberDecimal.intValue();
        int matchOrderNumber = matchOrderNumberDecimal.intValue();

        // 确保撤单数量为偶数（因为需要成对生成）
        if (cancelOrderNumber % 2 == 1) {
            cancelOrderNumber = cancelOrderNumber - 1;
        }

        // 对于科创板，确保撮合订单数量也为偶数
        if (marketType == MatchTypeEnum.KSH) {
            if (matchOrderNumber % 2 == 1) {
                matchOrderNumber = matchOrderNumber - 1;
            }
        }
        //预分配数量
        int preOrderNumber = invalidOrderNumber + noMatchOrderNumber + cancelOrderNumber + matchOrderNumber;

        // 未分配数量
        int remainingQuantity = setOrderNumber.intValue() - preOrderNumber;

        log.info("set:{},撮合类型:{}-废单,原始比例: {},占订单比例：{},订单数量:{}", setId, marketType.getDescription(), invalidOrderRationOri, invalidOrderRation, invalidOrderNumber);
        log.info("set:{},撮合类型:{}-不撮合,原始比例: {},占订单比例：{},订单数量:{}", setId, marketType.getDescription(), noMatchOrderRationOri, noMatchOrderRation, noMatchOrderNumber);
        log.info("set:{},撮合类型:{}-撤单,原始比例: {},占订单比例：{},订单数量:{}", setId, marketType.getDescription(), cancelOrderRationOri, cancelOrderRation, cancelOrderNumber);
        log.info("set:{},撮合类型:{}-撮合,原始比例: {},占订单比例：{},订单数量:{}", setId, marketType.getDescription(), matchOrderRationOri, matchOrderRation, matchOrderNumber);

        log.info("set:{},总订单数量:{},分配数量:{},未分配数量:{}", setId, setOrderNumber, preOrderNumber, remainingQuantity);

        // 科创板
        //将未分配的数量分配给数量最多的订单类型，
        // 如果是4，则平均分配给撮合成功和撤单，如果是奇数是3，则分配2给撮合成功和撤单数量较大者，剩余1分配不撮合和废单较大者；
        // 如果是2，分配2给撮合成功和撤单数量较大者，
        // 如果是1分配不撮合和废单较大者
        if(marketType.name().equals("KSH") && remainingQuantity != 0){
            // 根据剩余数量执行不同分配策略
            switch (remainingQuantity) {
                case QUANTITY_4:
                    // 平均分配给撮合成功和撤单
                    matchOrderNumber += ALLOCATION_2;
                    cancelOrderNumber += ALLOCATION_2;
                    break;
                case QUANTITY_3:
                    // 2分配给撮合/撤单较大者，1分配给不撮合/废单较大者
                    matchOrderNumber = allocateToLarger(matchOrderNumber, cancelOrderNumber, ALLOCATION_2);
                    noMatchOrderNumber = allocateToLarger(noMatchOrderNumber, invalidOrderNumber, ALLOCATION_1);
                    break;
                case QUANTITY_2:
                    // 全部分配给撮合/撤单较大者
                    matchOrderNumber = allocateToLarger(matchOrderNumber, cancelOrderNumber, ALLOCATION_2);
                    break;
                case QUANTITY_1:
                    // 分配给不撮合/废单较大者
                    noMatchOrderNumber = allocateToLarger(noMatchOrderNumber, invalidOrderNumber, ALLOCATION_1);
                    break;
                default:
                    // 默认分配给当前数量最大的订单类型
                   log.error("set:{},剩余数量分配错误!", setId);
            }
            remainingQuantity = 0; // 重置剩余数量
        }


        return new OrderQuantityResult(
                invalidOrderNumber,
                noMatchOrderNumber,
                cancelOrderNumber,
                matchOrderNumber
        );
    }

    /**
     * 将指定数量分配给两个订单类型中的较大者
     * @param order1 订单类型1数量
     * @param order2 订单类型2数量
     * @param amount 分配数量
     * @return 分配后的较大订单类型数量
     */
    private int allocateToLarger(int order1, int order2, int amount) {
        return order1 >= order2 ? order1 + amount : order2 + amount;
    }





}
