package kz.nikitos.hackingthebookshelf

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlinx.coroutines.runBlocking
import kz.nikitos.hackingthebookshelf.data.data_sources.InvalidCredentials
import kz.nikitos.hackingthebookshelf.data.data_sources.KtorTokenDataSource
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
private const val TAG = "TestingTag"
//@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ExampleInstrumentedTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var tokenDataSource: KtorTokenDataSource

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("kz.nikitos.hackingthebookshelf", appContext.packageName)

        runBlocking {
            val token = try {
                tokenDataSource.getToken("nikitos9862@style.ru", "321")
            } catch (e: InvalidCredentials) {
                Log.e(TAG, "useAppContext: wrong credentials", e)
                return@runBlocking
            }
            Log.i(TAG, "useAppContext: got token $token")
        }
    }
}