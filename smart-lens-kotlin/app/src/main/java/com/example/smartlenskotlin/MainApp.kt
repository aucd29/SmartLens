package com.example.smartlenskotlin

import android.app.Activity
import android.app.Application
import com.example.smartlenskotlin.di.component.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019. 2. 12. <p/>
 */

class MainApp : Application(), HasActivityInjector {
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // HasActivityInjector
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun activityInjector() = activityInjector
}