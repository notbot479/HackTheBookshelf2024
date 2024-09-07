package kz.nikitos.hackingthebookshelf.domain.data_sources

import kz.nikitos.hackingthebookshelf.data.models.UserCredentials

interface UserCredentialsDataSource {
    suspend fun getUserCredentials(): UserCredentials?

    suspend fun saveUserCredentials(username: String, password: String)
}