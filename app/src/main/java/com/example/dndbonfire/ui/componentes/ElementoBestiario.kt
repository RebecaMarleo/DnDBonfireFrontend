package com.example.dndbonfire.ui.componentes

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dndbonfire.data.ProveedorBestia
import com.example.dndbonfire.modelo.ResumenBestia
import com.example.dndbonfire.navegacion.Ruta
import com.example.dndbonfire.ui.theme.InformacionPrincipalStyle
import com.example.dndbonfire.ui.theme.Primary
import com.example.dndbonfire.ui.theme.Titulo1Style
import com.example.dndbonfire.ui.theme.Transaparente
import com.example.dndbonfire.viewmodel.BestiaViewModel
import kotlinx.coroutines.launch

@Composable
fun ElementoBestiario(
    bestia: ResumenBestia,
    navController: NavController,
    bestiaViewModel: BestiaViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    var contexto = LocalContext.current
    var nuevaLetra = false
    bestiaViewModel.bestiasInicial.forEach { b ->
        if (bestia.name == b.name) {
            nuevaLetra = true
        }
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val contentColor by animateColorAsState(
        targetValue = if (isPressed)
            MaterialTheme.colorScheme.onSecondary
        else
            MaterialTheme.colorScheme.onPrimaryContainer,
        animationSpec = tween(200)
    )
    val containerColor by animateColorAsState(
        targetValue = if (isPressed)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.primaryContainer,
        animationSpec = tween(200)
    )
    val borderColor by animateColorAsState(
        targetValue = if (isPressed)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.tertiary
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 4.dp,
                end = 16.dp,
                bottom = 8.dp
            )
    ) {
        Box(
            modifier = Modifier
                .size(50.dp, 30.dp)
                .padding(8.dp, 0.dp)
        ) {
            Text(
                text = bestia.name.first().toString(),
                style = Titulo1Style,
                color = if (nuevaLetra) {
                    Primary
                } else {
                    Transaparente
                },
                overflow = TextOverflow.Visible
            )
        }

        Card(
            modifier = Modifier
                .padding(
                    start = 16.dp
                )
                .combinedClickable(
                    onClick = {
                        coroutineScope.launch {
                            ProveedorBestia.cargarBestiaSeleccionada(bestia.index)
                        }
                        navController.navigate(Ruta.Bestia(bestia.index))
                    },
                    interactionSource = interactionSource
                )
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = CutCornerShape(8.dp)
                )
                .border(
                    width = 3.dp,
                    color = borderColor,
                    shape = CutCornerShape(16.dp)
                ),
            colors = CardDefaults
                .cardColors(
                    contentColor = contentColor,
                    containerColor = containerColor
                ),
            shape = CutCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    style = InformacionPrincipalStyle,
                    text = bestia.name
                )
            }
        }
    }
}