package com.example.smartlenskotlin.view.main

import android.app.Application
import android.graphics.Color
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import com.example.common.ICommandEventAware
import com.example.common.arch.SingleLiveEvent
import com.example.smartlenskotlin.R
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019. 2. 12. <p/>
 */

class MainViewModel @Inject constructor(application: Application) : AndroidViewModel(application)
    , ICommandEventAware {
    companion object {
        const val CMD_CHANGE_TF_METHOD = "change-tf-method"
    }

    override val commandEvent = SingleLiveEvent<Pair<String, Any>>()

    val tfMethodResId  = ObservableInt(R.string.main_tflite)
    val tfMethodColor  = ObservableInt(Color.BLUE)
    val visiblePreview = ObservableInt(View.VISIBLE)
}