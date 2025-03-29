package com.mobile.medialibraryapp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mobile.medialibraryapp.R
import com.mobile.medialibraryapp.ui.theme.ColorIcon
import com.mobile.medialibraryapp.util.ThemePreview
import com.mobile.medialibraryapp.viewmodel.MediaGalleryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComponent(
    modifier: Modifier = Modifier,
    title: String,
    showBackButton: Boolean = false,
    navController: NavHostController,
    navigate: () -> Unit = { navController.navigateUp() },
    menuItems: List<Pair<String, () -> Unit>> = emptyList()
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = title,
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = { navigate() }) {
                    Icon(
                        painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",

                    )
                }
            } else null
        },
        actions = {
            if (menuItems.isNotEmpty()) {
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            painterResource(id = R.drawable.ic_more),
                            contentDescription = "Menu",
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.White),
                        properties = PopupProperties(focusable = true),
                    ) {
                        menuItems.forEach { (label, action) ->
                            DropdownMenuItem(
                                text = { Text(text = label, color = ColorIcon) },
                                onClick = {
                                    expanded = false
                                    action() // Execute the associated function
                                }
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}


@ThemePreview
@Composable
fun TopBarPreview() {
    val viewModel: MediaGalleryViewModel = hiltViewModel()
    val menuItems = listOf(
        "Logout" to { viewModel.showToast("logout") },
        "Info" to { viewModel.showToast("Info Clicked") },
        "Delete" to { viewModel.showToast("Delete") }
    )
    TopBarComponent(showBackButton = true, title = "Dashboard", menuItems = menuItems, navController = rememberNavController())

}