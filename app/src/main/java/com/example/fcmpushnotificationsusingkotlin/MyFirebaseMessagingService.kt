package com.example.fcmpushnotificationsusingkotlin

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    val CHANNEL_ID: String =
        "My Channel" //there can be many channels, you can specify a specific channel id to work with
    val REQUEST_CODE: Int = 100 //for pending intent

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if(message.notification!=null){
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }
    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteViews =
            RemoteViews("com.example.fcmpushnotificationsusingkotlin", R.layout.notification_layout)
        remoteViews.setTextViewText(R.id.titleNotificationLayout, title)
        remoteViews.setTextViewText(R.id.messageNotificationLayout, message)
        remoteViews.setImageViewResource(
            R.id.imageView,
            R.drawable.baseline_notifications_active_24
        )
        return remoteViews
    }

    // NotificationManager is a class in the Android SDK that provides functionality to manage notifications shown to the user.
    // It allows you to create, update, and cancel notifications that appear in the notification drawer or as pop-up alerts.

fun generateNotification(title: String, message: String){
        var iNotify = Intent(applicationContext, MainActivity::class.java)
        iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //This flag is used to start an activity in a new task. If the activity is already running in another task, it will be moved to the front.

        var pendingIntent: PendingIntent = PendingIntent.getActivities(
            this, REQUEST_CODE,
            arrayOf(iNotify), PendingIntent.FLAG_IMMUTABLE
        ) //For example, if you want to create a notification that opens an activity when clicked, you would create a PendingIntent that opens the activity and attach it to the notification.
        //A PendingIntent in Android is a token that you can give to another application (like NotificationManager, AlarmManager, etc.)
        //which allows this application to execute a predefined piece of code on your behalf at a later time, even if your application is not running.


        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentIntent(pendingIntent)  //Pending Intent
                .setContentTitle("Tired of forgetting things? note it down here")
                .setSubText("Message from Valen")
                .setAutoCancel(true)

        builder = builder.setContent(getRemoteView(title, message))


        var notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            var name: CharSequence = "Custom Channel"
            var description: String = "Channel for push notification"
            var importance = NotificationManager.IMPORTANCE_DEFAULT

            var channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            notificationManager.createNotificationChannel(channel)
        }
notificationManager.notify(0, builder.build())

    }

}




