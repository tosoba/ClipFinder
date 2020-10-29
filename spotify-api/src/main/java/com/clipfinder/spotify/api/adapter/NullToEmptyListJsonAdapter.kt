package com.clipfinder.spotify.api.adapter

import com.squareup.moshi.*
import java.io.IOException
import java.lang.reflect.Type

class NullToEmptyListJsonAdapter(private val delegate: JsonAdapter<List<*>>) : JsonAdapter<List<*>?>() {
    override fun fromJson(reader: JsonReader): List<*>? = if (reader.peek() == JsonReader.Token.NULL) {
        reader.skipValue()
        emptyList<Any>()
    } else {
        delegate.fromJson(reader)
    }

    override fun toJson(writer: JsonWriter, value: List<*>?) {
        checkNotNull(value) { "Wrap JsonAdapter with .nullSafe()." }
        delegate.toJson(writer, value)
    }

    companion object {
        val FACTORY: Factory = object : Factory {
            override fun create(type: Type, annotations: Set<Annotation?>, moshi: Moshi): JsonAdapter<*>? {
                if (annotations.isNotEmpty() || Types.getRawType(type) != MutableList::class.java) return null
                val objectJsonAdapter = moshi.nextAdapter<List<*>>(this, type, annotations)
                return NullToEmptyListJsonAdapter(objectJsonAdapter)
            }
        }
    }
}