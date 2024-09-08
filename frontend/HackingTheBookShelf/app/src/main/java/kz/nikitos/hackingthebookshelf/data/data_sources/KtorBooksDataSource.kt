package kz.nikitos.hackingthebookshelf.data.data_sources

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import kz.nikitos.hackingthebookshelf.data.models.DebtData
import kz.nikitos.hackingthebookshelf.data.utils.BASE_URL
import kz.nikitos.hackingthebookshelf.domain.data_sources.BooksDataSource
import kz.nikitos.hackingthebookshelf.domain.repositories.JWTTokenRepository
import javax.inject.Inject

class KtorBooksDataSource @Inject constructor(
    private val ktorClient: HttpClient,
    private val jwtTokenRepository: JWTTokenRepository,
) : BooksDataSource {
    override suspend fun getDebtBooks(): List<DebtData> {
        val jwtToken = jwtTokenRepository.getToken()
        return ktorClient.get(BOOKS_DEBT_URL) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $jwtToken")
            }
        }.body()
    }

    private companion object {
        const val BOOKS_DEBT_URL = "$BASE_URL/books/debt/my/"
    }
}