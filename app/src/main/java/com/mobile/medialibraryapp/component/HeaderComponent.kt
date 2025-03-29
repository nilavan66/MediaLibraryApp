package com.mobile.medialibraryapp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mobile.medialibraryapp.R
import com.mobile.medialibraryapp.ui.theme.ColorIcon
import com.mobile.medialibraryapp.ui.theme.Dp12
import com.mobile.medialibraryapp.util.ThemePreview


@Composable
fun HeaderComponent(
    modifier: Modifier,
    navController: NavHostController,
    navigate: () -> Unit = { navController.navigateUp() },
    isHome: Boolean = false

) {
    val interactionSource = remember { MutableInteractionSource() }
    var expanded by remember { mutableStateOf(false) }
    val menuItemData = List(4) { "Option ${it + 1}" }

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
    )
    {
        val (icBack, logo, menu) = createRefs()
        if (!isHome) {
            IconButton(
                onClick = {
                    navigate()
                },
                modifier = Modifier
                    .constrainAs(icBack) {
                        centerVerticallyTo(parent)
                        start.linkTo(parent.start)
                    }) {
                Icon(
                    painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = ColorIcon
                )
            }
        }

        Text(
            text = stringResource(R.string.app_name),
            color = ColorIcon,
            modifier = Modifier
                .padding(Dp12)
                .constrainAs(logo) {
                    centerVerticallyTo(parent)
                    centerHorizontallyTo(parent)
                }
        )

        Box(modifier = Modifier
            .constrainAs(menu) {
                centerVerticallyTo(parent)
                end.linkTo(parent.end)
            }) {
            IconButton(
                onClick = {
                    expanded = true
                }) {
                Icon(
                    painterResource(id = R.drawable.ic_more),
                    contentDescription = null,
                    tint = ColorIcon
                )
            }
            DropdownMenu(
                modifier = Modifier
                    .padding(end = Dp12)
                    .background(Color.White),
                expanded = expanded,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = true),
            ) {
                menuItemData.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                color = ColorIcon,
                                modifier = modifier.padding(0.dp)
                            )
                        },
                        onClick = { expanded = false }
                    )
                }
            }
        }

    }
}

@ThemePreview
@Composable
fun HeaderComponentPreview() {
    HeaderComponent(modifier = Modifier, navController = rememberNavController())
}