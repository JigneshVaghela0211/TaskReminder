package com.starter.app.ui.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.starter.app.databinding.CreateCountActivityBinding
import com.starter.app.databinding.DialogDateSelectionBinding
import com.starter.app.databinding.DialogNumberOfDaysBinding
import com.starter.app.databinding.DialogRecurrenceSelectionBinding
import com.starter.app.databinding.DialogTimeSelectionBinding
import com.starter.app.ui.base.BaseActivity
import com.starter.app.adapters.RecurrenceAdapter
import com.starter.app.utils.DateUtils
import com.starter.app.utils.RecurrenceType
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateTaskActivity : BaseActivity() {
    
    lateinit var binding: CreateCountActivityBinding
    private var selectedDate: Date = DateUtils.getTodayDate()
    private var selectedTime: Calendar = Calendar.getInstance()
    private var selectedRecurrence: RecurrenceType = RecurrenceType.DOES_NOT_REPEAT
    
    override fun findFragmentPlaceHolder(): Int {
        return 0
    }

    override fun createViewBinding(): View {
        binding = CreateCountActivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDateInput()
        setupTimeInput()
        setupRecurrenceInput()
        setDefaultDate()
        setDefaultTime()
        setDefaultRecurrence()
    }

    private fun setupDateInput() {
        binding.textInputDate.setOnClickListener {
            showDateSelectionDialog()
        }
        binding.textInputEditTextDate.setOnClickListener {
            showDateSelectionDialog()
        }
    }

    private fun setupTimeInput() {
        binding.textInputTime.setOnClickListener {
            showTimeSelectionDialog()
        }
        binding.etTime.setOnClickListener {
            showTimeSelectionDialog()
        }
    }
    
    private fun setDefaultDate() {
        binding.textInputEditTextDate.setText(DateUtils.formatDateForDisplay(selectedDate))
    }

    private fun setDefaultTime() {
        val timeFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        binding.etTime.setText(timeFormat.format(selectedTime.time))
    }

    private fun setupRecurrenceInput() {
        binding.textInputRecurrence.setOnClickListener {
            showRecurrenceSelectionDialog()
        }
        binding.etRecurrence.setOnClickListener {
            showRecurrenceSelectionDialog()
        }
    }

    private fun setDefaultRecurrence() {
        binding.etRecurrence.setText(selectedRecurrence.getDisplayName())
        updateNextRecurrenceText()
    }
    
    private fun showDateSelectionDialog() {
        val dialogBinding = DialogDateSelectionBinding.inflate(layoutInflater)
        
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .create()
        
        dialogBinding.cardSpecificDate.setOnClickListener {
            dialog.dismiss()
            showDatePickerDialog()
        }
        
        dialogBinding.cardNumberOfDays.setOnClickListener {
            dialog.dismiss()
            showNumberOfDaysDialog()
        }
        
        dialogBinding.cardEventFromCalendar.setOnClickListener {
            dialog.dismiss()
            showCalendarEventSelection()
        }
        
        // Use default Material Design background instead of transparent
        dialog.show()
    }
    
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val (minDate, maxDate) = DateUtils.createDatePickerConstraints()
        
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                val selectedDate = selectedCalendar.time
                
                if (DateUtils.isDateAfterYesterday(selectedDate)) {
                    this.selectedDate = selectedDate
                    binding.textInputEditTextDate.setText(DateUtils.formatDateForDisplay(selectedDate))
                } else {
                    Toast.makeText(this, "Please select a date after yesterday", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        
        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.datePicker.maxDate = maxDate
        datePickerDialog.show()
    }
    
    private fun showNumberOfDaysDialog() {
        val dialogBinding = DialogNumberOfDaysBinding.inflate(layoutInflater)
        
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .create()
        
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        dialogBinding.btnOk.setOnClickListener {
            val daysText = dialogBinding.etNumberOfDays.text.toString()
            if (daysText.isNotEmpty()) {
                try {
                    val days = daysText.toInt()
                    if (days > 0 && days <= 365) {
                        val newDate = DateUtils.addDaysToDate(DateUtils.getTodayDate(), days)
                        selectedDate = newDate
                        binding.textInputEditTextDate.setText(DateUtils.formatDateForDisplay(newDate))
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Please enter a number between 1 and 365", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter number of days", Toast.LENGTH_SHORT).show()
            }
        }
        
        dialog.show()
    }
    
    private fun showCalendarEventSelection() {
        Toast.makeText(this, "Calendar event selection coming soon!", Toast.LENGTH_SHORT).show()
    }

    private fun showTimeSelectionDialog() {
        val dialog = Dialog(this)
        val dialogBinding = DialogTimeSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.cardAllDay.setOnClickListener {
            // Set time to 12:00 AM (00:00) and display "All day"
            selectedTime.set(Calendar.HOUR_OF_DAY, 0)
            selectedTime.set(Calendar.MINUTE, 0)
            binding.etTime.setText("All day")
            dialog.dismiss()
        }

        dialogBinding.cardEnterTime.setOnClickListener {
            showTimePickerDialog()
            dialog.dismiss()
        }

        // Remove transparent background - use default Material Design background
        dialog.show()
    }

    private fun showTimePickerDialog() {
        val currentHour = selectedTime.get(Calendar.HOUR_OF_DAY)
        val currentMinute = selectedTime.get(Calendar.MINUTE)

        val timePickerDialog = android.app.TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                val timeFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                binding.etTime.setText(timeFormat.format(selectedTime.time))
            },
            currentHour,
            currentMinute,
            true // 24-hour format
        )

        timePickerDialog.show()
    }

    private fun showRecurrenceSelectionDialog() {
        val dialogBinding = DialogRecurrenceSelectionBinding.inflate(layoutInflater)
        
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()
        
        val recurrenceTypes = listOf(
            RecurrenceType.DOES_NOT_REPEAT,
            RecurrenceType.DAILY,
            RecurrenceType.WEEKLY,
            RecurrenceType.MONTHLY_SAME_DAY,
            RecurrenceType.LAST_DAY_OF_MONTH,
            RecurrenceType.ANNUALLY_SAME_DAY,
            RecurrenceType.EVERY_WEEKDAY,
            RecurrenceType.CUSTOM_DAYS
        )
        
        val adapter = RecurrenceAdapter(recurrenceTypes) { recurrenceType ->
            dialog.dismiss()
            handleRecurrenceSelection(recurrenceType)
        }
        
        dialogBinding.recyclerViewRecurrence.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        dialogBinding.recyclerViewRecurrence.adapter = adapter
        
        // Set minimum height for the dialog
        dialog.setOnShowListener {
            val displayMetrics = resources.displayMetrics
            val height = (displayMetrics.heightPixels * 0.6).toInt()
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height)
        }
        
        dialog.show()
    }
    
    private fun handleRecurrenceSelection(recurrenceType: RecurrenceType) {
        when (recurrenceType) {
            RecurrenceType.CUSTOM_DAYS -> {
                showCustomDaysDialog()
            }
            else -> {
                selectedRecurrence = recurrenceType
                binding.etRecurrence.setText(recurrenceType.getDisplayName())
                updateNextRecurrenceText()
            }
        }
    }
    
    private fun showCustomDaysDialog() {
        val dialogBinding = DialogNumberOfDaysBinding.inflate(layoutInflater)
        dialogBinding.textViewTitle.text = "Custom Recurrence"
        dialogBinding.etNumberOfDays.hint = "Enter number of days"
        
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .create()
        
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        dialogBinding.btnOk.setOnClickListener {
            val daysText = dialogBinding.etNumberOfDays.text.toString()
            if (daysText.isNotEmpty()) {
                try {
                    val days = daysText.toInt()
                    if (days > 0 && days <= 365) {
                        selectedRecurrence = RecurrenceType.CUSTOM_DAYS
                        binding.etRecurrence.setText("Every $days days")
                        updateNextRecurrenceText()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Please enter a number between 1 and 365", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter number of days", Toast.LENGTH_SHORT).show()
            }
        }
        
        dialog.show()
    }
    
    private fun updateNextRecurrenceText() {
        if (selectedRecurrence == RecurrenceType.DOES_NOT_REPEAT) {
            binding.textViewNextRecurrence.visibility = View.GONE
        } else {
            val nextRecurrence = selectedRecurrence.getNextRecurrenceDate(selectedTime)
            val dateFormat = java.text.SimpleDateFormat("dd/MM/yy HH:mm", java.util.Locale.getDefault())
            val nextRecurrenceText = "Next recurrence: ${dateFormat.format(nextRecurrence.time)}"
            binding.textViewNextRecurrence.text = nextRecurrenceText
            binding.textViewNextRecurrence.visibility = View.VISIBLE
        }
    }
}