package com.sunwayworld.utils.util;


import com.sunwayworld.utils.constant.RegexConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/02
 *     desc  : 正则相关工具类
 * </pre>
 */
public final class RegexUtils {

    private RegexUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    ///////////////////////////////////////////////////////////////////////////
    // If u want more please visit http://toutiao.com/i6231678548520731137
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 验证手机号（简单）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileSimple(CharSequence input) {
        return isMatch(RegexConstants.REGEX_MOBILE_SIMPLE, input);
    }

    /**
     * 验证手机号（精确）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileExact(CharSequence input) {
        return isMatch(RegexConstants.REGEX_MOBILE_EXACT, input);
    }

    /**
     * 验证电话号码
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isTel(CharSequence input) {
        return isMatch(RegexConstants.REGEX_TEL, input);
    }

    /**
     * 验证身份证号码15位
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard15(CharSequence input) {
        return isMatch(RegexConstants.REGEX_ID_CARD15, input);
    }

    /**
     * 验证身份证号码18位
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard18(CharSequence input) {
        return isMatch(RegexConstants.REGEX_ID_CARD18, input);
    }

    /**
     * 验证邮箱
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isEmail(CharSequence input) {
        return isMatch(RegexConstants.REGEX_EMAIL, input);
    }

    /**
     * 验证URL
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isURL(CharSequence input) {
        return isMatch(RegexConstants.REGEX_URL, input);
    }

    /**
     * 验证汉字
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isZh(CharSequence input) {
        return isMatch(RegexConstants.REGEX_ZH, input);
    }

    /**
     * 验证用户名
     * <p>取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位</p>
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isUsername(CharSequence input) {
        return isMatch(RegexConstants.REGEX_USERNAME, input);
    }

    /**
     * 验证yyyy-MM-dd格式的日期校验，已考虑平闰年
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isDate(CharSequence input) {
        return isMatch(RegexConstants.REGEX_DATE, input);
    }

    /**
     * 验证IP地址
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIP(CharSequence input) {
        return isMatch(RegexConstants.REGEX_IP, input);
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    /**
     * 获取正则匹配的部分
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return 正则匹配的部分
     */
    public static List<String> getMatches(String regex, CharSequence input) {
        if (input == null) return null;
        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }



    public static String replace5$(String sourceString, ArrayList<String> arrReplace) {
        return replace(sourceString, RegexConstants.STRING_5$, arrReplace);
    }

    /**
     * 将源字符串中每一个匹配的子串替换成arrReplace对应的字符串 例如： ArrayList<String> arrReplace = new ArrayList<String>(); arrReplace.add("#"); arrReplace.add("%"); System.out.println(replace("abcabc", "b", arrReplace));
     * 输出结果：a#ca%c
     *
     * @param sourceString
     *            源字符串
     * @param pattern
     *            正则表达式
     * @param arrReplace
     *            替换字符集
     * @return 替换后的字符串
     */
    public static String replace(String sourceString, String pattern, ArrayList<String> arrReplace) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(sourceString);
        StringBuffer sb = new StringBuffer();
        int count = 0;
        while (m.find()) {
            m.appendReplacement(sb, arrReplace.size() > count ? arrReplace.get(count++) : "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获取正则匹配分组
     *
     * @param input 要分组的字符串
     * @param regex 正则表达式
     * @return 正则匹配分组
     */
    public static String[] getSplits(String input, String regex) {
        if (input == null) return null;
        return input.split(regex);
    }

    /**
     * 替换正则匹配的第一部分
     *
     * @param input       要替换的字符串
     * @param regex       正则表达式
     * @param replacement 代替者
     * @return 替换正则匹配的第一部分
     */
    public static String getReplaceFirst(String input, String regex, String replacement) {
        if (input == null) return null;
        return Pattern.compile(regex).matcher(input).replaceFirst(replacement);
    }

    /**
     * 替换所有正则匹配的部分
     *
     * @param input       要替换的字符串
     * @param regex       正则表达式
     * @param replacement 代替者
     * @return 替换所有正则匹配的部分
     */
    public static String getReplaceAll(String input, String regex, String replacement) {
        if (input == null) return null;
        return Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }

    /**
     * str:有效的数字 字符 如：-5;5;-0.99;99.66 cout:小数位的个数 0 表示整数 ; >0 表示小数点后位数 ;-1 表示浮点小数 不控制位数
     */
    public static boolean validDecimal(String str, long count) {
        String reg;
        if (count == 0) {
            reg = "^-?\\d+$";// 整数
        } else if (count > 0) {
            // 刘永领 特殊情况判断：如果小数点后位数小于精度 认为合法 。根据具体客户要求 是否需要自动补0
            // 如:精度是3 客户录入 1.1 认为有效 如果是整数也有效
            try {
                int pos = str.indexOf(".");
                if ((str.substring(pos, str.length())).length() <= count)
                    return true;
            } catch (Exception e) {
                return true;
            }

            reg = "\\d+(\\.\\d{" + count + "})";
        } else if (count == -1) {
            reg = "\\d+(\\.\\d+)";
        } else {
            throw new RuntimeException("传入的小数位参数有误！");
        }
        if (str.matches(reg)) {
            return true;
        }
        return false;
    }

    public static boolean isNumber(String str) {
        if (str == null) {
            return true;
        }
        // Pattern pattern = Pattern.compile("[0-9]*");
        String regExp = "^(-|\\+)?\\d+(\\.\\d+)?$";
        Pattern pattern = Pattern.compile(regExp);
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否只是数字、字母和下划线，过滤掉特殊字符
     *
     * @param str
     * @return
     */
    public static boolean isNumberOrCharacter(String str) {
        if (str == null) {
            return false;
        }
        String regExp = "^[a-zA-Z0-9_]+$";
        Pattern pattern = Pattern.compile(regExp);
        return pattern.matcher(str).matches();
    }
}