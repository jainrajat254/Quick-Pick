package org.rajat.quickpick.di.modules

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.rajat.quickpick.data.remote.MenuItemApiServiceImpl
import org.rajat.quickpick.data.repository.MenuItemRepositoryImpl
import org.rajat.quickpick.domain.repository.MenuItemRepository
import org.rajat.quickpick.domain.service.MenuItemApiService
import org.rajat.quickpick.presentation.viewmodel.MenuItemViewModel

val menuItemModule = module {
    single<MenuItemApiService> { MenuItemApiServiceImpl(get()) }
    singleOf(::MenuItemRepositoryImpl) bind MenuItemRepository::class
    viewModel { MenuItemViewModel(get(), get()) }
}
