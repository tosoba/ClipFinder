package com.example.there.findclips.di.module.ui

import com.example.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity
}