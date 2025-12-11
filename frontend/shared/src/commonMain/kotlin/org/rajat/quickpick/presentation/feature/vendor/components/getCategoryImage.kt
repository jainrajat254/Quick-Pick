package org.rajat.quickpick.presentation.feature.vendor.components

import org.jetbrains.compose.resources.DrawableResource
import quickpick.shared.generated.resources.Res
import quickpick.shared.generated.resources.*

fun getCategoryImage(category: String): DrawableResource {
    return when (category.lowercase()) {
        "pizza" -> Res.drawable.fcpizza
        "pasta" -> Res.drawable.fcpasta
        "garlic bread" -> Res.drawable.fcgarlicbread
        "burgers" -> Res.drawable.fcburgers
        "fries" -> Res.drawable.fcfries
        "shakes" -> Res.drawable.fcshakes
        "dosa" -> Res.drawable.fcdosa
        "idli" -> Res.drawable.fcidli
        "vada" -> Res.drawable.fcvada
        "uttapam" -> Res.drawable.fcuttapam
        "momos" -> Res.drawable.fcmomos
        "noodles" -> Res.drawable.fcnoodles
        "manchurian" -> Res.drawable.fcmanchurian
        "beverages", "drinks", "tea", "coffee" -> Res.drawable.fcbeverages
        "snacks" -> Res.drawable.fcsnacks
        "desserts" -> Res.drawable.fcdesserts
        "ice cream" -> Res.drawable.fcicecream
        "sandwiches" -> Res.drawable.fcsandwiches
        "wraps" -> Res.drawable.fcwraps
        "salads" -> Res.drawable.fcsalads
        "juices" -> Res.drawable.fcjuices
        else -> Res.drawable.storeimage
    }
}