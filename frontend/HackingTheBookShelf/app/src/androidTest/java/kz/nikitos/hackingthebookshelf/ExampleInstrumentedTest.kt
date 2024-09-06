package kz.nikitos.hackingthebookshelf

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.ContentType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kz.nikitos.hackingthebookshelf.data.data_sources.InvalidCredentials
import kz.nikitos.hackingthebookshelf.data.data_sources.TokenDataSource

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
private const val TAG = "TestingTag"
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("kz.nikitos.hackingthebookshelf", appContext.packageName)

        val ktorClient = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        runBlocking {
            val token = try {
                TokenDataSource(ktorClient).getToken("nikitos9862@style.ru", "321")
            } catch (e: InvalidCredentials) {
                Log.e(TAG, "useAppContext: wrong credentials", e)
                return@runBlocking
            }
            Log.i(TAG, "useAppContext: got token $token")
        }
    }
}