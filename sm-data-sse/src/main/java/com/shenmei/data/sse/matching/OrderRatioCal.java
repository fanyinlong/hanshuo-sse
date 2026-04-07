package com.shenmei.data.sse.matching;

public class OrderRatioCal {

    /*
   计算撮合比
     */
    public static void calMatchRatio(Long orderNumber,Double SuccRatio, Double tradeRatio) {
        //成交撮合单数
        // 计算成交撮合单数
        Long succOrderNumber = (long) (orderNumber * SuccRatio);
        // 计算成交订单数
        Long succTradeNumber = (long) (orderNumber * tradeRatio);

    }

    public static void main(String[] args) {
        System.out.println("hello world");
    }
}
