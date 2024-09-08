package kz.nikitos.hackingthebookshelf

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kz.nikitos.hackingthebookshelf.domain.repositories.JWTTokenRepository
import kz.nikitos.hackingthebookshelf.ui.LoginState
import kz.nikitos.hackingthebookshelf.ui.LoginViewModel
import kz.nikitos.hackingthebookshelf.ui.composables.LoginScreen
import kz.nikitos.hackingthebookshelf.ui.theme.HackingTheBookShelfTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var jwtTokenRepository: JWTTokenRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askNotificationPermission()

        if (alreadyAuthorised()) goToLibraryActivity()

        enableEdgeToEdge()

        setContent {
            HackingTheBookShelfTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = hiltViewModel<LoginViewModel>()
                    val loginState by viewModel.uiState.observeAsState(LoginState())
                    LoginScreen(
                        loginState = loginState,
                        onValueChange = viewModel::updateCredentials,
                        onLogin = viewModel::login,
                        onLoggedIn = ::goToLibraryActivity,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
            onNotificationsNotAllowed()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) { } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                onNotificationsNotAllowed()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun onNotificationsNotAllowed() {
        Toast.makeText(this, "Notifications are not allowed", Toast.LENGTH_LONG).show()
    }

    private fun alreadyAuthorised(): Boolean = runBlocking {
            try {
                jwtTokenRepository.getToken()
                return@runBlocking true
            } catch (e: Exception) {
                return@runBlocking false
            }
        }

    private fun goToLibraryActivity() {
        val goToLibraryActivity = Intent(this, LibraryActivity::class.java)
        goToLibraryActivity.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
        startActivity(goToLibraryActivity)
        this.finish()
    }
}