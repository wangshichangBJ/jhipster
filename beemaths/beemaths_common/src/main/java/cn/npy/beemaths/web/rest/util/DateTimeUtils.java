package cn.npy.beemaths.web.rest.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 功能:通用的静态工具函数(用于日期和时间处理)
 */
public class DateTimeUtils {

    public final static String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public final static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public final static String DATETIME_FORMAT_HHMMSS = "HHmmss";
    public final static String DATETIME_FORMAT_HHMMSS_SSS = "HHmmssSSS";
    public final static String DATETIME_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public final static String DATETIME_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String DATETIME_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public final static String DATETIME_FORMAT_YYYYMMDDHH = "yyyyMMddHH";
    public final static String DATETIME_FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    public final static String DATETIME_FORMAT_YYYY = "yyyy";
    public final static String DATETIME_FORMAT_YYYY_MM_DD_CN = "yyyy年MM月dd日";
    public final static String DATETIME_FORMAT_HH_MM_SS = "HH:mm:ss";
    public final static String DATE_FORMAT_YYYYPOINTMMPOINTDD = "yyyy.MM.dd";
    public final static String DATETIME_FORMAT_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss,SSS";
    public final static String DATE_FORMAT_YYMMDD = "yyMMdd";
    public final static String DATETIME_FORMAT_YYYYTMM_DD_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
    public final static String DATE_FORMAT_YYYYMM = "yyyyMM";
    public final static String DATE_FORMAT_YYYYMMDD_HH_MM_SS = "yyyyMMdd HH:mm:ss";
    public final static String DATE_FORMAT_MMDD = "MMdd";
    public final static String DATETIME_FORMAT_MMDDHHMMSS = "MMddHHmmss";

    /**
     * 比较两个时间大小
     *
     * @param first
     * @param second
     * @return <0: first<second
     * =0: first=second
     * >0: first>second
     */
    public static int compareTwoDate(Date first, Date second) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(first);
        c2.setTime(second);

        return c1.compareTo(c2);
    }

    /**
     * 获得当前时间字符串
     *
     * @param formatStr 日期格式
     * @return string yyyy-MM-dd 等
     */
    public static String getNowDateStr(String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(getNowDate());
    }

    /**
     * 获得系统当前时间
     *
     * @return Date
     */
    public static Date getNowDate() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 把日期按照指定格式的转化成字符串
     *
     * @param date      日期对象
     * @param formatStr 日期格式
     * @return 字符串式的日期, 格式为：yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTimeToString(Date date, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }

    /**
     * 把日期字符串转化成指定格式的日期对象
     *
     * @param dateStr   日期字符串
     * @param formatStr 日期格式
     * @return Date类型的日期
     * @throws Exception
     */
    public static Date getStringToDateTime(String dateStr, String formatStr) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.parse(dateStr);
    }

    /**
     * 把日期字符串转化成指定格式的日期字符串
     *
     * @param date       日期字符串
     * @param oldPattern 原日期格式
     * @param newPattern 新日期格式
     * @return String类型的日期
     * @throws Exception
     */
    public static String getStringToString(String date, String oldPattern, String newPattern) throws Exception {
        if (date == null || oldPattern == null || newPattern == null) {
            return "";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern);
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern);
        // 将给定的字符串中的日期提取出来
        Date d = sdf1.parse(date);
        return sdf2.format(d);
    }

    public static boolean isToday(Date theDay) {
        Calendar cNow = Calendar.getInstance();
        int iYear = cNow.get(Calendar.YEAR);
        int iDay = cNow.get(Calendar.DAY_OF_YEAR);

        Calendar cDay = Calendar.getInstance();
        cDay.setTime(theDay);
        int iTheYear = cDay.get(Calendar.YEAR);
        int iTheDay = cDay.get(Calendar.DAY_OF_YEAR);

        return (iTheYear == iYear) && (iTheDay == iDay);
    }

    /**
     * 计算两个日期之间的相差的天数. 计算方式：second - first
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差的天数
     */
    public static int daysBetween(Date smdate, Date bdate) throws Exception {
        int result = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long betweenDays = (time2 - time1) / (1000 * 3600 * 24);
        result = Integer.parseInt(String.valueOf(betweenDays));
        return result;
    }

    /**
     * 将时间戳转换为date
     *
     * @param seconds 时间戳
     * @return
     */
    public static Date getTimestampToDate(long seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(seconds);

        return calendar.getTime();
    }

    /**
     * 获取系统当前日期的前一天
     *
     * @return Date
     */
    public static Date beforeCurrentDate(String formatStr) throws Exception {
        Date ret = null;
        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        ret = formatter.parse(formatter.format(calendar.getTime()));
        return ret;
    }

    /**
     * 根据出生日期获取年龄
     * @param birthDay 出生日期
     * @return
     * @throws Exception
     */
    public static  int getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }
}
