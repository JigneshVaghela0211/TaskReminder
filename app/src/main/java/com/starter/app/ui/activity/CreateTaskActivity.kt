package com.starter.app.ui.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.starter.app.databinding.CreateCountActivityBinding
import com.starter.app.databinding.DialogDateSelectionBinding
import com.starter.app.databinding.DialogNumberOfDaysBinding
import com.starter.app.ui.base.BaseActivity
import com.starter.app.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateTaskActivity : BaseActivity() {
    
    lateinit var binding: CreateCountActivityBinding
    private var selectedDate: Date = DateUtils.getTodayDate()
    
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
        setDefaultDate()
    }

    private fun setupDateInput() {
        binding.textInputDate.setOnClickListener {
            showDateSelectionDialog()
        }
        binding.textInputEditTextDate.setOnClickListener {
            showDateSelectionDialog()
        }
    }
    
    private fun setDefaultDate() {
        binding.textInputEditTextDate.setText(DateUtils.formatDateForDisplay(selectedDate))
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
                    if (days > 0) {
                        val newDate = DateUtils.addDaysToDate(DateUtils.getTodayDate(), days)
                        selectedDate = newDate
                        binding.textInputEditTextDate.setText(DateUtils.formatDateForDisplay(newDate))
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Please enter a positive number", Toast.LENGTH_SHORT).show()
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
        Toast.makeText(this, "Calendar event selection coming soon", Toast.LENGTH_SHORT).show()
    }
}