# NotaVision
NotvaVision App is an Android mobile application designed to process images of music sheets. Utilizing advanced image processing techniques, it detects and reconstructs musical staves, making them easily readable in a scrollable horizontal view.

## Features
**Image Upload:** Upload images of music sheets directly from your gallery.
**Edge Detection:** Uses OpenCV's Canny Edge Detector to identify staves edges in the uploaded image.
**Hough Transformation:** Employs the Hough Transformation method to detect lines within the image.
**Stave Detection:** Automatically detects and groups staves by recognizing the five-line pattern characteristic of musical staves.
**Scrollable Stave View:** Displays the detected staves in a connected horizontal line within a scrollable view for easy reading.
**Multiple Uploads:** Allows uploading multiple music note images and processes each independently.

## Technologies Used
**OpenCV:** For image processing tasks such as edge detection and line detection.
**Canny Edge Detector:** To identify staves edges in the uploaded images.
**Hough Transformation:** To detect straight lines corresponding to staves.
**Android Development Framework:** The app is built using Android Studio.
