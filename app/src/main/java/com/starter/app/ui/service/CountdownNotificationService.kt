package com.starter.app.ui.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.starter.app.R
import com.starter.app.data.pojo.CountdownEvent
import com.starter.app.data.repository.CountdownRepository
import com.starter.app.ui.activity.HomeActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountdownNotificationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: CountdownRepository
) {
    
    companion object {
        const val CHANNEL_ID = "countdown_channel"
        const val CHANNEL_NAME = "Countdown Events"
        const val CHANNEL_DESCRIPTION = "Notifications for countdown events"
    }
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    fun showCountdownNotification(event: CountdownEvent) {
        val intent = Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_calendar)
            .setContentTitle(event.title)
            .setContentText("Your countdown event is approaching!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 250, 250, 250))
            .build()
        
        with(NotificationManagerCompat.from(context)) {
            notify(event.id.toInt(), notification)
        }
    }
    
    fun scheduleNotification(event: CountdownEvent) {
        // This would typically use WorkManager or AlarmManager
        // For now, we'll show the notification immediately if the event is close
        val currentTime = System.currentTimeMillis()
        val eventTime = event.targetDate.time
        val reminderTime = event.reminderTime ?: 0
        
        if (eventTime - currentTime <= reminderTime && event.notificationEnabled) {
            showCountdownNotification(event)
        }
    }
}