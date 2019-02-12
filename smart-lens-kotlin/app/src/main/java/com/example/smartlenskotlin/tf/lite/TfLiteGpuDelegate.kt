package com.example.smartlenskotlin.tf.lite

import org.tensorflow.lite.Delegate

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019. 2. 12. <p/>
 */


/**
 * class for `GpuDelegate`.
 *
 * WARNING: This is an experimental API and subject to change.
 */
object TFLiteGpuDelegate {
    /** Checks whether `GpuDelegate` is available.  */
    val isGpuDelegateAvailable: Boolean
        get() {
            try {
                Class.forName("org.tensorflow.lite.experimental.GpuDelegate")
                return true
            } catch (e: Exception) {
                return false
            }
        }

    /** Returns an instance of `GpuDelegate` if available.  */
    fun create(): Delegate {
        try {
            return Class.forName("org.tensorflow.lite.experimental.GpuDelegate")
                .asSubclass(Delegate::class.java)
                .getDeclaredConstructor()
                .newInstance()
        } catch (e: Exception) {
            throw IllegalStateException(e)
        }
    }
}
