package com.mobile.medialibraryapp.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mobile.medialibraryapp.dataclass.MediaEntity

@Database(entities = [MediaEntity::class], version = 1, exportSchema = false)
abstract class MediaDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao
}