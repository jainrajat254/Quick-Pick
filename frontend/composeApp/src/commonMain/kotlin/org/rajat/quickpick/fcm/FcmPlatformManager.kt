package org.rajat.quickpick.fcm

expect object FcmPlatformManager {
    fun initializeAndSendToken()
    fun sendTokenToServer(fcmToken: String)
    fun removeTokenFromServer()
}