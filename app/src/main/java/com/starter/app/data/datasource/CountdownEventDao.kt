package com.starter.app.data.datasource

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.starter.app.data.pojo.CountdownEvent
import java.util.Date

@Dao
interface CountdownEventDao {
    
    @Query("SELECT * FROM countdown_events WHERE isActive = 1 ORDER BY targetDate ASC")
    fun getAllActiveEvents(): LiveData<List<CountdownEvent>>
    
    @Query("SELECT * FROM countdown_events ORDER BY targetDate ASC")
    fun getAllEvents(): LiveData<List<CountdownEvent>>
    
    @Query("SELECT * FROM countdown_events WHERE id = :eventId")
    fun getEventByIdAsLiveData(eventId: Long): LiveData<CountdownEvent?>
    
    @Query("SELECT * FROM countdown_events WHERE id = :eventId")
    suspend fun getEventById(eventId: Long): CountdownEvent?
    
    @Query("SELECT * FROM countdown_events WHERE targetDate >= :currentDate AND isActive = 1 ORDER BY targetDate ASC")
    fun getUpcomingEvents(currentDate: Date): LiveData<List<CountdownEvent>>
    
    @Insert
    suspend fun insertEvent(event: CountdownEvent): Long
    
    @Update
    suspend fun updateEvent(event: CountdownEvent)
    
    @Delete
    suspend fun deleteEvent(event: CountdownEvent)
    
    @Query("UPDATE countdown_events SET isActive = 0 WHERE id = :eventId")
    suspend fun deactivateEvent(eventId: Long)
    
    @Query("SELECT * FROM countdown_events WHERE notificationEnabled = 1 AND targetDate <= :reminderTime AND isActive = 1")
    suspend fun getEventsForNotification(reminderTime: Date): List<CountdownEvent>
}