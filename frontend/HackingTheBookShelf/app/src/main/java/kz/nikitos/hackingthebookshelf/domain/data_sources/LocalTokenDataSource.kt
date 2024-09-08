package kz.nikitos.hackingthebookshelf.domain.data_sources

interface LocalTokenDataSource {
    suspend fun saveJWTToken(token: String)

    suspend fun getSavedJWT(): String?
}