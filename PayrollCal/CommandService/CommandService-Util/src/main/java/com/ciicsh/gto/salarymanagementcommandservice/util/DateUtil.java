package com.ciicsh.gto.salarymanagementcommandservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期处理工具类
 * Created by jiangtianning on 2017/11/09.
 */
public class DateUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	/**
	 * 日期转字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getDate(Date date, String pattern) {
		if(null == date) {
			return null;
		}
//		DateTimeFormatter dtf = new DateTimeFormatter(pattern);
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	/**
	 * 字符串转日期
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static Date toDate(String str, String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(str);
		} catch(Exception e) {
			logger.error("convert string to date error. str="+str+"; pattern="+pattern, e);
		}
		return null;
	}
	
	/**
	 * 获取时间
	 * @param date
	 * @return
	 */
	public static Integer getTime(Date date) {
		if(null == date) {
			return null;
		}
		return Integer.parseInt(getDate(date, "HHmm"));
	}
	
	/**
	 * 获取周
	 * @param date
	 * @return
	 */
	public static Integer getWeek(Date date) {
		if(null == date) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	/** 
	 * 获取总小时数：起始时间差/24+(起始时间差%24)>8?24:起始时间差%24
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Long getTotalHours(Long startTime, Long endTime) {
		long timeRange = endTime - startTime;
		long hours = timeRange / (1000*60*60);//得到小时数
		return Math.round(hours/24*24) + (hours%24>=8?24:hours%24);
	}
	
//	public static void main(String[] args) {
//		System.out.println(getWeek(toDate("20161022", "yyyyMMdd")));
//		System.out.println(getTotalHours(toDate("20161025221500", "yyyyMMddHHmmss").getTime(), toDate("20161026211500", "yyyyMMddHHmmss").getTime()));
//	}
	
	/**
	 * 获取年
	 * @return
	 */
	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
	}

	/**
	 * 判断是否为节假日
	 * @param holidayList
	 * @param date
	 * @return
	 */
	public static boolean isHoliday(List<String> holidayList, String date) {
		if (holidayList != null && holidayList.contains(date)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断date是否在time1和time2之间
	 * @param date
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isInTimes(Date date, int time1, int time2) {
		int time = Integer.parseInt(getDate(date, "HHmm"));
		return time>=time1 && time<=time2;
	}

	/**
	 * 判断date是否在time之后
	 * @param date
	 * @param time
	 * @return
	 */
	public static boolean isAfterTime(Date date, int time) {
		return (Integer.parseInt(getDate(date, "HHmm"))-time)>=0;
	}

	/**
	 * 获取date1到date2之间的小时数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getBetweenHours(Date date1, Date date2) {
		return (int) ((date1.getTime()-date2.getTime())/(1000*60*60));
	}
	
	/**
	 * 判断是否工作日
	 * @param date
	 * @return
	 */
	public static boolean isWorkDay(Date date) {
		return !isWeekend(date);
	}

	/**
	 * 判断是否周末
	 * @param date
	 * @return
	 */
	public static boolean isWeekend(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			return true;
		}
		return false;
	}

	/**
	 * 获取小时
	 * @param date
	 * @return
	 */
	public static int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * datetime 转为 date
	 * @param date
	 * @return
	 */
	public static Date getDate(Date date) {
		if(null == date) {
			return null;
		}
		LocalDate datetime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
		return Date.from(datetime.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 计算两个时间之间的小时数
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static float getHours(Date startTime, Date endTime) {
		long hourTimes = 60*60*1000;//1小时
		long minuteTimes = 60*1000;//1分钟
		long times = endTime.getTime()-startTime.getTime();//总时长
		Long hours = times/hourTimes;//小时数
		Long minute = (times - hours * hourTimes)/minuteTimes;//剩余分钟数
		float hour = hours.floatValue();
		if (minute > 0 && minute <= 15) {
			hour += 0.25f;
		} else if (minute > 15 && minute <= 30) {
			hour += 0.5f;
		} else if (minute > 30 && minute <= 45) {
			hour += 0.75f;
		} else if (minute > 45 && minute <= 60) {
			hour += 1f;
		}
		return hour;
	}
	
	/**
	 * 计算两个日期的天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDays(Date date1, Date date2) {
		LocalDate ldate1 = LocalDateTime.ofInstant(date1.toInstant(), ZoneId.systemDefault()).toLocalDate();
		LocalDate ldate2 = LocalDateTime.ofInstant(date2.toInstant(), ZoneId.systemDefault()).toLocalDate();
		Period period = Period.between(ldate2, ldate1);
		return period.getDays();
	}
	
	/**
	 * 判断是否在24小时内
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public static boolean isIn24Hour(Date startDate, Date endDate){ 
        long cha = endDate.getTime() - startDate.getTime(); 
        double result = cha * 1.0 / (1000 * 60 * 60); 
        if(result<=24){ 
             return true; 
        }else{ 
             return false; 
        } 

    }
}
