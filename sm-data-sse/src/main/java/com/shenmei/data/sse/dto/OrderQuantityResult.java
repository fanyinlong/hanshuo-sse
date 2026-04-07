package com.shenmei.data.sse.dto;

import lombok.Data;

/**
 * 订单数量计算结果
 */
@Data
public class OrderQuantityResult {
    // 废单数量
    private int invalidOrderNumber;
    // 不撮合订单数量
    private int noMatchOrderNumber;
    // 撤单数量
    private int cancelOrderNumber;
    // 撮合订单数量
    private int matchOrderNumber;

    public OrderQuantityResult(int invalidOrderNumber, int noMatchOrderNumber, int cancelOrderNumber, int matchOrderNumber) {
        this.invalidOrderNumber = invalidOrderNumber;
        this.noMatchOrderNumber = noMatchOrderNumber;
        this.cancelOrderNumber = cancelOrderNumber;
        this.matchOrderNumber = matchOrderNumber;
    }
}
