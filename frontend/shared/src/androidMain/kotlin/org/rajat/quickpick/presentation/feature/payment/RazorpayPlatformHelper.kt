package org.rajat.quickpick.presentation.feature.payment

import android.app.Activity
import android.util.Log
import org.json.JSONObject

object RazorpayPlatformHelper {
    @JvmStatic
    fun openCheckout(platformActivity: Any, razorpayKeyId: String, razorpayOrderId: String, amountInPaise: Long?) {
        try {
            val activity = platformActivity as? Activity ?: run {
                Log.e("RAZORPAYDEBUG", "openCheckout: platformActivity is not an Activity")
                return
            }

            val checkoutClass = Class.forName("com.razorpay.Checkout")
            val checkoutInstance = checkoutClass.getDeclaredConstructor().newInstance()

            // setKeyID
            try {
                val setKeyMethod = checkoutClass.getMethod("setKeyID", String::class.java)
                setKeyMethod.invoke(checkoutInstance, razorpayKeyId)
            } catch (e: Exception) {
                // ignore
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
                val openMethodAlt = checkoutClass.getMethod("open", Activity::class.java, String::class.java)
                openMethodAlt.invoke(checkoutInstance, activity, options.toString())
            }

        } catch (e: Exception) {
            Log.e("RAZORPAYDEBUG", "RazorpayPlatformHelper.openCheckout failed: ${e.message}")
        }
    }
}

