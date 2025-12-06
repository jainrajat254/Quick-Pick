package org.rajat.quickpick.fcm

actual object FcmPlatformManager {

    actual fun initializeAndSendToken(authToken: String) {
        println("iOS FCM initializeAndSendToken called with authToken")
    }

    actual fun sendTokenToServer(fcmToken: String, authToken: String) {
        println("iOS FCM sendTokenToServer called: $fcmToken")
    }

    actual fun removeTokenFromServer(authToken: String) {
        println("iOS FCM removeTokenFromServer called")
    }
}
