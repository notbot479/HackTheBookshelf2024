package kz.nikitos.hackingthebookshelf.domain.data_sources

interface NotificationTokenStorage {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
}