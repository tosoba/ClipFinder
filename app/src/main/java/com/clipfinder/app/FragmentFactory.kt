package com.clipfinder.app

import androidx.fragment.app.Fragment
import com.clipfinder.core.android.base.IFragmentFactory
import com.clipfinder.core.android.model.soundcloud.SoundCloudPlaylist
import com.clipfinder.core.android.model.soundcloud.SoundCloudTrack
import com.clipfinder.core.android.spotify.model.*
import com.clipfinder.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.clipfinder.soundcloud.dashboard.SoundCloudDashboardNavHostFragment
import com.clipfinder.soundcloud.playlist.SoundCloudPlaylistFragment
import com.clipfinder.soundcloud.videos.SoundCloudTrackVideosFragment
import com.clipfinder.spotify.account.SpotifyAccountNavHostFragment
import com.clipfinder.spotify.album.SpotifyAlbumFragment
import com.clipfinder.spotify.artist.SpotifyArtistFragment
import com.clipfinder.spotify.category.SpotifyCategoryFragment
import com.clipfinder.spotify.dashboard.SpotifyDashboardNavHostFragment
import com.clipfinder.spotify.playlist.SpotifyPlaylistFragment
import com.clipfinder.spotify.search.SpotifySearchFragment
import com.clipfinder.spotify.search.SpotifySearchMainFragment
import com.clipfinder.spotify.track.SpotifyTrackFragment
import com.clipfinder.spotify.track.videos.SpotifyTrackVideosFragment
import com.clipfinder.youtube.search.YoutubeSearchFragment

object FragmentFactory : IFragmentFactory, ISpotifyFragmentsFactory {
    override val newSpotifyDashboardNavHostFragment: Fragment
        get() = SpotifyDashboardNavHostFragment()

    override val newSpotifyAccountNavHostFragment: Fragment
        get() = SpotifyAccountNavHostFragment()

    override fun newSpotifyAlbumFragment(album: Album): Fragment = SpotifyAlbumFragment.new(album)

    override fun newSpotifyArtistFragment(artist: Artist): Fragment =
        SpotifyArtistFragment.new(artist)

    override fun newSpotifyCategoryFragment(category: Category): Fragment =
        SpotifyCategoryFragment.new(category)

    override fun newSpotifyPlaylistFragment(playlist: Playlist): Fragment =
        SpotifyPlaylistFragment.new(playlist)

    override fun newSpotifyTrackVideosFragment(track: Track): Fragment =
        SpotifyTrackVideosFragment.new(track)

    override fun newSpotifySearchMainFragment(query: String): Fragment =
        SpotifySearchMainFragment.newInstance(query)

    override fun newSpotifySearchFragment(query: String): Fragment =
        SpotifySearchFragment.newInstanceWithQuery(query)

    override fun newSpotifyTrackFragment(track: Track): Fragment = SpotifyTrackFragment.new(track)

    override fun newVideosSearchFragment(query: String): Fragment =
        YoutubeSearchFragment.newInstanceWithQuery(query)

    override val newSoundCloudDashboardNavHostFragment: Fragment
        get() = SoundCloudDashboardNavHostFragment()

    override fun newSoundCloudPlaylistFragmentWithPlaylist(playlist: SoundCloudPlaylist): Fragment =
        SoundCloudPlaylistFragment.new(playlist)

    override fun newSoundCloudTrackVideosFragment(track: SoundCloudTrack): Fragment =
        SoundCloudTrackVideosFragment.new(track)
}
