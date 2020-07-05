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

    override fun isAlbumSaved(albumId: String): Single<Boolean>
    override fun isArtistSaved(artistId: String): Single<Boolean>
    override fun isCategorySaved(categoryId: String): Single<Boolean>
    override fun isPlaylistSaved(playlistId: String): Single<Boolean>
    override fun isTrackSaved(trackId: String): Single<Boolean>

    override fun deleteAlbum(album: AlbumEntity): Completable
    override fun deleteArtist(artist: ArtistEntity): Completable
    override fun deleteCategory(category: CategoryEntity): Completable
    override fun deletePlaylist(playlist: PlaylistEntity): Completable
    override fun deleteTrack(track: TrackEntity): Completable
}
