package com.starter.app.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import com.starter.app.R
import com.starter.app.databinding.SettingActivityBinding
import com.starter.app.ui.base.BaseActivity

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: SettingActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupClickListeners()
        setupSwitchListeners()
        loadCurrentSettings()
    }

    private fun setupToolbar() {
        binding.imageViewBack.setOnClickListener(this)
        binding.textViewTitle.text = "Settings"
    }

    private fun setupClickListeners() {
        // General Settings
        binding.layoutNotifications.setOnClickListener(this)
        binding.layoutSound.setOnClickListener(this)
        binding.layoutVibration.setOnClickListener(this)
        binding.layoutLanguage.setOnClickListener(this)
        binding.layoutTheme.setOnClickListener(this)
        
        // Countdown Settings
        binding.layoutDefaultTime.setOnClickListener(this)
        binding.layoutTimeFormat.setOnClickListener(this)
        binding.layoutDateFormat.setOnClickListener(this)
        binding.layoutAutoDelete.setOnClickListener(this)
        
        // Reminder Settings
        binding.layoutReminderTime.setOnClickListener(this)
        binding.layoutReminderSound.setOnClickListener(this)
        binding.layoutReminderVibration.setOnClickListener(this)
        
        // Widget Settings
        binding.layoutWidgetTheme.setOnClickListener(this)
        binding.layoutWidgetSize.setOnClickListener(this)
        binding.layoutWidgetOpacity.setOnClickListener(this)
        
        // Advanced Settings
        binding.layoutBackup.setOnClickListener(this)
        binding.layoutRestore.setOnClickListener(this)
        binding.layoutExport.setOnClickListener(this)
        binding.layoutImport.setOnClickListener(this)
        
        // About
        binding.layoutAbout.setOnClickListener(this)
        binding.layoutRate.setOnClickListener(this)
        binding.layoutShare.setOnClickListener(this)
        binding.layoutPrivacy.setOnClickListener(this)
        binding.layoutTerms.setOnClickListener(this)
    }

    private fun setupSwitchListeners() {
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            // Handle notifications enable/disable
            saveSetting("notifications_enabled", isChecked)
        }

        binding.switchSound.setOnCheckedChangeListener { _, isChecked ->
            // Handle sound enable/disable
            saveSetting("sound_enabled", isChecked)
        }

        binding.switchVibration.setOnCheckedChangeListener { _, isChecked ->
            // Handle vibration enable/disable
            saveSetting("vibration_enabled", isChecked)
        }

        binding.switchAutoDelete.setOnCheckedChangeListener { _, isChecked ->
            // Handle auto delete enable/disable
            saveSetting("auto_delete_enabled", isChecked)
        }

        binding.switchReminderVibration.setOnCheckedChangeListener { _, isChecked ->
            // Handle reminder vibration enable/disable
            saveSetting("reminder_vibration_enabled", isChecked)
        }
    }

    private fun loadCurrentSettings() {
        // Load saved settings and update UI
        binding.switchNotifications.isChecked = getSetting("notifications_enabled", true)
        binding.switchSound.isChecked = getSetting("sound_enabled", true)
        binding.switchVibration.isChecked = getSetting("vibration_enabled", true)
        binding.switchAutoDelete.isChecked = getSetting("auto_delete_enabled", false)
        binding.switchReminderVibration.isChecked = getSetting("reminder_vibration_enabled", true)
        
        // Load current values
        binding.textViewLanguageValue.text = getSetting("language", "English")
        binding.textViewThemeValue.text = getSetting("theme", "Light")
        binding.textViewDefaultTimeValue.text = getSetting("default_time", "12:00")
        binding.textViewTimeFormatValue.text = getSetting("time_format", "24h")
        binding.textViewDateFormatValue.text = getSetting("date_format", "DD/MM/YYYY")
        binding.textViewAutoDeleteValue.text = getSetting("auto_delete_days", "30 days")
        binding.textViewReminderTimeValue.text = getSetting("reminder_time", "1 hour before")
        binding.textViewReminderSoundValue.text = getSetting("reminder_sound", "Default")
        binding.textViewWidgetThemeValue.text = getSetting("widget_theme", "Dark")
        binding.textViewWidgetSizeValue.text = getSetting("widget_size", "Medium")
        binding.textViewWidgetOpacityValue.text = getSetting("widget_opacity", "100%")
    }

    private fun saveSetting(key: String, value: Boolean) {
        val sharedPref = getSharedPreferences("app_settings", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    private fun saveSetting(key: String, value: String) {
        val sharedPref = getSharedPreferences("app_settings", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    private fun getSetting(key: String, defaultValue: Boolean): Boolean {
        val sharedPref = getSharedPreferences("app_settings", MODE_PRIVATE)
        return sharedPref.getBoolean(key, defaultValue)
    }

    private fun getSetting(key: String, defaultValue: String): String {
        val sharedPref = getSharedPreferences("app_settings", MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imageViewBack -> {
                finish()
            }
            R.id.layoutLanguage -> {
                showLanguageSelectionDialog()
            }
            R.id.layoutTheme -> {
                showThemeSelectionDialog()
            }
            R.id.layoutDefaultTime -> {
                showTimeSelectionDialog()
            }
            R.id.layoutTimeFormat -> {
                showTimeFormatSelectionDialog()
            }
            R.id.layoutDateFormat -> {
                showDateFormatSelectionDialog()
            }
            R.id.layoutAutoDelete -> {
                showAutoDeleteSelectionDialog()
            }
            R.id.layoutReminderTime -> {
                showReminderTimeSelectionDialog()
            }
            R.id.layoutReminderSound -> {
                showReminderSoundSelectionDialog()
            }
            R.id.layoutWidgetTheme -> {
                showWidgetThemeSelectionDialog()
            }
            R.id.layoutWidgetSize -> {
                showWidgetSizeSelectionDialog()
            }
            R.id.layoutWidgetOpacity -> {
                showWidgetOpacitySelectionDialog()
            }
            R.id.layoutAbout -> {
                showAboutDialog()
            }
            R.id.layoutRate -> {
                // Open app in Play Store
                showMessage("Opening Play Store...")
            }
            R.id.layoutShare -> {
                // Share app
                shareApp()
            }
            R.id.layoutPrivacy -> {
                // Open privacy policy
                showMessage("Opening Privacy Policy...")
            }
            R.id.layoutTerms -> {
                // Open terms and conditions
                showMessage("Opening Terms & Conditions...")
            }
            R.id.layoutBackup -> {
                // Backup data
                showMessage("Backing up data...")
            }
            R.id.layoutRestore -> {
                // Restore data
                showMessage("Restoring data...")
            }
            R.id.layoutExport -> {
                // Export data
                showMessage("Exporting data...")
            }
            R.id.layoutImport -> {
                // Import data
                showMessage("Importing data...")
            }
        }
    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf("English", "Spanish", "French", "German", "Italian", "Portuguese", "Russian", "Chinese", "Japanese", "Korean")
        showSingleChoiceDialog("Select Language", languages, binding.textViewLanguageValue.text.toString()) { selected ->
            binding.textViewLanguageValue.text = selected
            saveSetting("language", selected)
            showMessage("Language changed to $selected")
        }
    }

    private fun showThemeSelectionDialog() {
        val themes = arrayOf("Light", "Dark", "System Default")
        showSingleChoiceDialog("Select Theme", themes, binding.textViewThemeValue.text.toString()) { selected ->
            binding.textViewThemeValue.text = selected
            saveSetting("theme", selected)
            showMessage("Theme changed to $selected")
        }
    }

    private fun showTimeSelectionDialog() {
        // Show time picker dialog
        showMessage("Time selection dialog coming soon...")
    }

    private fun showTimeFormatSelectionDialog() {
        val formats = arrayOf("12h (AM/PM)", "24h")
        showSingleChoiceDialog("Select Time Format", formats, binding.textViewTimeFormatValue.text.toString()) { selected ->
            binding.textViewTimeFormatValue.text = selected.replace(" (AM/PM)", "")
            saveSetting("time_format", selected.replace(" (AM/PM)", ""))
        }
    }

    private fun showDateFormatSelectionDialog() {
        val formats = arrayOf("DD/MM/YYYY", "MM/DD/YYYY", "YYYY/MM/DD", "DD MMM YYYY", "MMM DD, YYYY")
        showSingleChoiceDialog("Select Date Format", formats, binding.textViewDateFormatValue.text.toString()) { selected ->
            binding.textViewDateFormatValue.text = selected
            saveSetting("date_format", selected)
        }
    }

    private fun showAutoDeleteSelectionDialog() {
        val options = arrayOf("Never", "7 days", "30 days", "90 days", "1 year")
        showSingleChoiceDialog("Auto Delete After", options, binding.textViewAutoDeleteValue.text.toString()) { selected ->
            binding.textViewAutoDeleteValue.text = selected
            saveSetting("auto_delete_days", selected)
        }
    }

    private fun showReminderTimeSelectionDialog() {
        val options = arrayOf("15 minutes before", "30 minutes before", "1 hour before", "2 hours before", "1 day before", "2 days before", "1 week before")
        showSingleChoiceDialog("Reminder Time", options, binding.textViewReminderTimeValue.text.toString()) { selected ->
            binding.textViewReminderTimeValue.text = selected
            saveSetting("reminder_time", selected)
        }
    }

    private fun showReminderSoundSelectionDialog() {
        val options = arrayOf("Default", "Bell", "Chime", "Whistle", "None")
        showSingleChoiceDialog("Reminder Sound", options, binding.textViewReminderSoundValue.text.toString()) { selected ->
            binding.textViewReminderSoundValue.text = selected
            saveSetting("reminder_sound", selected)
        }
    }

    private fun showWidgetThemeSelectionDialog() {
        val options = arrayOf("Light", "Dark", "Transparent", "Colored")
        showSingleChoiceDialog("Widget Theme", options, binding.textViewWidgetThemeValue.text.toString()) { selected ->
            binding.textViewWidgetThemeValue.text = selected
            saveSetting("widget_theme", selected)
        }
    }

    private fun showWidgetSizeSelectionDialog() {
        val options = arrayOf("Small", "Medium", "Large")
        showSingleChoiceDialog("Widget Size", options, binding.textViewWidgetSizeValue.text.toString()) { selected ->
            binding.textViewWidgetSizeValue.text = selected
            saveSetting("widget_size", selected)
        }
    }

    private fun showWidgetOpacitySelectionDialog() {
        val options = arrayOf("25%", "50%", "75%", "100%")
        showSingleChoiceDialog("Widget Opacity", options, binding.textViewWidgetOpacityValue.text.toString()) { selected ->
            binding.textViewWidgetOpacityValue.text = selected
            saveSetting("widget_opacity", selected)
        }
    }

    private fun showAboutDialog() {
        val aboutMessage = """
            Countdown Timer App
            Version 1.0.0
            
            A beautiful and functional countdown timer application 
            to help you track important events and deadlines.
            
            Developed with ❤️ for Android
        """.trimIndent()
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("About")
            .setMessage(aboutMessage)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun shareApp() {
        val shareIntent = android.content.Intent().apply {
            action = android.content.Intent.ACTION_SEND
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_TEXT, "Check out this amazing Countdown Timer app!")
        }
        startActivity(android.content.Intent.createChooser(shareIntent, "Share App"))
    }

    private fun showSingleChoiceDialog(title: String, options: Array<String>, currentValue: String, onSelection: (String) -> Unit) {
        val currentIndex = options.indexOf(currentValue).takeIf { it >= 0 } ?: 0
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(title)
            .setSingleChoiceItems(options, currentIndex) { dialog, which ->
                dialog.dismiss()
                onSelection(options[which])
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}