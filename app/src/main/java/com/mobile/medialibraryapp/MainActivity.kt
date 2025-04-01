package com.mobile.medialibraryapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mobile.medialibraryapp.navigation.CreateNavigationGraph
import com.mobile.medialibraryapp.ui.theme.MediaLibraryAppTheme
import com.mobile.medialibraryapp.util.Constants.Permission.STORAGE_PERMISSION_REQUEST_CODE
import com.mobile.medialibraryapp.viewmodel.MediaUploadViewModel
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