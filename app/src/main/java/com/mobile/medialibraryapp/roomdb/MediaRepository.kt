package com.mobile.medialibraryapp.roomdb

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.medialibraryapp.dataclass.MediaEntity
import com.mobile.medialibraryapp.util.parseDateToMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject


class MediaRepository @Inject constructor(
    private val mediaDao: MediaDao,
    private val firestore: FirebaseFirestore
) {

    fun getAllMedia(): Flow<List<MediaEntity>> {
        return mediaDao.getAllMedia()
    }

    fun getPagedMedia(): Flow<PagingData<MediaEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { mediaDao.getPagedMedia() }
        ).flow
    }

    suspend fun getMediaById(documentId: String): MediaEntity? = mediaDao.getMediaById(documentId)

    suspend fun deleteAllMedia(){
        mediaDao.deleteAll()
    }

    fun fetchMediaFromFirestore(viewModelScope: CoroutineScope) {
        firestore.collection("media_data")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.e("Firestore", "Error listening for updates", error)
                    return@addSnapshotListener
                }

                snapshots?.documentChanges?.forEach { change ->
                    val document = change.document
                    val documentId = document.id

                    val name = document.getString("name") ?: "Unknown"
                    val mediaType = document.getString("contentType") ?: "Unknown"
                    val size = document.getLong("size") ?: 0L
                    val uploadDate = document.getTimestamp("timeCreated")?.seconds ?: 0L
                    val mediaUrl = document.getString("downloadUrl") ?: ""

                    viewModelScope.launch(Dispatchers.IO) {
                        when (change.type) {
                            DocumentChange.Type.ADDED -> {
                                val exists = mediaDao.getMediaById(documentId) != null
                                if (!exists) {
                                    mediaDao.insertMedia(
                                        MediaEntity(documentId, name, mediaType, size, uploadDate, mediaUrl)
                                    )
                                    Log.d("Firestore", "Added media: $documentId => $name")
                                }
                            }

                            DocumentChange.Type.MODIFIED -> {
                                mediaDao.updateMediaById(documentId, name, mediaType, size, uploadDate, mediaUrl)
                                Log.d("Firestore", "Updated media: $documentId => $name")
                            }

                            DocumentChange.Type.REMOVED -> {
                                mediaDao.deleteMediaByDocumentId(documentId)
                                Log.d("Firestore", "Deleted media: $documentId")
                            }
                        }
                    }
                }
            }
    }

}

