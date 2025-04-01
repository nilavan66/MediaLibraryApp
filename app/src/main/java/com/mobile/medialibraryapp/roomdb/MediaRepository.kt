package com.mobile.medialibraryapp.roomdb

import android.util.Log
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

    suspend fun getMediaById(documentId: String): MediaEntity? = mediaDao.getMediaById(documentId)


    suspend fun insertMedia(media: MediaEntity) {
        mediaDao.insertMedia(media)
    }

    suspend fun deleteAllMedia(){
        mediaDao.deleteAll()
    }

    /*fun fetchMediaFromFirestore(viewModelScope: CoroutineScope) {

        firestore.collection("media_data")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.e("Firestore", "Error listening for updates", error)
                    return@addSnapshotListener
                }

                snapshots?.documentChanges?.forEach { change ->
                    val document = change.document
                    val documentId = document.id

                    when (change.type) {
                        DocumentChange.Type.ADDED -> {
                            val name = document.getString("name") ?: "Unknown"
                            val mediaType = document.getString("contentType") ?: "Unknown"
                            val size = document.getLong("size") ?: 0L
                            val uploadDate = document.getTimestamp("timeCreated")?.seconds ?: 0L
                            val mediaUrl = document.getString("downloadUrl") ?: ""

                            val mediaEntity = MediaEntity(
                                documentId = documentId,
                                name = name,
                                mediaType = mediaType,
                                size = size,
                                uploadDate = uploadDate,
                                mediaUrl = mediaUrl
                            )

                            viewModelScope.launch(Dispatchers.IO) {
                                val exists = mediaDao.isMediaExists(documentId) > 0
                                if (!exists) {
                                    mediaDao.insertMedia(mediaEntity)
                                }
                            }
                        }

                        DocumentChange.Type.REMOVED -> {
                            viewModelScope.launch(Dispatchers.IO) {
                                mediaDao.deleteMediaByDocumentId(documentId)
                            }
                        }
                        else -> {}
                    }
                }
            }
    }*/

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

