package org.rajat.quickpick.di

import org.rajat.quickpick.data.local.datastoreHelper
import org.rajat.quickpick.di.modules.adminManagementModule
import org.rajat.quickpick.di.modules.adminModule
import org.rajat.quickpick.di.modules.authModule
import org.rajat.quickpick.di.modules.collegeModule
import org.rajat.quickpick.di.modules.dataStoreModule
import org.rajat.quickpick.di.modules.networkModule
import org.rajat.quickpick.di.modules.profileModule
import org.rajat.quickpick.utils.Constants.DATASTORE_FILE_NAME

val quickPickModules = listOf(
    datastoreHelper(datastoreFileName = DATASTORE_FILE_NAME),
    dataStoreModule,
    networkModule,
    authModule,
    adminModule,
    collegeModule,
    adminManagementModule,
    profileModule
)