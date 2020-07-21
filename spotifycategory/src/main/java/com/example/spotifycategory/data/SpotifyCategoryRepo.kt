package com.example.spotifycategory.data

import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.ext.isPresent
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.db.CategoryDao
import com.example.spotifyapi.SpotifyBrowseApi
import com.example.spotifyapi.models.SimplePlaylist
import com.example.spotifyapi.util.domain
import com.example.spotifycategory.domain.repo.ISpotifyCategoryRepo
import com.example.spotifyrepo.mapper.db
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import io.reactivex.Completable
import io.reactivex.Single

class SpotifyCategoryRepo(
    private val categoryDao: CategoryDao,
    private val browseApi: SpotifyBrowseApi,
    private val auth: SpotifyAuth,
    private val preferences: SpotifyPreferences
) : ISpotifyCategoryRepo {

    override fun getPlaylistsForCategory(
        categoryId: String,
        offset: Int
    ): Single<Resource<Page<PlaylistEntity>>> = auth.withTokenSingle { token ->
        browseApi.getPlaylistsForCategory(
            authorization = token,
            categoryId = categoryId,
            offset = offset,
            country = preferences.country
        ).mapToResource {
            Page(
                items = result.items.map(SimplePlaylist::domain),
                offset = result.offset + SpotifyDefaults.LIMIT,
                total = result.total
            )
        }
    }

    override fun deleteCategory(category: CategoryEntity): Completable = Completable.fromCallable {
        categoryDao.delete(category.db)
    }

    override fun insertCategory(category: CategoryEntity): Completable = Completable.fromCallable {
        categoryDao.insert(category.db)
    }

    override fun isCategorySaved(categoryId: String): Single<Boolean> = categoryDao
        .findById(categoryId)
        .isPresent()
}
