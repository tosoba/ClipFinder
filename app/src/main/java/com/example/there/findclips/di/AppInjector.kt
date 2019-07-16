package com.example.there.findclips.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.example.coreandroid.util.ext.registerFragmentLifecycleCallbacks
import com.example.there.findclips.FindClipsApp
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment

object AppInjector {

    fun init(app: FindClipsApp) {
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
        if (activity is DaggerAppCompatActivity) {
            AndroidInjection.inject(activity)
        }

        activity.registerFragmentLifecycleCallbacks(object : androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentAttached(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment, context: Context) {
                if (f is DaggerFragment) AndroidSupportInjection.inject(f)
            }
        }, true)
    }
}