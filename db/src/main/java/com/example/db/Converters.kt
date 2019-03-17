package com.example.db

import android.arch.persistence.room.TypeConverter
import com.example.core.model.StringIdModel
import com.example.core.model.StringUrlModel
import com.example.db.model.spotify.SimplifiedAlbumDbModel
import com.example.db.model.spotify.SimplifiedArtistDbModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class SimplifiedAlbumConverter {
    @TypeConverter
    fun toString(obj: SimplifiedAlbumDbModel): String = Gson().toJson(obj)

    @TypeConverter
    fun toObject(
            string: String
    ): SimplifiedAlbumDbModel = Gson().fromJson(
            string,
            object : TypeToken<SimplifiedAlbumDbModel>() {}.type
    )
}

class SimplifiedArtistConverter {
    @TypeConverter
    fun listToString(list: List<SimplifiedArtistDbModel>): String = Gson().toJson(ArrayList(list))

    @TypeConverter
    fun stringToList(
            string: String
    ): List<SimplifiedArtistDbModel> = Gson().fromJson<ArrayList<SimplifiedArtistDbModel>>(
            string,
            object : TypeToken<ArrayList<SimplifiedArtistDbModel>>() {}.type
    )
}

class StringUrlModelConverter {
    @TypeConverter
    fun listToString(list: List<StringUrlModel>): String = Gson().toJson(ArrayList(list))

    @TypeConverter
    fun stringToList(
            string: String
    ): List<StringUrlModel> = Gson().fromJson<ArrayList<StringUrlModel>>(
            string,
            object : TypeToken<ArrayList<StringUrlModel>>() {}.type
    )
}

class StringIdModelConverter {
    @TypeConverter
    fun toString(obj: StringIdModel): String = Gson().toJson(obj)

    @TypeConverter
    fun toObject(
            string: String
    ): StringIdModel = Gson().fromJson(
            string,
            object : TypeToken<StringIdModel>() {}.type
    )
}