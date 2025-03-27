package com.mobile.medialibraryapp.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mobile.medialibraryapp.component.BaseComponent
import com.mobile.medialibraryapp.navigation.Screens
import com.mobile.medialibraryapp.navigation.navigateNew
import com.mobile.medialibraryapp.util.ThemePreview
import com.mobile.medialibraryapp.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    val viewModel: SplashViewModel = hiltViewModel()

    LaunchedEffect(key1 = Unit) {
        delay(3000)
        navController.navigateNew(Screens.LOGIN)
    }

    BaseComponent(viewModel = viewModel, stateObserver = { }) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            val (ivSplash) = createRefs()
            Text(
                text = "Hello World!",
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