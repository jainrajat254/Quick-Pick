package org.rajat.quickpick.utils

object Constants {
    const val DATASTORE_FILE_NAME = "quick_pick.preferences_pb"
    const val BASE_URL = "https://c19f727204c5.ngrok-free.app"

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
        const val RESEND_EMAIL_VERIFICATION = "/api/auth/resend-verification"// + {email} + /{userType}


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

        const val CREATE_MENU_ITEMS = "/api/menu-items"
        const val GET_MY_VENUE_ITEM = "/api/menu-items/my-menu" //
        const val GET_MY_AVAILABLE_MENU_ITEMS = "/api/menu-items/my-menu/available"
        const val GET_MY_MENU_ITEMS_BY_CATEGORY = "/api/menu-items/my-menu/category/" // + {category}
        const val SEARCH_MY_MENU_ITEMS = "/api/menu-items/my-menu/search" // {query}
        const val UPDATE_MENU_ITEMS= "/api/menu-items/" // {menuItemId}
        const val MY_MENU_PRICE_RANGE = "/api/menu-items/my-menu/price-range" // + {min} + {max}
        const val MY_MENU_CATEGORIES = "/api/menu-items/my-menu/categories"
        const val MY_MENU_STATS = "/api/menu-items/my-menu/stats"
        const val VENDOR_MENU = "/api/menu-items/vendor/" // + {vendorId}
        const val MENU_ITEM_BULK_DELETE = "/api/menu-items/bulk"


        const val CREATE_REVIEW = "/api/reviews"
        const val GET_REVIEW_BY_ID = "/api/reviews/" // + {reviewId}
        const val GET_REVIEWS_BY_VENDOR = "/api/reviews/vendor/" // + {vendorId}
        const val GET_REVIEWS_BY_VENDOR_PAGINATED = "/api/reviews/vendor/" // + {vendorId} + "/paginated"
        const val GET_REVIEWS_BY_USER = "/api/reviews/user/my-reviews"
        const val GET_REVIEWS_BY_RATING = "/api/reviews/vendor/" // + {vendorId} + "/rating/" + {rating}
        const val GET_REVIEWS_BY_RATING_ABOVE = "/api/reviews/vendor/" // + {vendorId} + "/rating-above/" + {minRating}
        const val GET_VENDOR_RATING = "/api/reviews/vendor/" // + {vendorId} + "/statistics"
        const val UPDATE_REVIEW = "/api/reviews/" // + {reviewId}
        const val DELETE_REVIEW = "/api/reviews/" // + {reviewId}
        const val DELETE_REVIEW_ADMIN = "/api/reviews/admin/" // + {reviewId}
        const val GET_REVIEW_COUNT_BY_VENDOR = "/api/reviews/vendor/" // + {vendorId} + "/count"
        const val GET_MY_REVIEW_COUNT = "/api/reviews/user/count"
        const val HAS_USER_REVIEWED_ORDER = "/api/reviews/order/" // + {orderId} + "/exists"

    }
}
