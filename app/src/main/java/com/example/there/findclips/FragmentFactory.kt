package com.example.there.findclips

import androidx.fragment.app.Fragment
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.model.soundcloud.SoundCloudPlaylist
import com.example.core.android.model.soundcloud.SoundCloudSystemPlaylist
import com.example.core.android.model.soundcloud.SoundCloudTrack
import com.example.core.android.model.spotify.*
import com.example.core.android.model.videos.VideoPlaylist
import com.example.soundclouddashboard.SoundCloudDashboardNavHostFragment
import com.example.soundcloudfavourites.SoundCloudFavouritesNavHostFragment
import com.example.soundcloudplaylist.SoundCloudPlaylistFragment
import com.example.soundcloudtrackvideos.SoundCloudTrackVideosFragment
import com.example.spotifyaccount.AccountNavHostFragment
import com.example.spotify.album.ui.SpotifyAlbumFragment
import com.example.spotify.artist.ui.SpotifyArtistFragment
import com.example.spotify.category.ui.SpotifyCategoryFragment
import com.example.spotify.dashboard.ui.SpotifyDashboardNavHostFragment
import com.example.spotifyfavourites.SpotifyFavouritesMainNavHostFragment
import com.example.spotify.playlist.ui.SpotifyPlaylistFragment
import com.example.spotifysearch.SpotifySearchMainFragment
import com.example.spotifysearch.spotify.SpotifySearchFragment
import com.example.spotifytrackvideos.TrackVideosFragment
import com.example.youtubefavourites.VideosFavouritesFragment
import com.example.youtubesearch.VideosSearchFragment
import com.example.youtubevideoplaylist.VideoPlaylistFragment

object FragmentFactory : IFragmentFactory {
    override val newSpotifyDashboardNavHostFragment: Fragment
        get() = SpotifyDashboardNavHostFragment()
    override val newSpotifyAccountNavHostFragment: Fragment
        get() = AccountNavHostFragment()
    override val newSpotifyFavouritesMainNavHostFragment: Fragment
        get() = SpotifyFavouritesMainNavHostFragment()

    override fun newSpotifyAlbumFragment(album: Album): Fragment = SpotifyAlbumFragment.newInstance(album)
    override fun newSpotifyArtistFragment(artist: Artist): Fragment = SpotifyArtistFragment.newInstance(artist)
    override fun newSpotifyCategoryFragment(category: Category): Fragment = SpotifyCategoryFragment.newInstance(category)
    override fun newSpotifyPlaylistFragment(playlist: Playlist): Fragment = SpotifyPlaylistFragment.newInstance(playlist)
    override fun newSpotifyTrackVideosFragment(track: Track): Fragment = TrackVideosFragment.newInstance(track)
    override fun newSpotifySearchMainFragment(query: String): Fragment = SpotifySearchMainFragment.newInstance(query)
    override fun newSpotifySearchFragment(query: String): Fragment = SpotifySearchFragment.newInstanceWithQuery(query)

    override fun newVideosSearchFragment(query: String): Fragment = VideosSearchFragment.newInstanceWithQuery(query)
    override fun newVideosSearchFragment(videoPlaylist: VideoPlaylist): Fragment = VideosSearchFragment.newInstanceWithVideoPlaylist(videoPlaylist)
    override fun newVideoPlaylistFragment(videoPlaylist: VideoPlaylist, thumbnailUrls: Array<String>): Fragment = VideoPlaylistFragment.newInstance(videoPlaylist, thumbnailUrls)
    override val newVideosFavouritesFragment: Fragment
        get() = VideosFavouritesFragment()

    override val newSoundCloudDashboardNavHostFragment: Fragment
        get() = SoundCloudDashboardNavHostFragment()
    override val newSoundCloudFavouritesNavHostFragment: Fragment
        get() = SoundCloudFavouritesNavHostFragment()

    override fun newSoundCloudPlaylistFragmentWithPlaylist(playlist: SoundCloudPlaylist): Fragment = SoundCloudPlaylistFragment.newInstance(playlist)
    override fun newSoundCloudPlaylistFragmentWithSystemPlaylist(playlist: SoundCloudSystemPlaylist): Fragment = SoundCloudPlaylistFragment.newInstance(playlist)
    override fun newSoundCloudTrackVideosFragment(track: SoundCloudTrack): Fragment = SoundCloudTrackVideosFragment.newInstance(track)
}