package com.example.albunfotos.vistas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.albunfotos.CameraAppViewModel
import com.example.albunfotos.FormRecepcionViewModel
import com.example.albunfotos.Pantalla

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapaUI(formRecepcionVm: FormRecepcionViewModel, cameraAppViewModel: CameraAppViewModel) {
    var imageName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Muestra el preview de la última fotografía tomada
        formRecepcionVm.fotos.lastOrNull()?.let { uri ->
            Image(
                painter = rememberImagePainter(data = uri),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Muestra un TextField para que el usuario pueda agregar el nombre
        TextField(
            value = imageName,
            onValueChange = { imageName = it },
            label = { Text("Ingrese el nombre de la imagen") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            formRecepcionVm.nombreImagen.value = imageName
            cameraAppViewModel.pantalla.value = Pantalla.FORM
        }) {
            Text("Guardar")
        }
    }
}