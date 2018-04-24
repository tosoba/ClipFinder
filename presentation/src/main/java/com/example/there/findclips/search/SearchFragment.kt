package com.example.there.findclips.search

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseMainFragment
import com.example.there.findclips.util.app
import javax.inject.Inject


class SearchFragment : BaseMainFragment<SearchViewModel>() {

    @Inject
    lateinit var viewModelFactory: SearchViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun initComponent() {
        activity?.app?.createSearchComponent()?.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
    }

    override fun releaseComponent() {
        activity?.app?.releaseSearchComponent()
    }
}
