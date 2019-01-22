package com.example.there.data.db

import android.arch.persistence.room.TypeConverter
import com.example.there.data.entity.spotify.IconData
import com.example.there.data.entity.spotify.OwnerData
import com.example.there.data.entity.spotify.SimplifiedAlbumData
import com.example.there.data.entity.spotify.SimplifiedArtistData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class SimplifiedAlbumDataConverter {
    @TypeConverter
    fun toString(obj: SimplifiedAlbumData): String = Gson().toJson(obj)

    @TypeConverter
    fun toObject(
            string: String
    ): SimplifiedAlbumData = Gson().fromJson(string, object : TypeToken<SimplifiedAlbumData>() {}.type)
}

class SimplifiedArtistDataConverter {
    @TypeConverter
    fun listToString(list: List<SimplifiedArtistData>): String {
        val arrayList = ArrayList(list)
        return Gson().toJson(arrayList)
    }

    @TypeConverter
    fun stringToList(
            string: String
    ): List<SimplifiedArtistData> = Gson().fromJson<ArrayList<SimplifiedArtistData>>(
            string,
            object : TypeToken<ArrayList<SimplifiedArtistData>>() {}.type
    )
}

class IconDataConverter {
    @TypeConverter
    fun listToString(list: List<IconData>): String {
        val arrayList = ArrayList(list)
        return Gson().toJson(arrayList)
    }

    @TypeConverter
    fun stringToList(
            string: String
    ): List<IconData> = Gson().fromJson<ArrayList<IconData>>(
            string,
            object : TypeToken<ArrayList<IconData>>() {}.type
    )
}

class OwnerDataConverter {
    @TypeConverter
    fun toString(obj: OwnerData): String = Gson().toJson(obj)

    @TypeConverter
    fun toObject(
            string: String
    ): OwnerData = Gson().fromJson(string, object : TypeToken<OwnerData>() {}.type)
}