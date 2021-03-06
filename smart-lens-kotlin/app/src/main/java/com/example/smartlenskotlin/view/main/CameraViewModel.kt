package com.example.smartlenskotlin.view.main

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.media.Image
import android.media.ImageReader
import android.util.Size
import androidx.lifecycle.AndroidViewModel
import com.example.smartlenskotlin.di.module.CameraDelegate
import com.example.smartlenskotlin.di.module.CameraPreviewListener
import com.example.smartlenskotlin.tf.detector.TfImageHelper
import com.example.smartlenskotlin.tf.mobile.TfMobileDetector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019. 2. 12. <p/>
 */

class CameraViewModel @Inject constructor(application: Application
    , val cameraDelegate: CameraDelegate
) : AndroidViewModel(application), CameraPreviewListener {

    var bitmap_: Bitmap? = null
    var croppedBitmap_: Bitmap? = null
    var sensorOrientation_: Int = 0
    var frameToCropTransform_: Matrix? = null
    var cropToFrameTransform_: Matrix? = null

    private var computing_: Boolean = false

    var callback: ((Bitmap?) -> Unit)? = null

    init {
        cameraDelegate.mCameraPreviewListener = this
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // CameraPreviewListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun onPreviewReadied(size: Size, cameraRotation: Int) {
        bitmap_        = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)
        croppedBitmap_ = Bitmap.createBitmap(TfMobileDetector.INPUT_SIZE.toInt()
            , TfMobileDetector.INPUT_SIZE.toInt(), Bitmap.Config.ARGB_8888)

        val display = cameraDelegate.windowManager.getDefaultDisplay()
        val screenOrientation = display.getRotation()

        sensorOrientation_ = cameraRotation + screenOrientation

        frameToCropTransform_ = TfImageHelper.getTransformationMatrix(size.height, size.width,
            TfMobileDetector.INPUT_SIZE.toInt(), TfMobileDetector.INPUT_SIZE.toInt(),
            sensorOrientation_, true)

        cropToFrameTransform_ = Matrix()
        frameToCropTransform_?.invert(cropToFrameTransform_)
    }

    override fun onImageAvailable(reader: ImageReader?) {
        if (computing_)
            return

        var image: Image? = null

        try {
            image = reader?.acquireLatestImage()

            if (image == null || bitmap_ == null)
                return

            computing_ = true

            TfImageHelper.imageToBitmap(image, bitmap_!!)

            val canvas = Canvas(croppedBitmap_)
            canvas.drawBitmap(bitmap_, frameToCropTransform_, null)

            image.close()

            callback?.invoke(croppedBitmap_)

            // 미친듯이 느림 =_ =
//            dp.add(io.reactivex.Single.just(croppedBitmap_)
//                .observeOn(AndroidSchedulers.mainThread())
//                .filter { it != null }
//                .subscribe { bmp ->
//                    croppedBitmapEvent.value = bmp
//                    computing_ = false
//                })
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            image?.close()

            computing_ = false
        }
    }
}
