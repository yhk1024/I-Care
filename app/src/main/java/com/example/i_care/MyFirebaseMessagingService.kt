package com.example.i_care

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Log the message
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(it.body)
        }
    }

    // 토큰을 얻기 위한 코드
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
        // Send the token to your server or save it locally
    }

    //sendNotification(messageBody: String?) 함수는 Firebase 클라우드 메시징을 통해 받은 메시지를 처리하고, 안드로이드 시스템 트레이에 알림을 표시하는 역할을 합니다.
    //messageBody: 알림에 표시할 메시지의 본문을 포함하는 문자열. 이 값은 Firebase 메시지를 통해 전달됩니다.
        //channelId: 알림 채널 ID를 가져옵니다. 알림 채널은 Android 8.0 (API 레벨 26) 이상에서 사용되며, 알림의 설정을 관리하는 데 사용됩니다.
            // default_notification_channel_id는 res/values/strings.xml 파일에 정의되어 있습니다.
        //NotificationCompat.Builder: 알림을 생성하는 데 사용되는 빌더 객체.
            //.setSmallIcon(R.drawable.ic_notification): 알림에 표시될 작은 아이콘을 설정합니다.
            //.setContentTitle(getString(R.string.notification_title)): 알림의 제목을 설정합니다.
                // notification_title도 res/values/strings.xml에 정의되어 있습니다.
            //.setContentText(messageBody): 알림의 본문을 설정합니다.
            //.setPriority(NotificationCompat.PRIORITY_HIGH): 알림의 우선순위를 높게 설정합니다.
            //.setAutoCancel(true): 사용자가 알림을 탭하면 자동으로 알림이 사라지도록 설정합니다.
        //NotificationManagerCompat: 알림을 관리하고 표시하는 클래스.
        //NotificationChannel: 알림 채널을 정의하는 클래스.
            //channelId: 채널 ID.
            //"Channel human readable title": 사용자에게 표시될 채널 이름.
            //NotificationManager.IMPORTANCE_DEFAULT: 채널의 중요도를 설정합니다.
        //notificationManager.notify: 알림을 시스템 트레이에 표시합니다.
            //0: 알림의 ID. 동일한 ID를 사용하면 이전 알림이 업데이트됩니다.
        //notificationBuilder.build(): 빌더를 사용하여 알림 객체를 생성합니다.

    private fun sendNotification(messageBody: String?) {
        val channelId = getString(R.string.default_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}
