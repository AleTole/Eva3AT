package com.example.albunfotos.vistas

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.albunfotos.CameraAppViewModel
import com.example.albunfotos.FormRecepcionViewModel
import com.example.albunfotos.Pantalla
import com.example.albunfotos.utilidades.crearArchivoImagenPublico
import com.example.albunfotos.utilidades.tomarFotografia

@Composable
fun PantallaCamaraUI(
    cameraController: LifecycleCameraController,
    cameraAppViewModel: CameraAppViewModel,
    formRecepcionVm: FormRecepcionViewModel,
    requestCameraPermission: () -> Unit,
    isCameraPermissionGranted: () -> Boolean,
) {
    val contexto = LocalContext.current
    var imageName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            factory = {
                PreviewView(it).apply {
                    controller = cameraController
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isCameraPermissionGranted()) {
                    val archivo = crearArchivoImagenPublico(contexto)
                    tomarFotografia(
                        cameraController,
                        archivo,
                        contexto,
                        imagenGuardadaOk = { uri ->
                            formRecepcionVm.fotos.add(uri)
                            imageName = ""
                            cameraAppViewModel.pantalla.value = Pantalla.MAPA
                        }
                    )
                } else {
                    requestCameraPermission()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            Text(text = "Capturar", fontSize = 20.sp)
        }



        Spacer(modifier = Modifier.height(16.dp))
    }

    Button(onClick = {
        cameraAppViewModel.pantalla.value = Pantalla.FORM
    }) {
        Text(text = "Volver")
    }
}