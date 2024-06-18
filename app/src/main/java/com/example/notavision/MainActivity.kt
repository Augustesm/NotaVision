package com.example.notavision

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import org.opencv.android.OpenCVLoader

class MainActivity : ComponentActivity() {
    private val SELECT_PHOTO_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!OpenCVLoader.initDebug()) {
            Log.e("OpenCV", "Initialization Failed")
        } else {
            Log.d("OpenCV", "Initialization Successful")
        }

        val imageButtonUpload: ImageButton = findViewById(R.id.imageButtonUpload)
        imageButtonUpload.setOnClickListener {
            selectPhoto()
        }

    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, SELECT_PHOTO_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            val uri: Uri? = data?.data
            uri?.let {
                val intent = Intent(this, DisplayStavesActivity::class.java)
                intent.putExtra("imageUri", it.toString())
                startActivity(intent)
            }
        }
    }

}
