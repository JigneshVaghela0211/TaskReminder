package com.starter.app.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.starter.app.R
import com.starter.app.data.repository.CountdownRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class CountdownWidgetProvider : AppWidgetProvider() {
    
    @Inject
    lateinit var repository: CountdownRepository
    
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
    
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }
    
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val events = repository.getUpcomingEvents(Date()).value
                val event = events?.firstOrNull()
                
                val views = RemoteViews(context.packageName, R.layout.widget_countdown)
                
                if (event != null) {
                    val timeRemaining = calculateTimeRemaining(event.targetDate)
                    views.setTextViewText(R.id.widget_title, event.title)
                    views.setTextViewText(R.id.widget_countdown, timeRemaining)
                    views.setTextViewText(R.id.widget_date, formatDate(event.targetDate))
                } else {
                    views.setTextViewText(R.id.widget_title, "No upcoming events")
                    views.setTextViewText(R.id.widget_countdown, "--:--:--")
                    views.setTextViewText(R.id.widget_date, "")
                }
                
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun calculateTimeRemaining(targetDate: Date): String {
        val currentTime = System.currentTimeMillis()
        val targetTime = targetDate.time
        val diff = targetTime - currentTime
        
        if (diff <= 0) {
            return "00:00:00"
        }
        
        val days = TimeUnit.MILLISECONDS.toDays(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60
        
        return when {
            days > 0 -> String.format("%dd %02dh %02dm", days, hours, minutes)
            hours > 0 -> String.format("%02dh %02dm %02ds", hours, minutes, seconds)
            else -> String.format("%02dm %02ds", minutes, seconds)
        }
    }
    
    private fun formatDate(date: Date): String {
        val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
        return sdf.format(date)
    }
    
    companion object {
        fun updateAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context, CountdownWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
            
            val intent = Intent(context, CountdownWidgetProvider::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            context.sendBroadcast(intent)
        }
    }
}