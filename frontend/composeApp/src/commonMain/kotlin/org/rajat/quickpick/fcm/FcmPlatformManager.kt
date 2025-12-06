package org.rajat.quickpick.fcm

expect object FcmPlatformManager {
    fun initializeAndSendToken(authToken: String)
    fun sendTokenToServer(fcmToken: String, authToken: String)
    fun removeTokenFromServer(authToken: String)
}