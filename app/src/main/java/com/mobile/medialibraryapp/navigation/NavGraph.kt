package com.mobile.medialibraryapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.mobile.medialibraryapp.view.LoginScreen
import com.mobile.medialibraryapp.view.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateNavigationGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screens.SPLASH) {

        composable(Screens.SPLASH) {
            SplashScreen(navController)
        }

        composable(Screens.LOGIN) {
            LoginScreen(navController)
        }

    }
}