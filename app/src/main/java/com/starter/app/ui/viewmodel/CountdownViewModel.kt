package com.starter.app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starter.app.data.pojo.CountdownEvent
import com.starter.app.data.repository.CountdownRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CountdownViewModel @Inject constructor(
    private val repository: CountdownRepository
) : ViewModel() {
    
    val allEvents: LiveData<List<CountdownEvent>> = repository.getAllEvents()
    val activeEvents: LiveData<List<CountdownEvent>> = repository.getAllActiveEvents()
    
    fun insertEvent(event: CountdownEvent) = viewModelScope.launch {
        repository.insertEvent(event)
    }
    
    fun updateEvent(event: CountdownEvent) = viewModelScope.launch {
        repository.updateEvent(event)
    }
    
    fun deleteEvent(event: CountdownEvent) = viewModelScope.launch {
        repository.deleteEvent(event)
    }
    
    fun deactivateEvent(event: CountdownEvent) = viewModelScope.launch {
        repository.deactivateEvent(event.id)
    }
    
    fun getEventById(id: Long): LiveData<CountdownEvent?> {
        return repository.getEventByIdAsLiveData(id)
    }
    
    fun getUpcomingEvents(date: Date): LiveData<List<CountdownEvent>> {
        return repository.getUpcomingEvents(date)
    }
}