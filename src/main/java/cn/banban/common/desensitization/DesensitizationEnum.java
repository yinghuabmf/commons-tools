package cn.banban.common.desensitization;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

/**
 * 脱敏枚举
 *
 * @author : banxiaohua
 * @date : 2020/3/17 11:02 AM
 */
@Slf4j
public enum DesensitizationEnum {

    /**
     * 中文名
     * 保留前一位
     */
    CHINESE_NAME(s -> {
        String name = StringUtils.left(s, 1);
        return StringUtils.rightPad(name, s.length(), "*");
    }),

    /**
     * 身份证号
     * 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     */
    ID_CARD(s -> {
        int minLength = 4;
        int length = s.length();
        if (length <= minLength) {
            return s;
        }
        String num = StringUtils.right(s, 4);
        return StringUtils.leftPad(num, s.length(), "*");
    }),

    /**
     * 固话
     *  后四位，其他隐藏<例子：****1234>
     */
    FIXED_PHONE(s -> {
        int minLength = 4;
        int length = s.length();
        if (length <= minLength) {
            return s;
        }
        return StringUtils.leftPad(StringUtils.right(s, 4), s.length(), "*");
    }),

    /**
     * 手机号
     *  前三位，后四位，其他隐藏<例子:138******1234>
     */
    MOBILE_PHONE(s -> {
        int minLength = 7;
        int length = s.length();
        if (length <= minLength) {
            return s;
        }
        return StringUtils.left(s, 3).concat(
                StringUtils.removeStart(
                        StringUtils.leftPad(StringUtils.right(s, 4), s.length(), "*"),
                        "***"));
    }),

    /**
     * [地址]
     *
     * 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     */
    ADDRESS(s -> {
        int minLength = 2;
        int length = s.length();
        if (length <= minLength) {
            return s;
        }
        return StringUtils.rightPad(StringUtils.left(s, length / 2), length, "*");
    }),

    /**
     * [电子邮箱]
     * 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     */
    EMAIL(email -> {
        int minLength = 1;
        int index = StringUtils.indexOf(email, "@");
        if (index <= minLength) {
            return email;
        }
        return StringUtils.rightPad(StringUtils.left(email, 1), index, "*").concat(
                StringUtils.mid(email, index, email.length()));
    }),

    /**
     * [银行卡号]
     * 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234>
     */
    BANK_CARD(cardNum -> {
        int minLength = 10;
        int length = cardNum.length();
        if (length <= minLength) {
            return cardNum;
        }
        return StringUtils.left(cardNum, 6).concat(
                StringUtils.removeStart(
                        StringUtils.leftPad(StringUtils.right(cardNum, 4), length, "*"), "******"));

    });

    @Getter
    private Function<String, String> function;

    DesensitizationEnum(Function<String, String> function) {
        this.function = function;
    }

}
