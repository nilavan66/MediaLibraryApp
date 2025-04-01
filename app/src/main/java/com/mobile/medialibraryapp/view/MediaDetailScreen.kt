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
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mobile.medialibraryapp.component.BaseComponent
import com.mobile.medialibraryapp.component.TopBarComponent
import com.mobile.medialibraryapp.component.VideoPlayerComponent
import com.mobile.medialibraryapp.component.VideoPlayerExo
import com.mobile.medialibraryapp.component.ZoomableImage
import com.mobile.medialibraryapp.dataclass.MediaEntity
import com.mobile.medialibraryapp.util.ThemePreview
import com.mobile.medialibraryapp.viewmodel.MediaDetailViewModel
import com.mobile.medialibraryapp.viewmodel.MediaViewModel
import com.mobile.medialibraryapp.viewmodel.VideoViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MediaDetailScreen(navController: NavHostController, documentId: String) {
    val viewModel: MediaDetailViewModel = hiltViewModel()
    val mediaViewModel: MediaViewModel = hiltViewModel()

    // Fetch media entity using documentId
    val media by produceState<MediaEntity?>(initialValue = null) {
        value = mediaViewModel.getMediaById(documentId)
    }

    val videoViewModel: VideoViewModel = hiltViewModel()

    BaseComponent(viewModel = viewModel, stateObserver = {}) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize(),
        ) {
            val (header, content) = createRefs()

            // Top App Bar
            TopBarComponent(
                title = "Detail Screen",
                navigate = {
                    //videoViewModel.clearPlayer()
                    navController.popBackStack()
                },
                modifier = Modifier.constrainAs(header) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                },
                menuItems = listOf(
                    "Download" to { viewModel.showToast("Download") },
                    "Delete" to { viewModel.showToast("Delete") },
                    "Info" to { viewModel.showToast("Info") }
                ),
                navController = navController
            )

            // Display content based on media type
            media?.let { mediaEntity ->
                when (mediaEntity.mediaType) {
                    "image/jpeg", "image/png" -> {
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
                            ZoomableImage(imageUrl = mediaEntity.mediaUrl)
                        }
                    }

                    "audio/mpeg", "audio/wav" -> {
                        // TODO: Implement audio player UI
                    }

                    "video/mp4", "video/mkv" -> {

                        val videoUrl =
                            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

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
                            /*VideoPlayerComponent(
                                videoUrl = mediaEntity.mediaUrl,
                                videoViewModel = videoViewModel,
                            )*/

                            VideoPlayerExo(videoUrl =mediaEntity.mediaUrl)
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
                // Show loading or error if media is null
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
        //videoViewModel.clearPlayer()  // Clear the player when exiting
        navController.popBackStack()  // Navigate back to Media Gallery
    }
}

@ThemePreview
@Composable
fun MediaDetailPreview() {
    MediaDetailScreen(navController = rememberNavController(), "")
}