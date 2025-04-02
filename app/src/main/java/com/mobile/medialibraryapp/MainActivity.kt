package com.mobile.medialibraryapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mobile.medialibraryapp.navigation.CreateNavigationGraph
import com.mobile.medialibraryapp.ui.theme.MediaLibraryAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Keep
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            MediaLibraryAppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CreateNavigationGraph(navController)
                }
            }
        }
    }

}