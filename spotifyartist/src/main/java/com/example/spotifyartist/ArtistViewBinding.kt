package com.example.spotifyartist

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.coreandroid.model.spotify.Artist

class ArtistViewBinding(
        val onFavouriteBtnClickListener: View.OnClickListener,
        val artist: MutableLiveData<Artist>
)

