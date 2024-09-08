package kz.nikitos.hackingthebookshelf.data.data_sources

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kz.nikitos.hackingthebookshelf.domain.data_sources.NotificationTokenStorage
import javax.inject.Inject

val Context.NotificationTokenStorage: DataStore<Preferences> by preferencesDataStore(name = FirebaseNotificationTokenStorage.NOTIFICATION_TOKEN_STORAGE_NAME)

class FirebaseNotificationTokenStorage @Inject constructor(
    @ApplicationContext private val context: Context
) : NotificationTokenStorage {
    private val tokenKey by lazy {
        stringPreferencesKey(NOTIFICATION_TOKEN_KEY)
    }

    override suspend fun saveToken(token: String) {
        context.NotificationTokenStorage.edit { storage ->
            storage[tokenKey] = token
        }
    }

    override suspend fun getToken(): String? {
        val storage = context.NotificationTokenStorage.data.first()
        return storage[tokenKey]
    }

    companion object {
        const val NOTIFICATION_TOKEN_STORAGE_NAME = "notification_token_storage"
        const val NOTIFICATION_TOKEN_KEY = "notification_token"
    }
}