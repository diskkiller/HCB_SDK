package com.hcb.hcbsdk.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by WangGuoWei on 2018/6/26
 */
public class CheckIdCard {
    public static String errorInfo;

    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr
     *            身份证号
     * @return true 有效：false 无效
     *
     */
    public static boolean IDCardValidate(String IDStr) {
        String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
        String Ai = "";
        // ================ 号码的长度18位 ================
        if (IDStr.length() != 18) {
            if(IDStr.length() != 15){
                errorInfo = "身份证长度不合法";
                return false;
            }else{
                String newIDStr = getNewIDCard(IDStr);
                //15位身份证转换失败
                if(newIDStr.equals(IDStr) ){
                    errorInfo = "身份证无效，不是合法的身份证号码";
                    return false;
                }
                IDStr = newIDStr;
            }
        }


        if (IDStr.length() != 18) {
            errorInfo = "身份证无效，不是合法的身份证号码";
            return false;
        }

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return false;
        }
        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 日
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return false;
        }
        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return false;
        }
        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return false;
            }
        } else {
            return true;
        }
        return true;
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    @SuppressWarnings("unchecked")
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
//      hashtable.put("71", "台湾");
//      hashtable.put("81", "香港");
//      hashtable.put("82", "澳门");
//      hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param strDate
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }




    //    位权值数组
    private static byte[] Wi=new byte[17];
    //    身份证前部分字符数
    private static final byte fPart = 6;
    //    身份证算法求模关键值
    private static final byte fMod = 11;
    //    旧身份证长度
    private static final byte oldIDLen = 15;
    //    新身份证长度
    private static final byte newIDLen = 18;
    //    新身份证年份标志
    private static final String yearFlag = "19";
    //    校验码串
    private static final String CheckCode="10X98765432";
    //    最小的行政区划码
    private static final int minCode = 150000;
    //    最大的行政区划码
    private static final int maxCode = 700000;
//    旧身份证号码
//    private String oldIDCard="";
//    新身份证号码
//    private String newIDCard="";
//    地区及编码


    //private String Area[][2] =
    private static void setWiBuffer(){
        for(int i=0;i<Wi.length;i++){
            int k = (int) Math.pow(2, (Wi.length-i));
            Wi[i] = (byte)(k % fMod);
        }
    }

    //获取新身份证的最后一位:检验位
    private static String getCheckFlag(String idCard){
        int sum = 0;
        //进行加权求和
        for(int i=0; i<17; i++){
            sum += Integer.parseInt(idCard.substring(i,i+1)) * Wi[i];
        }
        //取模运算，得到模值
        byte iCode = (byte) (sum % fMod);
        return CheckCode.substring(iCode,iCode+1);
    }

    //判断串长度的合法性
    private static boolean checkLength(final String idCard,boolean newIDFlag){
        boolean right = (idCard.length() == oldIDLen) || (idCard.length() == newIDLen);
        newIDFlag = false;
        if(right){
            newIDFlag = (idCard.length() == newIDLen);
        }
        return right;
    }

    //获取时间串
    private static String getIDDate(final String idCard,boolean newIDFlag){
        String dateStr = "";
        if(newIDFlag)
            dateStr = idCard.substring(fPart,fPart+8);
        else
            dateStr = yearFlag + idCard.substring(fPart,fPart+6);
        return dateStr;
    }

    //判断时间合法性
    private static boolean checkDate(final String dateSource){
        String dateStr = dateSource.substring(0,4)+"-"+dateSource.substring(4,6)+"-"+dateSource.substring(6,8);
        System.out.println(dateStr);
        DateFormat df = DateFormat.getDateInstance();
        df.setLenient(false);
        try {
            Date date= df.parse(dateStr);
            return (date!=null);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return false;
        }
    }

    //旧身份证转换成新身份证号码
    public static String getNewIDCard(final String oldIDCard){
        //初始化方法
        setWiBuffer();
        if(!checkIDCard(oldIDCard)){
            return oldIDCard;
        }
        String newIDCard = oldIDCard.substring(0, fPart);
        newIDCard += yearFlag;
        newIDCard += oldIDCard.substring(fPart, oldIDCard.length());
        String ch = getCheckFlag(newIDCard);
        newIDCard += ch;
        return newIDCard;
    }

    //新身份证转换成旧身份证号码
    public static String getOldIDCard(final String newIDCard){
        //初始化方法
        setWiBuffer();
        if(!checkIDCard(newIDCard)){
            return newIDCard;
        }
        String oldIDCard = newIDCard.substring(0,fPart)+
                newIDCard.substring(fPart+yearFlag.length(),newIDCard.length()-1);
        return oldIDCard;
    }

    //判断身份证号码的合法性
    public static boolean checkIDCard(final String idCard){
        //初始化方法
        setWiBuffer();
        boolean isNew = false;
        //String message = "";
        if (!checkLength(idCard,isNew)){
            //message = "ID长度异常";
            return false;
        }
        String idDate = getIDDate(idCard, isNew);
        if(!checkDate(idDate)){
            //message = "ID时间异常";
            return false;
        }
        if(isNew){
            String checkFlag = getCheckFlag(idCard);
            String theFlag = idCard.substring(idCard.length()-1,idCard.length());
            if(!checkFlag.equals(theFlag)){
                //message = "新身份证校验位异常";
                return false;
            }
        }
        return true;
    }

    //获取一个随机的"伪"身份证号码
    public static String getRandomIDCard(final boolean idNewID){
        //初始化方法
        setWiBuffer();
        Random ran = new Random();
        String idCard = getAddressCode(ran)+getRandomDate(ran,idNewID)+getIDOrder(ran);
        if(idNewID){
            String ch = getCheckFlag(idCard);
            idCard += ch;
        }
        return idCard;
    }

    //产生随机的地区编码
    private static String getAddressCode(Random ran) {
        if(ran==null){
            return "";
        }else{
            int addrCode = minCode + ran.nextInt(maxCode-minCode);
            return Integer.toString(addrCode);
        }
    }



    //产生随机的出生日期
    private static String getRandomDate(Random ran, boolean idNewID) {
        // TODO Auto-generated method stub
        if(ran==null){
            return "";
        }
        int year = 0;
        if(idNewID){
            year = 1900 + ran.nextInt(2007-1900);
        }else{
            year = 1 + ran.nextInt(99);
        }
        int month = 1+ran.nextInt(12);
        int day = 0;
        if(month==2){
            day= 1+ran.nextInt(28);
        }else if(month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12){
            day= 1+ran.nextInt(31);
        }else{
            day= 1+ran.nextInt(30);
        }
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMaximumIntegerDigits(2);
        nf.setMinimumIntegerDigits(2);
        String dateStr = Integer.toString(year)+nf.format(month)+nf.format(day);
        return dateStr;
    }

    //产生随机的序列号
    private static String getIDOrder(Random ran) {
        // TODO Auto-generated method stub
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMaximumIntegerDigits(3);
        nf.setMinimumIntegerDigits(3);
        if(ran==null){
            return "";
        }else{
            int order = 1+ran.nextInt(999);
            return nf.format(order);
        }
    }

}
