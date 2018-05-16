package com.example.there.findclips.view

import android.databinding.BindingAdapter
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.example.there.findclips.R
import com.squareup.picasso.Picasso

@BindingAdapter("onNavigationItemSelectedListener")
fun bindOnNavigationItemSelectedListener(bottomNavigationView: BottomNavigationView, listener: BottomNavigationView.OnNavigationItemSelectedListener) {
    bottomNavigationView.setOnNavigationItemSelectedListener(listener)
}

@BindingAdapter("imageUrl")
fun bindImageUrl(view: ImageView, url: String?) {
    if (url != null && !url.isEmpty()) {
        Picasso.with(view.context)
                .load(url)
                .fit()
                .centerCrop()
                .error(R.drawable.placeholder)
                .into(view)
    }
}

@BindingAdapter("itemDecoration")
fun bindItemDecoration(recycler: RecyclerView, decoration: RecyclerView.ItemDecoration) {
    recycler.addItemDecoration(decoration)
}

@BindingAdapter("onScrollListener")
fun bindOnScrollListener(recycler: RecyclerView, listener: RecyclerView.OnScrollListener) {
    recycler.addOnScrollListener(listener)
}

@BindingAdapter("tabs")
fun bindTabs(tabLayout: TabLayout, tabs: Array<String>) {
    tabs.forEach { tabLayout.addTab(tabLayout.newTab().setText(it)) }
}

@BindingAdapter("onTabSelectedListener")
fun bindOnTabSelectedListener(tabLayout: TabLayout, listener: TabLayout.OnTabSelectedListener) {
    tabLayout.addOnTabSelectedListener(listener)
}

@BindingAdapter("onPageChangeListener")
fun bindOnPageChangeListener(viewPager: ViewPager, listener: ViewPager.OnPageChangeListener) {
    viewPager.addOnPageChangeListener(listener)
}