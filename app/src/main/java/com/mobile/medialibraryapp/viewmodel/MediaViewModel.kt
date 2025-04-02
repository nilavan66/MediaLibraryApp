package com.mobile.medialibraryapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.medialibraryapp.dataclass.MediaEntity
import com.mobile.medialibraryapp.roomdb.MediaRepository
import com.mobile.medialibraryapp.state.MediaGalleryState
import com.mobile.medialibraryapp.state.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(private val repository: MediaRepository) : BaseViewModel<MediaGalleryState>() {


    private val searchQuery = MutableStateFlow("")

    val mediaList: Flow<PagingData<MediaEntity>> = searchQuery
        .flatMapLatest { query ->
            repository.getPagedMedia()
                .map { pagingData ->
                    pagingData.filter { media ->
                        media.name.contains(query, ignoreCase = true) ||
                                media.mediaType.contains(query, ignoreCase = true)
                    }
                }
        }
        .cachedIn(viewModelScope)

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }
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