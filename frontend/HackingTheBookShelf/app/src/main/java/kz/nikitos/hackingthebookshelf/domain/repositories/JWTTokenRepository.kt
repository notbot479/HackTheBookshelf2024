package kz.nikitos.hackingthebookshelf.domain.repositories

interface JWTTokenRepository {
    suspend fun getToken(): String
    suspend fun setCredentials(username: String, password: String)
}