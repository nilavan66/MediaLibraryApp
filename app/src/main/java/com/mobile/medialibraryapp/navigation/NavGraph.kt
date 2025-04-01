package com.mobile.medialibraryapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.mobile.medialibraryapp.view.LoginScreen
import com.mobile.medialibraryapp.view.MediaDetailScreen
import com.mobile.medialibraryapp.view.MediaGalleryScreen
import com.mobile.medialibraryapp.view.MediaUploadScreen
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
        composable(Screens.MEDIA_GALLERY){
            MediaGalleryScreen(navController)
        }

        composable(
            route = "${Screens.MEDIA_DETAIL}/{documentId}",
            arguments = listOf(navArgument("documentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val documentId = backStackEntry.arguments?.getString("documentId") ?: ""
            MediaDetailScreen(navController = navController, documentId)
        }

        composable(Screens.MEDIA_UPLOAD){
            MediaUploadScreen(navController = navController)
        }
    }
}