package kz.nikitos.hackingthebookshelf.data.repositories

import kz.nikitos.hackingthebookshelf.domain.data_sources.LocalTokenDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.RemoteTokenDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.UserCredentialsDataSource
import kz.nikitos.hackingthebookshelf.domain.exceptions.NoCredentials
import kz.nikitos.hackingthebookshelf.domain.repositories.JWTTokenRepository
import javax.inject.Inject

class MyJWTTokenRepository @Inject constructor(
    private val jwtDataSource: LocalTokenDataSource,
    private val remoteTokenDataSource: RemoteTokenDataSource,
    private val dataStorageUserCredentialsDataSource: UserCredentialsDataSource,
    ) : JWTTokenRepository {
    override suspend fun getToken(): String {
        return jwtDataSource.getSavedJWT() ?: getTokenFromBackend()
    }

    override suspend fun setCredentials(username: String, password: String) =
        dataStorageUserCredentialsDataSource.saveUserCredentials(username, password)

    private suspend fun getTokenFromBackend(): String {
        val (username, password) = dataStorageUserCredentialsDataSource.getUserCredentials() ?: throw NoCredentials()
        val token = remoteTokenDataSource.getToken(username, password)
        jwtDataSource.saveJWTToken(token)
        return jwtDataSource.getSavedJWT()!!
    }

    private companion object {
        const val TAG = "HackTheBookShelfMyTokenRepository"
    }
}