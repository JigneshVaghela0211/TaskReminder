package com.starter.app.ui.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.starter.app.R
import com.starter.app.data.pojo.CountdownEvent
import com.starter.app.ui.viewmodel.CountdownViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateCountActivity : AppCompatActivity() {
    
    private val viewModel: CountdownViewModel by viewModels()
    private var selectedDateTime: Calendar = Calendar.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_count)
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        // Date picker
        findViewById<android.widget.DatePicker>(R.id.datePicker).setOnDateChangedListener { _, year, month, dayOfMonth ->
            selectedDateTime.set(Calendar.YEAR, year)
            selectedDateTime.set(Calendar.MONTH, month)
            selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
        
        // Time picker
        findViewById<android.widget.TimePicker>(R.id.timePicker).setOnTimeChangedListener { _, hourOfDay, minute ->
            selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            selectedDateTime.set(Calendar.MINUTE, minute)
        }
        
        // Create button
        findViewById<android.widget.Button>(R.id.btnCreateEvent).setOnClickListener {
            createCountdownEvent()
        }
    }
    
    private fun createCountdownEvent() {
        val title = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etEventTitle).text.toString().trim()
        val description = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etEventDescription).text.toString().trim()
        
        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter an event title", Toast.LENGTH_SHORT).show()
            return
        }
        
        val event = CountdownEvent(
            title = title,
            description = description,
            targetDate = selectedDateTime.time,
            isActive = true,
            themeColor = "#FF5722" // Default orange color
        )
        
        viewModel.insertEvent(event)
        Toast.makeText(this, "Countdown event created!", Toast.LENGTH_SHORT).show()
        finish()
    }
}