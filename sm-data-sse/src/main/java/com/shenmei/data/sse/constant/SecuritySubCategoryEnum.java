package com.shenmei.data.sse.constant;

import lombok.Getter;

@Getter
public enum SecuritySubCategoryEnum {

    KSH("KSH", "科创板"),
    ASH("ASH", "主板");

    private String code;
    private String description;

    SecuritySubCategoryEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }


}
