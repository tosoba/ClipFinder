package com.example.there.domain.repo.spotify

import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface ISpotifyRepository {
    val categories: Observable<List<CategoryEntity>>
    val featuredPlaylists: Observable<List<PlaylistEntity>>
    val dailyViralTracks: Observable<List<TopTrackEntity>>

    fun searchAll(query: String, offset: Int): Single<SearchAllEntity>

    fun getPlaylistsForCategory(categoryId: String, offset: Int): Single<EntityPage<PlaylistEntity>>
    fun getPlaylistTracks(playlistId: String, userId: String, offset: Int): Single<EntityPage<TrackEntity>>
    fun getTracksFromAlbum(albumId: String): Observable<EntityPage<TrackEntity>>
    fun getAlbum(albumId: String): Single<AlbumEntity>
    fun getSimilarTracks(trackId: String): Observable<List<TrackEntity>>
    fun getArtists(artistIds: List<String>): Single<List<ArtistEntity>>
    fun getTopTracksFromArtist(artistId: String): Single<List<TrackEntity>>
    fun getAlbumsFromArtist(artistId: String): Observable<List<AlbumEntity>>
    fun getRelatedArtists(artistId: String): Single<List<ArtistEntity>>
    fun getNewReleases(offset: Int): Single<EntityPage<AlbumEntity>>

    fun getCurrentUsersPlaylists(offset: Int): Single<EntityPage<PlaylistEntity>>
    fun getCurrentUsersTopTracks(offset: Int): Single<EntityPage<TrackEntity>>
    fun getCurrentUsersTopArtists(offset: Int): Single<EntityPage<ArtistEntity>>

    fun getCurrentUsersSavedTracks(offset: Int): Single<EntityPage<TrackEntity>>
    fun getCurrentUsersSavedAlbums(offset: Int): Single<EntityPage<AlbumEntity>>

    val favouriteAlbums: Flowable<List<AlbumEntity>>
    val favouriteArtists: Flowable<List<ArtistEntity>>
    val favouriteCategories: Flowable<List<CategoryEntity>>
    val favouritePlaylists: Flowable<List<PlaylistEntity>>
    val favouriteTracks: Flowable<List<TrackEntity>>
    fun insertAlbum(albumEntity: AlbumEntity): Completable
    fun insertArtist(artistEntity: ArtistEntity): Completable
    fun insertCategory(categoryEntity: CategoryEntity): Completable
    fun insertPlaylist(playlistEntity: PlaylistEntity): Completable
    fun insertTrack(trackEntity: TrackEntity): Completable

    val currentUser: Single<UserEntity>

    fun isAlbumSaved(albumEntity: AlbumEntity): Single<Boolean>
    fun isArtistSaved(artistEntity: ArtistEntity): Single<Boolean>
    fun isCategorySaved(categoryEntity: CategoryEntity): Single<Boolean>
    fun isPlaylistSaved(playlistEntity: PlaylistEntity): Single<Boolean>
    fun isTrackSaved(trackEntity: TrackEntity): Single<Boolean>

    fun deleteAlbum(albumEntity: AlbumEntity): Completable
    fun deleteArtist(artistEntity: ArtistEntity): Completable
    fun deleteCategory(categoryEntity: CategoryEntity): Completable
    fun deletePlaylist(playlistEntity: PlaylistEntity): Completable
    fun deleteTrack(trackEntity: TrackEntity): Completable

    fun getAudioFeatures(trackEntity: TrackEntity): Single<AudioFeaturesEntity>
}