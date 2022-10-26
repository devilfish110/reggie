package com.study.reggie.common;

import java.util.Random;

/**
 * @author That's all
 * 验证码工具类
 */
public class VerifyCodeUtil {

    /**
     * 随机生成size位数的验证码
     */
    public static String getRandomCode(int size) {
        String codeNum = "";
        int[] code = new int[3];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int num = random.nextInt(10) + 48;
            int uppercase = random.nextInt(26) + 65;
            int lowercase = random.nextInt(26) + 97;
            code[0] = num;
            code[1] = uppercase;
            code[2] = lowercase;
            codeNum += (char) code[random.nextInt(3)];
        }
        return codeNum;
    }
}
