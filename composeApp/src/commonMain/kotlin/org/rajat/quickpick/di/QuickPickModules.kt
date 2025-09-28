package org.rajat.quickpick.di

import org.rajat.quickpick.data.local.datastoreHelper
import org.rajat.quickpick.di.modules.dataStoreModule
import org.rajat.quickpick.utils.Constants.DATASTORE_FILE_NAME

val quickPickModules = listOf(
    datastoreHelper(datastoreFileName = DATASTORE_FILE_NAME),
    dataStoreModule
)