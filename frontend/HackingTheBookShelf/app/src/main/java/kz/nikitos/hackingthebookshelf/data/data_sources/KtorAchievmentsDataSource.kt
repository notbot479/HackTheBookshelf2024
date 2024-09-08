package kz.nikitos.hackingthebookshelf.data.data_sources

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import kz.nikitos.hackingthebookshelf.data.utils.BASE_URL
import kz.nikitos.hackingthebookshelf.domain.data_sources.AchievmentsDataSource
import kz.nikitos.hackingthebookshelf.domain.models.Achievment
import kz.nikitos.hackingthebookshelf.domain.repositories.JWTTokenRepository
import javax.inject.Inject

class KtorAchievmentsDataSource @Inject constructor(
    private val ktorClient: HttpClient,
    private val jwtTokenRepository: JWTTokenRepository
) : AchievmentsDataSource {
    override suspend fun getAllAchievments(): List<Achievment> {
        val token = jwtTokenRepository.getToken()
        return ktorClient.get(ACHIEVMENTS_URL) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }.body()
    }

    private companion object {
        const val ACHIEVMENTS_URL = "$BASE_URL/achievements/my/"
    }
}