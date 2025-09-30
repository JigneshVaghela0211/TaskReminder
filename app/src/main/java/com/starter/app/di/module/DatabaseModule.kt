package com.starter.app.di.module

import android.app.Application
import androidx.room.Room
import com.starter.app.data.datasource.CountdownEventDao
import com.starter.app.data.datasource.CountdownEventDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCountdownEventDatabase(application: Application): CountdownEventDatabase {
        return Room.databaseBuilder(
            application,
            CountdownEventDatabase::class.java,
            "countdown_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCountdownEventDao(database: CountdownEventDatabase): CountdownEventDao {
        return database.countdownEventDao()
    }
}