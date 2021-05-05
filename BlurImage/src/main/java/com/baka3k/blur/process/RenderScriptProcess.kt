package com.baka3k.blur.process

import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.baka3k.blur.BlurProcess
import kotlin.math.roundToInt

class RenderScriptProcess(private val renderScript: RenderScript) : BlurProcess {
    companion object {
        private const val preScale: Float = 0.2f
    }

    override fun blur(original: Bitmap, radius: Float): Bitmap? {
        return blur(original, preScale, radius)
    }

    private fun blur(bitmap: Bitmap, scale: Float, radius: Float): Bitmap {
        val width = (bitmap.width * scale).roundToInt()
        val height = (bitmap.height * scale).roundToInt()
        val inputBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)
        val theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        val tmpIn = Allocation.createFromBitmap(renderScript, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap)
        theIntrinsic.setRadius(radius)
        theIntrinsic.setInput(tmpIn)
        theIntrinsic.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)
        return outputBitmap!!
    }
}