package org.rajat.quickpick.utils

object Constants {
    const val DATASTORE_FILE_NAME = "quick_pick.preferences_pb"
    const val BASE_URL = "https://40132fcb3460.ngrok-free.app"

    object Endpoints {
        // Auth
        const val USER_LOGIN = "/api/auth/login/user"
        const val VENDOR_LOGIN = "/api/auth/login/vendor"
        const val USER_REGISTER = "/api/auth/register/user"
        const val VENDOR_REGISTER = "/api/auth/register/vendor"
        const val REFRESH_TOKEN = "/api/auth/refresh-token"
        const val FORGOT_PASSWORD = "/api/auth/forgot-password"
        const val RESET_PASSWORD = "/api/auth/reset-password"
        const val LOGOUT = "/api/auth/logout"

        // Admin
        const val ADMIN_LOGIN = "/api/admin/login"
        const val ADMIN_CREATE = "/api/admin/create"

        // College
        const val CREATE_COLLEGE = "/api/colleges/create"
        const val DELETE_COLLEGE = "/api/colleges/delete/" // + {id}
        const val UPDATE_COLLEGE = "/api/colleges/update/" // + {id}
        const val GET_COLLEGE_BY_ID = "/api/colleges/public/" // + {id}
        const val GET_ALL_CITIES = "/api/colleges/public/cities"
        const val GET_ALL_STATES = "/api/colleges/public/states"
        const val GET_COLLEGES_BY_CITY = "/api/colleges/public/city/" // + {city}
        const val GET_COLLEGES_BY_STATE = "/api/colleges/public/state/" // + {state}
        const val GET_TOTAL_COLLEGE_COUNT = "/api/colleges/public/count"
        const val GET_PUBLIC_COLLEGES = "/api/colleges/public"

        const val USERS = "/api/admin-management/users"
        const val VENDORS = "/api/admin-management/vendors"
        const val PENDING_VENDORS = "/api/admin-management/vendors/pending"

        const val GET_STUDENT_PROFILE = "/api/profile/user"
        const val GET_VENDOR_PROFILE = "/api/profile/vendor"

        const val GET_DEFAULT_VENDOR_CATEGORIES = "/api/vendor-categories/default"
        const val RESET_VENDOR_CATEGORIES_TO_DEFAULT = "/api/vendor-categories/reset"
        const val UPDATE_VENDOR_CATEGORIES = "/api/vendor-categories"

    }
}
