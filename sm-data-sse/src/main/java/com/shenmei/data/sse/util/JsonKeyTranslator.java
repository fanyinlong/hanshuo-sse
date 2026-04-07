package com.shenmei.data.sse.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonKeyTranslator {
    // Jackson ObjectMapper实例，用于JSON处理
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将JSON字符串中的键按照字典映射表进行转译
     * @param jsonString 原始JSON字符串
     * @param dict 键映射字典，key为原始键，value为转译后的键
     * @return 转译后的JSON字符串
     * @throws JsonProcessingException 当JSON解析或生成失败时抛出
     */
    public static String translateJsonKeys(String jsonString, Map<String, String> dict) throws JsonProcessingException {
        // 解析原始JSON为JsonNode
        JsonNode rootNode = objectMapper.readTree(jsonString);

        // 递归处理JSON节点，替换所有键
        JsonNode translatedNode = translateNode(rootNode, dict);

        // 将处理后的JsonNode转换为格式化的JSON字符串
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(translatedNode);
    }

    /**
     * 递归处理JSON节点，替换其中的键
     * @param node 要处理的JSON节点
     * @param dict 键映射字典
     * @return 处理后的JSON节点
     */
    private static JsonNode translateNode(JsonNode node, Map<String, String> dict) {
        // 如果是对象节点
        if (node.isObject()) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String originalKey = field.getKey();
                JsonNode valueNode = field.getValue();

                // 查找映射的键，如果没有则使用原键
                String translatedKey = dict.getOrDefault(originalKey, originalKey);

                // 递归处理子节点
                objectNode.set(translatedKey, translateNode(valueNode, dict));
            }
            return objectNode;
        }
        // 如果是数组节点
        else if (node.isArray()) {
            ArrayNode arrayNode = objectMapper.createArrayNode();
            for (JsonNode element : node) {
                // 递归处理数组中的每个元素
                arrayNode.add(translateNode(element, dict));
            }
            return arrayNode;
        }
        // 其他类型节点（字符串、数字、布尔值等）直接返回
        else {
            return node;
        }
    }

    // 示例用法
    public static void main(String[] args) {
        try {
            // 原始JSON字符串
            String originalJson = "{\n" +
                    "  \"name\": \"张三\",\n" +
                    "  \"age\": 30,\n" +
                    "  \"address\": {\n" +
                    "    \"city\": \"北京\",\n" +
                    "    \"street\": \"长安街\"\n" +
                    "  },\n" +
                    "  \"hobbies\": [\"读书\", \"运动\"]\n" +
                    "}";

            // 定义键映射字典
            Map<String, String> dict = new HashMap<>();
            dict.put("name", "xm");       // 姓名 -> xm
            dict.put("age", "nl");        // 年龄 -> nl
            dict.put("address", "dz");    // 地址 -> dz
            dict.put("city", "cs");       // 城市 -> cs
            dict.put("street", "jd");     // 街道 -> jd
            dict.put("hobbies", "xq");    // 爱好 -> xq

            // 转译JSON键
            String translatedJson = translateJsonKeys(originalJson, dict);

            // 输出结果
            System.out.println("原始JSON:");
            System.out.println(originalJson);
            System.out.println("\n转译后的JSON:");
            System.out.println(translatedJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
