package com.example.there.domain.repo.spotify

import com.example.there.domain.entity.spotify.*
import com.example.there.domain.repo.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface ISpotifyLocalRepo : IFavouriteTrackRepo<TrackEntity>,
        IFavouritePlaylistRepository<PlaylistEntity>,
        IFavouriteAlbumRepository<AlbumEntity>,
        IFavouriteArtistRepository<ArtistEntity>,
        IFavouriteCategoryRepository<CategoryEntity> {

    override val favouriteAlbums: Flowable<List<AlbumEntity>>
    override val favouriteArtists: Flowable<List<ArtistEntity>>
    override val favouriteCategories: Flowable<List<CategoryEntity>>
    override val favouritePlaylists: Flowable<List<PlaylistEntity>>
    override val favouriteTracks: Flowable<List<TrackEntity>>

    override fun insertAlbum(album: AlbumEntity): Completable
    override fun insertArtist(artist: ArtistEntity): Completable
    override fun insertCategory(category: CategoryEntity): Completable
    override fun insertPlaylist(playlist: PlaylistEntity): Completable
    override fun insertTrack(track: TrackEntity): Completable

    override fun isAlbumSaved(album: AlbumEntity): Single<Boolean>
    override fun isArtistSaved(artist: ArtistEntity): Single<Boolean>
    override fun isCategorySaved(category: CategoryEntity): Single<Boolean>
    override fun isPlaylistSaved(playlist: PlaylistEntity): Single<Boolean>
    override fun isTrackSaved(track: TrackEntity): Single<Boolean>

    override fun deleteAlbum(album: AlbumEntity): Completable
    override fun deleteArtist(artist: ArtistEntity): Completable
    override fun deleteCategory(category: CategoryEntity): Completable
    override fun deletePlaylist(playlist: PlaylistEntity): Completable
    override fun deleteTrack(track: TrackEntity): Completable
}