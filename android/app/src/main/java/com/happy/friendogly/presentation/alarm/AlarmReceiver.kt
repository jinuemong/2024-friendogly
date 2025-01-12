package com.happy.friendogly.presentation.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.model.Message
import com.happy.friendogly.presentation.ui.MainActivity
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatLifecycleObserver
import com.happy.friendogly.presentation.ui.permission.AlarmPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AlarmReceiver : FirebaseMessagingService() {
    private lateinit var notificationManager: NotificationManager

    override fun onNewToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (token.isNotBlank()) {
                AppModule.getInstance().saveAlarmTokenUseCase.invoke(token)
            }
        }
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.data[ALARM_TYPE] == "CHAT") {
            saveMessage(message)
            showChatAlarm(message.data["senderName"], message.data["content"])
        } else if (message.data[ALARM_TYPE] == "FOOTPRINT") {
            showWoofAlarm(message.data[ALARM_TITLE], message.data[ALARM_BODY])
        }
    }

    private fun saveMessage(message: RemoteMessage) =
        CoroutineScope(Dispatchers.IO).launch {
            val memberId = message.data["senderMemberId"]!!.toLong()
            val name = message.data["senderName"] ?: ""
            val content = message.data["content"] ?: ""
            val createdAt: LocalDateTime = LocalDateTime.parse(message.data["createdAt"])
            val profileUrl = message.data["profilePictureUrl"] ?: ""
            val chatRoomId: Long = message.data["chatRoomId"]?.toLong() ?: -1L
            val message: ChatComponent =
                when (message.data["messageType"]) {
                    "CHAT" ->
                        Message.Other(
                            createdAt = createdAt,
                            member =
                                ChatMember(
                                    id = memberId,
                                    name = name,
                                    profileImageUrl = profileUrl,
                                ),
                            content = content,
                        )
                    "LEAVE" ->
                        ChatComponent.Leave(
                            createdAt = createdAt,
                            member =
                                ChatMember(
                                    id = memberId,
                                    name = name,
                                    profileImageUrl = profileUrl,
                                ),
                        )

                    "ENTER" ->
                        ChatComponent.Enter(
                            createdAt = createdAt,
                            member =
                                ChatMember(
                                    id = memberId,
                                    name = name,
                                    profileImageUrl = profileUrl,
                                ),
                        )
                    else -> error("잘못된 타입이 들어왔습니다.")
                }

            AppModule.getInstance().saveChatMessageUseCase(chatRoomId, message)
        }

    private fun showChatAlarm(
        title: String?,
        body: String?,
    ) = CoroutineScope(Dispatchers.IO).launch {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (AppModule.getInstance().getChatAlarmUseCase.invoke()
                .getOrDefault(true) && ChatLifecycleObserver.getInstance().isBackground
        ) {
            createNotificationChannel()
            deliverNotification(title, body)
        }
    }

    private fun showWoofAlarm(
        title: String?,
        body: String?,
    ) = CoroutineScope(Dispatchers.IO).launch {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (AppModule.getInstance().getWoofAlarmUseCase.invoke().getOrDefault(true)) {
            createNotificationChannel()
            deliverNotification(title, body)
        }
    }

    private fun createNotificationChannel() {
        if (AlarmPermission.isValidPermissionSDK()) {
            val notificationChannel =
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH,
                )
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(
                notificationChannel,
            )
        }
    }

    private fun deliverNotification(
        title: String?,
        body: String?,
    ) {
        val contentIntent = MainActivity.getIntent(this)
        val contentPendingIntent =
            PendingIntent.getActivity(
                this,
                DEFAULT_INTENT_ID,
                contentIntent,
                PendingIntent.FLAG_IMMUTABLE,
            )

        val builder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        private const val CHANNEL_ID = "alarm_id"
        private const val CHANNEL_NAME = "alam"
        private const val ALARM_TITLE = "title"
        private const val ALARM_BODY = "body"
        private const val ALARM_TYPE = "type"

        const val NOTIFICATION_ID = 0
        const val DEFAULT_INTENT_ID = 1
    }
}
