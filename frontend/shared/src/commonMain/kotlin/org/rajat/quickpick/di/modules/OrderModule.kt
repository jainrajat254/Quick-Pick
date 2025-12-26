package org.rajat.quickpick.di.modules

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.rajat.quickpick.data.remote.OrderApiServiceImpl
import org.rajat.quickpick.data.remote.PaymentApiServiceImpl
import org.rajat.quickpick.data.repository.OrderRepositoryImpl
import org.rajat.quickpick.domain.repository.OrderRepository
import org.rajat.quickpick.domain.service.OrderApiService
import org.rajat.quickpick.domain.service.PaymentApiService
import org.rajat.quickpick.presentation.viewmodel.OrderViewModel

val orderModule = module {
    single<OrderApiService> { OrderApiServiceImpl(get()) }
    single<PaymentApiService> { PaymentApiServiceImpl(get()) }
    single<OrderRepository> { OrderRepositoryImpl(get()) }
    viewModelOf(::OrderViewModel)
}
