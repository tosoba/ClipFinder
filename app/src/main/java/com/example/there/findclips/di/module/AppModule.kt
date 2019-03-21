package com.example.there.findclips.di.module

import android.app.Application
import android.content.Context
import com.example.coreandroid.base.IFragmentFactory
import com.example.there.findclips.FragmentFactory
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: Application): Context

    @Binds
    abstract fun bindFragmentFactory(factory: FragmentFactory): IFragmentFactory
}