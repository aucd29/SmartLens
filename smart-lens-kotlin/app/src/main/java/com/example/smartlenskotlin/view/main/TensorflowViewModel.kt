package com.example.smartlenskotlin.view.main

import android.app.Application
import android.graphics.Bitmap
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.example.common.RecyclerViewModel
import com.example.smartlenskotlin.tf.detector.TfDetector
import com.example.smartlenskotlin.tf.detector.TfRecognition
import com.example.smartlenskotlin.tf.lite.TfLiteDetector
import com.example.smartlenskotlin.tf.mobile.TfMobileDetector
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019. 2. 12. <p/>
 */

class TensorflowViewModel @Inject constructor(application: Application
    , val tfLiteDetector: TfLiteDetector
    , val tfMobileDetector: TfMobileDetector
    , val dp: CompositeDisposable
) : RecyclerViewModel<TfRecognition>(application) {
    companion object {
        private val mLog = LoggerFactory.getLogger(TensorflowViewModel::class.java)
    }

    // https://stackoverflow.com/questions/29873859/how-to-implement-itemanimator-of-recyclerview-to-disable-the-animation-of-notify/30837162
    val itemAnimator = ObservableField<RecyclerView.ItemAnimator?>()
    val chipLayoutManager = ObservableField<ChipsLayoutManager>()
    var activeDetector: TfDetector? = null

    init {
        activeDetector = tfLiteDetector
        initAdapter("main_result_item")
    }

    fun recognizeImage(bmp: Bitmap) {
        activeDetector?.recognizeImage(bmp).let {
            if (mLog.isDebugEnabled) {
                mLog.debug("${it.toString()}")
            }

            dp.add(Single.just(it).observeOn(AndroidSchedulers.mainThread()).subscribe { list ->
                items.set(list)
            })
        }
    }
}