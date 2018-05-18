package com.example.there.data.db

import android.arch.persistence.room.TypeConverter
import com.example.there.data.entities.spotify.IconData
import com.example.there.data.entities.spotify.OwnerData
import com.example.there.data.entities.spotify.SimplifiedAlbumData
import com.example.there.data.entities.spotify.SimplifiedArtistData
import com.example.there.data.entities.videos.Statistics
import com.example.there.data.entities.videos.VideoContentDetails
import com.example.there.data.entities.videos.VideoSnippet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class DbTypeConverter<T> {
    private val gson: Gson = Gson()

    @TypeConverter
    fun toString(obj: T): String = gson.toJson(obj)

    @TypeConverter
    fun toObject(string: String): T = gson.fromJson(string, object : TypeToken<T>() {}.type)

    @TypeConverter
    fun listToString(list: List<T>): String = gson.toJson(list)

    @TypeConverter
    fun stringToList(string: String): List<T> = gson.fromJson(string, object : TypeToken<List<T>>() {}.type)
}

class SimplifiedAlbumDataConverter : DbTypeConverter<SimplifiedAlbumData>()
class SimplifiedArtistDataConverter : DbTypeConverter<SimplifiedArtistData>()
class IconDataConverter : DbTypeConverter<IconData>()
class OwnerDataConverter : DbTypeConverter<OwnerData>()

class VideoSnippetConverter : DbTypeConverter<VideoSnippet>()
class VideoContentDetailsConverter : DbTypeConverter<VideoContentDetails>()
class VideoStatisticsConverter : DbTypeConverter<Statistics>()