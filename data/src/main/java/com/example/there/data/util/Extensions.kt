package com.example.there.data.util

import com.example.there.data.entities.IconData

val List<IconData>.iconUrlOrDefault: String
    get() = getOrNull(0)?.url
            ?: "https://t.scdn.co/media/derived/r-b-274x274_fd56efa72f4f63764b011b68121581d8_0_0_274_274.jpg"