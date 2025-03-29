package com.mobile.medialibraryapp.view


import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mobile.medialibraryapp.R
import com.mobile.medialibraryapp.component.BaseComponent
import com.mobile.medialibraryapp.model.request.LoginRequest
import com.mobile.medialibraryapp.navigation.Screens
import com.mobile.medialibraryapp.navigation.navigateNew
import com.mobile.medialibraryapp.state.LoginState
import com.mobile.medialibraryapp.ui.theme.Dp12
import com.mobile.medialibraryapp.ui.theme.Dp16
import com.mobile.medialibraryapp.ui.theme.Dp24
import com.mobile.medialibraryapp.ui.theme.Dp400
import com.mobile.medialibraryapp.ui.theme.PageDimension
import com.mobile.medialibraryapp.util.ThemePreview
import com.mobile.medialibraryapp.util.isValidEmail
import com.mobile.medialibraryapp.util.validatePassword
import com.mobile.medialibraryapp.viewmodel.LoginViewModel


@Composable
fun LoginScreen(navController: NavHostController) {
    val viewModel: LoginViewModel = hiltViewModel()
    val context = LocalContext.current
    val activity = (context as? Activity)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    BaseComponent(viewModel = viewModel, stateObserver = { state ->
        when (state) {
            is LoginState.LoginSuccessState -> {
                viewModel.sharedPrefManager.userid = state.data.user?.uid.toString()
                viewModel.showToast("Logged in Successfully")
                navController.navigateNew(Screens.MEDIA_GALLERY)
            }

            else -> viewModel.dismissProgressBar()
        }

    }) {

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()

        ) {
            val (loginSection) = createRefs()

            Column(
                modifier = Modifier
                    .padding(horizontal = PageDimension)
                    .fillMaxWidth()
                    .widthIn(max = Dp400)
                    .constrainAs(loginSection) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(Dp16))
                Text(text = "Login Here", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(Dp16))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Dp12))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val icon =
                            if (passwordVisible) R.drawable.ic_password_visible else R.drawable.ic_password_hidden
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(icon),
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Dp24))

                Button(
                    onClick = {
                        if (validate(
                                loginRequest = LoginRequest(email, password), viewModel, context
                            )
                        ) {
                            viewModel.login(LoginRequest(email = email, password = password))
                        }
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }

            }

        }
    }
    BackHandler {
        activity?.finish()
    }

}

private fun validate(
    loginRequest: LoginRequest, viewModel: LoginViewModel, context: Context
): Boolean {
    val (email, password) = loginRequest
    if (email.trim().isEmpty()) {
        viewModel.showToast(context.getString(R.string.enter_email))
        return false
    } else if (!email.isValidEmail()) {
        viewModel.showToast(context.getString(R.string.enter_valid_email))
        return false
    } else if (password.trim().isEmpty()) {
        viewModel.showToast(context.getString(R.string.enter_password))
        return false
    } else {
        password.validatePassword()?.let {
            viewModel.showToast(it)
            return false
        }
    }
    return true

}


@ThemePreview
@Composable
fun LoginPreview() {
    LoginScreen(navController = rememberNavController())
}
