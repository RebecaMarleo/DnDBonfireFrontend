package com.example.dndbonfire.ui.componentes

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import coil3.compose.AsyncImage
import com.example.dndbonfire.R
import com.example.dndbonfire.data.DatosUsuario
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.ui.theme.InformacionPrincipalStyle
import com.example.dndbonfire.viewmodel.ImagenViewModel
import com.example.dndbonfire.viewmodel.TemaApp

@Composable
fun DialogoImagen(
    imagenViewModel: ImagenViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val actividad = context as? ComponentActivity
    var imagenUri by imagenViewModel.imagenUri
    val mostrarMensaje by imagenViewModel.mostrarMensajePermiso
    val mostrarDialogo by imagenViewModel.mostrarDialogoPermiso

    var uri by remember { mutableStateOf<Uri?>(null) }

    val launcherGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uriGaleria: Uri? ->
            imagenViewModel.modificarImagenUri(uriGaleria)
            imagenViewModel.mostrarDialogo(false)
        }
    )

    val launcherPermiso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    val launcherCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito) {
            imagenUri?.let { savedUri ->
                imagenViewModel.modificarBitmap(BitmapFactory.decodeStream(
                    context.contentResolver.openInputStream(savedUri)
                ))
            }
            imagenViewModel.mostrarDialogo(false)
        } else {
            // No se guardó foto válida → no guardes esa uri
            uri = null
            imagenUri = null
        }
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = CutCornerShape(8.dp)
                )
                .border(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = CutCornerShape(16.dp)
                ),
            colors = CardDefaults
                .cardColors(
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            shape = CutCornerShape(16.dp)
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 16.dp, end = 16.dp)
                    .size(24.dp),
                onClick = onDismiss
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(0.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(IntrinsicSize.Max)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledContainerColor = Gris
                    ),
                    shape = RectangleShape,
                    onClick = {
                        val mostrarMensajePermiso = actividad?.let {
                            shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA)
                        } ?: false

                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            uri = imagenViewModel.crearArchivoImagenCamara(context)
                            uri?.let { savedUri ->
                                imagenUri = savedUri
                                launcherCamara.launch(savedUri)
                            }
                        }
                        else if (mostrarMensajePermiso) {
                            imagenViewModel.mostrarDialogoPermiso(true)
                        } else {
                            launcherPermiso.launch(Manifest.permission.CAMERA)
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Elegir foto de la galería",
                            modifier = Modifier.size(36.dp).padding(end = 8.dp)
                        )
                        Text(
                            text = "Tomar foto",
                            style = InformacionPrincipalStyle
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledContainerColor = Gris
                    ),
                    shape = RectangleShape,
                    onClick = {
                        launcherGaleria.launch("image/*")
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Photo,
                            contentDescription = "Tomar una foto nueva",
                            modifier = Modifier.size(36.dp).padding(end = 8.dp)
                        )
                        Text(
                            text = "Elegir una foto de la galería",
                            style = InformacionPrincipalStyle
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledContainerColor = Gris
                    ),
                    shape = RectangleShape,
                    onClick = {
                        imagenViewModel.modificarImagenEliminada(true)
                        imagenViewModel.modificarImagenUri(null)
                        imagenViewModel.mostrarDialogo(false)
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Quitar foto",
                            modifier = Modifier.size(36.dp).padding(end = 8.dp)
                        )
                        Text(
                            text = "Eliminar foto",
                            style = InformacionPrincipalStyle
                        )
                    }
                }
            }
//            Row() {
//                Card(
//                    modifier = Modifier
//                        .size(48.dp)
//                        .padding(bottom = 4.dp)
//                        .border(
//                            width = 2.dp,
//                            color = MaterialTheme.colorScheme.tertiary,
//                            shape = CutCornerShape(8.dp)
//                        )
//                        .border(
//                            width = 3.dp,
//                            color = MaterialTheme.colorScheme.tertiary,
//                            shape = CutCornerShape(16.dp)
//                        )
//                        .combinedClickable(
//                            onClick = {
//
//                            }
//                        ),
//                    colors = CardDefaults
//                        .cardColors(
//                            contentColor = MaterialTheme.colorScheme.onTertiary,
//                            containerColor = MaterialTheme.colorScheme.tertiary
//                        ),
//                    shape = CutCornerShape(16.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Add,
//                        contentDescription = "Tomar una foto nueva",
//                        modifier = Modifier.size(36.dp)
//                    )
//                }
//
//                Card(
//                    modifier = Modifier
//                        .size(48.dp)
//                        .padding(bottom = 4.dp)
//                        .border(
//                            width = 2.dp,
//                            color = MaterialTheme.colorScheme.tertiary,
//                            shape = CutCornerShape(8.dp)
//                        )
//                        .border(
//                            width = 3.dp,
//                            color = MaterialTheme.colorScheme.tertiary,
//                            shape = CutCornerShape(16.dp)
//                        )
//                        .combinedClickable(
//                            onClick = {
//
//                            }
//                        ),
//                    colors = CardDefaults
//                        .cardColors(
//                            contentColor = MaterialTheme.colorScheme.onTertiary,
//                            containerColor = MaterialTheme.colorScheme.tertiary
//                        ),
//                    shape = CutCornerShape(16.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Create,
//                        contentDescription = "Elegir foto de la galería",
//                        modifier = Modifier.size(36.dp)
//                    )
//                }
//
//                Card(
//                    modifier = Modifier
//                        .size(48.dp)
//                        .padding(bottom = 4.dp)
//                        .border(
//                            width = 2.dp,
//                            color = MaterialTheme.colorScheme.tertiary,
//                            shape = CutCornerShape(8.dp)
//                        )
//                        .border(
//                            width = 3.dp,
//                            color = MaterialTheme.colorScheme.tertiary,
//                            shape = CutCornerShape(16.dp)
//                        )
//                        .combinedClickable(
//                            onClick = {
//
//                            }
//                        ),
//                    colors = CardDefaults
//                        .cardColors(
//                            contentColor = MaterialTheme.colorScheme.onTertiary,
//                            containerColor = MaterialTheme.colorScheme.tertiary
//                        ),
//                    shape = CutCornerShape(16.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Delete,
//                        contentDescription = "",
//                        modifier = Modifier.size(36.dp)
//                    )
//                }
//            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = {
                imagenViewModel.mostrarDialogoPermiso(false)
            },
            title = { Text("Permiso requerido") },
            text = { Text("La cámara es necesaria para tomar fotos dentro de la app.") },
            confirmButton = {
                Button(onClick = {
                    launcherPermiso.launch(Manifest.permission.CAMERA)
                    imagenViewModel.mostrarDialogoPermiso(false)
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = {
                    imagenViewModel.mostrarDialogoPermiso(false)
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}