package com.example.smartlenskotlin.di.module

import com.example.smartlenskotlin.tf.detector.TfDetector
import com.example.smartlenskotlin.tf.lite.TfLiteDetector
import com.example.smartlenskotlin.tf.mobile.TfMobileDetector
import dagger.Binds
import dagger.Module

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019. 2. 12. <p/>
 */

@Module
abstract class TensorflowModule {
    @Binds
    abstract fun bindTfLiteDetector(detector: TfLiteDetector): TfDetector

    @Binds
    abstract fun bindTfMobileDetector(detector: TfMobileDetector): TfDetector
}