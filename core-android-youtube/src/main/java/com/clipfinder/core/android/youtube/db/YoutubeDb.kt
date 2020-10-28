package com.clipfinder.core.android.youtube.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.clipfinder.core.android.youtube.db.dao.SearchDao
import com.clipfinder.core.android.youtube.db.model.SearchResponseEntity

@Database(
    version = 1,
    exportSchema = false,
    entities = [SearchResponseEntity::class]
)
@TypeConverters(YoutubeDbConverters::class)
abstract class YoutubeDb : RoomDatabase() {
    abstract fun searchDao(): SearchDao
}
