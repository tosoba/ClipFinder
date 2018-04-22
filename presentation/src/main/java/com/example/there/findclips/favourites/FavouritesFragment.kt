package com.example.there.findclips.favourites


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseMainFragment
import com.example.there.findclips.util.app


class FavouritesFragment : BaseMainFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun initComponent() {
        activity?.app?.createFavouritesComponent()
    }

    override fun initViewModel() {

    }

    override fun setupObservers() {

    }

    override fun releaseComponent() {
        activity?.app?.releaseFavouritesComponent()
    }
}
