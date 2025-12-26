package org.rajat.quickpick.presentation.feature.payment

import android.app.Activity
import android.util.Log
import org.json.JSONObject


actual fun openRazorpayCheckout(platformActivity: Any, razorpayKeyId: String, razorpayOrderId: String, amountInPaise: Long?) {
    try {
        val activity = platformActivity as? Activity ?: return

        val checkoutClass = try {
            Class.forName("com.razorpay.Checkout")
        } catch (e: ClassNotFoundException) {
            Log.e("RAZORPAYDEBUG", "Razorpay Checkout class not found: ${e.message}")
            return
        }

        val checkoutInstance = checkoutClass.getDeclaredConstructor().newInstance()

        try {
            val setKeyMethod = checkoutClass.getMethod("setKeyID", String::class.java)
            setKeyMethod.invoke(checkoutInstance, razorpayKeyId)
        } catch (_: Exception) {
        }

        val options = JSONObject()
        if (amountInPaise != null) {
            options.put("amount", amountInPaise)
        }
        options.put("order_id", razorpayOrderId)
        options.put("currency", "INR")

        try {
            val openMethod = checkoutClass.getMethod("open", Activity::class.java, JSONObject::class.java)
            openMethod.invoke(checkoutInstance, activity, options)
        } catch (e: NoSuchMethodException) {
            try {
                val openMethodAlt = checkoutClass.getMethod("open", Activity::class.java, String::class.java)
                openMethodAlt.invoke(checkoutInstance, activity, options.toString())
            } catch (ex: Exception) {
                Log.e("RAZORPAYDEBUG", "openRazorpayCheckout invoke failed: ${ex.message}")
            }
        } catch (e: Exception) {
            Log.e("RAZORPAYDEBUG", "openRazorpayCheckout failed: ${e.message}")
        }

    } catch (e: Exception) {
        Log.e("RAZORPAYDEBUG", "openRazorpayCheckout unexpected exception: ${e.message}")
    }
}
