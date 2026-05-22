package com.example.dndbonfire.ui.componentes

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.dndbonfire.data.ProveedorPartida
import com.example.dndbonfire.modelo.Partida
import com.example.dndbonfire.navegacion.Ruta
import com.example.dndbonfire.ui.theme.InformacionPrincipalStyle
import com.example.dndbonfire.ui.theme.Titulo5Style
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaUsuarioViewModel
import com.example.dndbonfire.viewmodel.PartidaViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import com.example.dndbonfire.viewmodel.SesionViewModel
import com.example.dndbonfire.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

@Composable
fun ElementoListaPartidas(
    partida: Partida,
    navController: NavController,
    mainViewModel: MainViewModel,
    partidaViewModel: PartidaViewModel,
    partidaUsuarioViewModel: PartidaUsuarioViewModel,
    usuarioViewModel: UsuarioViewModel,
    personajeViewModel: PersonajeViewModel,
    sesionViewModel: SesionViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val contexto = LocalContext.current

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

    Card(
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            )
            .combinedClickable(
                onClick = {
                    usuarioViewModel.cargarUsuarioDM(null)
                    partidaUsuarioViewModel.vaciarAventureros()
                    personajeViewModel.modificarSinPersonajes(false)
                    sesionViewModel.vaciarSesiones()
                    sesionViewModel.modificarSinSesiones(false)

                    coroutineScope.launch {
                        ProveedorPartida.cargarPartida(partida.id)
                    }
                    navController.navigate(Ruta.Partida(partida.id))
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
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
            ) {
                // título
                Text(
                    style = Titulo5Style,
                    text = partida.titulo ?: "Sin título"
                )

                // descripción
                Text(
                    text = partida.descripcion ?: "Sin descripción",
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )

                // número de jugadores
                if (partida.numMinJugadores != null && partida.numMaxJugadores != null) {
                    Text(
                        text = "⟡ De ${partida.numMinJugadores} a ${partida.numMaxJugadores} aventureros.",
                        style = InformacionPrincipalStyle
                    )
                } else if (partida.numMinJugadores != null) {
                    Text(
                        text = "⟡ A partir de ${partida.numMinJugadores} ${if (partida.numMinJugadores == 1) "aventurero" else "aventureros"}.",
                        style = InformacionPrincipalStyle
                    )
                } else if (partida.numMaxJugadores != null) {
                    Text(
                        text = "⟡ Hasta ${partida.numMaxJugadores} ${if (partida.numMaxJugadores == 1) "aventurero" else "aventureros"}.",
                        style = InformacionPrincipalStyle
                    )
                } else {
                    Text(
                        text = "⟡ Sin límite de aventureros.",
                        style = InformacionPrincipalStyle
                    )
                }

                // rango de los jugadores
                val rangoMin = when (partida.rangoMinJugadores) {
                    1 -> "novatos"
                    2 -> "intermedios"
                    3 -> "experimentados"
                    4 -> "legendarios"
                    else -> null
                }
                val rangoMax = when (partida.rangoMaxJugadores) {
                    1 -> "novatos"
                    2 -> "intermedios"
                    3 -> "experimentados"
                    4 -> "legendarios"
                    else -> null
                }
                if (partida.rangoMinJugadores != null && partida.rangoMaxJugadores != null) {
                    Text(
                        text = "⟡ Pueden participar desde aventureros $rangoMin hasta aventureros $rangoMax.",
                        style = InformacionPrincipalStyle
                    )
                } else if (partida.rangoMinJugadores != null) {
                    Text(
                        text = "⟡ Pueden participar aventureros $rangoMin${if(partida.rangoMinJugadores == 4) "" else " o con mayor experiencia"}.",
                        style = InformacionPrincipalStyle
                    )
                } else if (partida.rangoMaxJugadores != null) {
                    Text(
                        text = "⟡ Pueden participar aventureros $rangoMax${if(partida.rangoMaxJugadores == 1) "" else " o con menor experiencia"}.",
                        style = InformacionPrincipalStyle
                    )
                } else {
                    Text(
                        text = "⟡ Sin restricción de experiencia.",
                        style = InformacionPrincipalStyle
                    )
                }
            }
        }
    }
}