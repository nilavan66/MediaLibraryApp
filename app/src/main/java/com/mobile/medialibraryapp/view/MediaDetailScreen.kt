package com.mobile.medialibraryapp.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mobile.medialibraryapp.component.BaseComponent
import com.mobile.medialibraryapp.component.TopBarComponent
import com.mobile.medialibraryapp.component.ZoomableImage
import com.mobile.medialibraryapp.util.Constants.ScreenType.AUDIO_VIEW
import com.mobile.medialibraryapp.util.Constants.ScreenType.DATA
import com.mobile.medialibraryapp.util.Constants.ScreenType.IMAGE_VIEW
import com.mobile.medialibraryapp.util.Constants.ScreenType.SCREEN_TYPE
import com.mobile.medialibraryapp.util.Constants.ScreenType.VIDEO_VIEW
import com.mobile.medialibraryapp.util.ThemePreview
import com.mobile.medialibraryapp.viewmodel.MediaDetailViewModel


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MediaDetailScreen(navController: NavHostController) {
    val viewModel: MediaDetailViewModel = hiltViewModel()

    BaseComponent(viewModel = viewModel, stateObserver = {}) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize(),
        ) {
            val (header, content) = createRefs()
            TopBarComponent(
                title = "Detail Screen",
                showBackButton = true,
                modifier = Modifier
                    .constrainAs(header) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top)

                    },
                menuItems = listOf(
                    "Download" to { viewModel.showToast("Download") },
                    "Delete" to { viewModel.showToast("Delete") },
                    "Info" to { viewModel.showToast("Info") }),
                navController = navController
            )


            when (SCREEN_TYPE) {
                IMAGE_VIEW -> {
                    BoxWithConstraints(
                        modifier = Modifier
                            .constrainAs(content) {
                                top.linkTo(header.bottom)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                height = Dimension.fillToConstraints
                                width = Dimension.fillToConstraints
                            }
                    ) {
                        ZoomableImage(
                            imageUrl = DATA,
                        )
                    }
                }

                AUDIO_VIEW -> {}
                VIDEO_VIEW -> {}
            }
        }
    }
}

@ThemePreview
@Composable
fun MediaDetailPreview() {
    MediaDetailScreen(navController = rememberNavController())
}