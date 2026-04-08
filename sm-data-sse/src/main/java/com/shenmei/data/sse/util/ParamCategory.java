package com.shenmei.data.sse.util;

public enum ParamCategory {

    RATIO("比例","RATIO"),BASIC("基础","BASIC"),
    EXEC("执行","EXEC"),DIST("分布","DIST"),
    RULE("规则","RULE");

    private String name;
    private String value;

    private ParamCategory(String name,String value) {
        this.name = name;
        this.value = value;

    }
}
