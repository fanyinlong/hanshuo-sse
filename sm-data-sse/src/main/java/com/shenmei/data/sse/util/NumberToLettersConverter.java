package com.shenmei.data.sse.util;

public class NumberToLettersConverter {

    // 字符映射表：0-25→A-Z，26-51→a-z
    private static final char[] CHAR_MAP = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    // 基数（52个字符）
    private static final int BASE = 52;
    // 目标字母长度（3位）
    private static final int LETTER_LENGTH = 3;

    /**
     * 将5位数字字符串转换为3位字母字符串
     * @param fiveDigitStr 5位数字字符串（如"00123"、"99999"）
     * @return 3位字母组合（如"AaB"、"zZS"）
     * @throws IllegalArgumentException 若输入不是5位数字
     */
    public static String convert(String fiveDigitStr) {
        // 1. 验证输入（必须是5位数字）
        if (fiveDigitStr == null || !fiveDigitStr.matches("\\d{5}")) {
            throw new IllegalArgumentException("输入必须是5位数字字符串（如00123）");
        }

        // 2. 将5位数字转为整数（0-99999）
        int number = Integer.parseInt(fiveDigitStr);

        // 3. 将整数转换为3位52进制数（不足3位补0）
        int[] digits = new int[LETTER_LENGTH];
        for (int i = LETTER_LENGTH - 1; i >= 0; i--) {
            digits[i] = number % BASE; // 取余得当前位
            number = number / BASE;    // 整除得高位
        }

        // 4. 将52进制数位映射为字母
        StringBuilder result = new StringBuilder();
        for (int digit : digits) {
            result.append(CHAR_MAP[digit]);
        }

        return result.toString();
    }

    // 测试示例
    public static void main(String[] args) {
        System.out.println(convert("00000")); // 输出 "AAA"（0→A, 0→A, 0→A）
        System.out.println(convert("00001")); // 输出 "AAB"（0→A, 0→A, 1→B）
        System.out.println(convert("00052")); // 输出 "AAB"（52 = 1*52 + 0 → 0,1,0 → "ABA"）
        System.out.println(convert("99999")); // 输出 "kZ3"（计算见下方说明）
    }
}
