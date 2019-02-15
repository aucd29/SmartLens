package com.example.smartlenskotlin.di.component

import android.app.Application
import com.example.clone_daum.di.module.common.AppModule
import com.example.common.di.module.RxModule
import com.example.common.di.module.ViewModelFactoryModule
import com.example.smartlenskotlin.MainApp
import com.example.smartlenskotlin.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 12. 5. <p/>
 *
 * https://medium.com/@iammert/new-android-injector-with-dagger-2-part-1-8baa60152abe
 * https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample/app/src/main/java/com/android/example/github
 */

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class
    , AppModule::class
    , RxModule::class
    , ViewModelFactoryModule::class

    , ViewModelModule::class
    , TensorflowModule::class
    , Camera2Module::class
    , ChipModule::class
    , ActivityBindingModule::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): AppComponent
    }

    fun inject(app: MainApp)
}