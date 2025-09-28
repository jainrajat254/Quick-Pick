package org.rajat.quickpick.utils.toast

import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UINavigationController
import platform.UIKit.UIViewController

actual fun showToast(message: String?) {
    val topController = getTopViewController()

    val alert = UIAlertController.alertControllerWithTitle(
        title = "Toast",
        message = message,
        preferredStyle = UIAlertControllerStyleAlert
    )
    val action = UIAlertAction.actionWithTitle(
        title = "OK",
        style = UIAlertActionStyleDefault,
        handler = null
    )
    alert.addAction(action)
    topController?.presentViewController(alert, animated = true, completion = null)
}

fun getTopViewController(): UIViewController? {
    var topController: UIViewController? =
        UIApplication.sharedApplication().keyWindow?.rootViewController
    while (topController is UINavigationController) {
        topController = topController.visibleViewController
    }
    return topController
}