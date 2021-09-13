package com.verindrzya.mytodo.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.verindrzya.mytodo.R

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams){

    companion object {
        private const val CHANNEL_ID = "101"
        private const val CHANNEL_NAME = "Reminder Notification"
        private const val NOTIFICATION_ID = 201
    }

    override fun doWork(): Result {

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24)
            .setContentTitle("Todo is stacking")
            .setContentText("Hey man, your task is pilling up! Let's see what's next on the list, shall we?")
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)

            val notificationManager: NotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(NOTIFICATION_ID, builder.build())
        }

        return Result.success()
    }

}