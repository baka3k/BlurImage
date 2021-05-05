package com.baka3k.blur

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

object ImageUtils {
    fun getScreenshot(v: View): Bitmap {
        val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.draw(c)
        return b
    }
}