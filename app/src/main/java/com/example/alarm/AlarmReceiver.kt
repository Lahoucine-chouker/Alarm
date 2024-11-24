package com.example.alarm


import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.media.Ringtone
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        // Get the system's default alarm sound URI
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone: Ringtone = RingtoneManager.getRingtone(context, alarmUri)

        // Play the alarm sound
        ringtone.play()

        // Vibrate the device
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            val vibrationEffect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            vibrator.vibrate(vibrationEffect)
        }

        // Show a toast to notify the user
        Toast.makeText(context, "Alarm ringing!", Toast.LENGTH_SHORT).show()
    }
}