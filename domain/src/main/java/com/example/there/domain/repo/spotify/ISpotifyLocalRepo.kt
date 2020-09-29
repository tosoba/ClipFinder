package com.example.there.domain.repo.spotify

import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.IFavouriteAlbumRepository
import com.example.there.domain.repo.IFavouriteArtistRepository
import com.example.there.domain.repo.IFavouritePlaylistRepository
import com.example.there.domain.repo.IFavouriteTrackRepo
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface ISpotifyLocalRepo : IFavouriteTrackRepo<TrackEntity>,
    IFavouritePlaylistRepository<PlaylistEntity>,
    IFavouriteAlbumRepository<AlbumEntity>,
    IFavouriteArtistRepository<ArtistEntity> {

    override val favouriteAlbums: Flowable<List<AlbumEntity>>
    override val favouriteArtists: Flowable<List<ArtistEntity>>
    override val favouritePlaylists: Flowable<List<PlaylistEntity>>
    override val favouriteTracks: Flowable<List<TrackEntity>>

    override fun insertAlbum(album: AlbumEntity): Completable
    override fun insertArtist(artist: ArtistEntity): Completable
    override fun insertPlaylist(playlist: PlaylistEntity): Completable
    override fun insertTrack(track: TrackEntity): Completable

    override fun isAlbumSaved(albumId: String): Single<Boolean>
    override fun isArtistSaved(artistId: String): Single<Boolean>
    override fun isPlaylistSaved(playlistId: String): Single<Boolean>
    override fun isTrackSaved(trackId: String): Single<Boolean>

    override fun deleteAlbum(album: AlbumEntity): Completable
    override fun deleteArtist(artist: ArtistEntity): Completable
    override fun deletePlaylist(playlist: PlaylistEntity): Completable
    override fun deleteTrack(track: TrackEntity): Completable
}
