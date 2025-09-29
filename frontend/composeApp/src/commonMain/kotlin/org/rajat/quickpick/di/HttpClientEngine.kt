package org.rajat.quickpick.di

import io.ktor.client.engine.HttpClientEngineFactory

expect fun getKtorEngine(): HttpClientEngineFactory<*>
