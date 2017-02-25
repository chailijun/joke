package com.chailijun.joke.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    /**
     * 获取当前unix时间戳字符串
     * @return
     */
    public static String getTimeStamp(){
        return System.currentTimeMillis() / 1000 +"";
    }

    /**
     * 广告屏蔽
     * @param showTimeStr
     * @return true:展示
     */
    public static boolean isShowTime(String showTimeStr){
        Date  date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).parse(showTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null){
            long time = date.getTime();
            if (time - System.currentTimeMillis() < 0){
                return true;
            }else {
                return false;
            }
        }else {
            return true;
        }

    }
}
