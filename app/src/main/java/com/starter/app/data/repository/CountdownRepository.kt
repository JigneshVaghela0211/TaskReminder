package com.starter.app.data.repository

import androidx.lifecycle.LiveData
import com.starter.app.data.datasource.CountdownEventDao
import com.starter.app.data.pojo.CountdownEvent
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountdownRepository @Inject constructor(
    private val countdownEventDao: CountdownEventDao
) {
    
    fun getAllActiveEvents(): LiveData<List<CountdownEvent>> = 
        countdownEventDao.getAllActiveEvents()
    
    fun getAllEvents(): LiveData<List<CountdownEvent>> = 
        countdownEventDao.getAllEvents()
    
    fun getUpcomingEvents(currentDate: Date): LiveData<List<CountdownEvent>> = 
        countdownEventDao.getUpcomingEvents(currentDate)
    
    fun getEventByIdAsLiveData(eventId: Long): LiveData<CountdownEvent?> = 
        countdownEventDao.getEventByIdAsLiveData(eventId)
    
    suspend fun getEventById(eventId: Long): CountdownEvent? = 
        countdownEventDao.getEventById(eventId)
    
    suspend fun insertEvent(event: CountdownEvent): Long = 
        countdownEventDao.insertEvent(event)
    
    suspend fun updateEvent(event: CountdownEvent) = 
        countdownEventDao.updateEvent(event)
    
    suspend fun deleteEvent(event: CountdownEvent) = 
        countdownEventDao.deleteEvent(event)
    
    suspend fun deactivateEvent(eventId: Long) = 
        countdownEventDao.deactivateEvent(eventId)
    
    suspend fun getEventsForNotification(reminderTime: Date): List<CountdownEvent> = 
        countdownEventDao.getEventsForNotification(reminderTime)
}