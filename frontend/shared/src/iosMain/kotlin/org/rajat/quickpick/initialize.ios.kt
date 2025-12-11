package org.rajat.quickpick

import org.koin.core.context.stopKoin
import org.rajat.quickpick.di.initializeKoin

fun initializeIosDependencies() {

    println("🔄 initializeIosDependencies() called")

    runCatching { stopKoin() }

    initializeKoin {
        modules(
        )
    }
}
