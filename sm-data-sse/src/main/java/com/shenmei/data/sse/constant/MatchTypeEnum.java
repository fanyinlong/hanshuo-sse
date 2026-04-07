package com.shenmei.data.sse.constant;

/**
 * 匹配类型枚举
 * @author: teabot
 */
public enum MatchTypeEnum {
    KSH("科创板", "07", "08", "06", "05"), // 科创板
    ASH("主板", "03", "04", "02", "01");   // 主板

    private final String description;
    private final String invalidKey;
    private final String noMatchKey;
    private final String cancelKey;
    private final String matchKey;

    MatchTypeEnum(String description, String invalidKey, String noMatchKey, String cancelKey, String matchKey) {
        this.description = description;
        this.invalidKey = invalidKey;
        this.noMatchKey = noMatchKey;
        this.cancelKey = cancelKey;
        this.matchKey = matchKey;
    }

    public String getDescription() {
        return description;
    }

    public String getInvalidKey() {
        return invalidKey;
    }

    public String getNoMatchKey() {
        return noMatchKey;
    }

    public String getCancelKey() {
        return cancelKey;
    }

    public String getMatchKey() {
        return matchKey;
    }
}
