package org.rajat.quickpick.di

import kotlin.concurrent.Volatile

object TokenProvider {
    @Volatile
    var token: String? = null
}

