package kz.nikitos.hackingthebookshelf.domain.data_sources

interface RemoteTokenDataSource {
    suspend fun getToken(username: String, password: String): String
}