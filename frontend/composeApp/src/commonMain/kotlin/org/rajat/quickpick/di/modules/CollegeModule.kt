package org.rajat.quickpick.di.modules

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.rajat.quickpick.data.remote.CollegeApiServiceImpl
import org.rajat.quickpick.data.repository.CollegeRepositoryImpl
import org.rajat.quickpick.domain.repository.CollegeRepository
import org.rajat.quickpick.domain.service.CollegeApiService
import org.rajat.quickpick.presentation.viewmodel.CollegeViewModel

val collegeModule = module {

    single<CollegeApiService> { CollegeApiServiceImpl(get()) }
    singleOf(::CollegeRepositoryImpl) bind CollegeRepository::class
    viewModel { CollegeViewModel(get()) }
}
