package com.example.albunfotos

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.albunfotos.utilidades.obtenerUbicacionActual
import com.example.albunfotos.utilidades.permisoLocalizacion
import com.example.albunfotos.utilidades.solicitarPermisoUbicacion
import com.example.albunfotos.vistas.PantallaCamaraUI
import com.example.albunfotos.vistas.PantallaFormUI
import com.example.albunfotos.vistas.PantallaFotosUI
import com.example.albunfotos.vistas.PantallaMapaUI


enum class Pantalla {
    FORM,
    CAMARA,
    FOTOS,
    MAPA
}

class CameraAppViewModel : ViewModel() {
    val pantalla = mutableStateOf(Pantalla.FORM)
}

class FormRecepcionViewModel : ViewModel() {
    val nombreImagen = mutableStateOf("")
    val fotos = mutableStateListOf<Uri?>()
    val ubicacion = mutableStateOf<Location?>(null)

}

class MainActivity : ComponentActivity() {
    val cameraAppVm: CameraAppViewModel by viewModels()
    val formRecepcionVm: FormRecepcionViewModel by viewModels()

    private lateinit var cameraController: LifecycleCameraController

    private val requestCameraPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                Log.e("Permisos", "Permiso uso de camara denegado")
            }
        }

    private val requestLocationPermissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                obtenerUbicacionActual(this, formRecepcionVm)
            } else {
                Log.e("Permisos", "Permiso uso de ubicación denegado")
            }
        }

    private fun requestCameraPermission() {
        when {
            isCameraPermissionGranted() -> {
                openCamera()
            }
            else -> {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                } else {
                    requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        }
    }


    private fun requestLocationPermission() {
        when {
            permisoLocalizacion(this) -> {
                obtenerUbicacionActual(this, formRecepcionVm)
            }
            else -> {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {

                } else {

                    solicitarPermisoUbicacion(this)
                }
            }
        }
    }


    fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openCamera() {
        setContent {
            AppUI(
                cameraController,
                formRecepcionVm,
                cameraAppVm,
                ::requestCameraPermission,
                ::isCameraPermissionGranted,
            )
        }
        requestLocationPermission()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupCamera()
        requestCameraPermission()
        requestLocationPermission()
    }

    private fun setupCamera() {
        cameraController = LifecycleCameraController(this)
        cameraController.bindToLifecycle(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    }
}

@Composable
fun AppUI(
    cameraController: LifecycleCameraController,
    formRecepcionVm: FormRecepcionViewModel,
    cameraAppViewModel: CameraAppViewModel,
    requestCameraPermission: () -> Unit,
    isCameraPermissionGranted: () -> Boolean,
) {
    MaterialTheme {
        Surface {
            when (cameraAppViewModel.pantalla.value) {
                Pantalla.FORM -> {
                    PantallaFormUI(formRecepcionVm, cameraAppViewModel)
                }
                Pantalla.CAMARA -> {
                    PantallaCamaraUI(
                        cameraController,
                        cameraAppViewModel,
                        formRecepcionVm,
                        requestCameraPermission,
                        isCameraPermissionGranted
                    )
                }
                Pantalla.FOTOS -> {
                    PantallaFotosUI(
                        formRecepcionVm,
                        cameraAppViewModel,
                        onTomarFotoClick = {
                            cameraAppViewModel.pantalla.value = Pantalla.CAMARA
                        }
                    )
                }
                Pantalla.MAPA -> {
                    PantallaMapaUI(formRecepcionVm, cameraAppViewModel)
                }
            }
        }
    }
}

