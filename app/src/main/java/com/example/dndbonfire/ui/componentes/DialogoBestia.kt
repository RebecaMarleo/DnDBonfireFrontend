package com.example.dndbonfire.ui.componentes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.dndbonfire.data.ProveedorBestia
import com.example.dndbonfire.modelo.Accion
import com.example.dndbonfire.modelo.AccionLegendaria
import com.example.dndbonfire.modelo.Bestia
import com.example.dndbonfire.modelo.HabilidadEspecial
import com.example.dndbonfire.modelo.Reaccion
import com.example.dndbonfire.navegacion.Ruta
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.viewmodel.BestiaViewModel
import kotlinx.coroutines.launch
import kotlin.collections.joinToString
import kotlin.collections.orEmpty

@Composable
fun DialogoBestia(
    bestiaViewModel: BestiaViewModel,
    habilidad: HabilidadEspecial? = null,
    accion: Accion? = null,
    accionLegendaria: AccionLegendaria? = null,
    reaccion: Reaccion? = null,
    bestia: Bestia? = null,
    onDismiss: () -> Unit
) {
    val descTraducida by bestiaViewModel.descTraducida
    val descOriginal by bestiaViewModel.descOriginal
    var mostrarOriginal by rememberSaveable { mutableStateOf(false) }

    val scrollState = rememberScrollState()

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
            var traduccion = descTraducida ?: "Información no disponible"
            var original = descOriginal ?: "Texto original no disponible"

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
                    text = traduccion
                )

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

                if (mostrarOriginal) {
                    Text(
                        text = original
                    )
                }
            }
        }
    }
}