package org.rajat.quickpick.presentation.feature.vendor.components

import quickpick.composeapp.generated.resources.Res
import quickpick.composeapp.generated.resources.fcpizza
import quickpick.composeapp.generated.resources.fcpasta
import quickpick.composeapp.generated.resources.fcgarlicbread
import quickpick.composeapp.generated.resources.fcburgers
import quickpick.composeapp.generated.resources.fcfries
import quickpick.composeapp.generated.resources.fcshakes
import quickpick.composeapp.generated.resources.fcdosa
import quickpick.composeapp.generated.resources.fcidli
import quickpick.composeapp.generated.resources.fcvada
import quickpick.composeapp.generated.resources.fcuttapam
import quickpick.composeapp.generated.resources.fcmomos
import quickpick.composeapp.generated.resources.fcnoodles
import quickpick.composeapp.generated.resources.fcmanchurian
import quickpick.composeapp.generated.resources.fcbeverages
import quickpick.composeapp.generated.resources.fcsnacks
import quickpick.composeapp.generated.resources.fcdesserts
import quickpick.composeapp.generated.resources.fcicecream
import quickpick.composeapp.generated.resources.fcsandwiches
import quickpick.composeapp.generated.resources.fcwraps
import quickpick.composeapp.generated.resources.fcsalads
import quickpick.composeapp.generated.resources.fcjuices
import quickpick.composeapp.generated.resources.storeimage
import org.jetbrains.compose.resources.DrawableResource

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