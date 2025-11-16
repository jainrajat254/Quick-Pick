package org.rajat.quickpick.di.modules

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.rajat.quickpick.data.remote.ReviewApiServiceImpl
import org.rajat.quickpick.data.repository.ReviewRepositoryImpl
import org.rajat.quickpick.domain.repository.ReviewRepository
import org.rajat.quickpick.domain.service.ReviewApiService
import org.rajat.quickpick.presentation.viewmodel.ReviewViewModel

val reviewModule = module {
    single<ReviewApiService> { ReviewApiServiceImpl(get()) }
    singleOf(::ReviewRepositoryImpl) bind ReviewRepository::class
    viewModel { ReviewViewModel(get()) }
}

