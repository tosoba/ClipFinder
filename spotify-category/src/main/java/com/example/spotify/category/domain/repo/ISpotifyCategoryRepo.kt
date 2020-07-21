package com.example.spotify.category.domain.repo

import com.example.core.model.Resource
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import io.reactivex.Completable
import io.reactivex.Single

interface ISpotifyCategoryRepo {
    fun deleteCategory(category: CategoryEntity): Completable
    fun getPlaylistsForCategory(categoryId: String, offset: Int): Single<Resource<Page<PlaylistEntity>>>
    fun insertCategory(category: CategoryEntity): Completable
    fun isCategorySaved(categoryId: String): Single<Boolean>
}
