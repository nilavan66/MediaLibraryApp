package com.mobile.medialibraryapp.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.medialibraryapp.dataclass.MediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {

    @Query("SELECT * FROM media_table ORDER BY uploadDate DESC")
    fun getAllMedia(): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_table WHERE documentId = :documentId")
    suspend fun getMediaById(documentId: String): MediaEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMedia(media: MediaEntity)

    @Query("SELECT COUNT(*) FROM media_table WHERE documentId = :documentId")
    suspend fun isMediaExists(documentId: String): Int

    @Query("UPDATE media_table SET name = :name, mediaType = :mediaType, size = :size, uploadDate = :uploadDate, mediaUrl = :mediaUrl WHERE documentId = :documentId")
    suspend fun updateMediaById(
        documentId: String,
        name: String,
        mediaType: String,
        size: Long,
        uploadDate: Long,
        mediaUrl: String
    )

    @Query("DELETE FROM media_table WHERE documentId = :documentId")
    suspend fun deleteMediaByDocumentId(documentId: String)

    @Query("DELETE FROM media_table")
    suspend fun deleteAll()
}

