package sstow.utils;

import java.util.Calendar;

public class HolidayHelper {

	private HolidayHelper() {

	}
	static Calendar curTime = Calendar.getInstance();
	static Calendar holidayStart = Calendar.getInstance();
	static Calendar holidayEnd = Calendar.getInstance();

	public static boolean isChristmas() {
		setDate(holidayStart, Calendar.DECEMBER, 24, false);
		setDate(holidayEnd, Calendar.DECEMBER, 26, true);
		curTime = Calendar.getInstance();
		return curTime.after(holidayStart) && curTime.before(holidayEnd);
	}
	
	public static boolean isHalloween(){
		setDate(holidayStart, Calendar.OCTOBER, 29, false);
		setDate(holidayEnd, Calendar.NOVEMBER, 01, true);
		curTime = Calendar.getInstance();
		return curTime.after(holidayStart) && curTime.before(holidayEnd);
	}

	static void setDate(Calendar cal, int month, int date, boolean endOfDay) {
		cal.clear();
		cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, date);
		if (endOfDay) {
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
		} else {
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
	}
}