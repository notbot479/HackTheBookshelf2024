package kz.nikitos.hackingthebookshelf.domain.data_sources

interface TokenDataSource {
    suspend fun getToken(username: String, password: String): String
}