package com.example.core.android.base.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.args
import com.example.core.android.R
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.android.synthetic.main.fragment_item_list.view.*

abstract class ItemListFragment<S> : BaseMvRxFragment() {

    private val passedArgs: Args by args()

    protected abstract val epoxyController: TypedEpoxyController<S>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_item_list, container, false).also {
        it.item_list_recycler_view.apply {
            setController(epoxyController)
            layoutManager = layoutManagerFor(resources.configuration.orientation)
            setItemSpacingDp(passedArgs.itemSpacingDp)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        item_list_recycler_view?.layoutManager = layoutManagerFor(newConfig.orientation)
    }

    private fun layoutManagerFor(orientation: Int): RecyclerView.LayoutManager = GridLayoutManager(
        context,
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            passedArgs.spanCountLandscape
        } else {
            passedArgs.spanCountPortrait
        }
    )

    @Parcelize
    class Args(
        val spanCountPortrait: Int,
        val spanCountLandscape: Int,
        val itemSpacingDp: Int
    ) : Parcelable

    companion object {
        inline fun <reified F : ItemListFragment<S>, S> new(args: Args): F = F::class.java
            .newInstance()
            .apply { arguments = Bundle().apply { putParcelable(MvRx.KEY_ARG, args) } }
    }
}
