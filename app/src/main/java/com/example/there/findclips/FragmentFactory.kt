package com.example.there.findclips

import android.support.v4.app.Fragment
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.model.soundcloud.SoundCloudPlaylist
import com.example.coreandroid.model.soundcloud.SoundCloudSystemPlaylist
import com.example.coreandroid.model.spotify.*
import com.example.coreandroid.model.videos.VideoPlaylist
import com.example.soundcloudplaylist.SoundCloudPlaylistFragment
import com.example.spotifyalbum.AlbumFragment
import com.example.spotifyartist.ArtistFragment
import com.example.spotifycategory.CategoryFragment
import com.example.spotifyplaylist.PlaylistFragment
import com.example.spotifysearch.SpotifySearchMainFragment
import com.example.spotifytrack.TrackFragment
import com.example.spotifytrackvideos.TrackVideosFragment
import com.example.youtubefavourites.VideosFavouritesFragment
import com.example.youtubevideoplaylist.VideoPlaylistFragment
import javax.inject.Inject

class FragmentFactory @Inject constructor() : IFragmentFactory {
    override fun newSpotifyAlbumFragment(album: Album): Fragment = AlbumFragment.newInstance(album)
    override fun newSpotifyArtistFragment(artist: Artist): Fragment = ArtistFragment.newInstance(artist)
    override fun newSpotifyCategoryFragment(category: Category): Fragment = CategoryFragment.newInstance(category)
    override fun newSpotifyPlaylistFragment(playlist: Playlist): Fragment = PlaylistFragment.newInstance(playlist)
    override fun newSpotifyTrackFragment(track: Track): Fragment = TrackFragment.newInstanceWithTrack(track)
    override fun newSpotifyTrackVideosFragment(track: Track): Fragment = TrackVideosFragment.newInstance(track)
    override fun newSpotifySearchFragment(query: String): Fragment = SpotifySearchMainFragment.newInstance(query)

    override fun newVideoPlaylistFragment(videoPlaylist: VideoPlaylist, thumbnailUrls: Array<String>): Fragment = VideoPlaylistFragment.newInstance(videoPlaylist, thumbnailUrls)
    override val newVideosFavouritesFragment: Fragment
        get() = VideosFavouritesFragment()

    override fun newSoundCloudPlaylistFragmentWithPlaylist(playlist: SoundCloudPlaylist): Fragment = SoundCloudPlaylistFragment.newInstance(playlist)
    override fun newSoundCloudPlaylistFragmentWithSystemPlaylist(playlist: SoundCloudSystemPlaylist): Fragment = SoundCloudPlaylistFragment.newInstance(playlist)
}