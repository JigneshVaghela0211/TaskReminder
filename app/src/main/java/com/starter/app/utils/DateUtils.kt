package com.starter.app.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    
    const val DATE_FORMAT_DISPLAY = "dd/MM/yy"
    const val DATE_FORMAT_API = "yyyy-MM-dd"
    
    fun getTodayDate(): Date {
        return Calendar.getInstance().time
    }
    
    fun getTomorrowDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return calendar.time
    }
    
    fun addDaysToDate(date: Date, days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, days)
        return calendar.time
    }
    
    fun formatDateForDisplay(date: Date): String {
        val sdf = SimpleDateFormat(DATE_FORMAT_DISPLAY, Locale.getDefault())
        return sdf.format(date)
    }
    
    fun formatDateForApi(date: Date): String {
        val sdf = SimpleDateFormat(DATE_FORMAT_API, Locale.getDefault())
        return sdf.format(date)
    }
    
    fun parseDisplayDate(dateString: String): Date? {
        return try {
            val sdf = SimpleDateFormat(DATE_FORMAT_DISPLAY, Locale.getDefault())
            sdf.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }
    
    fun isDateAfterYesterday(date: Date): Boolean {
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_MONTH, -1)
        yesterday.set(Calendar.HOUR_OF_DAY, 23)
        yesterday.set(Calendar.MINUTE, 59)
        yesterday.set(Calendar.SECOND, 59)
        yesterday.set(Calendar.MILLISECOND, 999)
        
        return date.after(yesterday.time)
    }
    
    fun createDatePickerConstraints(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        
        val minDate = calendar.timeInMillis
        val maxDate = calendar.apply { add(Calendar.YEAR, 10) }.timeInMillis
        
        return Pair(minDate, maxDate)
    }
}