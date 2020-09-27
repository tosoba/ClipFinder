package com.clipfinder.core.spotify.model

interface ICategory {
    val href: String
    val icons: List<IImage>
    val id: String
    val name: String
}
