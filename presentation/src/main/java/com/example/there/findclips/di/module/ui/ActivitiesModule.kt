package com.example.there.findclips.di.module.ui

import com.example.there.findclips.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity
}