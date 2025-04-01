package com.mobile.medialibraryapp.component

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mobile.medialibraryapp.R
import com.mobile.medialibraryapp.util.Constants.ScreenType.DATA
import com.mobile.medialibraryapp.util.ThemePreview


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ZoomableImage(modifier: Modifier = Modifier, imageUrl: String) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    BoxWithConstraints(
        modifier = modifier
            .clipToBounds()
    ) {
        val maxWidth = constraints.maxWidth.toFloat()
        val maxHeight = constraints.maxHeight.toFloat()

        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        val newScale = (scale * zoom).coerceIn(1f, 3f)

                        val maxX = (newScale - 1) * maxWidth / 2
                        val maxY = (newScale - 1) * maxHeight / 2

                        val newOffset = Offset(
                            x = (offset.x + pan.x * newScale).coerceIn(-maxX, maxX),
                            y = (offset.y + pan.y * newScale).coerceIn(-maxY, maxY),
                        )

                        scale = newScale
                        offset = if (newScale == 1f) Offset.Zero else newOffset
                    }
                }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.img_default),
                contentScale = ContentScale.Fit,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
            )
        }
    }
}


@ThemePreview
@Composable
fun ZoomableImagePreview() {
    ZoomableImage(imageUrl = DATA)
}

@Composable
fun Box(modifier: Modifier, content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}
