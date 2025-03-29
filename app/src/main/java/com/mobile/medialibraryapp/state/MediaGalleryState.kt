package com.mobile.medialibraryapp.state

import com.google.firebase.auth.AuthResult


sealed class MediaGalleryState {
    object Init : MediaGalleryState()
    object MediaGallerySuccessState : MediaGalleryState()
    data class ShowMessage(val msg: String) : MediaGalleryState()
}