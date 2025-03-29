package com.mobile.medialibraryapp.view

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mobile.medialibraryapp.R
import com.mobile.medialibraryapp.component.BaseComponent
import com.mobile.medialibraryapp.component.TopBarComponent
import com.mobile.medialibraryapp.navigation.Screens
import com.mobile.medialibraryapp.navigation.navigateNew
import com.mobile.medialibraryapp.navigation.navigateTo
import com.mobile.medialibraryapp.state.MediaGalleryState
import com.mobile.medialibraryapp.ui.theme.Dp12
import com.mobile.medialibraryapp.ui.theme.Dp120
import com.mobile.medialibraryapp.ui.theme.Dp2
import com.mobile.medialibraryapp.ui.theme.Dp8
import com.mobile.medialibraryapp.util.Constants.ScreenType.DATA
import com.mobile.medialibraryapp.util.Constants.ScreenType.IMAGE_VIEW
import com.mobile.medialibraryapp.util.Constants.ScreenType.SCREEN_TYPE
import com.mobile.medialibraryapp.util.ThemePreview
import com.mobile.medialibraryapp.viewmodel.MediaGalleryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaGalleryScreen(navController: NavHostController) {
    val viewModel: MediaGalleryViewModel = hiltViewModel()
    val context = LocalContext.current
    val activity = (context as? Activity)
    val configuration = LocalConfiguration.current
    val columns = if (configuration.screenWidthDp < 600) 3 else 5


    BaseComponent(viewModel = viewModel, stateObserver = { state ->
        if (state is MediaGalleryState.MediaGallerySuccessState) {
            viewModel.showToast("Logged out successfully")
            navController.navigateNew(Screens.LOGIN)
        } else {
            viewModel.dismissProgressBar()
        }
    }) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize(),
        ) {
            val (header, content) = createRefs()
            TopBarComponent(
                title = "Dashboard",
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(header) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top)
                       // bottom.linkTo(content.top)
                    },
                menuItems = listOf("Logout" to { viewModel.logout() }),
                navController = navController
            )

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
                items(25) { index -> // Generates 25 items
                    CardContainer(navController)
                }
            }

        }
    }
    BackHandler {
        activity?.finish()
    }
}

@Composable
private fun CardContainer(navController: NavHostController) {
    val interactionSource = remember { MutableInteractionSource() }
    var data = "https://picsum.photos/200/500?random=${(0..1000).random()}"
    Card(
        modifier = Modifier
            .aspectRatio(1f, true)
            .size(Dp120)
            .clickable(interactionSource = interactionSource, indication = null) {
                SCREEN_TYPE = IMAGE_VIEW
                DATA = data
                navController.navigateTo(Screens.MEDIA_DETAIL)
            },
        elevation = CardDefaults.cardElevation(Dp2),
        shape = RoundedCornerShape(Dp8),

        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data)
                .crossfade(false).build(),

            placeholder = painterResource(R.drawable.img_default),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )


    }
}

@Preview
@Composable
fun CardPreview() {
    CardContainer(navController = rememberNavController())
}

@ThemePreview
@Composable
fun MediaGalleryPreview() {
    MediaGalleryScreen(navController = rememberNavController())
}