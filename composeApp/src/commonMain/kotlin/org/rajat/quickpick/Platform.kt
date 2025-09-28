package org.rajat.quickpick

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform