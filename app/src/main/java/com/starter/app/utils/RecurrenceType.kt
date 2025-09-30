package com.starter.app.utils

enum class RecurrenceType {
    DOES_NOT_REPEAT,
    DAILY,
    WEEKLY,
    MONTHLY_SAME_DAY,
    LAST_DAY_OF_MONTH,
    ANNUALLY_SAME_DAY,
    EVERY_WEEKDAY,
    CUSTOM_DAYS;

    fun getDisplayName(): String {
        return when (this) {
            DOES_NOT_REPEAT -> "Does not repeat"
            DAILY -> "Daily"
            WEEKLY -> "Weekly"
            MONTHLY_SAME_DAY -> "Monthly (same day of the month)"
            LAST_DAY_OF_MONTH -> "Last day of the month"
            ANNUALLY_SAME_DAY -> "Annually (same day)"
            EVERY_WEEKDAY -> "Every Weekday (Monday to Friday)"
            CUSTOM_DAYS -> "Custom number of days..."
        }
    }

    fun getNextRecurrenceDate(currentDate: java.util.Calendar): java.util.Calendar {
        val nextDate = currentDate.clone() as java.util.Calendar
        
        when (this) {
            DOES_NOT_REPEAT -> return nextDate
            DAILY -> nextDate.add(java.util.Calendar.DAY_OF_MONTH, 1)
            WEEKLY -> nextDate.add(java.util.Calendar.WEEK_OF_YEAR, 1)
            MONTHLY_SAME_DAY -> nextDate.add(java.util.Calendar.MONTH, 1)
            LAST_DAY_OF_MONTH -> {
                nextDate.add(java.util.Calendar.MONTH, 1)
                nextDate.set(java.util.Calendar.DAY_OF_MONTH, nextDate.getActualMaximum(java.util.Calendar.DAY_OF_MONTH))
            }
            ANNUALLY_SAME_DAY -> nextDate.add(java.util.Calendar.YEAR, 1)
            EVERY_WEEKDAY -> {
                do {
                    nextDate.add(java.util.Calendar.DAY_OF_MONTH, 1)
                } while (nextDate.get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SATURDAY || 
                         nextDate.get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY)
            }
            CUSTOM_DAYS -> {
                // This will be handled separately with custom input
                nextDate.add(java.util.Calendar.DAY_OF_MONTH, 1)
            }
        }
        
        return nextDate
    }
}