package com.example.there.findclips.di.search

import com.example.there.findclips.search.SearchFragment
import dagger.Subcomponent

@SearchScope
@Subcomponent(modules = [
    SearchModule::class
])
interface SearchSubComponent {
    fun inject(fragment: SearchFragment)
}