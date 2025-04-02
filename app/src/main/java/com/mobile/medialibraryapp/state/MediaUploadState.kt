package com.mobile.medialibraryapp.state

sealed class MediaUploadState {
    object Init : MediaUploadState()
    object Success : MediaUploadState()
    data class Error(val message: String) : MediaUploadState()
}