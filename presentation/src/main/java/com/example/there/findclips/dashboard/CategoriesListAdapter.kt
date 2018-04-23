package com.example.there.findclips.dashboard

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.domain.entities.CategoryEntity
import com.example.there.findclips.R
import com.example.there.findclips.databinding.CategoryItemBinding

class CategoriesListAdapter(private val categories: List<CategoryEntity>) : RecyclerView.Adapter<CategoriesListAdapter.DashboardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: CategoryItemBinding = DataBindingUtil.inflate(inflater, R.layout.category_item, parent, false)
        return DashboardViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.binding.category = categories[position]
    }

    class DashboardViewHolder(val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root)
}