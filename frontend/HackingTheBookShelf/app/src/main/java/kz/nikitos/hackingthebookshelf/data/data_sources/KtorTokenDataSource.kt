package kz.nikitos.hackingthebookshelf.data.data_sources

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kz.nikitos.hackingthebookshelf.data.utils.BASE_URL
import kz.nikitos.hackingthebookshelf.domain.data_sources.RemoteTokenDataSource
import javax.inject.Inject


class KtorTokenDataSource @Inject constructor(
    private val ktorClient: HttpClient
) : RemoteTokenDataSource {
    override suspend fun getToken(username: String, password: String): String {
        val response = ktorClient.post(TOKEN_URL) {
            contentType(ContentType.Application.Json)
            setBody(AuthData(username, password))
        }
        return when (val token = response.body<GetTokenResponse>()) {
            is GetTokenResponse.Error -> throw InvalidCredentials()
            is GetTokenResponse.JWTToken -> token.token
        }
    }
    private companion object {
        const val TOKEN_URL = "$BASE_URL/jwt/get_token/"
        const val TAG = "HackTheBookShelfKtorDataSource"
    }
}

@Serializable
data class AuthData(val username: String, val password: String)

@Serializable(with = JWTSerializer::class)
sealed interface GetTokenResponse {
    @Serializable
    data class JWTToken(val token: String) : GetTokenResponse

    @Serializable
    data class Error(@SerialName("error") val message: String) : GetTokenResponse
}

class InvalidCredentials: Exception()

object JWTSerializer : JsonContentPolymorphicSerializer<GetTokenResponse>(GetTokenResponse::class) {
    override fun selectDeserializer(content: JsonElement) = when {
        "error" in content.jsonObject -> GetTokenResponse.Error.serializer()
        else -> GetTokenResponse.JWTToken.serializer()
    }
}