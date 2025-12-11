package org.rajat.quickpick.di.modules

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.rajat.quickpick.data.remote.AuthApiServiceImpl
import org.rajat.quickpick.data.repository.AuthRepositoryImpl
import org.rajat.quickpick.domain.repository.AuthRepository
import org.rajat.quickpick.domain.service.AuthApiService
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel

val authModule = module {

    single<AuthApiService> { AuthApiServiceImpl(get()) }
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    viewModel { AuthViewModel(get()) }
}
