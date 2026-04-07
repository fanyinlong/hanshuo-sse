package com.shenmei.data.sse.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CIOrdIDGenerator {
    // 定义可用的随机字符集：数字0-9 + 大写字母A-Z + 小写字母a-z
    private static final String CHAR_SET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Random RANDOM = new Random();

    /**
     * 将数字转换为16进制并补充随机字符至10位
     * @param number 输入数字
     * @return 处理后的10位字符串
     */
    public static String convert(long number) {

        // 将数字转换为16进制（小写）
        String hexStr = Long.toHexString(number);

        // 计算需要补充的字符数量
        int append = 10 - hexStr.length();
        if (append < 0) {
            // 理论上不会走到这里，因为10亿以下的数16进制最多8位
            throw new IllegalStateException("16进制字符串长度超过10位");
        }

        // 生成随机补充字符
        StringBuilder randomChars = new StringBuilder();
        for (int i = 0; i < append; i++) {
            // 从字符集中随机选取一个字符
            int index = RANDOM.nextInt(CHAR_SET.length());
            randomChars.append(CHAR_SET.charAt(index));
        }

        // 拼接结果（16进制字符串 + 随机字符）
        return hexStr + randomChars.toString();
    }

    /**
     * 高并发随机数
     */
    public static String threadLocalIntRandom(int i) {
        StringBuffer sb = new StringBuffer();
        sb.append((long) ThreadLocalRandom.current().nextDouble(Math.pow(10, i)));
        /** 如果位数不一致，重新生成**/
        if (i != sb.length()) {
            return threadLocalIntRandom(i);
        }
        return sb.toString();
    }

    public static String randomByTimestampNum(int i) {
        return System.currentTimeMillis() + threadLocalIntRandom(i);
    }
    public static void main(String[] args) {
        // 测试示例
        long testNumber1 = 255; // 16进制为"ff"
        long testNumber2 = 123456789;
        long testNumber3 = 0;

        System.out.println(randomByTimestampNum(5));


        System.out.println(convert(Long.valueOf(randomByTimestampNum(5))));  // 输出类似：ffA3b7Z9x2（长度10）
        System.out.println(convert(testNumber2));  // 输出类似：75bcd15K9p（长度10）
        System.out.println(convert(testNumber3));  // 输出类似：0rT5y7u2i8（长度10）
    }
}
