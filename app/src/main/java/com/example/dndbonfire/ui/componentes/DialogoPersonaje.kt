package com.example.dndbonfire.ui.componentes

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.dndbonfire.modelo.Raza
import com.example.dndbonfire.modelo.Subclase
import com.example.dndbonfire.modelo.Subraza
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.viewmodel.PersonajeViewModel

@Composable
fun DialogoPersonaje(
    personajeViewModel: PersonajeViewModel,
    raza: Raza? = null,
    razaTraducida: Raza? = null,
    subraza: Subraza? = null,
    subrazaTraducida: Subraza? = null,
    subclase: Subclase? = null,
    subclaseTraducida: Subclase? = null,
    resultado: String = "",
    resultadoBorrar: String = "",
    resultadoBorrarRelacion: String = "",
    resultadoBorrarRelaciones: String = "",
    seleccion: String = "",
    onDismiss: () -> Unit
) {
    val urlTraducida by personajeViewModel.urlTraducida
    val urlOriginal by personajeViewModel.urlOriginal
    var mostrarOriginal by rememberSaveable { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .heightIn(max = 1000.dp)
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
            var traduccion = ""
            var original = ""
            when (seleccion) {
                "edad" -> {
                    traduccion = razaTraducida!!.age
                    original = raza!!.age
                }
                "alineamiento" -> {
                    traduccion = razaTraducida!!.alignment
                    original = raza!!.alignment
                }
                "subraza" -> {
                    traduccion = subrazaTraducida!!.desc
                    original = subraza!!.desc
                }
                "tamaño" -> {
                    traduccion = razaTraducida!!.size_description
                    original = raza!!.size_description
                }
                "idioma" -> {
                    traduccion = razaTraducida!!.language_desc
                    original = raza!!.language_desc
                }
                "subclase" -> {
                    var textoTraduccion = subclaseTraducida?.desc.orEmpty().joinToString("")
                    var textoOriginal = subclase?.desc.orEmpty().joinToString("")

                    traduccion = textoTraduccion
                    original = textoOriginal
                }
                "url" -> {
                    val textoTraduccion = urlTraducida?.desc?.joinToString("")
                        ?.takeIf { it.isNotBlank() }
                        ?: "Información no disponible"

                    val textoOriginal = urlOriginal?.desc?.joinToString("")
                        ?.takeIf { it.isNotBlank() }
                        ?: "Texto original no disponible"

                    traduccion = textoTraduccion
                    original = textoOriginal
                }
                "resultado" -> {
                    traduccion = resultado
                }
                else -> "hola"
            }

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
                    .verticalScroll(scrollState)
                    .padding(
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                Text(
                    text = if (seleccion != resultado) {
                        traduccion
                    } else {
                        if (resultado != "") {
                            resultado
                        } else if (resultadoBorrar != "") {
                            resultadoBorrar
                        } else if (resultadoBorrarRelacion != "") {
                            resultadoBorrarRelacion
                        } else {
                            resultadoBorrarRelaciones
                        }
                    }
                )

                if (seleccion != "resultado") {
                    Button(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.End)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CutCornerShape(8.dp)
                            )
                            .border(
                                width = 3.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CutCornerShape(16.dp)
                            ),
                        colors = ButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                            disabledContentColor = MaterialTheme.colorScheme.onBackground,
                            containerColor = MaterialTheme.colorScheme.secondary,
                            disabledContainerColor = Gris
                        ),
                        shape = CutCornerShape(16.dp),
                        onClick = {
                            mostrarOriginal = !mostrarOriginal
                        }
                    ) {
                        if (!mostrarOriginal) {
                            Text(
                                text = "Mostrar texto original",
                                fontSize = 14.sp
                            )
                        } else {
                            Text(
                                text = "Ocultar texto original",
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                if (mostrarOriginal) {
                    Text(
                        text = original
                    )
                }
            }
        }
    }
}