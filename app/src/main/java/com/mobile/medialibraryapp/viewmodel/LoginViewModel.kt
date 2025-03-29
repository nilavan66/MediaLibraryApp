package com.mobile.medialibraryapp.viewmodel


import androidx.lifecycle.viewModelScope
import com.mobile.medialibraryapp.model.State
import com.mobile.medialibraryapp.model.request.LoginRequest
import com.mobile.medialibraryapp.state.LoginState
import com.mobile.medialibraryapp.webservice.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: FirebaseAuthRepository) :
    BaseViewModel<LoginState>() {
    private var setLoginState: LoginState = LoginState.Init
        set(value) {
            field = value
            setState(setLoginState)
        }

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            authRepository.loginUser(loginRequest, _baseState).collect {
                when (it) {
                    is State.Error -> {
                        showToast(it.message)

                    }

                    is State.Success -> {
                        setLoginState = LoginState.LoginSuccessState(it.data)
                    }

                    else -> {
                        dismissProgressBar()
                    }
                }
            }
        }
    }
}