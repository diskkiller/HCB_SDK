package com.hcb.hcbsdk.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangguowei
 * @ClassName: SYStringUtils
 * @Description: 工具类
 * @date 15/10/28 上午10:45
 */
public class StringUtils {


    public final static String UTF_8 = "utf-8";

    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value
                .trim())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true
     */
    public static boolean isEquals(String... agrs) {
        String last = null;
        for (int i = 0; i < agrs.length; i++) {
            String str = agrs[i];
            if (isEmpty(str)) {
                return false;
            }
            if (last != null && !str.equalsIgnoreCase(last)) {
                return false;
            }
            last = str;
        }
        return true;
    }

    /**
     * 返回一个高亮spannable
     *
     * @param content 文本内容
     * @param color   高亮颜色
     * @param start   起始位置
     * @param end     结束位置
     * @return 高亮spannable
     */
    public static CharSequence getHighLightText(String content, int color, int start, int end) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        start = start >= 0 ? start : 0;
        end = end <= content.length() ? end : content.length();
        SpannableString spannable = new SpannableString(content);
        CharacterStyle span = new ForegroundColorSpan(color);
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }


    /**
     * 格式化文件大小，不保留末尾的0
     */
    public static String formatFileSize(long len) {
        return formatFileSize(len, false);
    }

    /**
     * appkey = md5(用户token + '_'GetUserRelativesStatistics + API_KEY)
     * API_KEY为api接口秘钥，值为：zhongxiangjia-jeif8
     *
     * @param token
     * @return
     */
    public static String getAppkey(String token) {

         L.debug("123","token:::"+token);
        try {
            return encryptMD5(token + "_zhongxiangjia-jeif8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptMD5(String data) throws Exception {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data.getBytes());
        return toHex(md5.digest());
    }

    public static String toHex(byte[] buffer) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }
        return sb.toString();
    }

    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4',
                '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes("UTF-8");
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md = mdInst.digest();
            //把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化文件大小，保留末尾的0，达到长度一致
     */
    public static String formatFileSize(long len, boolean keepZero) {
        String size;
        DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
        DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
        if (len < 1024) {
            size = String.valueOf(len + "B");
        } else if (len < 10 * 1024) {
            // [0, 10KB)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
        } else if (len < 100 * 1024) {
            // [10KB, 100KB)，保留一位小数
            size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
        } else if (len < 1024 * 1024) {
            // [100KB, 1MB)，个位四舍五入
            size = String.valueOf(len / 1024) + "KB";
        } else if (len < 10 * 1024 * 1024) {
            // [1MB, 10MB)，保留两位小数
            if (keepZero) {
                size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / 1024 / (float)
                        100)) + "MB";
            } else {
                size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100) + "MB";
            }
        } else if (len < 100 * 1024 * 1024) {
            // [10MB, 100MB)，保留一位小数
            if (keepZero) {
                size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float)
                        10)) + "MB";
            } else {
                size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
            }
        } else if (len < 1024 * 1024 * 1024) {
            // [100MB, 1GB)，个位四舍五入
            size = String.valueOf(len / 1024 / 1024) + "MB";
        } else {
            // [1GB, ...)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
        }
        return size;
    }


    public static String md5(String input) {

        String result = null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            result = String.format("%032x", number);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result;
    }

    /***
     * MD5加密 生成32位md5码
     *
     * @param //待加密字符串
     * @return 返回32位md5码
     */
    public static String md5Encode(String inStr) throws Exception {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static byte[] getK(byte[] k) {
        byte[] ks = new byte[16];
        for (int i = 0; i < k.length; i++) {
            ks[i] = k[i];
        }
        for (int i = k.length; i < ks.length; i++) {
            ks[i] = 0;
        }
        return ks;
    }

    public static byte[] getEncrytedData(byte[] data) {
        int a = data.length % 16;
        if (a != 0) {
            byte[] ks = new byte[data.length + 16 - a];
            for (int i = 0; i < data.length; i++) {
                ks[i] = data[i];
            }
            for (int i = data.length; i < data.length + 16 - a; i++) {
                ks[i] = 0;
            }
            return ks;
        } else {
            return data;
        }


    }

    public static String getRealData(byte[] decryptedByteArray) {
        int index = 0;
        for (int i = 0; i < decryptedByteArray.length; i++) {
            if (decryptedByteArray[i] == 0) {
                index = i;
                break;
            }
        }

        byte[] r = new byte[index];
        for (int i = 0; i < r.length; i++) {
            r[i] = decryptedByteArray[i];
        }

        return new String(r);
    }

    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static File saveFile(Bitmap bm, String fileName) throws IOException {
        String path = Environment.getExternalStorageDirectory() + "/jaaaja/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();

        return myCaptureFile;
    }


    /**
     * 保存图片缓存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static File saveCachePicFile(Bitmap bm, String fileName) throws IOException {
        String path = Environment.getExternalStorageDirectory() + "/jaaaja_cache_imageloader/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();

        return myCaptureFile;
    }


    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static File saveFileNoCompress(Bitmap bm, String fileName) throws IOException {
        String path = Environment.getExternalStorageDirectory() + "/jaaaja/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bos.flush();
        bos.close();

        return myCaptureFile;
    }


    public static String decode(String bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        //将每2位16进制整数组装成一个字节
        String hexString = "0123456789ABCDEF";
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt
                    (i + 1))));
        return new String(baos.toByteArray());
    }

    public static String hex2Str(String str) {
        String strArr[] = str.split("\\\\"); // 分割拿到形如 xE9 的16进制数据
        byte[] byteArr = new byte[strArr.length - 1];
        for (int i = 1; i < strArr.length; i++) {
            Integer hexInt = Integer.decode("0" + strArr[i]);
            byteArr[i - 1] = hexInt.byteValue();
        }

        try {
            return new String(byteArr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String deUnicode(String content) {//将16进制数转换为汉字
        String enUnicode = null;
        String deUnicode = null;
        for (int i = 0; i < content.length(); i++) {
            if (enUnicode == null) {
                enUnicode = String.valueOf(content.charAt(i));
            } else {
                enUnicode = enUnicode + content.charAt(i);
            }
            if (i % 4 == 3) {
                if (enUnicode != null) {
                    if (deUnicode == null) {
                        deUnicode = String.valueOf((char) Integer.valueOf(enUnicode, 16).intValue
                                ());
                    } else {
                        deUnicode = deUnicode + String.valueOf((char) Integer.valueOf(enUnicode,
                                16).intValue());
                    }
                }
                enUnicode = null;
            }

        }
        return deUnicode;
    }

    public static String enUnicode(String content) {//将汉字转换为16进制数
        String enUnicode = null;
        for (int i = 0; i < content.length(); i++) {
            if (i == 0) {
                enUnicode = getHexString(Integer.toHexString(content.charAt(i)).toUpperCase());
            } else {
                enUnicode = enUnicode + getHexString(Integer.toHexString(content.charAt(i))
                        .toUpperCase());
            }
        }
        return enUnicode;
    }

    private static String getHexString(String hexString) {
        String hexStr = "";
        for (int i = hexString.length(); i < 4; i++) {
            if (i == hexString.length())
                hexStr = "0";
            else
                hexStr = hexStr + "0";
        }
        return hexStr + hexString;
    }

    public static String hexToStringGBK(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
            return "";
        }
        return s;
    }

    public static String hanzi2Hex(String content) {
        String str = content;
        String hexString = "0123456789ABCDEF";
        StringBuilder sb = null;
        byte[] bytes;
        try {
            bytes = str.getBytes("utf-8");//如果此处不加编码转化，得到的结果就不是理想的结果，中文转码
            sb = new StringBuilder(bytes.length * 2);

            for (int i = 0; i < bytes.length; i++) {
                sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
                sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
                //sb.append("");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String result = sb.toString();
        return result;
    }

    /**
     * 自定义格式时间戳转换
     *
     * @param beginDate
     * @return
     */
    public static String timestampToDate(String beginDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sd = sdf.format(new Date(Long.parseLong(beginDate)*1000));
        return sd;
    }

    /**
     * 将字符串转为时间戳
     *
     * @param date
     * @return
     */
    public static String ChangDataTime(String date) {
        String re_time = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = sdf.parse(date);
            long l = d.getTime()/1000;
            re_time = String.valueOf(l);
            //            re_time = re_time.substring(0, 9);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }
}
