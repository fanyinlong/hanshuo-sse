package com.shenmei.data.sse.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 字典管理类，集中保存和提供各种字典映射
 */
public class DictManager {
    // 存储所有字典的容器：key为字典名称，value为具体的键值映射
    private static final Map<String, Map<String, String>> ALL_DICTS = new HashMap<>();

    // 静态初始化块：初始化所有字典
    static {
        initDicts();
    }

    /**
     * 初始化各种字典映射
     */
    private static void initDicts() {
        // 1. JSON键转译字典（示例1）
        Map<String, String> matchingType = new HashMap<>();
        matchingType.put("01", "主板撮合订单");
        matchingType.put("02", "主板撤单");
        matchingType.put("03", "主板废单");
        matchingType.put("04", "主板不撮合订单");
        matchingType.put("05", "科创板撮合订单");
        matchingType.put("06", "科创板撤单");
        matchingType.put("07", "科创板废单");
        matchingType.put("08", "科创板不撮合订单");
        ALL_DICTS.put("matchingType", matchingType);

        // 2. 状态码字典（示例2）

        // 3. 可以添加更多字典...
    }

    /**
     * 获取指定名称的字典
     * @param dictName 字典名称（如"jsonKeyDict"）
     * @return 对应的字典映射，若不存在则返回空Map
     */
    public static Map<String, String> getDict(String dictName) {
        // 为避免外部修改内部字典，返回一个新的HashMap副本
        return new HashMap<>(ALL_DICTS.getOrDefault(dictName, new HashMap<>()));
    }

    /**
     * 从指定字典中获取键对应的value
     * @param dictName 字典名称
     * @param key 要查询的键
     * @return 对应的value，若不存在则返回null
     */
    public static String getValueFromDict(String dictName, String key) {
        Map<String, String> dict = ALL_DICTS.get(dictName);
        return dict != null ? dict.get(key) : null;
    }

    /**
     * 动态添加新字典（可选方法，根据需求添加）
     * @param dictName 字典名称
     * @param dict 字典内容
     */
    public static void addDict(String dictName, Map<String, String> dict) {
        ALL_DICTS.put(dictName, new HashMap<>(dict)); // 存储副本，避免外部修改影响内部
    }
}
