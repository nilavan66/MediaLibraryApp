package com.mobile.medialibraryapp.state

sealed class MediaUploadState {
    object Idle : MediaUploadState()
    object Loading : MediaUploadState()
    object Success : MediaUploadState()
    data class Error(val message: String) : MediaUploadState()
}