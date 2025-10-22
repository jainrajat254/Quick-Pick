package org.rajat.quickpick.di.modules

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.rajat.quickpick.data.remote.MenuCategoryApiServiceImpl
import org.rajat.quickpick.data.repository.MenuCategoryRepositoryImpl
import org.rajat.quickpick.domain.repository.MenuCategoryRepository
import org.rajat.quickpick.domain.service.MenuCategoryApiService
import org.rajat.quickpick.presentation.viewmodel.MenuCategoryViewModel

val menuCategoryModule = module {

    single<MenuCategoryApiService> { MenuCategoryApiServiceImpl(get()) }
    singleOf(::MenuCategoryRepositoryImpl) bind MenuCategoryRepository::class
    viewModel { MenuCategoryViewModel(get()) }
}
