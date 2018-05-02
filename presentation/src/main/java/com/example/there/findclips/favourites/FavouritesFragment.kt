package com.example.there.findclips.favourites


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseSpotifyVMFragment
import com.example.there.findclips.main.MainFragment
import com.example.there.findclips.util.app
import javax.inject.Inject


class FavouritesFragment : BaseSpotifyVMFragment<FavouritesViewModel>(), MainFragment {

    override val title: String
        get() = "Favourites"

    override val bottomNavigationItemId: Int
        get() = R.id.action_favorites

    @Inject
    lateinit var vmFactory: FavouritesVMFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun initComponent() {
        activity?.app?.createFavouritesComponent()?.inject(this)
    }

    override fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this, vmFactory).get(FavouritesViewModel::class.java)
    }

    override fun releaseComponent() {
        activity?.app?.releaseFavouritesComponent()
    }
}
