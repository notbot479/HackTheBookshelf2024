package kz.nikitos.hackingthebookshelf.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.nikitos.hackingthebookshelf.data.data_sources.InvalidCredentials
import kz.nikitos.hackingthebookshelf.data.models.UserCredentials
import kz.nikitos.hackingthebookshelf.domain.data_sources.UserCredentialsDataSource
import kz.nikitos.hackingthebookshelf.domain.repositories.JWTTokenRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val jwtTokenRepository: JWTTokenRepository,
    private val userCredentialsDataSource: UserCredentialsDataSource,
) : ViewModel() {

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    private val _userCredentials = MutableLiveData<UserCredentials>()
    val userCredentials: LiveData<UserCredentials> = _userCredentials

    private val _uiState = MutableLiveData<LoginState>()
    val uiState: LiveData<LoginState> = _uiState

    init {
        _uiState.postValue(LoginState())
    }

    private suspend fun authorize() {
        jwtTokenRepository.getToken()
    }

    private suspend fun getUserCredentials(): UserCredentials = userCredentialsDataSource
        .getUserCredentials() ?: UserCredentials("", "")

    fun updateCredentials(userCredentials: UserCredentials) {
        val (username, password) = userCredentials
        viewModelScope.launch {
            jwtTokenRepository.setCredentials(username, password)
        }
    }

    fun login() {
        val isProcessing = true
        _uiState.postValue(_uiState.value?.copy(isProcessing = isProcessing))
            viewModelScope.launch {

                val userCredentials = getUserCredentials()

                var generalError: String? = null

                if (_uiState.value == null) {
                    generalError = "Enter auth data"
                    Log.i(TAG, "login: there was nothing")
                }

                val usernameIsEmpty = userCredentials.username.isEmpty()
                val passwordIsEmpty = userCredentials.password.isEmpty()

                var usernameErrorMessage: String? = null
                if (usernameIsEmpty) {
                    Log.i(TAG, "login: username was empty")
                    usernameErrorMessage = "Empty username"
                }

                var passwordErrorMessage: String? = null
                if (passwordIsEmpty) {
                    Log.i(TAG, "login: password was empty")
                    passwordErrorMessage = "Empty password"
                }

                var loginSucceeded = false

                if (!(usernameIsEmpty || passwordIsEmpty)) {
                    try {
                        authorize()
                        Log.i(TAG, "login: authorization succeeded")
                        loginSucceeded = true
                    } catch (e: InvalidCredentials) {
                        Log.i(TAG, "login: authorization failed")
                        generalError = "Wrong credentials"
                    } catch (e: Exception) {
                        Log.e(TAG, "login: looks like internet problem or service is unavailable")
                        generalError = "Service is unavailable"
                    }
                }
                val newUiState = LoginState(
                    usernameErrorMessage,
                    passwordErrorMessage,
                    generalError,
                    loginSucceeded,
                    false
                )
                Log.d(TAG, "login: $newUiState")
                _uiState.postValue(newUiState)
            }
        }

    private companion object {
        const val TAG = "HackTheBookShelfLoginViewModel"
    }
}

data class LoginState(
    val usernameError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
    val loginSucceeded: Boolean = false,
    val isProcessing: Boolean = false
)