package com.example.smartlenskotlin.di.module

import com.example.smartlenskotlin.view.main.MainFragment
import dagger.Module

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 12. 5. <p/>
 */

@Module(includes = [MainFragment.Module::class
])
class FragmentModule {

}