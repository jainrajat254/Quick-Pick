package org.rajat.quickpick.di.modules

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.rajat.quickpick.data.remote.AdminApiServiceImpl
import org.rajat.quickpick.data.remote.AdminManagementApiServiceImpl
import org.rajat.quickpick.data.repository.AdminManagementRepositoryImpl
import org.rajat.quickpick.data.repository.AdminRepositoryImpl
import org.rajat.quickpick.domain.repository.AdminManagementRepository
import org.rajat.quickpick.domain.repository.AdminRepository
import org.rajat.quickpick.domain.service.AdminApiService
import org.rajat.quickpick.domain.service.AdminManagementApiService
import org.rajat.quickpick.presentation.viewmodel.AdminManagementViewModel
import org.rajat.quickpick.presentation.viewmodel.AdminViewModel

val adminManagementModule = module {

    single<AdminManagementApiService> { AdminManagementApiServiceImpl(get()) }
    singleOf(::AdminManagementRepositoryImpl) bind AdminManagementRepository::class
    viewModel { AdminManagementViewModel(get()) }
}
