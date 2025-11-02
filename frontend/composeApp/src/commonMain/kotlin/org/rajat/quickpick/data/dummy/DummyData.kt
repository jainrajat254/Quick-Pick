package org.rajat.quickpick.data.dummy

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import org.rajat.quickpick.domain.modal.menuitems.CreateMenuItemResponse
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.OrderItemX
import org.rajat.quickpick.domain.modal.profile.GetStudentProfileResponse
import org.rajat.quickpick.domain.modal.search.GetVendorByIDResponse
import org.rajat.quickpick.presentation.feature.cart.CartItem
import org.rajat.quickpick.presentation.feature.profile.FaqItem
import org.rajat.quickpick.presentation.feature.profile.components.FaqItem
import org.rajat.quickpick.presentation.navigation.Routes

object DummyData {

    val colleges = listOf(
        College("1", "Indian Institute of Technology, Delhi"),
        College("2", "Delhi University"),
        College("3", "Jawaharlal Nehru University"),
        College("4", "Jamia Millia Islamia"),
        College("5", "Indian Institute of Technology, Mumbai"),
        College("6", "Indian Institute of Technology, Bangalore"),
        College("7", "BITS Pilani"),
        College("8", "Amity University")
    )

    val branches = listOf(
        Branch("1", "Computer Science"),
        Branch("2", "Information Technology"),
        Branch("3", "Electronics & Communication"),
        Branch("4", "Mechanical Engineering"),
        Branch("5", "Civil Engineering"),
        Branch("6", "Business Administration"),
        Branch("7", "Commerce"),
        Branch("8", "Arts & Humanities")
    )

    val genders = listOf("Male", "Female", "Prefer not to say")


    val vendors = listOf(
        GetVendorByIDResponse(
            id = "v1",
            storeName = "Pizza Paradise",
            vendorName = "Rajesh Kumar",
            profileImageUrl = null,
            collegeName = "Indian Institute of Technology, Delhi",
            foodCategories = listOf("Pizza", "Pasta", "Garlic Bread"),
            email = "rajesh@pizzaparadise.com",
            phone = "+91-9876543210",
            address = "Shop 12, IIT Delhi Campus",
            emailVerified = true,
            phoneVerified = true,
            role = "VENDOR",
            vendorDescription = "Best authentic Italian pizzas and pasta in campus. Made with fresh ingredients and love!"
        ),
        GetVendorByIDResponse(
            id = "v2",
            storeName = "Chai & Snacks Corner",
            vendorName = "Amit Sharma",
            profileImageUrl = null,
            collegeName = "Indian Institute of Technology, Delhi",
            foodCategories = listOf("Tea", "Coffee", "Snacks", "Samosa"),
            email = "amit@chaicorner.com",
            phone = "+91-9876543211",
            address = "Near Main Gate, IIT Delhi",
            emailVerified = true,
            phoneVerified = true,
            role = "VENDOR",
            vendorDescription = "Your favorite chai spot! Fresh tea, coffee, and crispy snacks all day long."
        ),
        GetVendorByIDResponse(
            id = "v3",
            storeName = "Burger Hub",
            vendorName = "Priya Singh",
            profileImageUrl = null,
            collegeName = "Indian Institute of Technology, Delhi",
            foodCategories = listOf("Burgers", "Fries", "Shakes"),
            email = "priya@burgerhub.com",
            phone = "+91-9876543212",
            address = "Food Court, IIT Delhi",
            emailVerified = true,
            phoneVerified = true,
            role = "VENDOR",
            vendorDescription = "Juicy burgers, crispy fries, and thick shakes. Fast food done right!"
        ),
        GetVendorByIDResponse(
            id = "v4",
            storeName = "South Indian Delights",
            vendorName = "Venkatesh Iyer",
            profileImageUrl = null,
            collegeName = "Indian Institute of Technology, Delhi",
            foodCategories = listOf("Dosa", "Idli", "Vada", "Uttapam"),
            email = "venkat@southindian.com",
            phone = "+91-9876543213",
            address = "Hostel Area, IIT Delhi",
            emailVerified = true,
            phoneVerified = false,
            role = "VENDOR",
            vendorDescription = "Authentic South Indian breakfast and meals. Crispy dosas and fluffy idlis!"
        ),
        GetVendorByIDResponse(
            id = "v5",
            storeName = "The Sandwich Shop",
            vendorName = "Neha Gupta",
            profileImageUrl = null,
            collegeName = "Indian Institute of Technology, Delhi",
            foodCategories = listOf("Sandwiches", "Wraps", "Salads"),
            email = "neha@sandwichshop.com",
            phone = "+91-9876543214",
            address = "Library Block, IIT Delhi",
            emailVerified = true,
            phoneVerified = true,
            role = "VENDOR",
            vendorDescription = "Healthy and delicious sandwiches, wraps, and fresh salads for quick meals."
        ),

        )

    // Food categories used by vendors
    val foodCategories = listOf(
        "Beverages", "Snacks", "Main Course", "Desserts", "Fast Food",
        "Indian", "Chinese", "South Indian", "North Indian", "Continental",
        "Sandwiches", "Pizza", "Burgers", "Rolls", "Salads", "Juices",
        "Tea/Coffee", "Ice Cream", "Sweets", "Breakfast"
    )

    // Dummy Menu Items for Pizza Paradise (v1)
    val pizzaParadiseMenu = listOf(
        CreateMenuItemResponse(
            id = "m1",
            name = "Margherita Pizza",
            description = "Classic pizza with tomato sauce, mozzarella cheese, and fresh basil",
            price = 249.0,
            category = "Pizza",
            veg = true,
            available = true,
            imageUrl = "https://example.com/margherita.jpg",
            vendorId = "v1",
            quantity = 50,
            createdAt = "2025-01-15T10:00:00Z",
            updatedAt = "2025-10-20T15:30:00Z"
        ),
        CreateMenuItemResponse(
            id = "m2",
            name = "Pepperoni Pizza",
            description = "Loaded with pepperoni slices and extra cheese",
            price = 349.0,
            category = "Pizza",
            veg = false,
            available = true,
            imageUrl = "https://example.com/pepperoni.jpg",
            vendorId = "v1",
            quantity = 30,
            createdAt = "2025-01-15T10:00:00Z",
            updatedAt = "2025-10-20T15:30:00Z"
        ),
        CreateMenuItemResponse(
            id = "m3",
            name = "Veggie Supreme",
            description = "Bell peppers, onions, mushrooms, olives, and tomatoes",
            price = 299.0,
            category = "Pizza",
            veg = true,
            available = true,
            imageUrl = null,
            vendorId = "v1",
            quantity = 40,
            createdAt = "2025-01-15T10:00:00Z",
            updatedAt = "2025-10-20T15:30:00Z"
        ),
        CreateMenuItemResponse(
            id = "m4",
            name = "Paneer Tikka Pizza",
            description = "Indian fusion pizza with paneer tikka and special sauce",
            price = 329.0,
            category = "Pizza",
            veg = true,
            available = true,
            imageUrl = "https://example.com/paneer-tikka-pizza.jpg",
            vendorId = "v1",
            quantity = 25,
            createdAt = "2025-01-15T10:00:00Z",
            updatedAt = "2025-10-20T15:30:00Z"
        ),
        CreateMenuItemResponse(
            id = "m5",
            name = "Cheese Pasta",
            description = "Creamy white sauce pasta with extra cheese",
            price = 179.0,
            category = "Pasta",
            veg = true,
            available = true,
            imageUrl = "https://example.com/cheese-pasta.jpg",
            vendorId = "v1",
            quantity = 35,
            createdAt = "2025-01-15T10:00:00Z",
            updatedAt = "2025-10-20T15:30:00Z"
        ),
        CreateMenuItemResponse(
            id = "m6",
            name = "Arrabiata Pasta",
            description = "Spicy red sauce pasta with herbs",
            price = 159.0,
            category = "Pasta",
            veg = true,
            available = true,
            imageUrl = null,
            vendorId = "v1",
            quantity = 35,
            createdAt = "2025-01-15T10:00:00Z",
            updatedAt = "2025-10-20T15:30:00Z"
        ),
        CreateMenuItemResponse(
            id = "m7",
            name = "Garlic Bread",
            description = "Crispy garlic bread with butter and herbs",
            price = 99.0,
            category = "Garlic Bread",
            veg = true,
            available = true,
            imageUrl = "https://example.com/garlic-bread.jpg",
            vendorId = "v1",
            quantity = 60,
            createdAt = "2025-01-15T10:00:00Z",
            updatedAt = "2025-10-20T15:30:00Z"
        ),
        CreateMenuItemResponse(
            id = "m8",
            name = "Cheesy Garlic Bread",
            description = "Garlic bread topped with melted mozzarella",
            price = 129.0,
            category = "Garlic Bread",
            veg = true,
            available = true,
            imageUrl = null,
            vendorId = "v1",
            quantity = 40,
            createdAt = "2025-01-15T10:00:00Z",
            updatedAt = "2025-10-20T15:30:00Z"
        )
    )

    // Dummy Menu Items for Burger Hub (v3)
    val burgerHubMenu = listOf(
        CreateMenuItemResponse(
            id = "m20",
            name = "Classic Veg Burger",
            description = "Crispy veg patty with lettuce, tomato, and mayo",
            price = 89.0,
            category = "Burgers",
            veg = true,
            available = true,
            imageUrl = "https://example.com/veg-burger.jpg",
            vendorId = "v3",
            quantity = 50,
            createdAt = "2025-02-01T10:00:00Z",
            updatedAt = "2025-10-21T12:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m21",
            name = "Chicken Burger",
            description = "Grilled chicken patty with special sauce",
            price = 129.0,
            category = "Burgers",
            veg = false,
            available = true,
            imageUrl = "https://example.com/chicken-burger.jpg",
            vendorId = "v3",
            quantity = 40,
            createdAt = "2025-02-01T10:00:00Z",
            updatedAt = "2025-10-21T12:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m22",
            name = "Paneer Burger",
            description = "Spicy paneer patty with mint chutney",
            price = 99.0,
            category = "Burgers",
            veg = true,
            available = true,
            imageUrl = null,
            vendorId = "v3",
            quantity = 35,
            createdAt = "2025-02-01T10:00:00Z",
            updatedAt = "2025-10-21T12:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m23",
            name = "Double Cheese Burger",
            description = "Two patties with double cheese and special sauce",
            price = 159.0,
            category = "Burgers",
            veg = true,
            available = true,
            imageUrl = "https://example.com/double-burger.jpg",
            vendorId = "v3",
            quantity = 25,
            createdAt = "2025-02-01T10:00:00Z",
            updatedAt = "2025-10-21T12:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m24",
            name = "French Fries (Regular)",
            description = "Crispy golden fries",
            price = 59.0,
            category = "Fries",
            veg = true,
            available = true,
            imageUrl = "https://example.com/fries.jpg",
            vendorId = "v3",
            quantity = 100,
            createdAt = "2025-02-01T10:00:00Z",
            updatedAt = "2025-10-21T12:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m25",
            name = "Peri Peri Fries",
            description = "Spicy peri peri seasoned fries",
            price = 79.0,
            category = "Fries",
            veg = true,
            available = true,
            imageUrl = null,
            vendorId = "v3",
            quantity = 80,
            createdAt = "2025-02-01T10:00:00Z",
            updatedAt = "2025-10-21T12:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m26",
            name = "Chocolate Shake",
            description = "Rich and creamy chocolate shake",
            price = 89.0,
            category = "Shakes",
            veg = true,
            available = true,
            imageUrl = "https://example.com/chocolate-shake.jpg",
            vendorId = "v3",
            quantity = 50,
            createdAt = "2025-02-01T10:00:00Z",
            updatedAt = "2025-10-21T12:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m27",
            name = "Mango Shake",
            description = "Fresh mango shake with cream",
            price = 99.0,
            category = "Shakes",
            veg = true,
            available = true,
            imageUrl = null,
            vendorId = "v3",
            quantity = 40,
            createdAt = "2025-02-01T10:00:00Z",
            updatedAt = "2025-10-21T12:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m28",
            name = "Oreo Shake",
            description = "Cookies and cream oreo shake",
            price = 109.0,
            category = "Shakes",
            veg = true,
            available = false,
            imageUrl = "https://example.com/oreo-shake.jpg",
            vendorId = "v3",
            quantity = 0,
            createdAt = "2025-02-01T10:00:00Z",
            updatedAt = "2025-10-21T12:00:00Z"
        )
    )

    // Dummy Menu Items for South Indian Delights (v4)
    val southIndianMenu = listOf(
        CreateMenuItemResponse(
            id = "m30",
            name = "Plain Dosa",
            description = "Crispy rice and lentil crepe",
            price = 49.0,
            category = "Dosa",
            veg = true,
            available = true,
            imageUrl = "https://example.com/plain-dosa.jpg",
            vendorId = "v4",
            quantity = 80,
            createdAt = "2025-02-05T09:00:00Z",
            updatedAt = "2025-10-22T08:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m31",
            name = "Masala Dosa",
            description = "Dosa filled with spiced potato filling",
            price = 69.0,
            category = "Dosa",
            veg = true,
            available = true,
            imageUrl = "https://example.com/masala-dosa.jpg",
            vendorId = "v4",
            quantity = 70,
            createdAt = "2025-02-05T09:00:00Z",
            updatedAt = "2025-10-22T08:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m32",
            name = "Cheese Dosa",
            description = "Dosa with melted cheese filling",
            price = 89.0,
            category = "Dosa",
            veg = true,
            available = true,
            imageUrl = null,
            vendorId = "v4",
            quantity = 50,
            createdAt = "2025-02-05T09:00:00Z",
            updatedAt = "2025-10-22T08:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m33",
            name = "Idli (2 pcs)",
            description = "Soft steamed rice cakes with sambhar and chutney",
            price = 39.0,
            category = "Idli",
            veg = true,
            available = true,
            imageUrl = "https://example.com/idli.jpg",
            vendorId = "v4",
            quantity = 100,
            createdAt = "2025-02-05T09:00:00Z",
            updatedAt = "2025-10-22T08:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m34",
            name = "Vada (2 pcs)",
            description = "Crispy lentil donuts with sambhar and chutney",
            price = 45.0,
            category = "Vada",
            veg = true,
            available = true,
            imageUrl = "https://example.com/vada.jpg",
            vendorId = "v4",
            quantity = 80,
            createdAt = "2025-02-05T09:00:00Z",
            updatedAt = "2025-10-22T08:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m35",
            name = "Onion Uttapam",
            description = "Thick pancake topped with onions",
            price = 59.0,
            category = "Uttapam",
            veg = true,
            available = true,
            imageUrl = null,
            vendorId = "v4",
            quantity = 40,
            createdAt = "2025-02-05T09:00:00Z",
            updatedAt = "2025-10-22T08:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m36",
            name = "Mix Veg Uttapam",
            description = "Uttapam with onions, tomatoes, and capsicum",
            price = 69.0,
            category = "Uttapam",
            veg = true,
            available = true,
            imageUrl = "https://example.com/uttapam.jpg",
            vendorId = "v4",
            quantity = 35,
            createdAt = "2025-02-05T09:00:00Z",
            updatedAt = "2025-10-22T08:00:00Z"
        )
    )

    // Dummy Menu Items for Momos Junction (v6)
    val momosJunctionMenu = listOf(
        CreateMenuItemResponse(
            id = "m40",
            name = "Veg Steamed Momos (8 pcs)",
            description = "Steamed vegetable momos with spicy chutney",
            price = 79.0,
            category = "Momos",
            veg = true,
            available = true,
            imageUrl = "https://example.com/veg-momos.jpg",
            vendorId = "v6",
            quantity = 100,
            createdAt = "2025-03-01T10:00:00Z",
            updatedAt = "2025-10-21T18:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m41",
            name = "Chicken Steamed Momos (8 pcs)",
            description = "Juicy chicken momos with special sauce",
            price = 99.0,
            category = "Momos",
            veg = false,
            available = true,
            imageUrl = "https://example.com/chicken-momos.jpg",
            vendorId = "v6",
            quantity = 80,
            createdAt = "2025-03-01T10:00:00Z",
            updatedAt = "2025-10-21T18:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m42",
            name = "Paneer Fried Momos (8 pcs)",
            description = "Crispy fried paneer momos",
            price = 109.0,
            category = "Momos",
            veg = true,
            available = true,
            imageUrl = null,
            vendorId = "v6",
            quantity = 60,
            createdAt = "2025-03-01T10:00:00Z",
            updatedAt = "2025-10-21T18:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m43",
            name = "Veg Hakka Noodles",
            description = "Stir-fried noodles with vegetables",
            price = 89.0,
            category = "Noodles",
            veg = true,
            available = true,
            imageUrl = "https://example.com/veg-noodles.jpg",
            vendorId = "v6",
            quantity = 50,
            createdAt = "2025-03-01T10:00:00Z",
            updatedAt = "2025-10-21T18:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m44",
            name = "Chicken Noodles",
            description = "Spicy chicken noodles",
            price = 119.0,
            category = "Noodles",
            veg = false,
            available = true,
            imageUrl = null,
            vendorId = "v6",
            quantity = 40,
            createdAt = "2025-03-01T10:00:00Z",
            updatedAt = "2025-10-21T18:00:00Z"
        ),
        CreateMenuItemResponse(
            id = "m45",
            name = "Veg Manchurian",
            description = "Vegetable balls in spicy Manchurian sauce",
            price = 99.0,
            category = "Manchurian",
            veg = true,
            available = true,
            imageUrl = "https://example.com/manchurian.jpg",
            vendorId = "v6",
            quantity = 45,
            createdAt = "2025-03-01T10:00:00Z",
            updatedAt = "2025-10-21T18:00:00Z"
        )
    )

    // Helper function to get menu items by vendor ID
    fun getMenuItemsByVendorId(vendorId: String): List<CreateMenuItemResponse> {
        return when (vendorId) {
            "v1" -> pizzaParadiseMenu
            "v3" -> burgerHubMenu
            "v4" -> southIndianMenu
            "v5" -> momosJunctionMenu
            else -> emptyList()
        }
    }

    fun getMenuItemsByVendorIdAndCategory(vendorId: String, category: String): List<CreateMenuItemResponse> {
        return getMenuItemsByVendorId(vendorId).filter { it.category == category }
    }

    fun getCategoriesByVendorId(vendorId: String): List<String> {
        return getMenuItemsByVendorId(vendorId)
            .mapNotNull { it.category }
            .distinct()
            .sorted()
    }

    data class VendorRating(
        val vendorId: String,
        val averageRating: Double,
        val totalReviews: Int
    )

    val vendorRatings = listOf(
        VendorRating("v1", 4.5, 128),
        VendorRating("v2", 4.2, 95),
        VendorRating("v3", 4.3, 87),
        VendorRating("v4", 4.7, 156),
        VendorRating("v5", 4.1, 63),
        VendorRating("v6", 4.6, 142),
        VendorRating("v7", 4.4, 71),
        VendorRating("v8", 4.0, 54)
    )

    fun getRatingByVendorId(vendorId: String): VendorRating? {
        return vendorRatings.find { it.vendorId == vendorId }
    }
}

data class College(
    val id: String,
    val name: String
)

data class Branch(
    val id: String,
    val name: String
)










//My Orders Screen Dummy Data
val dummyActiveOrders = listOf(
    GetOrderByIdResponse(
        id = "1", storeName = "Campus Cafe", totalAmount = 12.50,
        createdAt = "2025-10-23T10:15:00Z", orderStatus = "PENDING",
        orderItems = listOf(OrderItemX(menuItemName = "Coffee Latte", quantity = 1),
            OrderItemX(menuItemName = "Coffee Latte", quantity = 1))
    ),
    GetOrderByIdResponse(
        id = "2", storeName = "Hostel Kitchen B", totalAmount = 25.00,
        createdAt = "2025-10-23T09:30:00Z", orderStatus = "ACCEPTED",
        orderItems = listOf(
            OrderItemX(menuItemName = "Veggie Wrap", quantity = 1),
            OrderItemX(menuItemName = "Orange Juice", quantity = 1)
        )
    ),
    GetOrderByIdResponse(
        id = "3", storeName = "Pizza Place", totalAmount = 35.75,
        createdAt = "2025-10-22T18:00:00Z", orderStatus = "READY_FOR_PICKUP",
        orderItems = listOf(OrderItemX(menuItemName = "Pepperoni Pizza", quantity = 1))
    )
)
val dummyCompletedOrders = listOf(
    GetOrderByIdResponse(
        id = "4", storeName = "Canteen Central", totalAmount = 55.00,
        createdAt = "2025-10-21T13:00:00Z", orderStatus = "COMPLETED",
        orderItems = listOf(
            OrderItemX(menuItemName = "Chicken Curry Meal", quantity = 1),
            OrderItemX(menuItemName = "Rice", quantity = 1)
        )
    ),
    GetOrderByIdResponse(
        id = "5", storeName = "Burger Spot", totalAmount = 18.25,
        createdAt = "2025-10-20T19:45:00Z", orderStatus = "COMPLETED",
        orderItems = listOf(
            OrderItemX(menuItemName = "Bean and Veggie Burger", quantity = 1),
            OrderItemX(menuItemName = "Fries", quantity = 1)
        )
    )
)
val dummyCancelledOrders = listOf(
    GetOrderByIdResponse(
        id = "6", storeName = "Sweet Treats", totalAmount = 8.50,
        createdAt = "2025-10-19T11:00:00Z", orderStatus = "CANCELLED",
        orderItems = listOf(OrderItemX(menuItemName = "Strawberry Cheesecake", quantity = 1))
    )
)
val allOrders = dummyActiveOrders + dummyCompletedOrders + dummyCancelledOrders


//Dummy Profile
val profile = GetStudentProfileResponse(
    collegeName = "KIET GROUP OF INSTITUTIONS",
    fullName = "Pulkit Verma",
    email = "pulkitverma2008@gmail.com",
    profileImageUrl = "",
    department = "CSE",
    id = "",
    phone = "7252908379",
    phoneVerified = false,
    emailVerified = false,
    role = "STUDENT"
)


//Order items
val sampleItems1 = listOf<CartItem>(
    CartItem("1", "Classic Burger", 12.00, 1, null),
    CartItem("2", "Fries", 4.50, 2, null)
)



// Dummy data for the FAQ screen
val dummyFaqList = listOf(
    FaqItem(
        id = 1,
        question = "How do I place an order?",
        answer = "To place an order, navigate to the Home screen, select a vendor, choose your items, and add them to your cart. Once you're ready, go to your cart and tap 'Proceed to Checkout'."
    ),
    FaqItem(
        id = 2,
        question = "How does 'Cash on Take Away' work?",
        answer = "When you select 'Cash on Take Away', you don't need to pay online. Simply place your order, and you will pay in cash directly to the vendor when you pick up your food."
    ),
    FaqItem(
        id = 3,
        question = "How can I track my order?",
        answer = "You can see the status of your order (e.g., Pending, Preparing, Ready for Pickup) in the 'My Orders' section of the app under the 'Active' tab."
    ),
    FaqItem(
        id = 4,
        question = "How do I cancel my order?",
        answer = "You can cancel an order only if its status is still 'Pending'. Go to 'My Orders', find the active order, and tap on it to see the details. If available, a 'Cancel Order' button will be visible."
    ),
    FaqItem(
        id = 5,
        question = "What if I have an issue with my order?",
        answer = "If you have any problems with your food, payment, or pickup, please visit the 'Contact Us' section in the profile menu to get in touch with our support team."
    )
)