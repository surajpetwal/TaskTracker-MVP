package com.surajpetwal.tasktracker.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.surajpetwal.tasktracker.R
import com.surajpetwal.tasktracker.utils.NotificationHelper
import com.surajpetwal.tasktracker.utils.NotificationScheduler
import java.util.Calendar

class NotificationSettingsDialog : DialogFragment() {

    private lateinit var switchDailySummary: Switch
    private lateinit var switchTaskReminders: Switch
    private lateinit var tvDailyTime: TextView
    private lateinit var btnSetTime: Button
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private var selectedHour = 9
    private var selectedMinute = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_notification_settings, null)

        // Initialize views
        switchDailySummary = view.findViewById(R.id.switchDailySummary)
        switchTaskReminders = view.findViewById(R.id.switchTaskReminders)
        tvDailyTime = view.findViewById(R.id.tvDailyTime)
        btnSetTime = view.findViewById(R.id.btnSetTime)
        btnSave = view.findViewById(R.id.btnSave)
        btnCancel = view.findViewById(R.id.btnCancel)

        // Load saved preferences
        loadPreferences()

        // Set up time picker
        btnSetTime.setOnClickListener {
            showTimePicker()
        }

        // Save button
        btnSave.setOnClickListener {
            savePreferences()
            dismiss()
        }

        // Cancel button
        btnCancel.setOnClickListener {
            dismiss()
        }

        builder.setView(view)
            .setTitle("Notification Settings")

        return builder.create()
    }

    private fun showTimePicker() {
        TimePickerDialog(requireContext(), { _, hour, minute ->
            selectedHour = hour
            selectedMinute = minute
            updateTimeDisplay()
        }, selectedHour, selectedMinute, false).show()
    }

    private fun updateTimeDisplay() {
        val amPm = if (selectedHour >= 12) "PM" else "AM"
        val displayHour = if (selectedHour > 12) selectedHour - 12 else if (selectedHour == 0) 12 else selectedHour
        val minuteStr = String.format("%02d", selectedMinute)
        tvDailyTime.text = "Daily summary time: $displayHour:$minuteStr $amPm"
    }

    private fun loadPreferences() {
        val prefs = requireContext().getSharedPreferences("notification_prefs", 0)
        switchDailySummary.isChecked = prefs.getBoolean("daily_summary_enabled", true)
        switchTaskReminders.isChecked = prefs.getBoolean("task_reminders_enabled", true)
        selectedHour = prefs.getInt("daily_summary_hour", 9)
        selectedMinute = prefs.getInt("daily_summary_minute", 0)
        updateTimeDisplay()
    }

    private fun savePreferences() {
        val prefs = requireContext().getSharedPreferences("notification_prefs", 0)
        with(prefs.edit()) {
            putBoolean("daily_summary_enabled", switchDailySummary.isChecked)
            putBoolean("task_reminders_enabled", switchTaskReminders.isChecked)
            putInt("daily_summary_hour", selectedHour)
            putInt("daily_summary_minute", selectedMinute)
            apply()
        }

        // Apply notification settings
        val scheduler = NotificationScheduler(requireContext())
        val notificationHelper = NotificationHelper(requireContext())

        if (switchDailySummary.isChecked) {
            scheduler.scheduleDailySummary(selectedHour, selectedMinute)
        } else {
            scheduler.cancelDailySummary()
        }

        if (switchTaskReminders.isChecked) {
            scheduler.scheduleTaskReminders()
        } else {
            scheduler.cancelTaskReminders()
        }
    }

    companion object {
        fun newInstance(): NotificationSettingsDialog {
            return NotificationSettingsDialog()
        }
    }
}
