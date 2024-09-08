package kz.nikitos.hackingthebookshelf.data.data_sources

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kz.nikitos.hackingthebookshelf.domain.data_sources.LocalTokenDataSource
import javax.inject.Inject

val Context.JWTStorage: DataStore<Preferences> by preferencesDataStore(name = JWTDataSource.JWT_STORAGE_NAME)

class JWTDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalTokenDataSource {
    override suspend fun saveJWTToken(token: String) {
        val JWTKey = stringPreferencesKey(JWT_KEY)
        context.JWTStorage.edit { settings ->
            settings[JWTKey] = token
        }
    }

    override suspend fun getSavedJWT(): String? {
        val JWTKey = stringPreferencesKey(JWT_KEY)
        val preferences = context.JWTStorage.data.first()
        return preferences[JWTKey]
    }

    companion object {
        private const val JWT_KEY = "jwt_key"
        const val JWT_STORAGE_NAME = "settings_storage"
        const val TAG = "HackTheBookShelfJWTDataSource"
    }
}