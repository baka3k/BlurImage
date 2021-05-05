package com.baka3k.blur

import android.graphics.Bitmap

interface BlurProcess {
    fun blur(original: Bitmap, radius: Float): Bitmap?
}