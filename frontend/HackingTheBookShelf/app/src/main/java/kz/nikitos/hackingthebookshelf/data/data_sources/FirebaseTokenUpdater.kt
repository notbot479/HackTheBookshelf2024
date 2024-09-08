package kz.nikitos.hackingthebookshelf.data.data_sources

import io.ktor.client.HttpClient
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kz.nikitos.hackingthebookshelf.data.utils.BASE_URL
import javax.inject.Inject

class FirebaseTokenUpdater @Inject constructor(
    private val ktorClient: HttpClient
) {
    suspend fun updateToken(jwtToken: String, firebaseToken: String) {
        ktorClient.post(UPDATE_FIREBASE_TOKEN_URL) {
            expectSuccess = true
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $jwtToken")
            }
            setBody(mapOf(FIREBASE_TOKEN_FIELD to firebaseToken))
        }
    }

    private companion object {
        const val UPDATE_FIREBASE_TOKEN_URL = "$BASE_URL/notifications/sub/"
        const val FIREBASE_TOKEN_FIELD = "firebase_push_hash"
    }
}