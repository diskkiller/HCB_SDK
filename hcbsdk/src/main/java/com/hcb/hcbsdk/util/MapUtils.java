package com.hcb.hcbsdk.util;

/**
 * @author Administrator
 * @time 2016/9/22 0022 11:17
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class MapUtils {


    public static double toBaiDu_lng(double lng){
        return lng+0.0065;
    }

    public static double toBaiDu_lat(double lat){
        return lat+0.0060;
    }

    public static double toGaoDe_lng(double lng){
        return lng-0.0065;
    }

    public static double toGaoDe_lat(double lat){
        return lat-0.0060;
    }
}
