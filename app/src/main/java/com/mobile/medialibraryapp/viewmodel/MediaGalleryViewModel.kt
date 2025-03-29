package com.mobile.medialibraryapp.viewmodel

import androidx.lifecycle.viewModelScope
import com.mobile.medialibraryapp.model.State
import com.mobile.medialibraryapp.state.MediaGalleryState
import com.mobile.medialibraryapp.webservice.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaGalleryViewModel @Inject constructor(private val authRepository: FirebaseAuthRepository) :
    BaseViewModel<MediaGalleryState>() {

    private var setMediaGalleryState: MediaGalleryState = MediaGalleryState.Init
        set(value) {
            field = value
            setState(setMediaGalleryState)
        }

    fun logout() {
        viewModelScope.launch {
            authRepository.logoutUser(_baseState).collect { it ->
                when (it) {
                    is State.Success -> {

                        if (it.data){
                            sharedPrefManager.clearData()
                            setMediaGalleryState = MediaGalleryState.MediaGallerySuccessState
                        }else{
                            showToast("Logout failed! Try again")
                        }

                    }

                    is State.Error -> showToast(it.message)

                    else -> dismissProgressBar()
                }
            }
        }
    }

}