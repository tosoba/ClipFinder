package com.example.there.findclips.di.dashboard

import com.example.there.findclips.dashboard.DashboardFragment
import dagger.Subcomponent

@DashboardScope
@Subcomponent(modules = [DashboardModule::class])
interface DashboardSubComponent {
    fun inject(fragment: DashboardFragment)
}