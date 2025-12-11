package org.rajat.quickpick.di.modules

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.rajat.quickpick.data.remote.AdminApiServiceImpl
import org.rajat.quickpick.data.repository.AdminRepositoryImpl
import org.rajat.quickpick.domain.repository.AdminRepository
import org.rajat.quickpick.domain.service.AdminApiService
import org.rajat.quickpick.presentation.viewmodel.AdminViewModel

val adminModule = module {

    single<AdminApiService> { AdminApiServiceImpl(get()) }
    singleOf(::AdminRepositoryImpl) bind AdminRepository::class
    viewModel { AdminViewModel(get()) }
}
