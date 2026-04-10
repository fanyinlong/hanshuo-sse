package com.shenmei.data.sse.util;

public enum ParamCategory {

    PRODUCT("产品","PRODUCT"),BASIC("基础","BASIC"),
    TRADE("交易","TRADE");

    private String name;
    private String value;

    private ParamCategory(String name,String value) {
        this.name = name;
        this.value = value;

    }
}
