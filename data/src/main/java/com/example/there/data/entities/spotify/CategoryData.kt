package com.example.there.data.entities.spotify

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.example.there.data.db.IconDataConverter

@Entity(tableName = "categories")
data class CategoryData(
        @PrimaryKey
        val id: String,

        @TypeConverters(IconDataConverter::class)
        val icons: List<IconData>,

        val name: String
)