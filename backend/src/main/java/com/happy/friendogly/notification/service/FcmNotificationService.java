package com.happy.friendogly.notification.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.notification.domain.NotificationType;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Profile("!local")
public class FcmNotificationService implements NotificationService {

    private final FirebaseApp firebaseApp;

    public FcmNotificationService(FirebaseApp firebaseApp) {
        this.firebaseApp = firebaseApp;
    }

    @Override
    public void sendNotification(String title, String content, String receiverToken) {
        Message message = Message.builder()
                .putData("type", NotificationType.FOOTPRINT.toString())
                .putData("title", title)
                .putData("body", content)
                .setToken(receiverToken)
                .build();
        try {
            FirebaseMessaging.getInstance(this.firebaseApp).send(message);
        } catch (FirebaseMessagingException e) {
            throw new FriendoglyException("FCM을 통해 사용자에게 알림을 보내는 과정에서 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void sendNotification(String title, String content, List<String> receiverTokens) {
        MulticastMessage message = MulticastMessage.builder()
                .putData("type", NotificationType.FOOTPRINT.toString())
                .putData("title", title)
                .putData("body", content)
                .addAllTokens(receiverTokens)
                .build();
        try {
            FirebaseMessaging.getInstance(this.firebaseApp).sendEachForMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new FriendoglyException("FCM을 통해 사용자에게 알림을 보내는 과정에서 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}