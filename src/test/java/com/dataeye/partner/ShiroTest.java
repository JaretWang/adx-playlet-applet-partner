package com.dataeye.partner;

import com.dataeye.partner.bean.PlayletPartnerUser;
import com.dataeye.partner.security.MyRealm;
import com.dataeye.partner.security.SecurityTool;
import com.dataeye.partner.service.UserService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author jaret
 * @date 2024/10/22 11:46
 * @description
 */
@SpringBootTest
public class ShiroTest {

    @Autowired
    private UserService userService;

    /**
     * 程序生成一批用户
     */
    @Test
    public void addUser() {
        List<PlayletPartnerUser> dyzbUsers = generateUser(10);
        userService.insertUser(dyzbUsers);
    }

    /**
     * 初始化1000个账号到库表
     * 1. 账号具有唯一性：三个字母+4个数字，随机生成
     * 2. 密码：可重复，是6个数据随机组合
     */
    static void generateUserInsertSql(int num) {
        for (int i = 1; i <= num; i++) {
            String username = generateAccount();
            String password = generatePassword();
            String salt = new SecureRandomNumberGenerator().nextBytes(10).toHex();
            String finalPassword = new Sha256Hash(password, ByteSource.Util.bytes(salt), MyRealm.HASH_ITERATIONS).toString();
            String sql = "INSERT INTO `adx_autotag_service`.`dyzb_user`(`username`, `password`, `salt`, `remark`) VALUES ('" + username + "', '" + finalPassword + "', '" + salt + "', '" + password + "')";
            System.out.println(sql);
        }
    }

    static List<PlayletPartnerUser> generateUser(int num) {
        List<PlayletPartnerUser> entities = new LinkedList<>();
        for (int i = 1; i <= num; i++) {
            String username = generateAccount();
            String password = generatePassword();
            PlayletPartnerUser user = PlayletPartnerUser.builder().username(username).password(password).build();
            System.out.println(i + ", 账号: " + username + ", 密码: " + password);
            entities.add(user);
        }
        return entities;
    }

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";

    private static Random random = new SecureRandom();

    // 生成指定长度的随机字母数字字符串
    private static String generateRandomString(int length, String chars) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    // 生成账号（三个字母+四个数字）
    private static String generateAccount() {
        String letters = generateRandomString(3, ALPHABET);
        String numbers = generateRandomString(4, NUMBERS);
        return letters + numbers;
    }

    // 生成密码（六个数字或字母）
    private static String generatePassword() {
        return generateRandomString(6, NUMBERS);
    }

    @Test
    public void addOneUser() {
        PlayletPartnerUser user = PlayletPartnerUser.builder().username("wcj").password("123").build();
        userService.insertUser(Collections.singletonList(user));
    }

    public static void main(String[] args) {
        String encrypt = SecurityTool.encrypt("123");
        System.out.println(encrypt);

//        List<PlayletPartnerUser> dyzbUsers = generateUser(10);
//        System.out.println(dyzbUsers.size());

//        generateUserInsertSql(1000);
    }

}
