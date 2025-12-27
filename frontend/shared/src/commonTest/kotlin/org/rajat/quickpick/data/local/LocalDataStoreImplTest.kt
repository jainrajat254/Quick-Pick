package org.rajat.quickpick.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

private class TestPreferences(private val map: Map<Preferences.Key<*>, Any?> = emptyMap()) : Preferences {
    private val backing: Map<Preferences.Key<*>, Any?> = map.toMap()

    override fun asMap(): Map<Preferences.Key<*>, Any?> = backing

    override operator fun <T> get(key: Preferences.Key<T>): T? = backing[key] as T?
}

private class TestMutablePreferences(private val backing: MutableMap<Preferences.Key<*>, Any?> = mutableMapOf()) : MutablePreferences {
    override fun asMap(): Map<Preferences.Key<*>, Any?> = backing.toMap()

    override operator fun <T> get(key: Preferences.Key<T>): T? = backing[key] as T?

    override operator fun <T> set(key: Preferences.Key<T>, value: T?) {
        if (value == null) backing.remove(key) else backing[key] = value
    }

    override fun remove(key: Preferences.Key<*>) {
        backing.remove(key)
    }

    fun toPreferences(): Preferences = TestPreferences(backing.toMap())
}

private class FakeDataStore(initial: Preferences = TestPreferences()) : DataStore<Preferences> {
    private val state = MutableStateFlow(initial)
    override val data: Flow<Preferences> = state

    override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences {
        val new = transform(state.value)
        state.value = new
        return new
    }
}

class LocalDataStoreImplTest {

    @Test
    fun testClearAllPreservesOnboardedAndNotificationKeys() = runTest {
        val dataStore = FakeDataStore()
        val local = LocalDataStoreImpl(dataStore)

        local.setHasOnboarded(true)
        local.setHasRequestedNotificationPermission(true)

        local.saveToken("access")
        local.saveRefreshToken("refresh")
        local.saveTokenExpiryMillis(12345)
        local.saveId("id-1")
        local.saveUserRole("user")
        local.savePendingVerification("email@test.com", "user")

        assertEquals("access", local.getToken())
        assertEquals("refresh", local.getRefreshToken())
        assertEquals(12345, local.getTokenExpiryMillis())
        assertEquals("id-1", local.getId())
        assertEquals("user", local.getUserRole())
        assertEquals("email@test.com", local.getPendingVerificationEmail())

        local.clearAll()

        val prefsAfter = dataStore.data.first()
        val hasOnboarded = local.hasOnboarded.first()
        val hasRequested = local.getHasRequestedNotificationPermission()

        assertEquals(true, hasOnboarded)
        assertEquals(true, hasRequested)

        assertNull(local.getToken())
        assertNull(local.getRefreshToken())
        val tokenExpiry = local.getTokenExpiryMillis()
        assertEquals(null, tokenExpiry)
        assertNull(local.getId())
        assertNull(local.getUserRole())
        assertNull(local.getPendingVerificationEmail())
        assertNull(local.getPendingVerificationUserType())
    }
}

