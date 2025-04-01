package com.mobile.medialibraryapp.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize

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
import androidx.compose.ui.draw.clip
import androidx.hilt.navigation.compose.hiltViewModel
import com.mobile.medialibraryapp.viewmodel.VideoViewModel

@Composable
fun VideoPlayerExo(
    videoUrl: String,
    videoViewModel: VideoViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val player = remember { videoViewModel.player } // Retain player instance

    // Prevent video from resetting on rotation
    LaunchedEffect(videoUrl) {
        if (player.currentMediaItem == null || player.currentMediaItem?.localConfiguration?.uri.toString() != videoUrl) {
            videoViewModel.setVideoUrl(videoUrl)
        }
    }

    // Handle Lifecycle changes (Pause/Resume)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    player.playWhenReady = false
                    player.pause()
                }
                Lifecycle.Event.ON_RESUME -> {
                    player.playWhenReady = true
                    player.play()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    videoViewModel.releasePlayer() // Ensure player is properly released
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Display the Player
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = {
            PlayerView(context).apply {
                this.player = player
                useController = true
            }
        }
    )
}



