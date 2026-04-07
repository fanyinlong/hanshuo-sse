package com.shenmei.data.sse.dto;

import lombok.Data;

@Data
public class CountCommonDo {
    /**
     * 统计参数
     */
    private String value;
    /**
     * 统计数量
     */
    private int count;
}
