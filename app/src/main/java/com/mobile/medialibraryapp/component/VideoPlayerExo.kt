package com.mobile.medialibraryapp.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.ui.PlayerView
import com.mobile.medialibraryapp.viewmodel.VideoViewModel

@Composable
fun VideoPlayerExo(
    videoUrl: String,
    videoViewModel: VideoViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val player = remember { videoViewModel.player }

    LaunchedEffect(videoUrl) {
        if (player.currentMediaItem == null || player.currentMediaItem?.localConfiguration?.uri.toString() != videoUrl) {
            videoViewModel.setVideoUrl(videoUrl)
        }
    }


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
                    videoViewModel.releasePlayer()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


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



