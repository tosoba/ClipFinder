package com.example.coreandroid.base.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.args
import com.example.coreandroid.R
import com.example.coreandroid.util.ext.screenOrientation
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.android.synthetic.main.fragment_item_list.view.*


abstract class ItemListFragment : BaseMvRxFragment() {

    private val passedArgs: Args by args()

    protected abstract val epoxyController: EpoxyController

    private val listLayoutManager: RecyclerView.LayoutManager
        get() = if (context?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(context, passedArgs.spanCountLandscape, RecyclerView.VERTICAL, false)
        } else {
            GridLayoutManager(context, passedArgs.spanCountPortrait, RecyclerView.VERTICAL, false)
        }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_item_list, container, false).apply {
        this.item_list_recycler_view.apply {
            setController(epoxyController)
            layoutManager = listLayoutManager
            setItemSpacingDp(passedArgs.itemSpacingDp)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        item_list_recycler_view?.layoutManager = listLayoutManager
    }

    @Parcelize
    class Args(val spanCountPortrait: Int, val spanCountLandscape: Int, val itemSpacingDp: Int) : Parcelable

    companion object {
        fun new(
                args: Args, controller: EpoxyController, onInvalidate: () -> Unit
        ): ItemListFragment = object : ItemListFragment() {
            override val epoxyController: EpoxyController get() = controller
            override fun invalidate() = onInvalidate()
        }.apply {
            arguments = Bundle().apply {
                putParcelable(MvRx.KEY_ARG, args)
            }
        }
    }
}
