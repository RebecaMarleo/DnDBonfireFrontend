package com.example.dndbonfire.data

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MensajesFirebase: FirebaseMessagingService() {
    // envía token al servidor
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    // construye y envía las notificaciones
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }
}