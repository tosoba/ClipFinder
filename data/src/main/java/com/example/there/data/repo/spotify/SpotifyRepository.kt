package com.example.there.data.repo.spotify

import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.*
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.repo.spotify.datastore.ISpotifyDbDataStore
import com.example.there.domain.repo.spotify.datastore.ISpotifyRemoteDataStore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotifyRepository @Inject constructor(
        private val remoteDataStore: ISpotifyRemoteDataStore,
        private val dbDataStore: ISpotifyDbDataStore
) : ISpotifyRepository {

    override val categories: Observable<List<CategoryEntity>>
        get() = remoteDataStore.categories

    override val featuredPlaylists: Observable<List<PlaylistEntity>>
        get() = remoteDataStore.featuredPlaylists

    override val dailyViralTracks: Observable<List<TopTrackEntity>>
        get() = remoteDataStore.dailyViralTracks

    override val currentUser: Single<UserEntity>
        get() = remoteDataStore.currentUser

    override fun searchAll(
            query: String,
            offset: Int
    ): Single<SearchAllEntity> = remoteDataStore.searchAll(query, offset)

    override fun getPlaylistsForCategory(
            categoryId: String,
            offset: Int
    ): Single<EntityPage<PlaylistEntity>> = remoteDataStore.getPlaylistsForCategory(categoryId, offset)

    override fun getPlaylistTracks(
            playlistId: String,
            userId: String,
            offset: Int
    ): Single<EntityPage<TrackEntity>> = remoteDataStore.getPlaylistTracks(playlistId, userId, offset)

    override fun getAlbum(
            albumId: String
    ): Single<AlbumEntity> = remoteDataStore.getAlbum(albumId)

    override fun getArtists(
            artistIds: List<String>
    ): Single<List<ArtistEntity>> = remoteDataStore.getArtists(artistIds)

    override fun getSimilarTracks(
            trackId: String
    ): Observable<List<TrackEntity>> = remoteDataStore.getSimilarTracks(trackId)

    override fun getAlbumsFromArtist(
            artistId: String
    ): Observable<List<AlbumEntity>> = remoteDataStore.getAlbumsFromArtist(artistId)

    override fun getTopTracksFromArtist(
            artistId: String
    ): Single<List<TrackEntity>> = remoteDataStore.getTopTracksFromArtist(artistId)

    override fun getRelatedArtists(
            artistId: String
    ): Single<List<ArtistEntity>> = remoteDataStore.getRelatedArtists(artistId)

    override fun getTracksFromAlbum(
            albumId: String
    ): Observable<EntityPage<TrackEntity>> = remoteDataStore.getTracksFromAlbum(albumId)

    override fun getNewReleases(
            offset: Int
    ): Single<EntityPage<AlbumEntity>> = remoteDataStore.getNewReleases(offset)

    override fun getCurrentUsersPlaylists(
            offset: Int
    ): Single<EntityPage<PlaylistEntity>> = remoteDataStore.getCurrentUsersPlaylists(offset)

    override fun getCurrentUsersTopTracks(
            offset: Int
    ): Single<EntityPage<TrackEntity>> = remoteDataStore.getCurrentUsersTopTracks(offset)

    override fun getCurrentUsersTopArtists(
            offset: Int
    ): Single<EntityPage<ArtistEntity>> = remoteDataStore.getCurrentUsersTopArtists(offset)

    override fun getCurrentUsersSavedTracks(
            offset: Int
    ): Single<EntityPage<TrackEntity>> = remoteDataStore.getCurrentUsersSavedTracks(offset)

    override fun getCurrentUsersSavedAlbums(
            offset: Int
    ): Single<EntityPage<AlbumEntity>> = remoteDataStore.getCurrentUsersSavedAlbums(offset)

    override fun getAudioFeatures(
            trackEntity: TrackEntity
    ): Single<AudioFeaturesEntity> = remoteDataStore.getAudioFeatures(trackEntity)

    override val favouriteAlbums: Flowable<List<AlbumEntity>>
        get() = dbDataStore.favouriteAlbums

    override val favouriteArtists: Flowable<List<ArtistEntity>>
        get() = dbDataStore.favouriteArtists

    override val favouriteCategories: Flowable<List<CategoryEntity>>
        get() = dbDataStore.favouriteCategories

    override val favouritePlaylists: Flowable<List<PlaylistEntity>>
        get() = dbDataStore.favouritePlaylists

    override val favouriteTracks: Flowable<List<TrackEntity>>
        get() = dbDataStore.favouriteTracks

    override fun insertAlbum(album: AlbumEntity): Completable = dbDataStore.insertAlbum(album)

    override fun insertArtist(artist: ArtistEntity): Completable = dbDataStore.insertArtist(artist)

    override fun insertCategory(category: CategoryEntity): Completable = dbDataStore.insertCategory(category)

    override fun insertPlaylist(playlist: PlaylistEntity): Completable = dbDataStore.insertPlaylist(playlist)

    override fun insertTrack(track: TrackEntity): Completable = dbDataStore.insertTrack(track)

    override fun isAlbumSaved(album: AlbumEntity): Single<Boolean> = dbDataStore.isAlbumSaved(album)

    override fun isArtistSaved(artist: ArtistEntity): Single<Boolean> = dbDataStore.isArtistSaved(artist)

    override fun isCategorySaved(category: CategoryEntity): Single<Boolean> = dbDataStore.isCategorySaved(category)

    override fun isPlaylistSaved(playlist: PlaylistEntity): Single<Boolean> = dbDataStore.isPlaylistSaved(playlist)

    override fun isTrackSaved(track: TrackEntity): Single<Boolean> = dbDataStore.isTrackSaved(track)

    override fun deleteAlbum(album: AlbumEntity): Completable = dbDataStore.deleteAlbum(album)

    override fun deleteArtist(artist: ArtistEntity): Completable = dbDataStore.deleteArtist(artist)

    override fun deleteCategory(category: CategoryEntity): Completable = dbDataStore.deleteCategory(category)

    override fun deletePlaylist(playlist: PlaylistEntity): Completable = dbDataStore.deletePlaylist(playlist)

    override fun deleteTrack(track: TrackEntity): Completable = dbDataStore.deleteTrack(track)
}