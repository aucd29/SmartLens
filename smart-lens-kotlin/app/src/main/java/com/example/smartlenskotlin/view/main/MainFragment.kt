package com.example.smartlenskotlin.view.main

import android.graphics.Bitmap
import com.example.common.*
import com.example.common.di.module.injectOfActivity
import com.example.smartlenskotlin.databinding.MainFragmentBinding
import dagger.android.ContributesAndroidInjector

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019. 2. 12. <p/>
 */

class MainFragment : BaseDaggerFragment<MainFragmentBinding, CameraViewModel>() {
    lateinit var mTfViewModel: TensorflowViewModel

    override fun bindViewModel() {
        super.bindViewModel()

        mTfViewModel = mViewModelFactory.injectOfActivity(this, TensorflowViewModel::class.java)
    }

    override fun initViewBinding() {
        mViewModel.cameraDelegate.mTextureView = mBinding.texture
    }

    override fun initViewModelEvents() {
        mViewModel.apply {
            cameraDelegate.apply {
                observe(mErrorCallback) {
                    dialog(DialogParam(context = context
                        , message = it
                        , title = "error"
                        , listener = { res, dlg ->
                            activity?.finish()
                        }))
                }
            }

            callback = { it?.let { mTfViewModel.recognizeImage(it)} }

            // 겁나 느림 =_ =
//            observe(croppedBitmapEvent) {
//                mTfViewModel.recognizeImage(it)
//            }
        }
    }

    override fun onResume() {
        super.onResume()

        mViewModel.cameraDelegate.apply {
            startThread()

            mBinding.texture.apply {
                if (isAvailable) {
                    open(width, height)
                } else {
                    setSurfaceTextureListener(mSurfaceTextureListener)
                }
            }
        }
    }

    override fun onPause() {
        mViewModel.cameraDelegate.apply {
            close()
            stopThread()
        }

        super.onPause()
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MODULE
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @dagger.Module
    abstract class Module {
        @ContributesAndroidInjector
        abstract fun contributeInjector(): MainFragment
    }
}
