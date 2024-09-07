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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

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

    @Test
    fun getAllEvents() {
        runBlocking {
            val events = eventsDataSource.getAllEvents()
            Log.d(TAG, "getAllEvents: $events")
        }
    }

    @Test
    fun subscribeToEvent() {
        runBlocking {
            tokenRepository.setCredentials("dio@heaven.jp", "777")

            tokenRepository.getToken()

            val eventId = eventsDataSource.getAllEvents().random().id
            eventsDataSource.subscribeToEvent(eventId)
        }
    }

    private companion object {
        const val TAG = "HackTheBookShelfTestingTag"
    }
}