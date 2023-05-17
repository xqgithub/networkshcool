package com.talkcloud.networkshcool.baselibrary.utils;

import android.content.Context;

import com.talkcloud.networkshcool.baselibrary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringUtil {
    /**
     * 获取时间戳差 对应的时间
     *
     * @return
     */
    public static String getStringTimeCountdown(long targetTime, long curTime) {
        long timelong = targetTime - curTime;
        String time;
    /*    if (timelong < 3600) {
            time = String.format("%1$02d:%2$02d", timelong / 60, timelong % 60);
        } else {
            time = String.format("%1$d:%2$02d:%3$02d", timelong / 3600, timelong % 3600 / 60, timelong % 60);
        }*/
        time = String.format("%1$02d:%2$02d:%3$02d", timelong / 3600, timelong % 3600 / 60, timelong % 60);
        return time;
    }


    /*  将时间戳转换为时间  */
    public static String stampToDate(long timeSeconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeSeconds * 1000);  //参数是秒，需要毫秒转换
        return simpleDateFormat.format(date);
    }

    /*  将时间戳转换为时间  */
    public static String stampToMonth(long timeSeconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date(timeSeconds * 1000);  //参数是秒，需要毫秒转换
        return simpleDateFormat.format(date);
    }

    /*  将时间戳转换为时间  */
    public static String stampToTime(long timeSeconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(timeSeconds * 1000);  //参数是秒，需要毫秒转换
        return simpleDateFormat.format(date);
    }


    //两个时间戳是否是同一天 时间戳是long型的（11或者13）
    public static boolean isSameData(String currentTime, String lastTime) {
        try {
            Calendar nowCal = Calendar.getInstance();
            Calendar dataCal = Calendar.getInstance();
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            Long nowLong = new Long(currentTime);
            Long dataLong = new Long(lastTime);
            String data1 = df1.format(nowLong);
            String data2 = df2.format(dataLong);
            java.util.Date now = df1.parse(data1);
            java.util.Date date = df2.parse(data2);
            nowCal.setTime(now);
            dataCal.setTime(date);
            return isSameDay(nowCal, dataCal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                    && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                    && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        } else {
            return false;
        }
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }


    /**
     * 获取系统当前时间
     *
     * @return curTime
     */
    public static String getCurTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String curTime = formatter.format(curDate);
        return curTime;
    }

    //毫秒换成00:00:00
    public static String getCountTimeByLong(long finishTime) {
        int totalTime = (int) (finishTime / 1000);//秒
        int hour = 0, minute = 0, second = 0;

        if (3600 <= totalTime) {
            hour = totalTime / 3600;
            totalTime = totalTime - 3600 * hour;
        }
        if (60 <= totalTime) {
            minute = totalTime / 60;
            totalTime = totalTime - 60 * minute;
        }
        if (0 <= totalTime) {
            second = totalTime;
        }
        StringBuilder sb = new StringBuilder();

        if (hour < 10) {
            sb.append("0").append(hour).append(":");
        } else {
            sb.append(hour).append(":");
        }
        if (minute < 10) {
            sb.append("0").append(minute).append(":");
        } else {
            sb.append(minute).append(":");
        }
        if (second < 10) {
            sb.append("0").append(second);
        } else {
            sb.append(second);
        }
        return sb.toString();

    }

    //毫秒换成10'50''
    public static String getTimeByLong(long finishTime, Context context) {
        int totalTime = (int) (finishTime);//秒
        int minute = 0, second = 0;


        if (60 <= totalTime) {
            minute = totalTime / 60;
            totalTime = totalTime - 60 * minute;
        }
        if (0 <= totalTime) {
            second = totalTime;
        }
        StringBuilder sb = new StringBuilder();


        sb.append(minute).append(context.getResources().getString(R.string.minitefh));
        if (second < 10) {
            sb.append("0").append(second).append(context.getResources().getString(R.string.secondsfh));
        } else {
            sb.append(second).append(context.getResources().getString(R.string.secondsfh));
        }
        return sb.toString();

    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;

    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    /**
     * 按照毫秒来存储
     *
     * @param time
     * @return
     */
    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return "未知时间";
        }

        final long diff = now - time;

        if (diff < MINUTE_MILLIS) {
            return "刚刚";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1分钟前";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + "分钟前";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1小时前";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + "小时前";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "昨天";
        } else {
            return diff / DAY_MILLIS + "天前";
        }
    }
}