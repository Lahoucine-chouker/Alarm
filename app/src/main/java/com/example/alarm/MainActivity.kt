package com.example.alarm


import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var setAlarmButton: Button
    private lateinit var alarmStatusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setAlarmButton = findViewById(R.id.setAlarmButton)
        alarmStatusText = findViewById(R.id.alarmStatusText)

        setAlarmButton.setOnClickListener {
            showTimePickerDialog()
        }
    }

    private fun showTimePickerDialog() {
        // Initialize TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                Log.d("MainActivity", "Time selected: $hourOfDay:$minute")
                setAlarm(hourOfDay, minute)
            },
            0, 0, true
        )
        timePickerDialog.show()
    }

    private fun setAlarm(hourOfDay: Int, minute: Int) {
        try {
            Log.d("MainActivity", "Setting alarm for $hourOfDay:$minute")

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            // Format time for display (HH:mm)
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val formattedTime = timeFormat.format(calendar.time)

            // Get AlarmManager system service
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // Create an Intent to trigger the AlarmReceiver
            val alarmIntent = Intent(this, AlarmReceiver::class.java)

            // Create PendingIntent for AlarmReceiver
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                alarmIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_MUTABLE
            )

            // Set the alarm to trigger at the selected time
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

            // Log the time when the alarm is set
            Log.d("MainActivity", "Alarm set for: $formattedTime")

            // Update UI with the alarm time (display it in alarmStatusText)
            alarmStatusText.text = "Alarm set for: $formattedTime"
            Toast.makeText(this, "Alarm set for: $formattedTime", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e("MainActivity", "Error setting alarm", e)
        }
    }
}
