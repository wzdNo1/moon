package com.mipo.core.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by lyl on 2016/9/13.
 */
public class DateUtil {

	public static String DEFAULT = "dd-MM-yyyy HH:mm:ss";

	public static String SHORT_DEFAULT = "dd-MM-yyyy";
	/**
	 * 英文简写（默认）如：2010-12-01
	 */
	public static String FORMAT_SHORT = "yyyy-MM-dd";
	/**
	 * 英文全称 如：2010-12-01 23:15:06
	 */
	public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.S
	 */
	public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";
	/**
	 * 中文简写 如：12月01日
	 */
	public static String FORMAT_CN_Md = "MM月dd日";
	/**
	 * 中文简写 如：2010年12月01日
	 */
	public static String FORMAT_SHORT_CN = "yyyy年MM月dd日";
	/**
	 * 中文全称 如：2010年12月01日 23时15分06秒
	 */
	public static String FORMAT_LONG_CN = "yyyy年MM月dd日 HH时mm分ss秒";
	/**
	 * 精确到毫秒的完整中文时间
	 */
	public static String FORMAT_FULL_CN = "yyyy年MM月dd日 HH时mm分ss秒SSS毫秒";

	/**
	 * 精确到毫秒的完整中文时间
	 */
	public static String FORMAT_INT_MINITE = "yyyyMMddHHmmss";
	/**
	 * 日期
	 */
	public static String FORMAT_INT_DATE = "yyyyMMdd";
	/**
	 * 日期
	 */
	public static String FORMAT_INT_MONTH = "yyyyMM";
	/**
	 * 英文简写（默认）如：12.01
	 */
	public static String FORMAT_Md = "MM.dd";

	/**
	 * 英文简写（默认）如：2010.12.01 12:01
	 */
	public static String FORMAT_yMdHm = "yyyy.MM.dd HH:mm";

	/**
	 * 英文简写（默认）如：2018.12.01
	 */
	public static String FORMAT_yMd = "yyyy.MM.dd";


	/**
	 * 获得默认的 date pattern
	 */
	public static String getDatePattern() {
		return FORMAT_LONG;
	}

	public static Date getCurrent() {
		Calendar c = Calendar.getInstance();
		return c.getTime();
	}

	/**
	 * 根据时间戳（毫秒）获取日期
	 *
	 * @param timestamp
	 * @return
	 */
	public static Date getDateByTimestamp(long timestamp) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timestamp);
		return c.getTime();
	}

	/**
	 * 根据预设格式返回当前日期
	 *
	 * @return
	 */
	public static String getNow() {
		return format(new Date());
	}

	/**
	 * 根据用户格式返回当前日期
	 *
	 * @param format
	 * @return
	 */
	public static String getNow(String format) {
		return format(new Date(), format);
	}

	/**
	 * 使用预设格式格式化日期
	 *
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return format(date, getDatePattern());
	}

	/**
	 * 使用用户格式格式化日期
	 *
	 * @param date    日期
	 * @param pattern 日期格式
	 * @return
	 */
	public static String format(Date date, String pattern) {
		String returnValue = "";
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			returnValue = df.format(date);
		}
		return (returnValue);
	}

	/**
	 * 使用预设格式提取字符串日期
	 *
	 * @param strDate 日期字符串
	 * @return
	 */
	public static Date parse(String strDate) {
		return parse(strDate, getDatePattern());
	}

	/**
	 * 使用用户格式提取字符串日期
	 *
	 * @param strDate 日期字符串
	 * @param pattern 日期格式
	 * @return
	 */
	public static Date parse(String strDate, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		try {
			return df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 在日期上增加数个整月
	 *
	 * @param date 日期
	 * @param n    要增加的月数
	 * @return
	 */
	public static Date addMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}

	/**
	 * 在日期上增加天数
	 *
	 * @param date 日期
	 * @param n    要增加的天数
	 * @return
	 */
	public static Date addDay(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, n);
		return cal.getTime();
	}

	/**
	 * 增加的小时数
	 *
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addHour(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, n);
		return cal.getTime();
	}

	/**
	 * 增加的分钟数
	 *
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addMinute(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, n);
		return cal.getTime();
	}

	/**
	 * 获取时间戳
	 */
	public static String getTimeString() {
		SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);
		Calendar calendar = Calendar.getInstance();
		return df.format(calendar.getTime());
	}

	/**
	 * 获取日期年份
	 *
	 * @param date 日期
	 * @return
	 */
	public static String getYear(Date date) {
		return format(date).substring(0, 4);
	}

	/**
	 * 按默认格式的字符串距离今天的天数
	 *
	 * @param date 日期字符串
	 * @return
	 */
	public static int countDays(String date) {
		long t = Calendar.getInstance().getTime().getTime();
		Calendar c = Calendar.getInstance();
		c.setTime(parse(date));
		long t1 = c.getTime().getTime();
		return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
	}

	/**
	 * 按用户格式字符串距离今天的天数
	 *
	 * @param date   日期字符串
	 * @param format 日期格式
	 * @return
	 */
	public static int countDays(String date, String format) {
		long t = Calendar.getInstance().getTime().getTime();
		Calendar c = Calendar.getInstance();
		c.setTime(parse(date, format));
		long t1 = c.getTime().getTime();
		return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
	}

	/**
	 * 获取某天开始时间
	 *
	 * @param date
	 * @return
	 */
	public static Date getDayBegin(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取某天结束时间
	 *
	 * @param date
	 * @return
	 */
	public static Date getDayEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		date = getDayBegin(date);
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		calendar.add(Calendar.SECOND, -1);
		return calendar.getTime();
	}

	/**
	 * 取得指定时间间隔后的系统时间
	 * 示例：
	 * getDifferentTime( 1, 2, 3)
	 * 输出1小时2分3秒后的系统时间
	 * getDifferentTime( -24, 0, 0)
	 * 输出1天前的系统日期
	 *
	 * @param hour   小时
	 * @param minute 分钟
	 * @param second 秒
	 * @return String
	 */
	public static Date getDifferentTime(int day, int hour, int minute, int second) {
		GregorianCalendar calendar = (GregorianCalendar) Calendar.getInstance();
		calendar.add(Calendar.DATE, day);
		calendar.add(Calendar.HOUR, hour);
		calendar.add(Calendar.MINUTE, minute);
		calendar.add(Calendar.SECOND, second);
		return calendar.getTime();
	}

	/**
	 * 补位
	 * 如果num位数小于figure自定的位数，则在左边补0；如果大于，则不变
	 * 例如：
	 * figure = 4 时，num = 12，返回0012；num = 1100时，返回1100； num = 11001时，返回11001
	 *
	 * @param num
	 * @param figure
	 * @return
	 */
	public static String fillGap(int num, int figure) {
		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMinimumIntegerDigits(figure);
		formatter.setGroupingUsed(false);
		return formatter.format(num);
	}

	/**
	 * 获取周一
	 *
	 * @param time
	 * @return
	 */
	public static Date getMonday(Date time) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(time);

		//判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天

		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}

		cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return cal.getTime();
	}

	/**
	 * 获取两个日期相差的年份 Example:
	 * <br/><b>startDate:1992-02-17 startDate:2018-01-04 return:25</b><br/><b>start:1992-02-17 end:2018-03-20 return:26</b>
	 *
	 * @param startDate
	 * @param endDate   传null则为当前时间
	 * @return
	 */
	public static int getYearsBetween(Date startDate, Date endDate) {
		if (startDate == null)
			return 0;
		if (endDate == null)
			endDate = getCurrent();
		int intStart = Integer.parseInt(format(startDate, FORMAT_INT_DATE));
		int intEnd = Integer.parseInt(format(endDate, FORMAT_INT_DATE));
		return (intEnd - intStart) / 10000;
	}

	/**
	 * 获取一周中的第几天
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取一周中的第几天 默认第一天是周一
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		return getDayOfWeek(date, Calendar.MONDAY);
	}

	/**
	 * 获取一周中的第几天
	 * @param date 日期
	 * @param weekFirstDay 指定第一天是哪一天有周日到周六 1-7
	 * @return
	 */
	public static int getDayOfWeek(Date date, int weekFirstDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK) + 1 - weekFirstDay;
		return (day <= 0) ? (7 - day) : day;
	}

	public static Date setDayOfMonth(Date date, int day){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	/**
	 * 账单周期
	 * @param time
	 * @return
	 */
	public static int getWeekOfMonth(Date time) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
		int dayOfMonth = cal.get(Calendar.WEEK_OF_MONTH);//获取月中第几周
		if(dayWeek == Calendar.SUNDAY){//周日则减一
			dayOfMonth--;
		}
		cal.set(Calendar.DAY_OF_MONTH,1);
		int firstDayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if(firstDayWeek > Calendar.MONDAY)//本月第一天不是周二到周六 减一
			dayOfMonth--;

		if(dayOfMonth <= 0){//周期是上月的周期
			cal.add(Calendar.DAY_OF_MONTH, -1);
			return getWeekOfMonth(cal.getTime());
		}
		return dayOfMonth;
	}

	public static Date getMonthFirst() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH,1);
		return cal.getTime();
	}

	public static Boolean dateCompare(Date date1, Date date2){
		return date1.getTime() - date2.getTime() >0;
	}
}
