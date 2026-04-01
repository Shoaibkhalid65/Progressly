package com.example.progresstracker.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.progresstracker.MainActivity
import com.example.progresstracker.R

object MyNotificationManager {
    private const val CHANNEL_ID = "progress_channel_high_priority"

    fun createNotificationChannel(context: Context) {
        val name = context.getString(R.string.notification_channel_name)
        val descriptionText = context.getString(R.string.notification_channel_desc)
        // High importance ensures it heads-up and shows the status bar icon prominently
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    fun createNotification(context: Context){
        // Ensure the channel is always created before posting the notification
        createNotificationChannel(context)
        
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,0,intent, PendingIntent.FLAG_IMMUTABLE
        )
        // Use the new channel ID, and set priority to HIGH
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_recent_history) // Built-in clock/time icon
            .setContentTitle("My Notification")
            .setContentText("Hello World")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true) // Makes it persist 
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Ensures it shows on lock screen and status bar
            .setContentIntent(pendingIntent)


        with(NotificationManagerCompat.from(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notify(10, builder.build())
                } else {
                    // Fallback operation if permission is not granted
                    Toast.makeText(context, "Please grant notification permissions in settings to receive updates", Toast.LENGTH_SHORT).show()
                }
            } else {
                // For Android 12 and below, runtime permission is not required
                notify(10, builder.build())
            }
        }

    }

    fun cancelNotification(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            cancel(10) // 10 is the notification ID used in createNotification
        }
    }
}