package com.mobile.medialibraryapp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mobile.medialibraryapp.R
import com.mobile.medialibraryapp.ui.theme.ColorIcon
import com.mobile.medialibraryapp.ui.theme.Dp16
import com.mobile.medialibraryapp.ui.theme.Dp2
import com.mobile.medialibraryapp.util.ThemePreview
import com.mobile.medialibraryapp.viewmodel.MediaGalleryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComponent(
    modifier: Modifier = Modifier,
    title: String,
    showSearchButton: Boolean = false,
    isSearchVisible: Boolean = false,
    searchText: String ="",
    onSearchTextChange: (String) -> Unit ={},
    showBackButton: Boolean = true,
    navController: NavHostController,
    navigate: () -> Unit = { navController.navigateUp() },
    menuItems: List<Pair<String, () -> Unit>> = emptyList()
) {
    var expanded by remember { mutableStateOf(false) }
    var searchVisible by remember { mutableStateOf(isSearchVisible) }

    if (searchVisible) {

        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchText,
                    onQueryChange = { newText -> onSearchTextChange(newText) },
                    onSearch = {
                        expanded = true

                    },

                    expanded = true,
                    onExpandedChange = { expanded = true },
                    placeholder = {
                        Text(text = "Search")
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                        )
                    },
                    trailingIcon = {
                        Icon(Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                onSearchTextChange("")
                                    searchVisible = false

                            })
                    },
                )
            },
            expanded = false,
            onExpandedChange = {
                expanded = it
            },
            shape= SearchBarDefaults.inputFieldShape,
            modifier = modifier.padding(horizontal = Dp16).fillMaxWidth()
        ){

        }


    } else {

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
                if (showSearchButton) {
                    IconButton(onClick = { searchVisible = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"

                            )
                    }
                } else null


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
                                        action()
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
    TopBarComponent(
        showBackButton = true,
        title = "Dashboard",
        menuItems = menuItems,
        navController = rememberNavController(),
        modifier = TODO(),
        showSearchButton = TODO(),
        isSearchVisible = TODO(),
        searchText = TODO(),
        onSearchTextChange = TODO(),
        navigate = TODO()
    )

}