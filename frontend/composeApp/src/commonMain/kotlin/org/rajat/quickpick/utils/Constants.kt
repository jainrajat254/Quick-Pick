package org.rajat.quickpick.utils

object Constants {
    const val DATASTORE_FILE_NAME = "quick_pick.preferences_pb"
//    const val BASE_URL = "https://0771701c2b50.ngrok-free.app"
//    const val BASE_URL = "https://jq4ftr-ip-139-5-1-214.tunnelmole.net"
    const val BASE_URL = "https://jessika-interganglionic-goggly.ngrok-free.dev"
//    const val BASE_URL = "https://quick-pick-backend-nc96.onrender.com"
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
        const val CHANGE_PASSWORD = "/api/auth/change-password"
        const val RESET_PASSWORD_OTP = "/api/auth/reset-password-otp"
        const val SEND_PASSWORD_OTP = "/api/auth/send-password-otp"
        const val SEND_EMAIL_OTP = "/api/auth/send-email-otp"
        const val VERIFY_EMAIL_OTP = "/api/auth/verify-email-otp"


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

        // Search endpoints
        const val SEARCH_VENDORS = "/api/search/vendors"
        const val SEARCH_VENDORS_COLLEGE = "/api/search/vendors/college"
        const val SEARCH_VENDOR_BY_ID = "/api/search/vendors/" // + {vendorId}
        const val SEARCH_MENU_ITEMS = "/api/search/menu-items"
        const val STRICT_SEARCH_MENU_ITEMS = "/api/search/menu-items/strict-search"

        // Order endpoints (Student)
        const val CREATE_ORDER = "/api/orders"
        const val GET_ORDER_BY_ID = "/api/orders/" // + {orderId}
        const val GET_MY_ORDERS = "/api/orders/my-orders"
        const val GET_MY_ORDERS_BY_STATUS = "/api/orders/my-orders/status/" // + {status}
        const val CANCEL_ORDER = "/api/orders/" // + {orderId} + "/cancel"
        const val GET_MY_ORDER_STATS = "/api/orders/my-orders/stats"

        // Order endpoints (Vendor)
        const val GET_PENDING_ORDERS_VENDOR = "/api/orders/vendor/orders/pending"
        const val GET_VENDOR_ORDERS_PAGINATED = "/api/orders/vendor/orders/paginated"
        const val GET_VENDOR_ORDER_BY_ID = "/api/orders/vendor/orders/" // + {orderId}
        const val GET_VENDOR_ORDERS_BY_STATUS = "/api/orders/vendor/orders/status/" // + {status}
        const val UPDATE_ORDER_STATUS = "/api/orders/vendor/orders/" // + {orderId} + "/status"
        const val GET_VENDOR_ORDER_STATS = "/api/orders/vendor/orders/stats"

        // Cart endpoints
        const val ADD_TO_CART = "/api/cart/items"
        const val GET_CART = "/api/cart"
        const val UPDATE_CART_ITEM = "/api/cart/items/" // + {menuItemId}
        const val REMOVE_FROM_CART = "/api/cart/items/" // + {menuItemId}
        const val CLEAR_CART = "/api/cart"
        const val CREATE_ORDER_FROM_CART = "/api/orders/from-cart"
    }
}
