package com.mobile.medialibraryapp.view

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mobile.medialibraryapp.R
import com.mobile.medialibraryapp.component.BaseComponent
import com.mobile.medialibraryapp.component.TopBarComponent
import com.mobile.medialibraryapp.state.MediaUploadState
import com.mobile.medialibraryapp.ui.theme.Sp14
import com.mobile.medialibraryapp.ui.theme.Sp18
import com.mobile.medialibraryapp.util.ThemePreview
import com.mobile.medialibraryapp.viewmodel.MediaUploadViewModel

@Composable
fun MediaUploadScreen(navController: NavHostController) {
    val viewModel: MediaUploadViewModel = hiltViewModel()
    val progress by viewModel.uploadProgress.collectAsState()
    val permissionGranted by viewModel.permissionGranted.collectAsState()

    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    // Handle permission requests
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        viewModel.setPermissionState(allGranted)
    }

    // File picker
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedUri = uri
        }
    }

    BaseComponent(viewModel = viewModel, stateObserver = { state ->
        when (state) {
            is MediaUploadState.Success -> {
                viewModel.showToast("Upload Successful")
                navController.popBackStack()
            }
            is MediaUploadState.Error -> {
                viewModel.showToast(state.message)
            }
            else -> {}
        }
    }) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (header, content) = createRefs()

            TopBarComponent(
                title = "Media Upload",
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(header) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top)
                    },
                navController = navController
            )

            UploadCard(
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .constrainAs(content) {
                        top.linkTo(header.bottom)
                        bottom.linkTo(parent.bottom)
                        centerHorizontallyTo(parent)
                    },
                progress = progress,
                selectedFile = selectedUri?.lastPathSegment ?: "No file selected",
                onSelectFileClick = {
                    if (permissionGranted) {
                        filePickerLauncher.launch("audio/mp3, video/mp4, image/jpeg")
                    } else {
                        permissionLauncher.launch(
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                arrayOf(
                                    Manifest.permission.READ_MEDIA_IMAGES,
                                    Manifest.permission.READ_MEDIA_VIDEO,
                                    Manifest.permission.READ_MEDIA_AUDIO
                                )
                            } else {
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        )
                    }
                },
                onUploadClick = {
                    selectedUri?.let { uri ->
                        viewModel.uploadMedia(uri)
                    } ?: viewModel.showToast("Please select a file first")
                }
            )
        }
    }
}


@ThemePreview
@Composable
fun UploadDataPreview() {
    MediaUploadScreen(navController = rememberNavController())
}


@Composable
fun UploadCard(
    modifier: Modifier = Modifier,
    progress: Float = 0.2f,
    selectedFile: String,
    onSelectFileClick: () -> Unit,  // New: Callback for file selection
    onUploadClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .drawBehind {
                val strokeWidth = 4.dp.toPx()
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(colors.primary, colors.secondary)
                    ),
                    style = Stroke(width = strokeWidth, pathEffect = pathEffect),
                    cornerRadius = CornerRadius(16.dp.toPx())
                )
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.2f))

            // Upload Icon
            Icon(
                painterResource(id = R.drawable.ic_upload),
                contentDescription = "Upload Icon",
                tint = colors.primary,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.weight(0.2f))

            // Upload Text
            Text(
                text = "Upload Media",
                fontSize = Sp18,
                color = colors.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(0.2f))

            // **New**: File Selection Button
            Button(
                onClick = onSelectFileClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text(text = "Select File")
            }

            // **New**: Display selected file name
            if (selectedFile != "No file selected") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = selectedFile,
                    fontSize = Sp14,
                    color = colors.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.weight(0.2f))

            // Upload Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50)),
                color = colors.primary
            )

            Spacer(modifier = Modifier.weight(0.2f))

            // Upload Button
            Button(
                onClick = onUploadClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.6f),
                enabled = selectedFile != "No file selected"
            ) {
                Text(text = "Upload")
            }

            Spacer(modifier = Modifier.weight(0.4f))
        }
    }
}



/*

@ThemePreview
@Composable
fun UploadCardPreview() {
    UploadCard(
        onUploadClick = {}
    )
}*/
