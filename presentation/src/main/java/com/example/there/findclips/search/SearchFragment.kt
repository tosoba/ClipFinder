package com.example.there.findclips.search

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseVMFragment
import com.example.there.findclips.util.app
import javax.inject.Inject


class SearchFragment : BaseVMFragment<SearchViewModel>() {

    @Inject
    lateinit var viewModelFactory: SearchViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search_spinner_menu, menu)

        val item = menu?.findItem(R.id.spinner)
        val spinner = item?.actionView as? Spinner

        val adapter = ArrayAdapter<String>(context, R.layout.spinner_item, arrayOf("Spotify", "Youtube"))
        spinner?.adapter = adapter
        super.onCreateOptionsMenu(menu, inflater)
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
