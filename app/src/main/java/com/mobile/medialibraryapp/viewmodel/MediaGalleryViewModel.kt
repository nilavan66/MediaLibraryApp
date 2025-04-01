package com.mobile.medialibraryapp.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mobile.medialibraryapp.model.State
import com.mobile.medialibraryapp.state.MediaGalleryState
import com.mobile.medialibraryapp.webservice.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MediaGalleryViewModel @Inject constructor(private val authRepository: FirebaseAuthRepository) :
    BaseViewModel<MediaGalleryState>() {

    private var setMediaGalleryState: MediaGalleryState = MediaGalleryState.Init
        set(value) {
            field = value
            setState(setMediaGalleryState)
        }

    init {
        checkUserAuth()
    }

    private fun checkUserAuth() {
        val user = FirebaseAuth.getInstance().currentUser
        Timber.tag("AuthCheck").d("User: ${user?.uid ?: "Not logged in"}")

        if (user == null) {
            // Handle unauthenticated user (e.g., redirect to login screen)
            Timber.tag("AuthCheck").e("User is not authenticated!")
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logoutUser(_baseState).collect { it ->
                when (it) {
                    is State.Success -> {
                        setMediaGalleryState = if (it.data) {
                            sharedPrefManager.clearData()
                            MediaGalleryState.MediaGallerySuccessState
                        } else {
                            MediaGalleryState.ShowMessage("Logout failed! Try again")
                        }

                    }

                    is State.Error -> showToast(it.message)

                    else -> dismissProgressBar()
                }
            }
        }
    }
}