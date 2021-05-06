package com.baka3k.blur.example

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.baka3k.blur.BlurImage

class MainActivity : AppCompatActivity() {
    companion object {
        private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        private const val PERMISSIONS_REQUEST_CODE = 10
        private const val IMAGE_PATH = "/storage/emulated/0/download/a.jpg";
    }

    private lateinit var rootView: View
    private lateinit var imageView: ImageView
    private lateinit var btnBlurScreen: Button
    private lateinit var btnBlurPhotoByRenderScript: Button
    private lateinit var btnBlurPhotoByCPU: Button
    private lateinit var btnLoadPhoto: Button
    private val onClickListener: View.OnClickListener = View.OnClickListener {

        if (it.id == btnBlurPhotoByRenderScript.id) {
            BlurImage.getInstance(applicationContext).load(R.raw.a)
                .radius(22F)
                .withRenderScript()
                .into(imageView)
        } else if (it.id == btnBlurPhotoByCPU.id) {
            BlurImage.getInstance(applicationContext).load(R.raw.a)
                .radius(18f)
                .withCPU()
                .into(imageView)
        } else if (it.id == btnBlurScreen.id) {
            BlurImage.getInstance(applicationContext).load(rootView)
                .radius(20f)
                .withRenderScript()
                .into(imageView)
        } else {
            val bitmap = BitmapFactory.decodeResource(resources, R.raw.a)
            imageView.setImageBitmap(bitmap)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rootView = findViewById(R.id.rootView)
        btnBlurScreen = findViewById(R.id.btnBlurScreen)
        btnBlurPhotoByCPU = findViewById(R.id.btnBlurByCPU)
        btnBlurPhotoByRenderScript = findViewById(R.id.btnBlurImageByRenderScript)
        btnLoadPhoto = findViewById(R.id.btnLoadImage)
        imageView = findViewById(R.id.imageView)

        btnBlurPhotoByCPU.setOnClickListener(onClickListener)
        btnBlurPhotoByRenderScript.setOnClickListener(onClickListener)
        btnLoadPhoto.setOnClickListener(onClickListener)
        btnBlurScreen.setOnClickListener(onClickListener)
    }

    private fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PERMISSIONS_REQUIRED.all {
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            true
        }
    }

    private fun checkPermission() {
        if (!hasPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        checkPermission()
    }
}