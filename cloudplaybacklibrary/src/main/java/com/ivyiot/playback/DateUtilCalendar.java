package com.ivyiot.playback;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtilCalendar {

    public static int getMonthDays(int year, int month) {
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1) {
            month = 12;
            year -= 1;
        }
        int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int days = 0;

        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            arr[1] = 29;
        }

        try {
            days = arr[month - 1];
        } catch (Exception e) {
            e.getStackTrace();
        }

        return days;
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeekDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    public static CustomDateCalendar getNextSunday() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 7 - getWeekDay() + 1);
        CustomDateCalendar date = new CustomDateCalendar(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
        return date;
    }

    public static int[] getWeekSunday(int year, int month, int day, int pervious) {
        int[] time = new int[3];
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.add(Calendar.DAY_OF_MONTH, pervious);
        time[0] = c.get(Calendar.YEAR);
        time[1] = c.get(Calendar.MONTH) + 1;
        time[2] = c.get(Calendar.DAY_OF_MONTH);
        return time;

    }

    /**
     * @param year
     * @param month
     * @return ??????????????????1??????????????????????????????
     */
    public static int getWeekDayFromDate(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(year, month));
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return week_index;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date getDateFromString(int year, int month) {
        String dateString = year + "-" + (month > 9 ? month : (0 + month)) + "-" + 01;
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static boolean isToday(CustomDateCalendar date) {
        if(null == date){
            return false;
        }
        return (date.year == getYear() &&
                date.month == getMonth()
                && date.day == getCurrentMonthDay());
    }

    public static boolean isCurrentMonth(CustomDateCalendar date) {
        if(null == date){
            return false;
        }
        return (date.year == getYear() &&
                date.month == getMonth());
    }

    /*????????????????????????*/
    public static boolean isBeforeToday(CustomDateCalendar date) {
        if(null == date){
            return false;
        }
        if(date.year < getYear()){
            return true;
        }else if(date.year > getYear()){
            return false;
        }else if(date.year == getYear()){
            if(date.month < getMonth()){
                return true;
            }else if(date.month > getMonth()){
                return false;
            }else if(date.month == getMonth()){
                if(date.day < getCurrentMonthDay()){
                    return true;
                }else if(date.day >= getCurrentMonthDay()){
                    return false;
                }
            }
        }
        return false;
    }
}
