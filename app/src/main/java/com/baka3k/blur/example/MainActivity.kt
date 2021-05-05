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
import androidx.lifecycle.lifecycleScope
import com.baka3k.blur.BlurImage
import com.baka3k.blur.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    companion object {
        private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        private const val PERMISSIONS_REQUEST_CODE = 10
        private const val IMAGE_PATH = "/storage/emulated/0/download/a.jpg";
    }

    private lateinit var rootView: View
    private lateinit var imageView: ImageView
    private lateinit var btnBlurScreen: Button
    private lateinit var btnBlurPhoto: Button
    private lateinit var btnLoadPhoto: Button
    private val onClickListener: View.OnClickListener = View.OnClickListener {
        if (it.id == btnBlurPhoto.id) {
            lifecycleScope.launch {
//                BlurImage(applicationContext).radius(11F).load(IMAGE_PATH).into(imageView)
                BlurImage(applicationContext).radius(18F).load(R.raw.a).into(imageView)
            }
        }
        if (it.id == btnBlurScreen.id) {
            lifecycleScope.launch {
                BlurImage(applicationContext).radius(18f).load(rootView).into(imageView)
            }
        } else {
            lifecycleScope.launch {
                val bitmap = withContext(Dispatchers.IO) {
                    BitmapFactory.decodeResource(resources, R.raw.a)
                }
                imageView.setImageBitmap(bitmap)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rootView = findViewById(R.id.rootView)
        btnBlurScreen = findViewById(R.id.btnBlurScreen)
        btnBlurPhoto = findViewById(R.id.btnBlurImage)
        btnLoadPhoto = findViewById(R.id.btnLoadImage)
        imageView = findViewById(R.id.imageView)

        btnBlurPhoto.setOnClickListener(onClickListener)
        btnLoadPhoto.setOnClickListener(onClickListener)
        btnBlurScreen.setOnClickListener(onClickListener)
    }

    private fun hasPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PERMISSIONS_REQUIRED.all {
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            return true
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