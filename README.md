# NotaVision
NotvaVision App is an Android mobile application designed to process images of music sheets. Utilizing advanced image processing techniques, it detects and reconstructs musical staves, making them easily readable in a scrollable horizontal view.

## Features
<p><b>Image Upload:</b> Upload images of music sheets directly from your gallery.</p>
<p><b>Edge Detection:</b> Uses OpenCV's Canny Edge Detector to identify staves edges in the uploaded image.</p>
<p><b>Hough Transformation:</b> Employs the Hough Transformation method to detect lines within the image.</p>
<p><b>Stave Detection:</b> Automatically detects and groups staves by recognizing the five-line pattern characteristic of musical staves.</p>
<p><b>Scrollable Stave View:</b> Displays the detected staves in a connected horizontal line within a scrollable view for easy reading.</p>
<p><b>Multiple Uploads</b>: Allows uploading multiple music note images and processes each independently.</p>

## Technologies Used
<p><b>OpenCV:</b> For image processing tasks such as edge detection and line detection.</p>
<p><b>Canny Edge Detector:</b> To identify staves edges in the uploaded images.</p>
<p><b>Hough Transformation:</b> To detect straight lines corresponding to staves.</p>
<p><b>Android Development Framework:</b> The app is built using Android Studio.</p>
