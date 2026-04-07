package com.shenmei.data.sse.util;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 单机版10位不重复订单号生成器
 * 字符集：0-9、A-Z、a-z（共62个字符）
 * 结构：时间戳差(6位) + 序列号(4位)，总长度10位
 */
public class OrderNumberGenerator {
    // 62进制字符映射表
    private static final char[] BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    // 基准时间：2023-01-01 00:00:00
    private static final long BASE_TIMESTAMP = 1672531200L;

    // 序列号计数器，每秒重置
    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    // 上一秒的时间戳
    private static long LAST_TIMESTAMP = -1L;

    // 序列号最大值(62^4 - 1)
    private static final int MAX_SEQUENCE = 62 * 62 * 62 * 62 - 1;

    /**
     * 生成10位不重复订单号
     * @return 10位订单号
     */
    public synchronized static String generate() {
        long currentTimestamp = getCurrentTimestamp();

        // 处理时间回拨
        if (currentTimestamp < LAST_TIMESTAMP) {
            throw new RuntimeException("时间回拨异常，无法生成订单号");
        }

        // 如果是新的一秒，重置序列号
        if (currentTimestamp != LAST_TIMESTAMP) {
            SEQUENCE.set(0);
            LAST_TIMESTAMP = currentTimestamp;
        }

        // 获取序列号并自增
        int sequence = SEQUENCE.getAndIncrement();

        // 检查序列号是否超出最大值
        if (sequence > MAX_SEQUENCE) {
            // 等待到下一秒
            currentTimestamp = waitNextSecond(LAST_TIMESTAMP);
            SEQUENCE.set(0);
            LAST_TIMESTAMP = currentTimestamp;
            sequence = SEQUENCE.getAndIncrement();
        }

        // 计算时间戳差
        long deltaTime = currentTimestamp - BASE_TIMESTAMP;

        // 转换为62进制并拼接
        String timePart = encodeToBase62(deltaTime, 6);
        String sequencePart = encodeToBase62(sequence, 4);

        return timePart + sequencePart;
    }

    /**
     * 将数字转换为指定长度的62进制字符串
     * @param number 要转换的数字
     * @param length 目标长度
     * @return 62进制字符串
     */
    private static String encodeToBase62(long number, int length) {
        if (number < 0) {
            throw new IllegalArgumentException("数字必须是非负数");
        }

        StringBuilder sb = new StringBuilder();

        if (number == 0) {
            sb.append(BASE62_CHARS[0]);
        } else {
            while (number > 0) {
                int remainder = (int) (number % 62);
                sb.append(BASE62_CHARS[remainder]);
                number = number / 62;
            }
        }

        // 补前导0以达到指定长度
        while (sb.length() < length) {
            sb.append(BASE62_CHARS[0]);
        }

        // 反转得到正确的顺序
        return sb.reverse().toString();
    }

    /**
     * 获取当前时间戳(秒)
     */
    private static long getCurrentTimestamp() {
        return Instant.now().getEpochSecond();
    }

    /**
     * 等待到下一秒
     */
    private static long waitNextSecond(long lastTimestamp) {
        long timestamp = getCurrentTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentTimestamp();
        }
        return timestamp;
    }

    // 测试方法
    public static void main(String[] args) {
        // 生成10个测试订单号
        for (int i = 0; i < 10; i++) {
            System.out.println(generate());
        }
    }
}

