package com.mobile.medialibraryapp.view

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mobile.medialibraryapp.R
import com.mobile.medialibraryapp.component.BaseComponent
import com.mobile.medialibraryapp.component.DialogComponent
import com.mobile.medialibraryapp.component.TopBarComponent
import com.mobile.medialibraryapp.component.VideoPlayerExo
import com.mobile.medialibraryapp.component.ZoomableImage
import com.mobile.medialibraryapp.dataclass.MediaEntity
import com.mobile.medialibraryapp.util.ThemePreview
import com.mobile.medialibraryapp.viewmodel.MediaDetailViewModel
import com.mobile.medialibraryapp.viewmodel.MediaViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MediaDetailScreen(navController: NavHostController, documentId: String) {
    val viewModel: MediaDetailViewModel = hiltViewModel()
    val mediaViewModel: MediaViewModel = hiltViewModel()


    val media by produceState<MediaEntity?>(initialValue = null) {
        value = mediaViewModel.getMediaById(documentId)
    }

    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        DialogComponent(
            showDialog = {
                showDialog = it
            },
            data = media
        )
    }

    BaseComponent(viewModel = viewModel, stateObserver = {}) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize(),
        ) {
            val (header, content) = createRefs()


            TopBarComponent(
                title = "Detail Screen",
                navigate = {
                    navController.popBackStack()
                },
                modifier = Modifier.constrainAs(header) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                },
                menuItems = listOf(
                    "Download" to { media?.let { viewModel.downloadFile(it.mediaUrl, it.name) } },
                    "Delete" to { media?.let { viewModel.deleteMediaDocument(it.documentId) } },
                    "Info" to { showDialog = true }
                ),
                navController = navController
            )


            media?.let { mediaEntity ->
                when (mediaEntity.mediaType) {
                    "image/jpeg", "image/webp", "image/png" -> {
                        BoxWithConstraints(
                            modifier = Modifier.constrainAs(content) {
                                top.linkTo(header.bottom)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                height = Dimension.fillToConstraints
                                width = Dimension.fillToConstraints
                            }
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(mediaEntity.mediaUrl)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(R.drawable.img_default),
                                contentScale = ContentScale.Fit,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center))


                        }
                    }

                    "audio/mpeg", "audio/wav", "video/mp4", "video/mkv" -> {

                        BoxWithConstraints(
                            modifier = Modifier
                                .wrapContentSize()
                                .constrainAs(content) {
                                    top.linkTo(header.bottom)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    height = Dimension.fillToConstraints
                                    width = Dimension.fillToConstraints
                                }
                        ) {
                            VideoPlayerExo(videoUrl = mediaEntity.mediaUrl)
                        }
                    }

                    else -> {
                        Text(
                            text = "Unsupported Media Type",
                            modifier = Modifier.constrainAs(content) {
                                centerHorizontallyTo(parent)
                                centerVerticallyTo(parent)
                            }
                        )
                    }
                }
            } ?: run {

                CircularProgressIndicator(
                    modifier = Modifier.constrainAs(content) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent)
                    }
                )
            }
        }
    }
    BackHandler {
        navController.popBackStack()
    }

}

@ThemePreview
@Composable
fun MediaDetailPreview() {
    MediaDetailScreen(navController = rememberNavController(), "")
}