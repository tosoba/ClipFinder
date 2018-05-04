package com.example.there.findclips.di.category

import com.example.there.findclips.category.fragment.CategoryFragment
import dagger.Subcomponent

@CategoryScope
@Subcomponent(modules = [CategoryModule::class])
interface CategorySubComponent {
    fun inject(categoryFragment: CategoryFragment)
}