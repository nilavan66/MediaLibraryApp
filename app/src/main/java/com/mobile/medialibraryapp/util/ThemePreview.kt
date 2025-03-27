package com.mobile.medialibraryapp.util

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES

import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light theme",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
)

@Preview(
    name = "Dark theme",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,

)

@Preview(
    name = "Tab view",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    device = "spec:width=1280dp,height=800dp,dpi=240"

    )
annotation class ThemePreview