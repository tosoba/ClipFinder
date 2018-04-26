package com.example.there.data.util

import com.example.there.data.entities.IconData

private const val DEFAULT_ICON_URL = "https://t.scdn.co/media/derived/r-b-274x274_fd56efa72f4f63764b011b68121581d8_0_0_274_274.jpg"

val List<IconData>.firstIconUrlOrDefault: String
    get() = getOrNull(0)?.url ?: DEFAULT_ICON_URL

val List<IconData>.secondIconUrlOrDefault: String
    get() = getOrNull(1)?.url ?: getOrNull(0)?.url ?: DEFAULT_ICON_URL