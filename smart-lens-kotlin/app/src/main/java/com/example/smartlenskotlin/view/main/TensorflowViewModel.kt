package com.example.smartlenskotlin.view.main

import android.app.Application
import android.graphics.Bitmap
import com.example.common.RecyclerViewModel
import com.example.smartlenskotlin.tf.detector.TfDetector
import com.example.smartlenskotlin.tf.detector.TfRecognition
import com.example.smartlenskotlin.tf.lite.TfLiteDetector
import com.example.smartlenskotlin.tf.mobile.TfMobileDetector
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019. 2. 12. <p/>
 */

class TensorflowViewModel @Inject constructor(application: Application
    , val tfLiteDetector: TfLiteDetector
    , val tfMobileDetector: TfMobileDetector
) : RecyclerViewModel<TfRecognition>(application) {
    var activeDetector: TfDetector? = null

    init {
        activeDetector = tfLiteDetector
        initAdapter("main_result_item")
    }

    fun recognizeImage(bmp: Bitmap) {
        activeDetector?.recognizeImage(bmp).let { items.set(it) }
    }
}