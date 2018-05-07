package com.example.there.findclips.favourites


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseSpotifyVMFragment
import com.example.there.findclips.util.app
import javax.inject.Inject


class FavouritesFragment : BaseSpotifyVMFragment<FavouritesViewModel>() {

    @Inject
    lateinit var vmFactory: FavouritesVMFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun initComponent() {
        activity?.app?.createFavouritesComponent()?.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, vmFactory).get(FavouritesViewModel::class.java)
    }

    override fun releaseComponent() {
        activity?.app?.releaseFavouritesComponent()
    }
}
