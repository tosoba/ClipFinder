package com.example.there.findclips.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.coreandroid.di.Injectable
import com.example.there.findclips.FindClipsApp
import com.example.there.findclips.util.ext.registerFragmentLifecycleCallbacks
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

object AppInjector {

    fun init(app: FindClipsApp) {
        DaggerAppComponent
                .builder()
                .application(app)
                .build()
                .inject(app)

        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = handleActivityCreated(activity)

            override fun onActivityStarted(activity: Activity) = Unit

            override fun onActivityResumed(activity: Activity) = Unit

            override fun onActivityPaused(activity: Activity) = Unit

            override fun onActivityStopped(activity: Activity) = Unit

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) = Unit

            override fun onActivityDestroyed(activity: Activity) = Unit
        })
    }

    private fun handleActivityCreated(activity: Activity) {
        if (activity is HasSupportFragmentInjector) {
            AndroidInjection.inject(activity)
        }

        activity.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                if (f is Injectable) AndroidSupportInjection.inject(f)
            }
        }, true)
    }
}