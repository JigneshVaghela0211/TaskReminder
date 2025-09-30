package com.starter.app.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.starter.app.data.pojo.CountdownEvent
import com.starter.app.utils.DateTypeConverter

@Database(
    entities = [CountdownEvent::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTypeConverter::class)
abstract class CountdownDatabase : RoomDatabase() {
    abstract fun countdownEventDao(): CountdownEventDao
    
    companion object {
        const val DATABASE_NAME = "countdown_database"
    }
}