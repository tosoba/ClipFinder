package com.example.there.findclips.favourites


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseVMFragment
import com.example.there.findclips.util.app
import javax.inject.Inject


class FavouritesFragment : BaseVMFragment<FavouritesViewModel>() {

    @Inject
    lateinit var viewModelFactory: FavouritesViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun initComponent() {
        activity?.app?.createFavouritesComponent()?.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FavouritesViewModel::class.java)
    }

    override fun releaseComponent() {
        activity?.app?.releaseFavouritesComponent()
    }
}
