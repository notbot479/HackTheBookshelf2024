package kz.nikitos.hackingthebookshelf

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import kz.nikitos.hackingthebookshelf.data.data_sources.FirebaseTokenUpdater
import kz.nikitos.hackingthebookshelf.data.utils.BASE_URL
import kz.nikitos.hackingthebookshelf.domain.data_sources.NotificationTokenStorage
import kz.nikitos.hackingthebookshelf.domain.repositories.JWTTokenRepository
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var notificationTokenStorage: NotificationTokenStorage

    @Inject
    lateinit var ktorClient: HttpClient

    @Inject
    lateinit var jwtTokenRepository: JWTTokenRepository

    @Inject
    lateinit var firebaseTokenUpdater: FirebaseTokenUpdater

    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d(TAG, token)
            runBlocking {
                notificationTokenStorage.saveToken(token)
            }
        })
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "onNewToken: new firebase token received")
        runBlocking {
            notificationTokenStorage.saveToken(token)
            try {
                val jwtToken = jwtTokenRepository.getToken()
                firebaseTokenUpdater.updateToken(jwtToken, token)
            } catch (e: Exception) {
                Log.e(TAG, "onNewToken: error updating token on backend", e)
            }
        }
        Log.d(TAG, "onNewToken: $token")
    }

    private companion object {
        const val TAG = "HackTheBookShelfFirebaseMessaging"
    }
}