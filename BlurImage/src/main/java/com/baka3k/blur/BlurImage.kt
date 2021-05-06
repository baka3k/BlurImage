package com.baka3k.blur

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.renderscript.RenderScript
import android.view.View
import android.widget.ImageView
import com.baka3k.blur.process.Executor
import com.baka3k.blur.process.KotlinBlurProcess
import com.baka3k.blur.process.RenderScriptProcess
import java.util.concurrent.Callable


class BlurImage private constructor(private val context: Context) {
    companion object {
        private const val MAX_RADIUS = 25F
        private const val MIN_RADIUS = 0F

        @Volatile
        private var INSTANCE: BlurImage? = null

        fun getInstance(context: Context): BlurImage {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BlurImage(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private var blurProcess: BlurProcess = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        RenderScriptProcess(RenderScript.create(context.applicationContext))
    } else {
        KotlinBlurProcess()
    }

    private var radius = 10F // default
    private var source: Bitmap? = null
    private var decodePhotoTask: Callable<Bitmap>? = null

    /**
     * radius from 1 to 25
     * */
    fun radius(radius: Float): BlurImage {
        this.radius = if (radius <= MAX_RADIUS && radius > MIN_RADIUS) {
            radius
        } else {
            MAX_RADIUS
        }
        return this
    }

    fun withRenderScript(): BlurImage {
        blurProcess = RenderScriptProcess(RenderScript.create(context.applicationContext))
        return this
    }

    fun withCPU(): BlurImage {
        blurProcess = KotlinBlurProcess()
        return this
    }

    fun load(bitmap: Bitmap): BlurImage {
        source = bitmap
        return this
    }

    fun load(resource: Int): BlurImage {
        decodePhotoTask = DecodePhotoFromResourceTask(context.resources, resourceId = resource)
        return this
    }

    fun load(pathFile: String): BlurImage {
        val bmOptions = BitmapFactory.Options()
        decodePhotoTask = DecodePhotoFromFileTask(pathFile, bmOptions)
        return this
    }

    fun load(pathFile: String, bmOptions: BitmapFactory.Options): BlurImage {
        decodePhotoTask = DecodePhotoFromFileTask(pathFile, bmOptions)
        return this
    }

    fun load(view: View): BlurImage {
        decodePhotoTask = DecodePhotoFromView(view)
        return this
    }

    private fun blur(): Bitmap? {
        return if (source == null) {
            null
        } else {
            blurProcess.blur(source!!, radius)
        }
    }

    fun into(imageView: ImageView) {
        Executor.io {
            if (decodePhotoTask != null) {
                source = decodePhotoTask?.call()
            }
            decodePhotoTask = null
            if (source != null && !source!!.isRecycled) {
                val blurBitmap = blur()
                if (blurBitmap != null) {
                    Executor.ui {
                        imageView.setImageBitmap(blurBitmap)
                    }
                }
                source?.recycle()
                source = null
            }
        }
    }

    private inner class DecodePhotoFromResourceTask(
        private val resource: Resources,
        private val resourceId: Int
    ) :
        Callable<Bitmap> {
        override fun call(): Bitmap {
            return BitmapFactory.decodeResource(resource, resourceId)
        }
    }

    private inner class DecodePhotoFromFileTask(
        private val pathFile: String,
        private val bmOptions: BitmapFactory.Options
    ) :
        Callable<Bitmap> {
        override fun call(): Bitmap {
            return BitmapFactory.decodeFile(pathFile, bmOptions)
        }
    }

    private inner class DecodePhotoFromView(
        private val view: View
    ) :
        Callable<Bitmap> {
        override fun call(): Bitmap {
            return ImageUtils.getScreenshot(view)
        }
    }
}