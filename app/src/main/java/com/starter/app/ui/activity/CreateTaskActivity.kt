package com.starter.app.ui.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.starter.app.R
import com.starter.app.databinding.CreateCountActivityBinding
import com.starter.app.databinding.DialogDateSelectionBinding
import com.starter.app.databinding.DialogNumberOfDaysBinding
import com.starter.app.databinding.DialogRecurrenceSelectionBinding
import com.starter.app.databinding.DialogTimeSelectionBinding
import com.starter.app.databinding.DialogDaysBeforeDeadlineBinding
import com.starter.app.databinding.DialogFontSelectionBinding
import com.starter.app.ui.base.BaseActivity
import com.starter.app.adapters.RecurrenceAdapter
import com.starter.app.adapters.TimeUnitAdapter
import com.starter.app.adapters.FontAdapter
import com.starter.app.databinding.DialogTimeUnitSelectionBinding
import com.starter.app.utils.DateUtils
import com.starter.app.utils.RecurrenceType
import com.starter.app.utils.TimeUnitType
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateTaskActivity : BaseActivity(), View.OnClickListener {
    
    lateinit var binding: CreateCountActivityBinding
    private var selectedDate: Date = DateUtils.getTodayDate()
    private var selectedTime: Calendar = Calendar.getInstance()
    private var selectedRecurrence: RecurrenceType = RecurrenceType.DOES_NOT_REPEAT
    private var selectedTimeUnit: TimeUnitType = TimeUnitType.AUTO_SELECTION
    private var enteredCount: Int = 1
    private var selectedDaysBeforeDeadline: Int = 1
    private var selectedFont: String = "Default"
    
    override fun findFragmentPlaceHolder(): Int {
        return 0
    }

    override fun createViewBinding(): View {
        binding = CreateCountActivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOnClickedListeners()
        setupDateInput()
        setupTimeInput()
        setupRecurrenceInput()
        setupCountInput()
        setupDecreaseInput()
        setupFontInput()
        setDefaultDate()
        setDefaultTime()
        setDefaultRecurrence()
    }

    private fun setOnClickedListeners() {
        binding.imageViewBack.setOnClickListener(this)
        binding.colorCirclesLayout.setOnClickListener(this)
        binding.btnSelectTheme.setOnClickListener(this)
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
    
    private fun setupCountInput() {
        binding.textInputCountFor.setOnClickListener {
            showTimeUnitSelectionDialog()
        }
        binding.editTextCountFor.setOnClickListener {
            showTimeUnitSelectionDialog()
        }
        
        // Make edit text non-focusable like recurrence field
        binding.editTextCountFor.isFocusable = false
        binding.editTextCountFor.isFocusableInTouchMode = false
    }

    private fun setupDecreaseInput() {
        binding.textInputDecrease.setOnClickListener {
            showDaysBeforeDeadlineDialog()
        }
        binding.editTextDecrease.setOnClickListener {
            showDaysBeforeDeadlineDialog()
        }
        
        // Make edit text non-focusable like other fields
        binding.editTextDecrease.isFocusable = false
        binding.editTextDecrease.isFocusableInTouchMode = false
    }

    private fun setupFontInput() {
        binding.textInputFont.setOnClickListener {
            showFontSelectionDialog()
        }
        binding.editTextFont.setOnClickListener {
            showFontSelectionDialog()
        }
        
        // Make edit text non-focusable like other fields
        binding.editTextFont.isFocusable = false
        binding.editTextFont.isFocusableInTouchMode = false
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
                    showMessage("Please select a date after yesterday")
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
                        showMessage("Please enter a number between 1 and 365")
                    }
                } catch (e: NumberFormatException) {
                    showMessage("Please enter a valid number")
                }
            } else {
                showMessage("Please enter number of days")
            }
        }
        
        dialog.show()
    }
    
    private fun showCalendarEventSelection() {
        showMessage("Calendar event selection coming soon!")
    }


    
    private fun showTimeUnitSelectionDialog() {
        val dialogBinding = DialogTimeUnitSelectionBinding.inflate(layoutInflater)
        
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()
        
        val timeUnitTypes = listOf(
            TimeUnitType.AUTO_SELECTION,
            TimeUnitType.CONCISE_TIME_NOTATION,
            TimeUnitType.HOURS_MINUTES_SECONDS,
            TimeUnitType.DAYS,
            TimeUnitType.WEEKS,
            TimeUnitType.MONTHS,
            TimeUnitType.YEARS
        )
        
        val adapter = TimeUnitAdapter(timeUnitTypes) { timeUnitType ->
            // Validate count input
            try {

                dialog.dismiss()
                handleTimeUnitSelection(timeUnitType)
            } catch (e: NumberFormatException) {
                showMessage("Please enter a valid number")
            }
        }
        
        dialogBinding.recyclerViewTimeUnit.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        dialogBinding.recyclerViewTimeUnit.adapter = adapter
        
        dialog.show()
        
        // Let the dialog size itself to content
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        
        // Ensure the RecyclerView measures properly
        dialogBinding.recyclerViewTimeUnit.requestLayout()
    }
    
    private fun handleTimeUnitSelection(timeUnitType: TimeUnitType) {
        selectedTimeUnit = timeUnitType
//        enteredCount = count
        binding.editTextCountFor.setText(" ${timeUnitType.getUnitName()}")
        showMessage("Selected: ${timeUnitType.getUnitName()}")
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
        
        dialog.show()
        
        // Let the dialog size itself to content
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        
        // Ensure the RecyclerView measures properly
        dialogBinding.recyclerViewRecurrence.requestLayout()
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
                        showMessage("Please enter a number between 1 and 365")
                    }
                } catch (e: NumberFormatException) {
                    showMessage("Please enter a valid number")
                }
            } else {
                showMessage("Please enter number of days")
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

    private fun showDaysBeforeDeadlineDialog() {
        val dialogBinding = DialogDaysBeforeDeadlineBinding.inflate(layoutInflater)
        
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .create()
        
        // Set current value if available
        if (selectedDaysBeforeDeadline > 0) {
            dialogBinding.etDays.setText(selectedDaysBeforeDeadline.toString())
        }
        
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        dialogBinding.btnOk.setOnClickListener {
            val daysText = dialogBinding.etDays.text.toString()
            if (daysText.isNotEmpty()) {
                try {
                    val days = daysText.toInt()
                    if (days > 0 && days <= 365) {
                        selectedDaysBeforeDeadline = days
                        binding.editTextDecrease.setText("$days days")
                        dialog.dismiss()
                    } else {
                        showMessage("Please enter a number between 1 and 365")
                    }
                } catch (e: NumberFormatException) {
                    showMessage("Please enter a valid number")
                }
            } else {
                showMessage("Please enter number of days")
            }
        }
        
        dialog.show()
    }
    
    private fun showFontSelectionDialog() {
        val dialogBinding = DialogFontSelectionBinding.inflate(layoutInflater)
        
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()
        
        val fontOptions = listOf(
            "Default",
            "Arial",
            "Times New Roman",
            "Helvetica",
            "Georgia",
            "Verdana",
            "Roboto",
            "Open Sans",
            "Lato",
            "Montserrat",
            "Poppins"
        )
        
        val adapter = FontAdapter(fontOptions) { fontName ->
            dialog.dismiss()
            handleFontSelection(fontName)
        }
        
        dialogBinding.recyclerViewFonts.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        dialogBinding.recyclerViewFonts.adapter = adapter
        
        dialog.show()
        
        // Let the dialog size itself to content
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        
        // Ensure the RecyclerView measures properly
        dialogBinding.recyclerViewFonts.requestLayout()
    }
    
    private fun handleFontSelection(fontName: String) {
        selectedFont = fontName
        binding.editTextFont.setText(fontName)
        showMessage("Selected font: $fontName")
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imageViewBack -> {
                finish()
            }
            binding.colorCirclesLayout.id->{
                loadActivity(ColorPickerActivity::class.java).start()
            }
            binding.btnSelectTheme.id->{
                loadActivity(SelectThemeActivity::class.java).start()
            }
        }
    }
}