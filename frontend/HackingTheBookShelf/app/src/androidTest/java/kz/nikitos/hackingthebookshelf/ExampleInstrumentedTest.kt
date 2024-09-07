package kz.nikitos.hackingthebookshelf

import android.util.Log
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kz.nikitos.hackingthebookshelf.data.data_sources.DataStorageUserCredentialsDataSource
import kz.nikitos.hackingthebookshelf.data.data_sources.InvalidCredentials
import kz.nikitos.hackingthebookshelf.data.data_sources.KtorEventsDataSource
import kz.nikitos.hackingthebookshelf.data.data_sources.KtorTokenDataSource
import kz.nikitos.hackingthebookshelf.data.repositories.MyJWTTokenRepository
import kz.nikitos.hackingthebookshelf.domain.models.Event
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.reflect.KSuspendFunction0

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@HiltAndroidTest
class ExampleInstrumentedTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var tokenDataSource: KtorTokenDataSource

    @Inject
    lateinit var eventsDataSource: KtorEventsDataSource

    @Inject
    lateinit var tokenRepository: MyJWTTokenRepository

    @Inject
    lateinit var dataStorageUserCredentialsDataSource: DataStorageUserCredentialsDataSource

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun registerOrLoggingInFailure() {
        runBlocking {
            val token = try {
                tokenDataSource.getToken("morning_protection@terricon.kz", "6")
            } catch (e: InvalidCredentials) {
                Log.e(TAG, "useAppContext: wrong credentials", e)
                return@runBlocking
            }
            assert(false)
            Log.i(TAG, "useAppContext: got token $token")
        }
    }

    @Test
    fun registerOrLoggingInSuccess() {
        runBlocking {
            val token = try {
                tokenDataSource.getToken("successfull@success.kz", "777")
            } catch (e: InvalidCredentials) {
                Log.e(TAG, "useAppContext: wrong credentials", e)
                assert(false)
                return@runBlocking
            }
            Log.i(TAG, "useAppContext: got token $token")
        }
    }

    private fun getEvents(getFunc: KSuspendFunction0<List<Event>>, funcName: String) {
        runBlocking {
            val events = getFunc()
            Log.d(TAG, "$funcName: $events")
        }
    }

    @Test
    fun getUpcomingTodayEvents() {
        getEvents(eventsDataSource::getUpcomingTodayEvents, "upcoming today")
    }

    @Test
    fun getAllEvents() {
        getEvents(eventsDataSource::getAllEvents, "all")
    }

    @Test
    fun getUpcomingEvents() {
        getEvents(eventsDataSource::getUpcomingEvents, "upcoming")
    }

    @Test
    fun getPastTodayEvents() {
        getEvents(eventsDataSource::getStartedEvents, "past today")
    }

    @Test
    fun subscribeToEvent() {
        runBlocking {
            tokenRepository.setCredentials("dio@heaven.jp", "777")

            val token = tokenRepository.getToken()
            Log.d(TAG, "subscribeToEvent: token is $token")

            val eventId = eventsDataSource.getAllEvents().random().id
            eventsDataSource.subscribeToEvent(eventId)
            println("Hello, world!")
        }
    }

    @Test
    fun getMyEvents() {
        runBlocking {
            tokenRepository.setCredentials("dio@heaven.jp", "777")

            val token = tokenRepository.getToken()
        }
        getEvents(eventsDataSource::getMySubscriptions, "my subscriptions")
    }

    @Test
    fun unsubscribeEvent() {
        runBlocking {
            tokenRepository.setCredentials("dio@heaven.jp", "777")

            val token = tokenRepository.getToken()
            eventsDataSource.unsubscribeEvent(2)
        }
    }

    private companion object {
        const val TAG = "HackTheBookShelfTestingTag"
    }
}