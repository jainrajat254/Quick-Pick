package org.rajat.quickpick.di.modules

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.rajat.quickpick.data.remote.SearchApiServiceImpl
import org.rajat.quickpick.data.repository.SearchRepositoryImpl
import org.rajat.quickpick.domain.repository.SearchRepository
import org.rajat.quickpick.domain.service.SearchApiService
import org.rajat.quickpick.presentation.viewmodel.HomeViewModel

val homeModule = module {
    single<SearchApiService> { SearchApiServiceImpl(get()) }
    singleOf(::SearchRepositoryImpl) bind SearchRepository::class
    viewModel { HomeViewModel(get()) }
}

