package com.starter.app.utils

enum class TimeUnitType {
    CONCISE_TIME_NOTATION,
    HOURS_MINUTES_SECONDS,
    DAYS,
    WEEKS,
    MONTHS,
    YEARS,
    AUTO_SELECTION;

    fun getDisplayName(): String {
        return when (this) {
            CONCISE_TIME_NOTATION -> "Concise Time Notation"
            HOURS_MINUTES_SECONDS -> "Hours, Minutes and Seconds"
            DAYS -> "Days"
            WEEKS -> "Weeks"
            MONTHS -> "Months"
            YEARS -> "Years"
            AUTO_SELECTION -> "Auto-Selection"
        }
    }

    fun getUnitName(): String {
        return when (this) {
            CONCISE_TIME_NOTATION -> "units"
            HOURS_MINUTES_SECONDS -> "seconds"
            DAYS -> "days"
            WEEKS -> "weeks"
            MONTHS -> "months"
            YEARS -> "years"
            AUTO_SELECTION -> "auto"
        }
    }

    fun formatTime(milliseconds: Long): String {
        return when (this) {
            CONCISE_TIME_NOTATION -> formatConciseTime(milliseconds)
            HOURS_MINUTES_SECONDS -> formatHoursMinutesSeconds(milliseconds)
            DAYS -> "${milliseconds / (24 * 60 * 60 * 1000)} days"
            WEEKS -> "${milliseconds / (7 * 24 * 60 * 60 * 1000)} weeks"
            MONTHS -> "${milliseconds / (30 * 24 * 60 * 60 * 1000)} months"
            YEARS -> "${milliseconds / (365 * 24 * 60 * 60 * 1000)} years"
            AUTO_SELECTION -> autoFormatTime(milliseconds)
        }
    }

    private fun formatConciseTime(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 0 -> "${days}d ${hours % 24}h ${minutes % 60}m ${seconds % 60}s"
            hours > 0 -> "${hours}h ${minutes % 60}m ${seconds % 60}s"
            minutes > 0 -> "${minutes}m ${seconds % 60}s"
            else -> "${seconds}s"
        }
    }

    private fun formatHoursMinutesSeconds(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return when {
            hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
            minutes > 0 -> String.format("%02d:%02d", minutes, seconds % 60)
            else -> String.format("%02d seconds", seconds)
        }
    }

    private fun autoFormatTime(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = days / 30
        val years = days / 365

        return when {
            years > 0 -> "$years year${if (years > 1) "s" else ""}"
            months > 0 -> "$months month${if (months > 1) "s" else ""}"
            weeks > 0 -> "$weeks week${if (weeks > 1) "s" else ""}"
            days > 0 -> "$days day${if (days > 1) "s" else ""}"
            hours > 0 -> "$hours hour${if (hours > 1) "s" else ""}"
            minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""}"
            else -> "$seconds second${if (seconds != 1L) "s" else ""}"
        }
    }
}