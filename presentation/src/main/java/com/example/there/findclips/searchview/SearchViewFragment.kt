package com.example.there.findclips.searchview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import com.example.there.findclips.R
import com.example.there.findclips.util.setTextColors
import kotlinx.android.synthetic.main.fragment_search_view.*


class SearchViewFragment : Fragment() {

    var onSearchListener: OnSearchListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initSearchView()
    }

    private fun initSearchView() {
        searchView.setTextColors()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean = true

            override fun onQueryTextSubmit(query: String?): Boolean {
                onSearchListener?.onSearch(query ?: "")
                return false
            }
        })
    }

    fun setQueryHint(hint: String) {
        searchView.queryHint = hint
    }

    interface OnSearchListener {
        fun onSearch(query: String)
    }
}
