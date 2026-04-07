package com.shenmei.data.sse.constant;

/**
 * 描述: 订单撮合数量枚举
 *
 * @author: teabot
 */
public enum MatchOrderQtyEnum {

    MATCH_ORDER_QTY_ONE(1, "1撮1"),
    MATCH_ORDER_QTY_TWO(2, "1撮2"),
    MATCH_ORDER_QTY_TEN(10, "1撮10"),
    ;
    private final int matchOrderQty;
    private final String matchOrderQtyDesc;

    MatchOrderQtyEnum(int matchOrderQty, String matchOrderQtyDesc) {
        this.matchOrderQty = matchOrderQty;
        this.matchOrderQtyDesc = matchOrderQtyDesc;
    }

    public int getMatchOrderQty() {
        return matchOrderQty;
    }

    public String getMatchOrderQtyDesc() {
        return matchOrderQtyDesc;
    }
}