package com.mobile.medialibraryapp.viewmodel

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.mobile.medialibraryapp.state.MediaUploadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MediaUploadViewModel @Inject constructor(
    private val storage: FirebaseStorage,
) : BaseViewModel<MediaUploadState>() {

    private var setMediaUploadState: MediaUploadState = MediaUploadState.Init
        set(value) {
            field = value
            setState(setMediaUploadState)
        }

    private val _uploadProgress = MutableStateFlow(0f)
    val uploadProgress: StateFlow<Float> = _uploadProgress

    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted: StateFlow<Boolean> = _permissionGranted

    fun setPermissionState(granted: Boolean) {
        _permissionGranted.value = granted
    }


    fun uploadMedia(uri: Uri, selectedFileName: String) {

        val storageRef = storage.reference.child("media_files/$selectedFileName")
        showToast("Uploading $selectedFileName")

        val uploadTask = storageRef.putFile(uri)

        uploadTask.addOnProgressListener { taskSnapshot ->
            _uploadProgress.value =
                taskSnapshot.bytesTransferred.toFloat() / taskSnapshot.totalByteCount
        }.addOnSuccessListener {
            showToast("Upload Successful. Metadata will be saved automatically.")
            setMediaUploadState = MediaUploadState.Success
        }.addOnFailureListener {
            showToast("Upload failed")
        }
    }
}



