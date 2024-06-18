package com.example.notavision

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import kotlin.math.max
import kotlin.math.min

class DisplayStavesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_staves)
        val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
        displayStaves(imageUri)
        val imageButtonSelect: ImageButton = findViewById(R.id.imageButtonBottomRight)
        imageButtonSelect.setOnClickListener {
            selectPhoto()
        }
    }
    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)  // Assuming single selection for simplicity
        }
        startActivityForResult(intent, SELECT_PHOTO_REQUEST_CODE)  // Start the activity and expect a result
    }

    companion object {
        private const val SELECT_PHOTO_REQUEST_CODE = 0
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            val newUri: Uri? = data?.data
            newUri?.let {
                displayStaves(it)  // Refresh the displayed staves with the new image
            }
        }
    }

    private fun displayStaves(uri: Uri) {
        val originalBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val inputMat = Mat()
        Utils.bitmapToMat(originalBitmap, inputMat)

        val linesList = detectLines(inputMat)
        val staveLines = filterStaveLines(linesList)
        val groupedStaves = groupStaves(staveLines)

        val layoutStaves = findViewById<LinearLayout>(R.id.layoutStaves)
        layoutStaves.removeAllViews()  // Clear previous views if any
        groupedStaves.forEach { group ->
            val rect = calculateBoundingRect(group, inputMat, 30)  // Optional padding can be adjusted
            val staveMat = Mat(inputMat, rect)
            val staveBitmap = Bitmap.createBitmap(staveMat.cols(), staveMat.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(staveMat, staveBitmap)

            val imageView = ImageView(this).apply {
                val scale = 3 // Scaling factor
                layoutParams = LinearLayout.LayoutParams(
                    staveBitmap.width * scale, // Scale width
                    staveBitmap.height * scale // Scale height
                )
                setImageBitmap(staveBitmap)
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
            runOnUiThread {
                layoutStaves.addView(imageView)
            }
        }


    }

    private fun detectLines(inputMat: Mat): List<Line> {
        val edges = Mat()
        Imgproc.Canny(inputMat, edges, 50.0, 150.0)
        val lines = Mat()
        Imgproc.HoughLinesP(edges, lines, 1.0, Math.PI / 180, 50, 50.0, 10.0)
        val linesList = mutableListOf<Line>()
        for (i in 0 until lines.rows()) {
            val vec = lines.get(i, 0)
            val x1 = vec[0]
            val y1 = vec[1]
            val x2 = vec[2]
            val y2 = vec[3]
            linesList.add(Line(x1, y1, x2, y2))
        }
        return linesList
    }

    private fun filterStaveLines(lines: List<Line>): List<Line> {
        // Filtering by length and horizontal orientation
        val minLength = 300 // Minimum length to consider a line as part of a stave
        val maxOrientationDifference = Math.toRadians(10.0) // Maximum deviation from horizontal

        return lines.filter {
            val length = Math.hypot(it.x2 - it.x1, it.y2 - it.y1)
            val orientation = Math.abs(Math.atan2(it.y2 - it.y1, it.x2 - it.x1))
            length > minLength && orientation < maxOrientationDifference
        }
    }

    private fun groupStaves(lines: List<Line>): List<List<Line>> {
        val groupedStaves = mutableListOf<MutableList<Line>>()
        val tolerance = 10 // Tolerance in pixels to group lines as part of the same stave

        if (lines.isEmpty()) return groupedStaves

        // Sort by Y position
        val sortedLines = lines.sortedBy { it.y1 }

        var currentGroup = mutableListOf(sortedLines.first())

        for (i in 1 until sortedLines.size) {
            val currentLine = sortedLines[i]
            val previousLine = sortedLines[i - 1]
            // Group lines if they are within vertical tolerance
            if (Math.abs(currentLine.y1 - previousLine.y1) < tolerance) {
                currentGroup.add(currentLine)
            } else {
                if (currentGroup.size >= 5) { // Assuming stave has at least 5 lines
                    groupedStaves.add(currentGroup)
                }
                currentGroup = mutableListOf(currentLine)
            }
        }
        if (currentGroup.size >= 5) groupedStaves.add(currentGroup)

        return groupedStaves
    }

    private fun calculateBoundingRect(staveLines: List<Line>, inputMat: Mat, padding: Int = 20): Rect {
        val xMin = staveLines.minOf { it.x1.toInt() }
        val xMax = staveLines.maxOf { it.x2.toInt() }
        val yMin = max(0, (staveLines.minOf { it.y1 } - padding).toInt())
        val yMax = min(inputMat.rows(), (staveLines.maxOf { it.y2 } + padding).toInt())

        return Rect(xMin, yMin, xMax - xMin, yMax - yMin)
    }



    data class Line(val x1: Double, val y1: Double, val x2: Double, val y2: Double)

}
