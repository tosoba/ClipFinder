package com.example.spotifyartist.data

import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.ext.isPresent
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.db.ArtistDao
import com.example.spotifyapi.SpotifyArtistsApi
import com.example.spotifyapi.models.Artist
import com.example.spotifyapi.models.SimpleAlbum
import com.example.spotifyapi.models.Track
import com.example.spotifyapi.util.domain
import com.example.spotifyartist.domain.repo.ISpotifyArtistRepo
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.Completable
import io.reactivex.Single

class SpotifyArtistRepo(
    private val artistDao: ArtistDao,
    private val artistsApi: SpotifyArtistsApi,
    private val auth: SpotifyAuth,
    private val preferences: SpotifyPreferences
) : ISpotifyArtistRepo {

    override fun deleteArtist(artist: ArtistEntity): Completable = Completable.fromCallable {
        artistDao.delete(artist.db)
    }

    override fun getAlbumsFromArtist(
        artistId: String,
        offset: Int
    ): Single<Resource<Paged<List<AlbumEntity>>>> = auth.withTokenSingle { token ->
        artistsApi.getArtistsAlbums(
            authorization = token,
            id = artistId,
            offset = offset
        ).mapToResource {
            Paged(
                contents = items.map(SimpleAlbum::domain),
                offset = offset,
                total = total
            )
        }
    }

    override fun getRelatedArtists(
        artistId: String
    ): Single<Resource<List<ArtistEntity>>> = auth.withTokenSingle { token ->
        artistsApi.getArtistsRelatedArtists(
            authorization = token,
            id = artistId
        ).mapToResource { result.map(Artist::domain) }
    }

    override fun getTopTracksFromArtist(
        artistId: String
    ): Single<Resource<List<TrackEntity>>> = auth.withTokenSingle { token ->
        artistsApi.getArtistsTopTracks(
            authorization = token,
            id = artistId,
            country = preferences.country
        ).mapToResource { result.map(Track::domain) }
    }

    override fun insertArtist(artist: ArtistEntity): Completable = Completable.fromCallable {
        artistDao.insert(artist.db)
    }

    override fun isArtistSaved(
        artistId: String
    ): Single<Boolean> = artistDao.findById(artistId).isPresent()
}
