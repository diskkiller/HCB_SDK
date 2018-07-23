package sdk.hecaibao.com.hcb_sdk.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yue
 * @date 2017/8/4 10:06
 *
 */
//已测试
public class StringRegex {
    /**
     * 是否以xxx结尾
     * @param content 字符串内容
     * @param endRegex 结尾的字符串的正则校验
     * @param insensitiveCase 是否忽略大小写
     * @return 是否匹配
     */
    public static boolean endWith(String content, String endRegex, boolean insensitiveCase ){
        StringBuilder sb = new StringBuilder();
        if (!endRegex.startsWith(".*")){
            sb.append(".*");
        }
        sb.append(endRegex);
        if (!endRegex.endsWith("$")){
            sb.append("$");
        }
        endRegex = sb.toString();
        Pattern pattern = getCasePattern(endRegex,insensitiveCase);
        Matcher m = pattern.matcher(content);
        return m.matches();
    }

    /**
     * 是否包含xxx
     * @param content
     * @param regex
     * @param insensitiveCase
     * @return
     */
    public static boolean contains(String content,String regex,boolean insensitiveCase){
        StringBuilder sb = new StringBuilder();
        if (!regex.startsWith(".*")){
            sb.append(".*");
        }
        sb.append(regex);
        if (!regex.endsWith(".*")){
            sb.append(".*");
        }
        regex = sb.toString();

        Pattern pattern = getCasePattern(regex,insensitiveCase);
        Matcher m = pattern.matcher(content);
        return m.matches();
    }

    /**
     * 是否以xxx开头
     * @param content
     * @param regex
     * @param insensitiveCase
     * @return
     */
    public static boolean startWith(String content,String regex,boolean insensitiveCase){
        StringBuilder sb = new StringBuilder();
        if (!regex.startsWith("^")){
            sb.append("^");
        }
        sb.append(regex);
        if (!regex.endsWith(".*")){
            sb.append(".*");
        }
        regex = sb.toString();
        Pattern pattern = getCasePattern(regex,insensitiveCase);
        Matcher matcher = pattern.matcher(content);
        return matcher.matches();
    }
    private static Pattern getCasePattern(String regex,boolean insensitiveCase){
        if (insensitiveCase){
            return Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        }else {
            return Pattern.compile(regex);
        }
    }
}
