package com.example.db.model.spotify

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.example.db.StringListConverter

@Entity(tableName = "categories")
data class CategoryDbModel(
        @PrimaryKey
        val id: String,

        @TypeConverters(StringListConverter::class)
        val icons: List<String>,

        val name: String
)