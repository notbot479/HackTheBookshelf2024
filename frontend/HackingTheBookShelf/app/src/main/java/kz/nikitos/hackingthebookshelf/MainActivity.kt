package kz.nikitos.hackingthebookshelf

import android.Manifest
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
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import kz.nikitos.hackingthebookshelf.ui.LoginViewModel
import kz.nikitos.hackingthebookshelf.ui.composables.LoginScreen
import kz.nikitos.hackingthebookshelf.ui.theme.HackingTheBookShelfTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
        enableEdgeToEdge()
        setContent {
            HackingTheBookShelfTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = LoginScreenDest,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<LoginScreenDest> {
                            val viewModel = hiltViewModel<LoginViewModel>()
                            val loginState by viewModel.uiState.observeAsState()
                                LoginScreen(
                                    loginState = loginState,
                                    onValueChange = viewModel::updateCredentials,
                                    onLogin = viewModel::login,
                                    onLoggedIn = {
                                        navController.navigate(MainScreen)
                                    }
                                )
                        }
                        composable<MainScreen> {
                            Text("Hello, world!")
                        }
                    }
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
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                onNotificationsNotAllowed()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun onNotificationsNotAllowed() {
        Toast.makeText(this, "Notifications are not allowed", Toast.LENGTH_LONG).show()
    }
}

@Serializable
object LoginScreenDest
//data class LoginScreenDest(
//    val username: String?,
//    val password: String?,
//    val loginErrorMessage: String?,
//    val passwordErrorMessage: String?
//)

@Serializable
object MainScreen