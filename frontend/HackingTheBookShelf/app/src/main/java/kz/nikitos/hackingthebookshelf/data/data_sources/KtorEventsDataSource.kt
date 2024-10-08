package kz.nikitos.hackingthebookshelf.data.data_sources

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import kz.nikitos.hackingthebookshelf.data.mappers.EventMapper
import kz.nikitos.hackingthebookshelf.data.models.EventData
import kz.nikitos.hackingthebookshelf.data.utils.BASE_URL
import kz.nikitos.hackingthebookshelf.domain.data_sources.EventsDataSource
import kz.nikitos.hackingthebookshelf.domain.models.Event
import kz.nikitos.hackingthebookshelf.domain.repositories.JWTTokenRepository
import javax.inject.Inject

class KtorEventsDataSource @Inject constructor(
    private val ktorClient: HttpClient,
    private val jwtTokenRepository: JWTTokenRepository,
    private val eventMapper: EventMapper
) :
    EventsDataSource {
    override suspend fun getAllEvents(): List<Event> =
        getEvents(ALL_EVENTS_URL)

    override suspend fun getUpcomingEvents(): List<Event> =
        getEvents(UPCOMING_EVENTS_URL)

    override suspend fun getUpcomingTodayEvents(): List<Event> =
        getEvents(UPCOMING_TODAY_EVENTS_URL)

    override suspend fun getStartedEvents(): List<Event> =
        getEvents(STARTED_EVENTS_URL)

    override suspend fun registerPossibleEvents(): List<Event> =
        getEvents(REGISTER_POSSIBLE_EVENTS_URL)

    private suspend fun getEvents(url: String): List<Event> {
        return try {
            ktorClient.get(url).body<List<EventData>>().mapToDomain()
        } catch (e: Throwable) {
            emptyList()
        }
    }

    override suspend fun subscribeToEvent(eventId: Int) {
        val jwtToken = jwtTokenRepository.getToken()
        ktorClient.post(EVENT_SUBSCRIPTION_URL) {
            expectSuccess = true
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $jwtToken")
            }
            url {
                appendPathSegments("$eventId/")
            }
        }
    }

    override suspend fun getMySubscriptions(): List<Event>
        {
            val jwtToken = jwtTokenRepository.getToken()
            return try {
                ktorClient
                    .get(GET_EVENTS_REGISTERED_ON) {
                        headers {
                            append(HttpHeaders.Authorization, "Bearer $jwtToken")
                        }
                    }
                    .body<List<EventData>>()
                    .mapToDomain()
            } catch (e: Throwable) {
                emptyList()
            }
        }

    override suspend fun unsubscribeEvent(eventId: Int) {
        val jwtToken = jwtTokenRepository.getToken()
        ktorClient.delete(EVENT_SUBSCRIPTION_URL) {
            expectSuccess = true
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $jwtToken")
            }
            url {
                appendPathSegments("$eventId/")
            }
        }
    }

    private fun List<EventData>.mapToDomain() = eventMapper(this)

    private companion object {
        const val BASE_EVENTS_URL = "$BASE_URL/events"

        const val ALL_EVENTS_URL = "$BASE_EVENTS_URL/"
        const val UPCOMING_EVENTS_URL = "$BASE_EVENTS_URL/upcoming/"
        const val UPCOMING_TODAY_EVENTS_URL = "$BASE_EVENTS_URL/active-today/"
        const val STARTED_EVENTS_URL = "$BASE_EVENTS_URL/active-now/"
        const val REGISTER_POSSIBLE_EVENTS_URL = "$BASE_EVENTS_URL/register-available"
        const val GET_EVENTS_REGISTERED_ON = "$BASE_EVENTS_URL/my/"

        const val EVENT_SUBSCRIPTION_URL = "$BASE_EVENTS_URL/register/"
    }
}