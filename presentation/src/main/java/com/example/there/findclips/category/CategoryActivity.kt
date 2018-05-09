package com.example.there.findclips.category

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseSpotifyVMActivity
import com.example.there.findclips.databinding.ActivityCategoryBinding
import com.example.there.findclips.entities.Category
import com.example.there.findclips.listfragments.SpotifyPlaylistsFragment
import com.example.there.findclips.util.accessToken
import com.example.there.findclips.util.app
import kotlinx.android.synthetic.main.activity_category.*
import javax.inject.Inject

class CategoryActivity : BaseSpotifyVMActivity<CategoryViewModel>() {

    private val category: Category by lazy { intent.getParcelableExtra(EXTRA_CATEGORY) as Category }

    private val view: CategoryView by lazy {
        CategoryView(state = viewModel.viewState,
                category = category,
                onFavouriteBtnClickListener = View.OnClickListener {
                    Toast.makeText(this, "Added to favourites.", Toast.LENGTH_SHORT).show()
                })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initToolbar()

        if (savedInstanceState == null) {
            viewModel.loadPlaylists(accessToken, category.id)
        }
    }

    private fun initView() {
        val binding: ActivityCategoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        binding.view = view
    }

    private fun initToolbar() {
        setSupportActionBar(category_toolbar)
        category_toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
        category_toolbar.setNavigationOnClickListener { onBackPressed() }
        title = category.name
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.playlists.observe(this, Observer {
            it?.let {
                val fragment = supportFragmentManager.findFragmentById(R.id.category_spotify_playlists_fragment) as SpotifyPlaylistsFragment
                fragment.addItems(it)
            }
        })
    }

    override fun initComponent() {
        app.createCategoryComponent().inject(this)
    }

    override fun releaseComponent() {
        app.releaseCategoryComponent()
    }

    @Inject
    lateinit var factory: CategoryVMFactory

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(CategoryViewModel::class.java)
    }

    companion object {
        private const val EXTRA_CATEGORY = "EXTRA_CATEGORY"

        fun start(activity: Activity, category: Category) {
            val intent = Intent(activity, CategoryActivity::class.java).apply {
                putExtra(EXTRA_CATEGORY, category)
            }
            activity.startActivity(intent)
        }
    }
}
