package com.shenmei.data.common.utils.sse;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Encrypt {
    
    // 缓存已派生的密钥，避免重复计算 PBKDF2
    private static final Map<String, SecretKeySpec> KEY_CACHE = new ConcurrentHashMap<>();
    private static final String DEFAULT_SALT = "sse_default_salt_2025_fixed";
    
    public static String encrypt(String plainText, String key) throws Exception {
        if (plainText == null || key == null) {
            return plainText;
        }
        
        // 从缓存获取密钥，避免重复的 PBKDF2 计算
        SecretKeySpec secretKey = getSecretKey(key);
        
        // 生成随机 IV（16 字节）
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        
        // AES/CBC 加密
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        
        // 组合 IV + 加密数据（不再需要盐，因为密钥已缓存）
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
        
        return Base64.getEncoder().encodeToString(combined);
    }
    
    public static String decrypt(String cipherText, String key) throws Exception {
        if (cipherText == null || cipherText.length() == 0) {
            return cipherText;
        }
        
        try {
            byte[] combined = Base64.getDecoder().decode(cipherText);
            
            // 兼容旧格式：检测数据长度判断是旧格式还是新格式
            // 旧格式：盐 (16) + IV(16) + 密文，最小长度 48
            // 新格式：IV(16) + 密文，最小长度 32
            byte[] salt;
            byte[] iv;
            byte[] encrypted;
            
            if (combined.length >= 48) {
                // 旧格式：提取盐和 IV
                salt = Arrays.copyOfRange(combined, 0, 16);
                iv = Arrays.copyOfRange(combined, 16, 32);
                encrypted = Arrays.copyOfRange(combined, 32, combined.length);
            } else if (combined.length >= 32) {
                // 新格式：使用固定盐
                salt = DEFAULT_SALT.getBytes(StandardCharsets.UTF_8);
                iv = Arrays.copyOfRange(combined, 0, 16);
                encrypted = Arrays.copyOfRange(combined, 16, combined.length);
            } else {
                // 长度不够，直接返回原文（可能是未加密的数据）
                return cipherText;
            }
            
            // 根据盐派生密钥
            SecretKeySpec secretKey = getSecretKeyWithSalt(key, salt);
            
            // AES/CBC 解密
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] decrypted = cipher.doFinal(encrypted);
            
            return new String(decrypted, StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            // 解密失败，返回原文（避免影响业务）
            return cipherText;
        }
    }
    
    /**
     * 根据指定盐派生密钥（用于兼容旧数据）
     */
    private static SecretKeySpec getSecretKeyWithSalt(String key, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(key.toCharArray(), salt, 100000, 128);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }
    
    /**
     * 获取或缓存密钥（使用固定盐，用于新数据加密）
     */
    private static SecretKeySpec getSecretKey(String key) {
        return KEY_CACHE.computeIfAbsent(key, k -> {
            try {
                byte[] salt = DEFAULT_SALT.getBytes(StandardCharsets.UTF_8);
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                KeySpec spec = new PBEKeySpec(k.toCharArray(), salt, 100000, 128);
                SecretKey tmp = factory.generateSecret(spec);
                return new SecretKeySpec(tmp.getEncoded(), "AES");
            } catch (Exception e) {
                throw new RuntimeException("密钥生成失败", e);
            }
        });
    }

    public static String mask(String input) {
        if (input == null || input.length() <= 4) {
            return input;
        }
        int length = input.length();
        String start = input.substring(0, 2);
        String end = input.substring(length - 2, length);
        return start + "******" + end;
    }
}
