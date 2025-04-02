package com.mobile.medialibraryapp.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.mobile.medialibraryapp.R
import com.mobile.medialibraryapp.component.BaseComponent
import com.mobile.medialibraryapp.navigation.Screens
import com.mobile.medialibraryapp.navigation.navigateNew
import com.mobile.medialibraryapp.state.SplashState
import com.mobile.medialibraryapp.util.ThemePreview
import com.mobile.medialibraryapp.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    val viewModel: SplashViewModel = hiltViewModel()

    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(key1 = Unit) {
        delay(3000)


        if (auth.currentUser != null) {

            navController.navigate(Screens.MEDIA_GALLERY) {
                popUpTo(Screens.SPLASH) { inclusive = true }
            }
        } else {

            navController.navigate(Screens.LOGIN) {
                popUpTo(Screens.SPLASH) { inclusive = true }
            }
        }
    }

    BaseComponent(viewModel = viewModel, stateObserver = {}) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            val (ivSplash) = createRefs()
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                modifier = Modifier.constrainAs(ivSplash) {
                    centerVerticallyTo(parent)
                    centerHorizontallyTo(parent)
                })
        }
    }
}

@Composable
@ThemePreview
fun SplashPreview() {
    SplashScreen(navController = rememberNavController())
}