package com.sddody.study.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service


@Service
class FCMService {

    /**
     *
     * @param chatRoomTopic
     * @param title
     * @param message
     * 채팅방 구독하는 fcm 메시지
     */
    fun sendNotificationToChatRoom(chatRoomTopic: String, title: String, message: String) {
        println("chatRoom notifictation")
        println(chatRoomTopic + "," + title + "," + message)
        val notification = Notification(title, message)
        val message = Message.builder()
            .putData("title",title)
            .putData("message",message)
            .setTopic(chatRoomTopic)
            .build()

        FirebaseMessaging.getInstance().send(message)
    }

    fun sendNewNotification(topic: String, title: String, message: String, studyId : Long ?= null) {
        println("send new Notification")
        println("$topic,$title,$message")

        val actualId = studyId?.run {studyId} ?: 0

        val message = Message.builder()
            .putData("title",title)
            .putData("id",actualId.toString())
            .putData("message",message)
            .setTopic(topic)
            .build()

        FirebaseMessaging.getInstance().send(message)
    }
}