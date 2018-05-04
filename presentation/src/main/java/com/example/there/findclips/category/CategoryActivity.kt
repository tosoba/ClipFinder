package com.example.there.findclips.category

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.category.fragment.CategoryFragment
import com.example.there.findclips.databinding.ActivityCategoryBinding
import com.example.there.findclips.entities.Category
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : AppCompatActivity() {

    private val category: Category by lazy { intent.getParcelableExtra(EXTRA_CATEGORY) as Category }

    private val view: CategoryActivityView by lazy {
        CategoryActivityView(category = category, onFavouriteBtnClickListener = View.OnClickListener {
            Toast.makeText(this, "Added to favourites.", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityCategoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        binding.view = view
        initToolbar()
        title = category.name
        showFragment()
    }

    private fun initToolbar() {
        setSupportActionBar(category_toolbar)
        category_toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
        category_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun showFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.category_fragment_container, CategoryFragment.newInstance(category))
                .commit()
    }

    companion object {
        private const val EXTRA_CATEGORY = "EXTRA_CATEGORY"

        fun start(activity: AppCompatActivity, category: Category) {
            val intent = Intent(activity, CategoryActivity::class.java).apply {
                putExtra(EXTRA_CATEGORY, category)
            }
            activity.startActivity(intent)
        }
    }
}
