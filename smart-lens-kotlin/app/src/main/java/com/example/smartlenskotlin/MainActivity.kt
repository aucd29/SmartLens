package com.example.smartlenskotlin

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.example.common.*
import com.example.common.di.module.injectOfActivity
import com.example.common.runtimepermission.PermissionParams
import com.example.common.runtimepermission.runtimePermissions
import com.example.smartlenskotlin.databinding.MainActivityBinding
import com.example.smartlenskotlin.view.main.MainFragment
import com.example.smartlenskotlin.view.main.MainViewModel
import com.example.smartlenskotlin.view.main.TensorflowViewModel
import javax.inject.Inject

class MainActivity : BaseDaggerRuleActivity<MainActivityBinding, MainViewModel>() {
    lateinit var mTfViewModel: TensorflowViewModel

    @Inject
    lateinit var layoutManager: ChipsLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runtimePermissions(PermissionParams(this@MainActivity
            , arrayListOf(Manifest.permission.CAMERA)
            , { req, res ->
                if (res && savedInstanceState == null) {
                    init()
                }
            }
            , 7912))
    }

    override fun bindViewModel() {
        super.bindViewModel()

        mTfViewModel = mViewModelFactory.injectOfActivity(this@MainActivity, TensorflowViewModel::class.java)
        mTfViewModel.chipLayoutManager.set(layoutManager)

        mBinding.setTfModel(mTfViewModel)
    }

    override fun initViewBinding() {
        keepScreen(true)
    }

    override fun initViewModelEvents() {
    }

    private fun init() {
        supportFragmentManager.show(FragmentParams(R.id.camera_container
            , MainFragment::class.java
            , commit = FragmentCommit.NOW, backStack = false))
    }

    override fun onCommandEvent(cmd: String, data: Any) {
        when (cmd) {
            MainViewModel.CMD_CHANGE_TF_METHOD -> mViewModel.run {
                if (tfMethodResId.get() == R.string.main_tflite) {
                    tfMethodResId.set(R.string.main_tfmobile)
                    tfMethodColor.set(Color.BLUE)
                } else {
                    tfMethodResId.set(R.string.main_tflite)
                    tfMethodColor.set(Color.RED)
                }
            }
        }
    }

    override fun onDestroy() {
        keepScreen(false)

        super.onDestroy()
    }
}
