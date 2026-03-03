package org.rajat.quickpick.di.modules

import org.koin.dsl.module
import org.rajat.quickpick.presentation.viewmodel.VendorViewModel

val vendorModule = module {
    single { VendorViewModel(get()) }
}
