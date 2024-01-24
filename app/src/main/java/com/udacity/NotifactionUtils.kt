package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0
fun NotificationManager.sendNotification(
    title: String,
    status: String,
    applicationContext: Context
) {

    val contentIntent =
        Intent(applicationContext, DetailActivity::class.java).putExtra("status", status)
            .putExtra("name", title)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val action = NotificationCompat.Action.Builder(
        R.drawable.ic_assistant_black_24dp,
        applicationContext.getString(R.string.notification_button),
        contentPendingIntent
    ).build()

    val builder = NotificationCompat.Builder(
        applicationContext,
        MainActivity.CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(title)
        .setContentText(applicationContext.getString(R.string.notification_description))

        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        .addAction(action)

        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}