package kz.nikitos.hackingthebookshelf

import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import kz.nikitos.hackingthebookshelf.domain.data_sources.NotificationTokenStorage
import kz.nikitos.hackingthebookshelf.domain.repositories.JWTTokenRepository
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var notificationTokenStorage: NotificationTokenStorage

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
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        runBlocking {
            notificationTokenStorage.saveToken(token)
        }
        Log.d(TAG, "onNewToken: $token")
    }

    private companion object {
        const val TAG = "HackTheBookShelfFirebaseMessaging"
    }
}