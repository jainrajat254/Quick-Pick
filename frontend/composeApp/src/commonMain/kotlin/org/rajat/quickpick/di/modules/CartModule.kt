package org.rajat.quickpick.di.modules

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.rajat.quickpick.data.remote.CartApiServiceImpl
import org.rajat.quickpick.data.repository.CartRepositoryImpl
import org.rajat.quickpick.domain.repository.CartRepository
import org.rajat.quickpick.domain.service.CartApiService
import org.rajat.quickpick.presentation.viewmodel.CartViewModel

val cartModule = module {
    single<CartApiService> { CartApiServiceImpl(get()) }
    single<CartRepository> { CartRepositoryImpl(get()) }
    viewModelOf(::CartViewModel)
}

