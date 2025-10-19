package org.rajat.quickpick.utils

object Constants {
    const val DATASTORE_FILE_NAME = "quick_pick.preferences_pb"
    const val BASE_URL = "https://your-api-url.com"

    object Endpoints {
        const val USER_LOGIN = "/api/auth/login/user"
        const val VENDOR_LOGIN = "/api/auth/login/vendor"
        const val USER_REGISTER = "/api/auth/register/user"
        const val VENDOR_REGISTER = "/api/auth/register/vendor"
    }
}
