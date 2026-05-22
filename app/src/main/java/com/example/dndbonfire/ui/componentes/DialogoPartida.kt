package com.example.dndbonfire.ui.componentes

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.dndbonfire.modelo.Raza
import com.example.dndbonfire.modelo.Subclase
import com.example.dndbonfire.modelo.Subraza
import com.example.dndbonfire.viewmodel.PersonajeViewModel

@Composable
fun DialogoPartida(
    resultado: String = "",
    resultadoBorrar: String = "",
    resultadoCrearRelacion: String = "",
    resultadoEliminarRelacion: String = "",
    resultadoEliminarRelaciones: String = "",
    resultadoCrearSesion: String = "",
    resultadoModificarSesion: String = "",
    onDismiss: () -> Unit
) {
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
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                text =
                    if (resultado != "") {
                        resultado
                    } else if (resultadoBorrar != "") {
                        resultadoBorrar
                    } else if (resultadoCrearRelacion != "") {
                        resultadoCrearRelacion
                    } else if (resultadoEliminarRelacion != "") {
                        resultadoEliminarRelacion
                    } else if (resultadoEliminarRelaciones != "") {
                        resultadoEliminarRelaciones
                    } else if (resultadoCrearSesion != "") {
                        resultadoCrearSesion
                    } else {
                        resultadoModificarSesion
                    }
            )
        }
    }
}