package com.aebiz.app.acc.modules.commons.utils;

import com.aebiz.app.acc.modules.models.em.PasswordStrengthEnum;

/**
 * 密码强度（得分）工具类
 * Created by hechao on 2017/7/27.
 */
public class PasswordStrengthUtil {

    private PasswordStrengthUtil(){}

    public static PasswordStrengthEnum getStrengthLevel(String rawpasswd) {
        PasswordStrengthEnum safelevel = PasswordStrengthEnum.WEAK;
        if (rawpasswd == null) {
            return safelevel;
        }
        int grade = 0;
        int index = 0;
        char[] passwdChars = rawpasswd.toCharArray();

        int numIndex = 0;
        int sLetterIndex = 0;
        int lLetterIndex = 0;
        int symbolIndex = 0;

        for (char passwdChar : passwdChars) {
            /*
             * 数字 48-57 A-Z 65 - 90 a-z 97 - 122 !"#$%&'()*+,-./ (ASCII码：33~47)
             * :;<=>?@ (ASCII码：58~64) [\]^_` (ASCII码：91~96) {|}~
             * (ASCII码：123~126)
             */
            if (passwdChar >= 48 && passwdChar <= 57) {
                numIndex++;
            } else if (passwdChar >= 65 && passwdChar <= 90) {
                lLetterIndex++;
            } else if (passwdChar >= 97 && passwdChar <= 122) {
                sLetterIndex++;
            } else if ((passwdChar >= 33 && passwdChar <= 47)
                    || (passwdChar >= 58 && passwdChar <= 64)
                    || (passwdChar >= 91 && passwdChar <= 96)
                    || (passwdChar >= 123 && passwdChar <= 126)) {
                symbolIndex++;
            }
        }
        /*
         * 一、密码长度: 5 分: 小于等于 4 个字符 10 分: 5 到 7 字符 25 分: 大于等于 8 个字符
         */
        if (passwdChars.length <= 4) {
            index = 5;
        } else if (passwdChars.length <= 7) {
            index = 10;
        } else {
            index = 25;
        }
        grade += index;

        /*
         * 二、字母: 0 分: 没有字母 10 分: 全都是小（大）写字母 20 分: 大小写混合字母
         */
        if (lLetterIndex == 0 && sLetterIndex == 0) {
            index = 0;
        } else if (lLetterIndex != 0 && sLetterIndex != 0) {
            index = 20;
        } else {
            index = 10;
        }
        grade += index;
        /*
         * 三、数字: 0 分: 没有数字 10 分: 1 个数字 20 分: 大于 1 个数字
         */
        if (numIndex == 0) {
            index = 0;
        } else if (numIndex == 1) {
            index = 10;
        } else {
            index = 20;
        }
        grade += index;

        /*
         * 四、符号: 0 分: 没有符号 10 分: 1 个符号 25 分: 大于 1 个符号
         */
        if (symbolIndex == 0) {
            index = 0;
        } else if (symbolIndex == 1) {
            index = 10;
        } else {
            index = 25;
        }
        grade += index;
        /*
         * 五、奖励: 2 分: 字母和数字 3 分: 字母、数字和符号 5 分: 大小写字母、数字和符号
         */
        if ((sLetterIndex != 0 || lLetterIndex != 0) && numIndex != 0) {
            index = 2;
        } else if ((sLetterIndex != 0 || lLetterIndex != 0) && numIndex != 0
                && symbolIndex != 0) {
            index = 3;
        } else if (sLetterIndex != 0 && lLetterIndex != 0 && numIndex != 0
                && symbolIndex != 0) {
            index = 5;
        }
        grade += index;


       if (grade >= 60) {
            safelevel = PasswordStrengthEnum.STRONG;
        } else if (grade >= 50) {
            safelevel = PasswordStrengthEnum.MIDDLE;
        } else if (grade >= 25) {
            safelevel = PasswordStrengthEnum.WEAK;
        }
        return safelevel;
    }
}
