package com.example.there.data.entities.spotify

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.example.there.data.db.IconDataConverter

@Entity(tableName = "categories")
data class CategoryData(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: String,

        @ColumnInfo(name = "icons")
        @TypeConverters(IconDataConverter::class)
        val icons: List<IconData>,

        @ColumnInfo(name = "name")
        val name: String
)