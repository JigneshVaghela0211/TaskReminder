package com.starter.app.ui.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.starter.app.ui.widget.CountdownWidgetProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CountdownWidgetUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            // Update all countdown widgets
            CountdownWidgetProvider.updateAllWidgets(applicationContext)
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
    
    companion object {
        const val WORK_NAME = "countdown_widget_update"
    }
}