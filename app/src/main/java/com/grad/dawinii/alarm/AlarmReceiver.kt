package com.grad.dawinii.alarm

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.grad.dawinii.R
import com.grad.dawinii.view.main.MainScreenActivity


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val nextActivity = Intent(context, MainScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, nextActivity, 0)
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, "Dawinii")
                .setSmallIcon((R.drawable.ic_pill))
                .setContentTitle("Dawinii")
                .setContentText("It's time to take your medicine")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(123, builder.build())

        var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val mediaPlayer = MediaPlayer.create(context, alarmUri)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

}