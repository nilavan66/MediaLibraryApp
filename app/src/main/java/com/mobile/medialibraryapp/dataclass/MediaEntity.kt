package com.mobile.medialibraryapp.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_table")
data class MediaEntity(
    @PrimaryKey(autoGenerate = false) val documentId: String,
    val name: String,
    val mediaType: String,
    val size: Long,
    val uploadDate: Long,
    val mediaUrl: String
)
