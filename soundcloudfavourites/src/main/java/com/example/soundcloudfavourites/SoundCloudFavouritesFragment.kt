package com.example.soundcloudfavourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.core.android.util.ext.mainContentFragment

class SoundCloudFavouritesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_sound_cloud_favourites,
        container,
        false
    ).apply {
        mainContentFragment?.disablePlayButton()
    }
}
