package com.example.albunfotos.vistas

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberImagePainter

@Composable
fun FotoItem(uri: Uri?, onDeleteClick: () -> Unit) {
    var isDialogVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    uri?.let {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = rememberImagePainter(data = uri),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        isDialogVisible = true
                    }
                    .height(100.dp)
                    .weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = onDeleteClick) {
                Text(text = "Eliminar")
            }
        }

        if (isDialogVisible) {
            Dialog(
                onDismissRequest = {
                    isDialogVisible = false
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Image(
                        painter = rememberImagePainter(data = uri),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                }
                            }
                    )
                }
            }
        }
    }
}



