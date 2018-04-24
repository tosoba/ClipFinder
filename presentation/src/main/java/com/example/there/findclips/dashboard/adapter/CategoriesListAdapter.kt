package com.example.there.findclips.dashboard.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.domain.entities.CategoryEntity
import com.example.there.findclips.R
import com.example.there.findclips.databinding.CategoryItemBinding

class CategoriesListAdapter(private val categories: List<CategoryEntity>) : RecyclerView.Adapter<CategoriesListAdapter.CategoriesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: CategoryItemBinding = DataBindingUtil.inflate(inflater, R.layout.category_item, parent, false)
        return CategoriesViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.binding.category = categories[position]
    }

    class CategoriesViewHolder(val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root)
}