package kz.nikitos.hackingthebookshelf.data.data_sources

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kz.nikitos.hackingthebookshelf.data.models.UserCredentials
import kz.nikitos.hackingthebookshelf.domain.data_sources.UserCredentialsDataSource
import javax.inject.Inject

val Context.UserCredentialsStorage: DataStore<Preferences> by preferencesDataStore(name = DataStorageUserCredentialsDataSource.CREDENTIALS_STORAGE_NAME)

class DataStorageUserCredentialsDataSource @Inject constructor(@ApplicationContext private val context: Context) :
    UserCredentialsDataSource {
    private val usernameKey by lazy { stringPreferencesKey(USERNAME_KEY) }
    private val passwordKey by lazy { stringPreferencesKey(PASSWORD_KEY) }
    override suspend fun getUserCredentials(): UserCredentials? {
        val preferences = context.UserCredentialsStorage.data.first()
        val username = preferences[usernameKey]
        val password = preferences[passwordKey]
        return if (username != null && password != null) UserCredentials(
            username,
            password
        ) else null
    }

    override suspend fun saveUserCredentials(username: String, password: String) {
        context.UserCredentialsStorage.edit { preferences ->
            preferences[usernameKey] = username
            preferences[passwordKey] = password
        }
    }

    companion object {
        private const val USERNAME_KEY = "username"
        private const val PASSWORD_KEY = "password"
        const val CREDENTIALS_STORAGE_NAME = "credentials_storage"
    }
}