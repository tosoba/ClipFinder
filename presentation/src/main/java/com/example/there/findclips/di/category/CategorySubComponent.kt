package com.example.there.findclips.di.category

import com.example.there.findclips.category.CategoryActivity
import dagger.Subcomponent

@CategoryScope
@Subcomponent(modules = [CategoryModule::class])
interface CategorySubComponent {
    fun inject(categoryActivity: CategoryActivity)
}