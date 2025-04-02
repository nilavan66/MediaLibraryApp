package com.mobile.medialibraryapp.view

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.mobile.medialibraryapp.R
import com.mobile.medialibraryapp.component.BaseComponent
import com.mobile.medialibraryapp.component.TopBarComponent
import com.mobile.medialibraryapp.dataclass.MediaEntity
import com.mobile.medialibraryapp.navigation.Screens
import com.mobile.medialibraryapp.navigation.navigateNew
import com.mobile.medialibraryapp.navigation.navigateTo
import com.mobile.medialibraryapp.state.BaseState
import com.mobile.medialibraryapp.state.MediaGalleryState
import com.mobile.medialibraryapp.ui.theme.Dp12
import com.mobile.medialibraryapp.ui.theme.Dp120
import com.mobile.medialibraryapp.ui.theme.Dp16
import com.mobile.medialibraryapp.ui.theme.Dp2
import com.mobile.medialibraryapp.ui.theme.Dp8
import com.mobile.medialibraryapp.util.Constants.ScreenType.AUDIO
import com.mobile.medialibraryapp.util.Constants.ScreenType.IMAGE
import com.mobile.medialibraryapp.util.Constants.ScreenType.SCREEN_TYPE
import com.mobile.medialibraryapp.util.Constants.ScreenType.VIDEO
import com.mobile.medialibraryapp.util.ThemePreview
import com.mobile.medialibraryapp.viewmodel.MediaGalleryViewModel
import com.mobile.medialibraryapp.viewmodel.MediaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaGalleryScreen(navController: NavHostController) {
    val mediaViewModel: MediaViewModel = hiltViewModel()

    val mediaList = mediaViewModel.mediaList.collectAsLazyPagingItems()

    val viewModel: MediaGalleryViewModel = hiltViewModel()

    val context = LocalContext.current
    val activity = (context as? Activity)

    val configuration = LocalConfiguration.current
    val columns = if (configuration.screenWidthDp < 600) 3 else 5
    var searchVisible by remember { mutableStateOf(false) }

    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(searchText) {
        mediaViewModel.setSearchQuery(searchText)
    }

    BaseComponent(viewModel = viewModel, stateObserver = { state ->
        if (state is MediaGalleryState.MediaGallerySuccessState) {
            viewModel.showToast("Logged out successfully")
            navController.navigateNew(Screens.LOGIN)
            mediaViewModel.deleteAllMedia()
        } else {
            viewModel.dismissProgressBar()
        }
    }) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize(),
        ) {
            val (header, content, fab) = createRefs()
            TopBarComponent(
                title = "Dashboard",
                showBackButton = false,
                showSearchButton = true,
                isSearchVisible = searchVisible,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(header) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top)
                    },
                menuItems = listOf("Logout" to { viewModel.logout() }),
                navController = navController,
                onSearchTextChange = { searchText = it },
                searchText = searchText,
            )

            val filteredList by remember {
                derivedStateOf {
                    mediaList.itemSnapshotList.items.filter {
                        it.name.contains(searchText, ignoreCase = true) ||
                                it.mediaType.contains(searchText, ignoreCase = true)
                    }
                }
            }

            LazyVerticalGrid(columns = GridCells.Fixed(columns),
                contentPadding = PaddingValues(Dp12),
                horizontalArrangement = Arrangement.spacedBy(Dp8),
                verticalArrangement = Arrangement.spacedBy(Dp8),
                state = rememberLazyGridState(),
                modifier = Modifier
                    .fillMaxHeight()
                    .constrainAs(content) {
                        top.linkTo(header.bottom)
                        height = Dimension.fillToConstraints
                        bottom.linkTo(parent.bottom)
                    }) {

                if (mediaList.loadState.refresh is LoadState.Loading) {
                    item {
                        CircularProgressIndicator()
                    }
                }

                items(mediaList.itemCount) { index ->

                    val media = mediaList[index]
                    media?.let {
                        CardContainer(navController, documentId = it.documentId)
                    }
                }

                if (mediaList.loadState.append is LoadState.Loading) {
                    item {
                        CircularProgressIndicator()
                    }
                }
            }

            FloatingActionButton(
                modifier = Modifier
                    .padding(Dp16)
                    .constrainAs(fab) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                content = {
                    Icon(painterResource(R.drawable.ic_upload), contentDescription = null)
                },
                elevation = FloatingActionButtonDefaults.elevation(Dp8),
                onClick = { navController.navigateTo(Screens.MEDIA_UPLOAD) })

        }
    }
    BackHandler {
        activity?.finish()
            navController.popBackStack()

    }
}

@Composable
private fun CardContainer(navController: NavHostController, documentId: String) {
    val viewModel: MediaViewModel = hiltViewModel()
    var media by remember { mutableStateOf<MediaEntity?>(null) }
    var thumbnailUri by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(documentId) {
        media = viewModel.getMediaById(documentId)

        media?.let {
            if (it.mediaType.startsWith("video")) {
                coroutineScope.launch(Dispatchers.IO) {
                    val thumbnail = getVideoThumbnailUri(it.mediaUrl, context)
                    withContext(Dispatchers.Main) {
                        thumbnailUri = thumbnail
                    }
                }
            } else {
                thumbnailUri = it.mediaUrl
            }
        }
    }

    val (placeholder, screenType) = when (media?.mediaType) {
        "image/jpeg", "image/webp", "image/png" -> R.drawable.ic_image to IMAGE
        "audio/mpeg", "audio/wav" -> R.drawable.ic_music to AUDIO
        "video/mp4", "video/mkv" -> R.drawable.ic_video to VIDEO
        else -> R.drawable.ic_error to null
    }

    Card(
        modifier = Modifier
            .size(Dp120)
            .aspectRatio(1f)
            .clickable {
                if (screenType == null || media?.mediaUrl.isNullOrEmpty()) {
                    Toast.makeText(context, "Media Unavailable", Toast.LENGTH_SHORT).show()

                } else {
                    screenType.let {
                        SCREEN_TYPE = it
                        navController.navigate("${Screens.MEDIA_DETAIL}/$documentId")
                    }
                }

            },
        elevation = CardDefaults.cardElevation(Dp2),
        shape = RoundedCornerShape(Dp8),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        AsyncImage(
            model = if (screenType == null || media?.mediaUrl.isNullOrEmpty()) {
                R.drawable.ic_error
            } else {
                thumbnailUri ?: media?.mediaUrl
            },
            placeholder = painterResource(placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

private fun getVideoThumbnailUri(videoUrl: String, context: Context): String? {
    return try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(videoUrl, HashMap())
        val bitmap = retriever.frameAtTime
        retriever.release()

        bitmap?.let {
            saveBitmapToCache(it, context)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun saveBitmapToCache(bitmap: Bitmap, context: Context): String? {
    return try {
        val file = File(context.cacheDir, "video_thumbnail_${System.currentTimeMillis()}.jpg")
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos)
        fos.flush()
        fos.close()
        file.toURI().toString()
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}


@Preview
@Composable
fun CardPreview() {
    CardContainer(navController = rememberNavController(), "")
}

@ThemePreview
@Composable
fun MediaGalleryPreview() {
    MediaGalleryScreen(navController = rememberNavController())
}