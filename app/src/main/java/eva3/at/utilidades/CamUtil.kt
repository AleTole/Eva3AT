package com.example.albunfotos.utilidades


import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import java.io.File
import java.time.LocalDateTime


fun generarNombreSegunFechaHastaSegundo(): String =
    LocalDateTime.now().toString().replace(Regex("[T:.-]"), "").substring(0, 14)

fun crearArchivoImagenPublico(contexto: Context): File =
    File(
        contexto.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "${generarNombreSegunFechaHastaSegundo()}.jpg"
    )

fun tomarFotografia(
    cameraController: LifecycleCameraController,
    archivo: File,
    contexto: Context, imagenGuardadaOk: (uri: Uri) -> Unit) {
    val outputFileOptions = ImageCapture.OutputFileOptions.Builder(archivo).build()

    cameraController.takePicture(
        outputFileOptions,
        ContextCompat.getMainExecutor(contexto),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = outputFileResults.savedUri
                if (savedUri != null) {
                    Log.d("ImageCapture", "Image saved successfully: $savedUri")
                    imagenGuardadaOk(savedUri)
                } else {
                    Log.e("ImageCapture", "Saved URI is null")
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("ImageCapture", "Error capturing image", exception)
            }
        }
    )
}
