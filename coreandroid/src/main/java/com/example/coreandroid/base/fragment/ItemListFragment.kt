package com.example.coreandroid.base.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
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
import com.example.coreandroid.R
import com.example.coreandroid.util.ext.screenOrientation
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_item_list.view.*
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named


abstract class ItemListFragment<S> : BaseMvRxFragment() {

    private val passedArgs: Args by args()

    protected abstract val epoxyController: TypedEpoxyController<S>

    protected val builder by inject<Handler>(named("builder"))
    protected val differ by inject<Handler>(named("differ"))

    private val spanCount: Int
        get() = if (context?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            passedArgs.spanCountLandscape
        } else passedArgs.spanCountPortrait

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_item_list, container, false).apply {
        this.item_list_recycler_view.apply {
            setController(epoxyController)
            layoutManager = GridLayoutManager(context, spanCount, RecyclerView.VERTICAL, false)
            setItemSpacingDp(passedArgs.itemSpacingDp)
        }
    }

    //TODO: make it so spanCount changes on config change
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
//        item_list_recycler_view?.layoutManager = listLayoutManager
    }

    @Parcelize
    class Args(val spanCountPortrait: Int, val spanCountLandscape: Int, val itemSpacingDp: Int) : Parcelable

    companion object {
        inline fun <reified F : ItemListFragment<S>, S> new(
            args: Args
        ): F = F::class.java.newInstance().apply {
            arguments = Bundle().apply {
                putParcelable(MvRx.KEY_ARG, args)
            }
        }
    }
}
