package com.example.there.data.apis.spotify

import com.example.there.data.entities.spotify.AlbumData
import com.example.there.data.entities.spotify.ArtistData
import com.example.there.data.entities.spotify.TrackData
import com.example.there.data.responses.*
import io.reactivex.Observable
import retrofit2.http.*

interface SpotifyApi {

    @GET("browse/categories")
    fun getCategories(@Header("Authorization") authorization: String,
                      @Query("country") country: String = DEFAULT_COUNTRY,
                      @Query("locale") locale: String = DEFAULT_LOCALE,
                      @Query("offset") offset: String = DEFAULT_OFFSET,
                      @Query("limit") limit: String = DEFAULT_LIMIT): Observable<CategoriesResponse>

    @GET("browse/featured-playlists")
    fun getFeaturedPlaylists(@Header("Authorization") authorization: String,
                             @Query("country") country: String = DEFAULT_COUNTRY,
                             @Query("offset") offset: String = DEFAULT_OFFSET,
                             @Query("limit") limit: String = DEFAULT_LIMIT): Observable<PlaylistsResponse>

    @GET("tracks/{id}")
    fun getTrack(@Header("Authorization") authorization: String,
                 @Path("id") id: String): Observable<TrackData>

    @GET("tracks")
    fun getTracks(@Header("Authorization") authorization: String,
                  @Query("ids") ids: String): Observable<TracksOnlyResponse>

    @GET("search")
    fun searchAll(@Header("Authorization") authorization: String,
                  @Query("q") query: String,
                  @Query("type") type: String = ALL_SEARCH_TYPES,
                  @Query("offset") offset: String = DEFAULT_OFFSET,
                  @Query("limit") limit: String = DEFAULT_LIMIT): Observable<SearchAllResponse>

    @GET("browse/categories/{category_id}/playlists")
    fun getPlaylistsForCategory(@Header("Authorization") authorization: String,
                                @Path("category_id") categoryId: String): Observable<PlaylistsResponse>

    @GET("users/{user_id}/playlists/{playlist_id}/tracks")
    fun getPlaylistTracks(@Header("Authorization") authorization: String,
                          @Path("user_id") userId: String,
                          @Path("playlist_id") playlistId: String): Observable<PlaylistTracksResponse>

    @GET("artists/{id}")
    fun getArtist(@Header("Authorization") authorization: String,
                  @Path("id") artistId: String): Observable<ArtistData>

    @GET("artists")
    fun getArtists(@Header("Authorization") authorization: String,
                   @Query("ids") artistIds: String): Observable<ArtistsOnlyResponse>

    @GET("albums/{id}")
    fun getAlbum(@Header("Authorization") authorization: String,
                 @Path("id") albumId: String): Observable<AlbumData>

    @GET("recommendations")
    fun getSimilarTracks(@Header("Authorization") authorization: String,
                         @Query("limit") limit: String = "100",
                         @Query("seed_tracks") trackId: String): Observable<SimilarTracksResponse>

    companion object {
        private const val DEFAULT_LIMIT = "50"
        private const val DEFAULT_OFFSET = "0"
        private const val DEFAULT_COUNTRY = "SE"
        private const val DEFAULT_LOCALE = "sv_se"

        private const val ALL_SEARCH_TYPES = "album,artist,playlist,track"
    }
}