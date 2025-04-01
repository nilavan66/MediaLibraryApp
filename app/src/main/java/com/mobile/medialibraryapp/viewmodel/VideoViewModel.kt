package com.mobile.medialibraryapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val context: Application
) : ViewModel() {

    val player: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build()
    }

    fun setVideoUrl(url: String) {
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        player.playWhenReady = true
    }

    fun releasePlayer() {
        player.release()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}




