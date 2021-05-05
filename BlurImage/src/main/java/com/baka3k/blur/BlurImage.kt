package com.baka3k.blur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.renderscript.RenderScript
import android.view.View
import android.widget.ImageView
import com.baka3k.blur.process.RenderScriptProcess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BlurImage(private val context: Context) {
    companion object {
        private const val MAX_RADIUS = 25F
        private const val MIN_RADIUS = 0F
    }

    private val blurProcess: BlurProcess

    private var radius = 10F // default
    private var source: Bitmap? = null

    init {
        blurProcess = RenderScriptProcess(
            RenderScript.create(context.applicationContext)
        )
    }

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

    suspend fun load(bitmap: Bitmap): BlurImage {
        source = bitmap
        return this
    }

    suspend fun load(resource: Int): BlurImage = withContext(Dispatchers.IO) {
        source = BitmapFactory.decodeResource(context.resources, resource)
        this@BlurImage
    }

    suspend fun load(pathFile: String): BlurImage = withContext(Dispatchers.IO) {
        val bmOptions = BitmapFactory.Options()
        source = BitmapFactory.decodeFile(pathFile, bmOptions)
        this@BlurImage
    }

    suspend fun load(pathFile: String, bmOptions: BitmapFactory.Options): BlurImage =
        withContext(Dispatchers.IO) {
            source = BitmapFactory.decodeFile(pathFile, bmOptions)
            this@BlurImage
        }

    suspend fun load(view: View): BlurImage = withContext(Dispatchers.IO) {
        source = ImageUtils.getScreenshot(view)
        this@BlurImage
    }

    private suspend fun blur(): Bitmap? = withContext(Dispatchers.IO) {
        if (source == null) {
            null
        } else {
            blurProcess.blur(source!!, radius)
        }
    }

    suspend fun into(imageView: ImageView) = withContext(Dispatchers.Main) {
        imageView.setImageBitmap(blur())
    }
}