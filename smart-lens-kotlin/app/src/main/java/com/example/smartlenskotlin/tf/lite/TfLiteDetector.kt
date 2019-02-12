package com.example.smartlenskotlin.tf.lite

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import com.example.smartlenskotlin.tf.detector.TfDetector
import com.example.smartlenskotlin.tf.detector.TfRecognition
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Float
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019. 2. 12. <p/>
 */

class TfLiteDetector @Inject constructor(val mContext: Context)
    : TfDetector {
    companion object {
        private val MAX_RESULTS = 3
        private val BATCH_SIZE  = 1
        private val PIXEL_SIZE  = 3
        private val THRESHOLD   = 0.1f

        private val MODEL_PATH  = "mobilenet_v1_1.0_224_quant.tflite"
        private val LABEL_PATH  = "labels.txt"
        private val INPUT_SIZE  = 224
    }

    private var mInterpreter: Interpreter? = null
    private var mInputSize = 0
    private var mLabelList: List<String> = ArrayList()

    init {
        loadModel()
    }

    override fun loadModel(): Boolean {
        var tfliteOptions: Interpreter.Options? = null
        if (TFLiteGpuDelegate.isGpuDelegateAvailable) {
            tfliteOptions = Interpreter.Options()
            tfliteOptions.addDelegate(TFLiteGpuDelegate.create())
        }

        try {
            mLabelList = loadLabelList(mContext.assets, LABEL_PATH)

        } catch (e: Exception) {
            throw RuntimeException("Error initializing TensorFlow!", e)
        }

        if (tfliteOptions == null)
            mInterpreter = Interpreter(loadModelFile(mContext.assets, MODEL_PATH))
        else
            mInterpreter = Interpreter(loadModelFile(mContext.assets, MODEL_PATH), tfliteOptions)

        mInputSize = INPUT_SIZE

        return true
    }

    override fun close() = true

    override fun recognizeImage(bitmap: Bitmap): List<TfRecognition> {
        val byteBuffer = convertBitmapToByteBuffer(bitmap)
        val result = Array(1) { ByteArray(mLabelList.size) }

        mInterpreter?.run(byteBuffer, result)

        return getSortedResult(result)
    }

    @Throws(IOException::class)
    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        val labelList = ArrayList<String>()
        val reader = BufferedReader(InputStreamReader(assetManager.open(labelPath)))
        var line: String?

        while (true) {
            line = reader.readLine()
            if (line == null) {
                break
            }

            labelList.add(line)
        }

        reader.close()

        return labelList
    }

    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel

        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength

        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(BATCH_SIZE * mInputSize * mInputSize * PIXEL_SIZE)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(mInputSize * mInputSize)

        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0

        for (i in 0 until mInputSize) {
            for (j in 0 until mInputSize) {
                val value = intValues[pixel++]

                byteBuffer.put((value shr 16 and 0xFF).toByte())
                byteBuffer.put((value shr 8 and 0xFF).toByte())
                byteBuffer.put((value and 0xFF).toByte())
            }
        }

        return byteBuffer
    }

    private fun getSortedResult(labelProbArray: Array<ByteArray>): List<TfRecognition> {
        val pq = PriorityQueue<TfRecognition>(
            MAX_RESULTS,
            Comparator<TfRecognition> { lhs, rhs -> Float.compare(lhs.confidence!!, rhs.confidence!!) })

        for (i in mLabelList.indices) {
            val confidence = (labelProbArray[0][i].toInt() and 0xff) / 255.0f
            if (confidence > THRESHOLD) {
                pq.add(TfRecognition("" + i,
                        if (mLabelList.size > i) mLabelList.get(i) else "unknown",
                        confidence,null))
            }
        }

        val recognitions = ArrayList<TfRecognition>()
        val recognitionsSize = Math.min(pq.size, MAX_RESULTS)
        for (i in 0 until recognitionsSize) {
            recognitions.add(pq.poll())
        }

        return recognitions
    }
}