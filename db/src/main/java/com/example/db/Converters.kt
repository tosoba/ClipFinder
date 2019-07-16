package com.example.db

import androidx.room.TypeConverter
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

class StringListConverter {
    @TypeConverter
    fun listFromString(string: String): List<String> = Gson().fromJson(string, object : TypeToken<List<String>>() {}.type)

    @TypeConverter
    fun stringToList(list: List<String>): String = Gson().toJson(list)
}
