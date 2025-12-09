package org.rajat.quickpick.di.modules

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.rajat.quickpick.data.remote.ProfileApiServiceImpl
import org.rajat.quickpick.data.repository.ProfileRepositoryImpl
import org.rajat.quickpick.domain.repository.ProfileRepository
import org.rajat.quickpick.domain.service.ProfileApiService
import org.rajat.quickpick.presentation.viewmodel.ProfileViewModel

val profileModule = module {

    single<ProfileApiService> { ProfileApiServiceImpl(get()) }
    singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class
    viewModel { ProfileViewModel(get(), get()) }
}