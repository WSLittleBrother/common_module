package com.sunwayworld.utils.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/16
 *     desc  : 字符串相关工具类
 * </pre>
 */
public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isTrimEmpty(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断字符串是否为null或全为空白字符
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空白字符<br> {@code false}: 不为null且不全空白字符
     */
    public static boolean isSpace(String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return a == null ? b == null : a.equalsIgnoreCase(b);
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }




    /**
     * 将url中的特殊字符进行编码转换
     *
     * @param
     * @return
     * @exception
     */
    public static String encodeUrl(String url) {
        String result = url;

        result = result.replaceAll("\\?", "@");
        result = result.replaceAll("&", "-");

        return result;
    }

    /**
     * 将url中的特殊字符进行解码转换
     *
     * @param
     * @return
     * @exception
     */
    public static String decodeUrl(String decodeUrl) {
        String result = decodeUrl;

        result = result.replaceAll("@", "?");
        result = result.replaceAll("-", "&");

        return result;
    }

    public static long parseLong(String str) {
        try {

            return Long.parseLong(str);
        } catch (Exception ex) {
            return 0L;
        }
    }

    public static int parseInt(String str) {
        try {

            return Integer.parseInt(str);
        } catch (Exception ex) {
            return 0;
        }
    }
    public static int parseInt(String str,int i) {
        try {

            return Integer.parseInt(str);
        } catch (Exception ex) {
            return i;
        }
    }

    public static double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj.getClass().getName().equals("java.lang.String")) {
            return toString((String) obj).trim();
        }
        if (obj.getClass().getName().equals("java.lang.Integer")) {
            return toString((Integer) obj).trim();
        }
        if (obj.getClass().getName().equals("java.lang.Long")) {
            return toString((Long) obj).trim();
        }
        if (obj.getClass().getName().equals("java.sql.Date")) {
            return toString((Date) obj).trim();
        }
        if (obj.getClass().getName().equals("java.util.Date")) {
            return toString((java.util.Date) obj).trim();
        }
        if (obj.getClass().getName().equals("java.lang.Float")) {
            return toString((Float) obj).trim();
        }
        if (obj.getClass().getName().equals("java.sql.Timestamp")) {
            return toString((Timestamp) obj).trim();
        }
        if (obj.getClass().getName().equals("java.lang.Double")) {
            return toString((Double) obj).trim();
        }

        return obj.toString().trim();
    }

    // 字符串 不启用trim
    public static String toString_alias(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj.getClass().getName().equals("java.lang.String")) {
            return toString((String) obj);
        }
        if (obj.getClass().getName().equals("java.lang.Integer")) {
            return toString((Integer) obj).trim();
        }
        if (obj.getClass().getName().equals("java.lang.Long")) {
            return toString((Long) obj).trim();
        }
        if (obj.getClass().getName().equals("java.sql.Date")) {
            return toString((Date) obj).trim();
        }
        if (obj.getClass().getName().equals("java.util.Date")) {
            return toString((java.util.Date) obj).trim();
        }
        if (obj.getClass().getName().equals("java.lang.Float")) {
            return toString((Float) obj).trim();
        }
        if (obj.getClass().getName().equals("java.sql.Timestamp")) {
            return toString((Timestamp) obj).trim();
        }
        if (obj.getClass().getName().equals("java.lang.Double")) {
            return toString((Double) obj).trim();
        }

        return obj.toString();
    }

    public static String toCSV(Object obj) {
        String str = toString(obj);
        return str.replaceAll("\"", "\"\"").replaceAll("\n", "").replaceAll("\r", "");
    }

    public static String toString(int obj) {
        return String.valueOf(obj);
    }

    public static String toString(long obj) {
        return String.valueOf(obj);
    }

    public static String toString(double obj) {
        return String.valueOf(obj);
    }

    public static String toString(float obj) {
        return String.valueOf(obj);
    }

    public static String toString(boolean obj) {
        return String.valueOf(obj);
    }

    public static String toString(char obj) {
        return String.valueOf(obj);
    }

    private static String toString(String obj) {
        if (obj == null) {
            return "";
        }
        return obj;
    }

    private static String toString(Integer obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    private static String toString(Long obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    private static String toString(Date obj) {
        if (obj == null) {
            return "";
        }

        DateFormat dftime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = dftime.format(obj);
        if (str.indexOf("00:00:00") < 0) {
            return str;
        } else {
            DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return df.format(obj);
        }
    }

    private static String toString(Double obj) {
        if (obj == null) {
            return "";
        }
        // liuyl fix bug .old BigDecimal return like 2.45459999999999999999999999999999999999999345345
        return obj.doubleValue() + "";
        // BigDecimal bd = new BigDecimal(obj);
        // return bd.toString();
    }

    private static String toString(Float obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    private static String toString(Timestamp obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    /**
     * 在 source 后边填充 target 总长len (target 单字符)
     *
     * @param source
     * @param target
     * @param len
     * @return
     */
    public static String fillAfter(String source, String target, int len) {
        StringBuffer bf = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            bf.append(target);
        }
        String temp = bf.toString();
        temp = temp.substring(source.length(), len);
        return source + temp;
    }

    /**
     * 在 source 前边填充 target (target 单字符),填充完后总长度为len
     *
     * @param source
     * @param target
     * @param len
     * @return
     */
    public static String fillBefore(String source, String target, int len) {
        StringBuffer bf = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            bf.append(target);
        }
        String temp = bf.toString();
        temp = temp.substring(0, len - source.length());
        return temp + source;
    }

    public static String tofirstUpperCase(String source, int index) {
        String temp = source.substring(0, index);
        source = temp.toUpperCase() + source.substring(index, source.length());
        return source;
    }

    public static String toOnlyfirstUpperCase(String source, int index) {
        String temp = source.substring(0, index);
        source = temp.toUpperCase() + source.substring(index, source.length()).toLowerCase();
        return source;
    }

    public static String tofirstLowerCase(String source, int index) {
        String temp = source.substring(0, index);
        source = temp.toLowerCase() + source.substring(index, source.length());
        return source;
    }

    public static String and(String value, String target) {

        BigInteger _value = new BigInteger(value, 2);
        BigInteger _target = new BigInteger(target, 2);

        return StringUtils.fillBefore(Integer.toBinaryString(_value.and(_target).intValue()), "0", value.length());
    }

    public static String or(String value, String target) {
        BigInteger _value = new BigInteger(value, 2);
        BigInteger _target = new BigInteger(target, 2);
        return StringUtils.fillBefore(Integer.toBinaryString(_value.or(_target).intValue()), "0", value.length());
    }

    public static String round(double d, int n) {
        if (d == 0) {
            d = 0;
        }
        if (n < 0) {
            n = 0;
        }
        String str = "";
        if (n == 0) {
            str = "0";
        } else {
            str = "0.";
        }
        for (int i = 0; i < n; i++) {
            str = str + "0";
        }
        DecimalFormat formater = new DecimalFormat(str);
        BigDecimal b = new BigDecimal(d + "");
        double tempd = b.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();

        if (tempd == 0) {
            tempd = 0;
        }
        return formater.format(tempd);
    }

    public static boolean isEmpty(String str) {
        return "".equals(StringUtils.toString(str));
    }

    public static boolean isNotEmpty(String str) {
        return !"".equals(StringUtils.toString(str));
    }

    // 全替换字符串
    public static String replaceAll(String strSource, String strFrom, String strTo) {
        if (strSource == null) {
            return null;
        }
        int i = 0;
        if ((i = strSource.indexOf(strFrom, i)) >= 0) {
            char[] cSrc = strSource.toCharArray();
            char[] cTo = strTo.toCharArray();
            int len = strFrom.length();
            StringBuffer buf = new StringBuffer(cSrc.length);
            buf.append(cSrc, 0, i).append(cTo);
            i += len;
            int j = i;
            while ((i = strSource.indexOf(strFrom, i)) > 0) {
                buf.append(cSrc, j, i - j).append(cTo);
                i += len;
                j = i;
            }
            buf.append(cSrc, j, cSrc.length - j);
            return buf.toString();
        }
        return strSource;
    }

    public static String hiddenCenter(String str, String replaceStr, int len) {
        int length = toString(str).length();
        if (length >= len * 2 + 1) {
            return str.substring(0, len + 1) + str.substring(len + 1, length - len).replaceAll(".", replaceStr) + str.substring(length - len);
        }
        return "";
    }

    /**
     * 处理oracle contains(全文检索) 转义特殊字符
     *
     * @param sql
     * @return
     */
    public static String escapeSpecialCharsInOralceText(String sql) {
        String[] specialcharacters = { "-", "&", "|", "!", "(", ")", "{", "}", "[", "]", "^", "\"", "~", "*", "?", ":", "'" };
        for (String str : specialcharacters) {
            sql = sql.replace(str, "\\" + str);
        }
        return sql;
    }

    /**
     * 去除字符串数组中重复的值
     *
     * @param source
     * @return String [] str
     * 例如：source: a,b,c,d,a
     * 		String [] str: [a,b,c,d]
     */
    public static String [] removeRepeatString(String source){
        String[] target = source.split(",");
        Set<String> set = new TreeSet<String>();
        for (int i = 0; i < target.length; i++) {
            set.add(target[i]);
        }
        target = (String[]) set.toArray(new String[0]);
        return target;
    }

    /**
     * 通过正则过滤html标签
     *
     * @param htmlStr
     * @return
     */
    public static String filterHtmlTag(String htmlStr){
        String  regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        Pattern p_html  = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签
        return htmlStr;
    }


    /**
     * 判断Key是否存在与数组中
     *
     * @return
     */
    public static boolean isInclude(String[] keys,String key){
        for(String _key:keys){
            if(_key.equals(key)){
                return true;
            }
        }
        return false;
    }

    public static int getInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static double getdouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static long getlong(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static float getfloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (Exception ex) {
            return 0;
        }

    }

    public static boolean getboolean(String str) {
        try {
            return Boolean.getBoolean(str);
        } catch (Exception ex) {
            return false;
        }

    }

    public static Boolean getBoolean(String str) {
        try {
            return Boolean.valueOf(str);
        } catch (Exception ex) {
            return null;
        }

    }

    public static String getString(String str) {
        if (str == null) {
            return null;
        }
        return str;
    }

    public static Integer getInteger(String str) {
        try {
            if (str == null || str.equals("")) {
                return null;
            }

            return new Integer(str);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Float getFloat(String str) {
        try {
            if (str == null || str.equals("")) {
                return null;
            }

            return new Float(str);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Long getLong(String str) {
        try {
            if (str == null || str.equals("")) {
                return null;
            }

            return new Long(str);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Long getLongByFloat(String str) {
        try {
            if (str == null || str.equals("")) {
                return null;
            }
            return ((Float) Float.parseFloat(str)).longValue();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static Date getSqlDate(String str) {
        try {
            if (str == null || str.equals("")) {
                return null;
            }
            DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date tempDate = df.parse(str);
            return new Date(tempDate.getTime());
        } catch (ParseException ex) {
            return null;
        }
    }

    public static java.util.Date getUtilDate(String str) {
        try {

            if (str == null || str.trim().equals("")) {
                return null;
            }
            DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date tempDate = df.parse(str);
            return tempDate;
        } catch (ParseException ex) {
            return null;
        }
    }

    public static Double getDouble(String str) {
        try {
            if (str == null || str.equals("")) {
                return null;
            }
            return new Double(str);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static Timestamp getTimestamp(String str) {
        try {
            if (str == null || str.equals("")) {
                return null;
            }
            if (str.length() == 10) {
                DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");// DateFormat.getDateInstance(DateFormat.MEDIUM,
                // java.util.Locale.CHINA);
                java.util.Date tempDate = df.parse(str);
                return new Timestamp(tempDate.getTime());
            }
            DateFormat df = DateFormat.getDateTimeInstance();
            java.util.Date tempDate = df.parse(str);
            return new Timestamp(tempDate.getTime());
        } catch (ParseException ex) {
            return null;
        }
    }

}