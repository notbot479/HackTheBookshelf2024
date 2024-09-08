package kz.nikitos.hackingthebookshelf.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import kz.nikitos.hackingthebookshelf.data.models.UserCredentials
import kz.nikitos.hackingthebookshelf.ui.LoginState

@Composable
fun LoginScreen(
    loginState: LoginState,
    onValueChange: (userCredentials: UserCredentials) -> Unit,
    onLogin: () -> Unit,
    onLoggedIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (loginState.loginSucceeded) {
        onLoggedIn()
    }

    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val isUsernameError = loginState.usernameError != null
    val isPasswordError = loginState.passwordError != null

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                onValueChange(UserCredentials(it, password))
            },
            label = {
                Text(text = "Электронная почта")
            },
            singleLine = true,
            isError = isUsernameError,
            supportingText = {
                if (isUsernameError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = loginState.usernameError!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                onValueChange(UserCredentials(username, it))
            },
            label = {
                Text(text = "Пароль")
            },
            singleLine = true,
            supportingText = {
                if (isPasswordError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = loginState.passwordError!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            visualTransformation = PasswordVisualTransformation()
        )

        loginState.generalError?.let { generalError ->
            Text(text = generalError, color = MaterialTheme.colorScheme.error)
        }

        if (loginState.isProcessing) {
            CircularProgressIndicator()
        }

        Button(onClick = onLogin) {
            Text(text = "Войти")
        }
    }
}