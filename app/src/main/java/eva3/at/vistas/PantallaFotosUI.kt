package com.example.albunfotos.vistas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.albunfotos.CameraAppViewModel
import com.example.albunfotos.FormRecepcionViewModel
import com.example.albunfotos.Pantalla


@Composable
fun PantallaFotosUI(
    formRecepcionVm: FormRecepcionViewModel,
    cameraAppViewModel: CameraAppViewModel,
    onTomarFotoClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        LazyColumn {
            items(formRecepcionVm.fotos) { uri ->
                FotoItem(uri = uri, onDeleteClick = {
                    formRecepcionVm.fotos.remove(uri)
                })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                cameraAppViewModel.pantalla.value = Pantalla.FORM
            }) {
                Text(text = "Volver")
            }

            Button(onClick = {
                onTomarFotoClick()
            }) {
                Text(text = "Tomar Foto")
            }
        }
    }
}