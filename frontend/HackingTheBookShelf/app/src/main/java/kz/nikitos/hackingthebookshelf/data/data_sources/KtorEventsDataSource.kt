package kz.nikitos.hackingthebookshelf.data.data_sources

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kz.nikitos.hackingthebookshelf.data.models.Event
import kz.nikitos.hackingthebookshelf.data.models.EventSubscripition
import kz.nikitos.hackingthebookshelf.data.utils.BASE_URL
import kz.nikitos.hackingthebookshelf.domain.data_sources.EventsDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.NotificationTokenStorage
import kz.nikitos.hackingthebookshelf.domain.repositories.JWTTokenRepository
import javax.inject.Inject

class KtorEventsDataSource @Inject constructor(
    private val ktorClient: HttpClient,
    private val notificationTokenStorage: NotificationTokenStorage,
    private val jwtTokenRepository: JWTTokenRepository
) :
    EventsDataSource {
    override suspend fun getAllEvents(): List<Event> =
        ktorClient.get(ALL_EVENTS_URL).body()

    override suspend fun getUpcomingEvents(): List<Event> =
        ktorClient.get(UPCOMING_EVENTS_URL).body()

    override suspend fun getUpcomingTodayEvents(): List<Event> =
        ktorClient.get(UPCOMING_TODAY_EVENTS_URL).body()

    override suspend fun getStartedEvents(): List<Event> =
        ktorClient.get(STARTED_EVENTS_URL).body()

    override suspend fun registerPossibleEvents(): List<Event> =
        ktorClient.get(REGISTER_POSSIBLE_EVENTS_URL).body()

    override suspend fun subscribeToEvent(eventId: Int) {
        val notificationToken = notificationTokenStorage.getToken()!!
        val jwtToken = jwtTokenRepository.getToken()
        ktorClient.post(EVENT_SUBSCRIPTION_URL) {
            contentType(ContentType.Application.Json)
            setBody(EventSubscripition(notificationToken, eventId))
            headers {
                append(HttpHeaders.Authorization, jwtToken)
            }
        }
    }

    private companion object {
        const val BASE_EVENTS_URL = "$BASE_URL/events"

        const val ALL_EVENTS_URL = "$BASE_EVENTS_URL/"
        const val UPCOMING_EVENTS_URL = "$BASE_EVENTS_URL/upcoming/"
        const val UPCOMING_TODAY_EVENTS_URL = "$BASE_EVENTS_URL/active-today/"
        const val STARTED_EVENTS_URL = "$BASE_EVENTS_URL/active-now/"
        const val REGISTER_POSSIBLE_EVENTS_URL = "$BASE_EVENTS_URL/register-available"

        const val EVENT_SUBSCRIPTION_URL = "$BASE_EVENTS_URL/sub/"
    }
}