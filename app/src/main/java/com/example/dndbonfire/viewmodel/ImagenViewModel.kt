package com.example.dndbonfire.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import java.io.File

class ImagenViewModel: ViewModel() {
    private val _imagenUri = mutableStateOf<Uri?>(null)
    val imagenUri = _imagenUri

    fun modificarImagenUri(imagen: Uri?) {
        _imagenUri.value = imagen
    }

    private val _bitmap = mutableStateOf<Bitmap?>(null)
    val bitmap = _bitmap

    fun modificarBitmap(bitmap: Bitmap?) {
        _bitmap.value = bitmap
    }

    fun uriABase64(context: Context, uri: Uri): String {
        val bytes = context.contentResolver.openInputStream(uri)?.use {
            it.readBytes()
        } ?: ByteArray(0)
        return Base64.encodeToString(bytes, Base64.NO_WRAP).trim()
    }

    private val _imagenEliminada = mutableStateOf(false)
    val imagenEliminada = _imagenEliminada

    fun modificarImagenEliminada(imagenEliminada: Boolean) {
        _imagenEliminada.value = imagenEliminada
    }

    fun crearArchivoImagenCamara(context: Context): Uri {
        val nombreArchivo = "camara.jpg"
        val file = File(context.filesDir, nombreArchivo)
        file.outputStream().use { out ->
            bitmap.value?.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        _imagenUri.value = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }

    private val _mostrarDialogoImagen = mutableStateOf(false)
    val mostrarDialogoImagen = _mostrarDialogoImagen

    fun mostrarDialogo(mostrar: Boolean) {
        _mostrarDialogoImagen.value = mostrar
    }

    private val _mostrarMensajePermiso = mutableStateOf(false)
    val mostrarMensajePermiso = _mostrarMensajePermiso

    fun mostrarMensaje(mostrar: Boolean) {
        _mostrarMensajePermiso.value = mostrar
    }

    private val _mostrarDialogoPermiso = mutableStateOf(false)
    val mostrarDialogoPermiso = _mostrarDialogoPermiso

    fun mostrarDialogoPermiso(mostrar: Boolean) {
        _mostrarDialogoPermiso.value = mostrar
    }
}