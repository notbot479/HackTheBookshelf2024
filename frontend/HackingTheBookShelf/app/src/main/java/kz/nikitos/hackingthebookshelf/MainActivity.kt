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
import dagger.hilt.android.AndroidEntryPoint
import kz.nikitos.hackingthebookshelf.ui.LoginViewModel
import kz.nikitos.hackingthebookshelf.ui.composables.LoginScreen
import kz.nikitos.hackingthebookshelf.ui.theme.HackingTheBookShelfTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askNotificationPermission()

        if (alreadyAuthorised()) goToLibraryActivity()

        enableEdgeToEdge()

        setContent {
            HackingTheBookShelfTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = hiltViewModel<LoginViewModel>()
                    val loginState by viewModel.uiState.observeAsState()
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

    private fun alreadyAuthorised(): Boolean {
        return false
    }

    private fun goToLibraryActivity() {
        val goToLibraryActivity = Intent(this, LibraryActivity::class.java)
        goToLibraryActivity.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
        startActivity(goToLibraryActivity)
        this.finish()
    }
}