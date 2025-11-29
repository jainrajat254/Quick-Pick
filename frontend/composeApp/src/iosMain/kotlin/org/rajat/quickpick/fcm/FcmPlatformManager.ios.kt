package org.rajat.quickpick.fcm

actual object FcmPlatformManager {

    actual fun initializeAndSendToken() {
        println("iOS FCM initializeAndSendToken called")
    }

    actual fun sendTokenToServer(fcmToken: String) {
        println("iOS FCM sendTokenToServer called: $fcmToken")
    }

    actual fun removeTokenFromServer() {
        println("iOS FCM removeTokenFromServer called")
    }
}

