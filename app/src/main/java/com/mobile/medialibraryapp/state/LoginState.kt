package com.mobile.medialibraryapp.state

import com.google.firebase.auth.AuthResult

sealed class LoginState {
    object Init : LoginState()

    data class LoginSuccessState(val data: AuthResult) : LoginState()

    data class ShowMessage(val msg: String) : LoginState()

}