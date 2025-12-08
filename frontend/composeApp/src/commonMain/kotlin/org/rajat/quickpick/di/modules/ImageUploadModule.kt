package org.rajat.quickpick.di.modules

import org.koin.dsl.module
import org.rajat.quickpick.data.remote.ImageUploadApiServiceImpl
import org.rajat.quickpick.data.repository.ImageUploadRepositoryImpl
import org.rajat.quickpick.domain.repository.ImageUploadRepository
import org.rajat.quickpick.domain.service.ImageUploadApiService

val imageUploadModule = module {
    single<ImageUploadApiService> { ImageUploadApiServiceImpl(get()) }
    single<ImageUploadRepository> { ImageUploadRepositoryImpl(get()) }
}

