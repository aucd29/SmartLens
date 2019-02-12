package com.example.smartlenskotlin.di.module

import androidx.lifecycle.ViewModel
import com.example.common.di.module.ViewModelKey
import com.example.smartlenskotlin.view.main.CameraViewModel
import com.example.smartlenskotlin.view.main.MainViewModel
import com.example.smartlenskotlin.view.main.TensorflowViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 12. 6. <p/>
 */

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(vm: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TensorflowViewModel::class)
    abstract fun bindTensorflowViewModel(vm: TensorflowViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CameraViewModel::class)
    abstract fun bindCameraViewModel(vm: CameraViewModel): ViewModel
}
