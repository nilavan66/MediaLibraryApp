package com.mobile.medialibraryapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.medialibraryapp.dataclass.MediaEntity
import com.mobile.medialibraryapp.roomdb.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(private val repository: MediaRepository) : ViewModel() {
    val mediaList: StateFlow<List<MediaEntity>> = repository.getAllMedia()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        fetchMediaFromFirestore()
    }

    fun fetchMediaFromFirestore() {
        repository.fetchMediaFromFirestore(viewModelScope)
    }

    suspend fun getMediaById(documentId: String): MediaEntity? {
        return repository.getMediaById(documentId)
    }

    fun getAllMedia(): Flow<List<MediaEntity>> {
        return repository.getAllMedia()
    }

    fun deleteAllMedia(){
        viewModelScope.launch{
            repository.deleteAllMedia()
        }
    }
}