package com.example.db.model.spotify

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.example.core.model.StringUrlModel
import com.example.db.StringUrlModelConverter

@Entity(tableName = "categories")
data class CategoryDbModel(
        @PrimaryKey
        val id: String,

        @TypeConverters(StringUrlModelConverter::class)
        val icons: List<StringUrlModel>,

        val name: String
)