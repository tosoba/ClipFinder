package com.example.there.findclips.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseMainFragment
import com.example.there.findclips.util.app


class SearchFragment : BaseMainFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun initComponent() {
        activity?.app?.createSearchComponent()
    }

    override fun initViewModel() {

    }

    override fun setupObservers() {

    }

    override fun releaseComponent() {
        activity?.app?.releaseSearchComponent()
    }
}
