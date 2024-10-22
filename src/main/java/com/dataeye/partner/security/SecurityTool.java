package com.dataeye.partner.security;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

/**
 * @author jaret
 * @date 2024/10/21 17:31
 * @description
 */
public class SecurityTool {

    // RSA 公钥
    private static final String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCXWMIDUU5dSVYDdJDZHxukeBivngyS77ndtsdYWQ4+yCoV5Moqr4j/Sj72xkDn+3iPvuPXS4LhTerFS0Oqoa4f3pyII/+ubLPLW9CJnIr/ZbBn0qd0pWZHI9HYNQ4xagRzcEHourZTf5ClYM7OrU28ZWJqgVuHfrgbWSp+QjjegwIDAQAB";

    // RSA 私钥
    private static final String RSA_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJdYwgNRTl1JVgN0kNkfG6R4GK+eDJLvud22x1hZDj7IKhXkyiqviP9KPvbGQOf7eI++49dLguFN6sVLQ6qhrh/enIgj/65ss8tb0Imciv9lsGfSp3SlZkcj0dg1DjFqBHNwQei6tlN/kKVgzs6tTbxlYmqBW4d+uBtZKn5CON6DAgMBAAECgYAo21EkzCI3lMCfliw0MO2pbLJdVnFWiVjHvbv64a+yyx5xYp4g/9of8M8ml0aWWZCmXYXsbPocEIi8UiEmQxdaINMntcFPXIjmVzOy1oucmmW9vCWVz8LsJ3F4vcgqZIDJwj4sllf3J1Ob37V3ItyYbMZ4rpu77TXfiI0guV2wAQJBAMnhV7/eg1H0mlScp0YXgegLZiE+RTC/tGSNhKFsmzk5cWdBEUO+EJDkYMWf4qd9Nxcu0PCMUrvvFWOIs6Y18IMCQQC/62UMT9xcquRk/tZTADT/M8r4Q9MtN4/gd1DAW9bypHxKSsU5qunAJ+jgnnjWlsR9Y55aoN2yYR6F6lqE4foBAkEAq5y6umkOGvoiw3CTpbrP/JDMFdBYgvNEBwJiT9MCNOgG5e5jHcIRVQo+1WBwDU8dKMDsbr/qiMXZ2c3YYZ+SvwJAPFHJwqLfCakUe8G0rwWjooouwvTeTJnn+I7HrI2w8zZLcjYp4sQbD6/7e42TQS7Ftvs2bBmfVE89/qz3okg8AQJBAJwGqWYl+q4y3SnOwmL55dKqoHapmdDncUHMnLPfXaj+u6KEoKRZqJR+F8RDyo52xJ7GxmCGzl1kacjh3J/GQ78=";
    private static final RSA RSA_OBJ = new RSA(RSA_PRIVATE_KEY, RSA_PUBLIC_KEY);

    /**
     * 使用私钥解密
     */
    public static String decrypt(String encryptedPassword) {
        return RSA_OBJ.decryptStr(encryptedPassword, KeyType.PrivateKey);
    }

    /**
     * 使用公钥加密
     */
    public static String encrypt(String password) {
        return RSA_OBJ.encryptBase64(password, KeyType.PublicKey);
    }

    public static void main(String[] args) {
        String password = "123";
        String encryptedPassword = RSA_OBJ.encryptBase64(password, KeyType.PublicKey);
        System.out.println("加密后的密码: " + encryptedPassword);

        String fontPwd = "M+wvw4Ht/jkigJGDdRe+HZvDjzmW3QQXKp05DxvPg1gWSV8day6KLS9ysH3cUmZQwalWUz4DQEjX6z36xRG+9ycMPEKIPLDMG7+0o7/EF9qKsNLDKqlQ+CoaGXCmFUy813bf1Lo1HItxav0aggQ14mBOoKdLQotmJ/UMv7S3hm0=";
        String decryptedPassword = RSA_OBJ.decryptStr(fontPwd, KeyType.PrivateKey);
        System.out.println("解密后的密码: " + decryptedPassword);
    }

}
