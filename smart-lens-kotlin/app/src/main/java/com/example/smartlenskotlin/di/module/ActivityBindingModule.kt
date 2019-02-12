package com.example.smartlenskotlin.di.module

import com.example.smartlenskotlin.MainActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 12. 6. <p/>
 */

@Module(includes = [])
abstract class ActivityBindingModule {
    @ContributesAndroidInjector(modules = [FragmentModule::class
        , MainActivityModule::class
    ])
    abstract fun contributeMainActivity(): MainActivity
}

// https://stackoverflow.com/questions/48533899/how-to-inject-members-in-baseactivity-using-dagger-android
@Module
class MainActivityModule {
    @Provides
    fun provideFragmentManager(activity: MainActivity) =
        activity.supportFragmentManager
}
