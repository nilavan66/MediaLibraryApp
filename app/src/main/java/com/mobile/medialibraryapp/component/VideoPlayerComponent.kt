package com.mobile.medialibraryapp.component

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.media3.ui.PlayerView
import com.mobile.medialibraryapp.viewmodel.VideoViewModel

@Composable
fun VideoPlayerComponent(
    videoUrl: String,
    videoViewModel: VideoViewModel,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var lifecycle by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event -> lifecycle = event }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var currentUrl by rememberSaveable { mutableStateOf(videoUrl) } // Save state on rotation

    LaunchedEffect(currentUrl) {
        videoViewModel.setVideoUrl(currentUrl)
    }

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                useController = true
                // Enable Fullscreen Button
                setFullscreenButtonClickListener { isFullScreen ->
                    if (isFullScreen) {
                        enterFullScreen(context)
                    } else {
                        exitFullScreen(context)
                    }
                }
                player = videoViewModel.player
            }
        },
        update = { playerView ->
            when (lifecycle) {
                Lifecycle.Event.ON_PAUSE -> {
                    playerView.onPause()
                    playerView.player?.pause()
                }
                Lifecycle.Event.ON_RESUME -> {
                    playerView.onResume()
                    playerView.player?.play()
                }
                Lifecycle.Event.ON_DESTROY -> {
                   // videoViewModel.clearPlayer()
                }
                else -> Unit
            }
        },
        modifier = Modifier
            //.fillMaxWidth()
            .wrapContentSize()
            .aspectRatio(16f / 9f) // Maintain aspect ratio
    )
}

fun enterFullScreen(context: Context) {
    val activity = context as? Activity ?: return
    activity.window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

fun exitFullScreen(context: Context) {
    val activity = context as? Activity ?: return
    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}