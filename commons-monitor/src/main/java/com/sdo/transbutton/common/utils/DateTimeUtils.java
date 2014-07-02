/**
 * 
 */
package com.sdo.transbutton.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.sdo.transbutton.common.exception.SystemException;

/**
 * 日期/时间工具类
 * @description 提供有关日期/时间的常用静态操作方法
 * @author Lincoln
 */

public class DateTimeUtils {

	/**
	 * 日期格式:数据库日期格式(yyyyMMdd)
	 */
	public static SimpleDateFormat FORMAT_DATE_DB = new SimpleDateFormat("yyyyMMdd");

	/**
	 * 日期格式:时间格式(HHmmss)
	 */
	public static SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("HHmmss");

	/**
	 * 日期格式:小时分钟格式(HHmm)
	 */
	public static SimpleDateFormat FORMAT_HOUR_MINUTE = new SimpleDateFormat("HHmm");

	/**
	 * 日期格式：时间格式(HH:mm:ss)
	 */
	public static SimpleDateFormat FORMAT_TIME_PAGE = new SimpleDateFormat("HH:mm:ss");

	/**
	 * 日期格式:页面日期格式(yyyy-MM-dd)
	 */
	public static SimpleDateFormat FORMAT_DATE_PAGE = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 日期格式:银行日期时间格式(yyyyMMddHHmmss)
	 */
	public static SimpleDateFormat FORMAT_DATETIME_BACKEND = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 日期格式:本地日期明码格式(yyyy年MM月dd HH:mm:ss)
	 */
	public static SimpleDateFormat FORMAT_LOCAL = new SimpleDateFormat("yyyy年MM月dd HH:mm:ss");

	/**
	 * 日期格式:本地日期明码格式(yyyy-MM-dd HH:mm:ss)
	 */
	public static SimpleDateFormat FORMAT_FULL_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 日期格式:完整日期/时间格式
	 */
	public static SimpleDateFormat EXAC_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");

	/**
	 * 日期格式:(yyyy)
	 */
	public static SimpleDateFormat FORMAT_DATE_YEAR = new SimpleDateFormat("yyyy");
	
	public static long dateSecond = 24*60*60*1000;
	
	/**
	 * 默认开始记录数 0000000000
	 */
	public static String BEGININDEXNO = "0000000000";
	/**
	 * 默认结束记录数 0000000000
	 */
	public static String ENDINDEXNO = "9999999999";
	

	/**
	 * 验证日期字符串是否为[yyyy-MM-dd]格式
	 * 
	 * @param dateStr
	 * @return
	 */
	public static boolean isPageDateStr(String dateStr) {
		try {
			FORMAT_DATE_PAGE.parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 验证日期字符串是否为[yyyyMMdd]格式
	 * 
	 * @param dateStr
	 * @return
	 */
	public static boolean isDBDateStr(String dateStr) {
		try {
			FORMAT_DATE_DB.parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
		
		return true;
	}

	/**
	 * 取得当前年字符串; 时间格式:yyyy
	 * 
	 * @return
	 */
	public static String getCurrentYear() {
		return FORMAT_DATE_YEAR.format(new Date());
	}

	/**
	 * Date -> String(yyyyMMdd)
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDbDate(Date date) {
		if (date == null) {
			return "";
		}

		return FORMAT_DATE_DB.format(date);
	}

	/**
	 * String[yyyy-MM-dd HH:mm:ss]-> String[yyyyMMdd]
	 * 
	 * @param fullTime
	 * @return
	 */
	public static String convertDbDateByFullTime(String fullTime) {
		Date fullDate = parseFullDateTime(fullTime);
		return formatDbDate(fullDate);
	}

	/**
	 * String(yyyyMMdd) -> Date
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDbDate(String strDate) {
		if (strDate == null) {
			return null;
		}

		try {
			return FORMAT_DATE_DB.parse(strDate);
		} catch (ParseException e) {
			throw new RuntimeException("将字符串" + strDate + "解析为" + FORMAT_DATE_DB.toPattern() + "格式的日期时发生异常:", e);
		}
	}

	/**
	 * String(yyyy-MM-dd) -> Date
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date parsePageDate(String strDate) {
		if (strDate == null) {
			return null;
		}

		try {
			return FORMAT_DATE_PAGE.parse(strDate);
		} catch (ParseException e) {
			throw new RuntimeException("将字符串" + strDate + "解析为" + FORMAT_DATE_DB.toPattern() + "格式的日期时发生异常:", e);
		}
	}

	/**
	 * String(yyyyMMddHHmmss) -> Date
	 * 
	 * @param dateTime
	 *            时间字符串(yyyyMMddHHmmss)
	 * @return
	 */
	public static Date parseBackendDateTime(String dateTime) {
		if (dateTime == null) {
			return null;
		}

		try {
			return FORMAT_DATETIME_BACKEND.parse(dateTime);
		} catch (ParseException e) {
			throw new RuntimeException("将字符串" + dateTime + "解析为" + FORMAT_DATETIME_BACKEND.toPattern() + "格式的日期时发生异常:", e);
		}
	}

	/**
	 * String(yyyy-MM-dd HH:mm:ss) -> Date
	 * 
	 * @param dateTime
	 *            时间字符串(yyyy-MM-dd HH:mm:ss)
	 * @return
	 */
	public static Date parseFullDateTime(String dateTime) {
		if (dateTime == null) {
			return null;
		}

		try {
			return FORMAT_FULL_DATETIME.parse(dateTime);
		} catch (ParseException e) {
			throw new RuntimeException("将字符串" + dateTime + "解析为" + FORMAT_FULL_DATETIME.toPattern() + "格式的日期时发生异常:", e);
		}
	}
	
	/**
	 * String(yyyy-MM-dd HH:mm:ss,S) -> Date
	 * 
	 * @param dateTime
	 *            时间字符串(yyyy-MM-dd HH:mm:ss,S)
	 * @return
	 */
	public static Date parseExacDateTime(String dateTime) {
		if (dateTime == null) {
			return null;
		}
		
		try {
			return EXAC_DATE_TIME_FORMAT.parse(dateTime);
		} catch (ParseException e) {
			throw new RuntimeException("将字符串" + dateTime + "解析为" + FORMAT_FULL_DATETIME.toPattern() + "格式的日期时发生异常:", e);
		}
	}

	/**
	 * Date -> String(yyyy-MM-dd)
	 * 
	 * @param date
	 * @return
	 */
	public static String formatPageDate(Date date) {
		if (date == null) {
			return "";
		}

		return FORMAT_DATE_PAGE.format(date);
	}

	/**
	 * Date -> String(yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param date
	 * @return
	 */
	public static String formatFullDate(Date date) {
		if (date == null) {
			return "";
		}

		return FORMAT_FULL_DATETIME.format(date);
	}

	/**
	 * Date -> String(yyyyMMddHHmmss)
	 * 
	 * @param date
	 * @return
	 */
	public static String formatFull2Date(Date date) {
		if (date == null) {
			return "";
		}

		return FORMAT_DATETIME_BACKEND.format(date);
	}

	/**
	 * String(YYYY-MM-DD)-> String(YYYYMMDD)
	 * 
	 * @param pageDate
	 * @return
	 */
	public static String convertDate4Page2DB(String pageDate) {
		if (pageDate == null) {
			return "";
		}
		if (pageDate.length() != 10) {
			return pageDate;
		}
		return pageDate.replaceAll("-", "");
	}

	/**
	 * String(YYYYMMDD)->String(YYYY-MM-DD)
	 * 
	 * @param pageDate
	 * @return
	 */
	public static String convertDate4DB2Page(String dbDate) {
		if (dbDate == null) {
			return "";
		}
		if (dbDate.length() != 8) {
			return dbDate;
		} else {
			return dbDate.substring(0, 4) + "-" + dbDate.substring(4, 6) + "-" + dbDate.substring(6, 8);
		}
	}

	/**
	 * String(HHMMSS) -> String(HH-MM-SS)
	 * 
	 * @param dbDate
	 * @return
	 */
	public static String convertTime4DB2Page(String dbDate) {
		if (dbDate == null) {
			return "";
		}
		if (dbDate.length() != 6) {
			return dbDate;
		} else {
			return dbDate.substring(0, 2) + ":" + dbDate.substring(2, 4) + ":" + dbDate.substring(4, 6);
		}
	}

	/**
	 * String(HH-MM-SS) -> String(HHMMSS)
	 * 
	 * @param pageDate
	 * @return
	 */
	public static String convertTime4Page2DB(String pageTime) {
		if (pageTime == null) {
			return "";
		}
		if (pageTime.length() != 8) {
			return pageTime;
		}
		return pageTime.replaceAll(":", "");
	}

	/**
	 * String(HHmmss) -> String(HH:mm:ss)
	 * 
	 * @param dbTime
	 * @return
	 */
	public static String dbTimeToPageTime(String dbTime) throws ParseException {
		if (dbTime == null) {
			return "";
		}
		return FORMAT_TIME_PAGE.format(FORMAT_TIME.parse(dbTime));
	}

	/**
	 * 把日期，时间转化为格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            日期，格式：yyyyMMdd
	 * @param time
	 *            时间，格式：HHmmss
	 * @return
	 */
	public static String getDateTime(String date, String time) {
		StringBuffer sb = new StringBuffer();
		sb.append(convertDate4DB2Page(date));
		sb.append(" ");

		try {
			sb.append(dbTimeToPageTime(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * 把日期，时间转化为格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            日期，格式：yyyyMMdd
	 * @param time
	 *            时间，格式：HHmmss
	 * @return
	 */
	public static String getDateTime() {
		return FORMAT_FULL_DATETIME.format(new Date());
	}

	/**
	 * 取得当前日期字符串; 日期格式:yyyyMMdd
	 * 
	 * @return
	 */
	public static String getCurrentFullDate() {
		return FORMAT_DATE_DB.format(new Date());
	}
	/**
	 * 取得当前精确时间("yyyy-MM-dd HH:mm:ss,S")
	 * 
	 * @return
	 */
	public static String getCurrentExacDateTime() {
		return EXAC_DATE_TIME_FORMAT.format(new Date());
	}

	/**
	 * 取得当前日期字符串; 日期格式:yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getCurrentPageDate() {
		return FORMAT_DATE_PAGE.format(new Date());
	}

	/**
	 * 取得当前时间字符串; 时间格式:hhmmss
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		return FORMAT_TIME.format(new Date());
	}
	
	/**
	 * 取得当前时间字符串; 时间格式(yyyyMMddHHmmss)
	 * @return
	 */
	public static String getCurrentTimeForBACKEND() {
		Date date = new Date();
		return FORMAT_DATETIME_BACKEND.format(date);
	}

	/**
	 * 解析时间字符串;
	 * 
	 * @param time
	 * @return
	 */
	public static Date parseTime(String time) {
		try {
			return FORMAT_TIME.parse(time);
		} catch (ParseException e) {
			throw new RuntimeException("将字符串" + time + "按照" + FORMAT_TIME.toPattern() + "格式进行解析时发生异常:", e);
		}
	}

	/**
	 * Date -->> yyyy年MM月dd HH:mm:ss
	 * 
	 * @param date
	 */
	public static String formatLocalDate(Date date) {
		return FORMAT_LOCAL.format(date);
	}

	/**
	 * HH:mm:ss ->> HHmmss
	 * 
	 * @param pageTime
	 * @return
	 */
	public static String pageTimeToDbTime(String pageTime) {
		return pageTime.replaceAll(":", "");
	}

	/**
	 * 将日期转换为指定格式
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formateDate2Str(Date date, String pattern) {
		SimpleDateFormat s = new SimpleDateFormat(pattern);
		return s.format(date);
	}

	/**
	 * 将日期中的2007-1-1转化为20070101格式
	 * 
	 * @param datestr
	 * @return
	 */
	public static String dateStringFormat(String datestr) {
		if (datestr == null || datestr.equals(""))
			return null;
		String[] str1 = datestr.split("-");
		if (str1.length == 3) {
			if (str1[1].length() == 1) {
				str1[1] = "0" + str1[1];
			}
			if (str1[2].length() == 1) {
				str1[2] = "0" + str1[2];
			}
		} else
			return datestr;
		datestr = str1[0] + str1[1] + str1[2];
		return datestr;
	}
	
	/**
	 * 天数偏移
	 * 
	 * @param String 当前日期
	 * @param dayNum 偏移天数
	 * @return
	 */
    public static String getDateTimeForword(String date,int dayNum){
    	if (date == null) {
			return "";
		}
    	Date tempdate = null;
    	if(date.indexOf("-")==-1){
    		try {
    			tempdate = FORMAT_DATE_DB.parse(date);
    		} catch (ParseException e) {
    			throw new RuntimeException("将字符串" + date + "解析为" + FORMAT_DATE_PAGE.toPattern() + "格式的日期时发生异常:", e);
    		}
    	}else{
			try {
				tempdate = FORMAT_DATE_PAGE.parse(date);
			} catch (ParseException e) {
				throw new RuntimeException("将字符串" + date + "解析为" + FORMAT_DATE_PAGE.toPattern() + "格式的日期时发生异常:", e);
			}
    	}
		tempdate = getDataTimeForword(tempdate, dayNum);
		return FORMAT_DATE_DB.format(tempdate);
    }
    

	/**
	 * 天数偏移
	 * 
	 * @param date 当前日期
	 * @param dayNum 偏移天数
	 * @return
	 */
	public static Date getDataTimeForword(Date date,int dayNum){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, dayNum);
		return cal.getTime();
	}
	
	/**
     * 小时偏移
     * 
     * @param date 当前日期
     * @param timeNum 偏移小时数
     * @return
     */
    public static Date getDataTimeOffset(Date date,int timeNum){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, timeNum);
        return cal.getTime();
    }
    
    /**
     * 月份偏移
     * @param oriDate
     * @param amount
     * @return
     */
    public static Date getDataMONTHOffset(Date oriDate, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(oriDate);
		cal.add(Calendar.MONTH, amount);
		return cal.getTime();
	}

	

	/**
	 * 取得指定格式的当前时间
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getTime(String pattern) {
		return new SimpleDateFormat(pattern).format(new Date());
	}

	/**
	 * 取得指定时间的偏移时间
	 * 
	 * @param transferTime
	 *            原始时间（yyyy-MM-dd HH:ss:mm）
	 * @param calendarType
	 *            偏移单位（Calendar的常量）
	 * @param i
	 *            偏移量
	 * @return
	 */
	public static String getExcursionTime(String transferTime, int calendarType, int i) {
		Date parseFullDateTime = parseFullDateTime(transferTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseFullDateTime);
		calendar.add(calendarType, i);
		return FORMAT_FULL_DATETIME.format(calendar.getTime());
	}

	/**
	 * 取得指定时间的偏移时间
	 * 
	 * @param calendarType
	 *            偏移单位（Calendar的常量）
	 * @param i
	 *            偏移量
	 * @param 日期格式
	 * @return
	 */
	public static String getExcursionTime(int calendarType, int i, String pattern) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(calendarType, i);
		return new SimpleDateFormat(pattern).format(calendar.getTime());
	}

	/**
	 * 取得当前小时和分钟构成的长整型数组,例如(12:30 = 1230)
	 * 
	 * @return
	 */
	public static Long getCurrentHourMinute() {
		return Long.parseLong(FORMAT_HOUR_MINUTE.format(new Date()));
	}
	
	/**
	 * 求两个日期的天数之差
	 * 
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public static int getOddDateNum(String beginTime,String endTime){
		Date dateBegin=parseFullDateTime(beginTime);
		Date dateEnd =parseFullDateTime(endTime);
		int odd=(int)((dateEnd.getTime()-dateBegin.getTime())/(dateSecond));
		return odd;
	}
	
	/**
	 * 取系统时间零点
	 * @return
	 */
	public static Date getCurrentZeroTime() {
	    Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
	}
	
    /**
     * 计算格林威治时间（包含时差计算）
     * 
     * @return
     * @author lindongchenggcheng@SDOcom
     */
    public static long unixTime() {
        java.util.Calendar cal1 = new GregorianCalendar(1970, 0, 1, 0, 0, 0);
        java.util.Calendar cal = java.util.Calendar.getInstance(java.util.Locale.CHINA);
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        long now = cal1.getTimeInMillis() / 1000;
        long now1 = cal.getTimeInMillis() / 1000;
        long times = now1 - now;
        return times;
    }
    
    /**
     * 当前时区时间
     * @return
     */
    public static Date getCurrentLocaleTime(){
        java.util.Calendar cal = java.util.Calendar.getInstance(java.util.Locale.CHINA);
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTime();
    }

	public static String getBeginIdByDate(String beginDate) {
		//如果日期为空,则返回空
		if(StringUtils.isBlank(beginDate)){
			return null;
		}
		
		String newBeginDate=beginDate.replace("-","");
		if(newBeginDate.length()!=8 || !NumberUtils.isLong(newBeginDate)){
			throw new SystemException("非法的日期格式["+beginDate+"]");
		}
		
		return newBeginDate+"0000000000";
	}
	
	public static String getEndIdByDate(String beginDate) {
		//如果日期为空,则返回空
		if(StringUtils.isBlank(beginDate)){
			return null;
		}
		
		String newBeginDate=beginDate.replace("-","");
		if(newBeginDate.length()!=8 || !NumberUtils.isLong(newBeginDate)){
			throw new SystemException("非法的日期格式["+beginDate+"]");
		}
		
		return newBeginDate+"9999999999";
	}
	
	/**
	 * 取当前月份第一天日期  (yyyy-MM-dd)
	 * @return  Date
	 */
	public static Date getCurrentMothFirstDayByDate(){
		Date nowTime=new Date(System.currentTimeMillis());//取系统时间
		Date fistDay = null;
		try {
			SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-01");
			String today = sformat.format(nowTime);
			fistDay = parsePageDate(today);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fistDay;
	}
	/**
	 * 取当前月份第一天日期   (yyyy-MM-dd)
	 * @return  String
	 */
	public static String getCurrentMothFirstDayByString(){
		Date nowTime=new Date(System.currentTimeMillis());//取系统时间
		String today = null;
		try {
			SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-01");
			today = sformat.format(nowTime);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return today;
	}
	
	public static Date parseDateByString(String date,String dateFormat){
		try {
			return new SimpleDateFormat(dateFormat).parse(date);
		} catch (ParseException e) {
			throw new SystemException("解析字符串【"+date+"】为【"+dateFormat+"】格式的日期对象时发生异常：",e);
		}
	}
}
