package com.example.dndbonfire.ui.componentes

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dndbonfire.data.ProveedorPartida
import com.example.dndbonfire.modelo.Sesion
import com.example.dndbonfire.navegacion.Ruta
import com.example.dndbonfire.ui.theme.Titulo5Style
import com.example.dndbonfire.util.ConversorUnix
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaUsuarioViewModel
import com.example.dndbonfire.viewmodel.PartidaViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import com.example.dndbonfire.viewmodel.SesionViewModel
import com.example.dndbonfire.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

@Composable
fun ElementoListaSesion(
    sesion: Sesion,
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
    val partidas = partidaViewModel.partidasUnidas
    val usuarioLogeado by mainViewModel.usuarioLogeado

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

    // estas variables son necesarias para actualizar la interfaz sin recargar la página
    var asiste by remember { mutableStateOf(false) }
    sesion.jugadoresApuntados.forEach { idJugador ->
        if (idJugador == usuarioLogeado?.id) {
            asiste = true
        }
    }
    var jugadoresTotalesApuntados = sesion.jugadoresApuntados.size
    // se actualiza con asiste
    if (asiste) {
        jugadoresTotalesApuntados = sesion.jugadoresApuntados.size
    }

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
                        ProveedorPartida.cargarPartida(sesion.idPartida)
                    }
                    navController.navigate(Ruta.Partida(sesion.idPartida))
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
                // título de la partida a la que pertenece la sesión
                Text(
                    style = Titulo5Style,
                    text = partidas.firstOrNull { partida ->
                        partida.id == sesion.idPartida
                    }?.titulo ?: "Sin título"
                )

                // fecha
                Text(
                    text = "Fecha: ${ConversorUnix.fechaYMDHMSaFechaDMYHMS(sesion.fecha)}",
                )

                // asistencias
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (System.currentTimeMillis() < ConversorUnix.fechaYMDHMSConTAMilis(sesion.fecha)) {
                        Text(
                            text =
                                if (jugadoresTotalesApuntados == 1)
                                    "⟡ Se ha apuntado un aventurero."
                                else
                                    "⟡ Se han apuntado $jugadoresTotalesApuntados aventureros.",
                            fontSize = 14.sp
                        )
                    } else {
                        Text(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
                            text = if (sesion.jugadoresApuntados.size == 1) "Asistió un aventurero." else "Asistieron ${sesion.jugadoresApuntados.size} aventureros.",
                            fontSize = 14.sp
                        )
                    }
                    // icono para apuntarse
                    IconButton(
                        modifier = Modifier
                            .clip(CutCornerShape(30.dp)),
                        colors = IconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary,
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        onClick = {
                            if (asiste) {
                                sesionViewModel.modificarJugadoresApuntados(sesion, "eliminar", usuarioLogeado!!.id)
                                Toast.makeText(contexto, "Cancelaste tu asistencia a esta sesión", Toast.LENGTH_SHORT).show()
                            } else {
                                sesionViewModel.modificarJugadoresApuntados(sesion, "añadir", usuarioLogeado!!.id)
                                Toast.makeText(contexto, "Confirmaste tu asistencia a esta sesión", Toast.LENGTH_SHORT).show()
                            }
                            asiste = !asiste
                        }
                    ) {
                        Icon(
                            imageVector = if (asiste) Icons.Default.Clear else Icons.Default.Check,
                            contentDescription = if (asiste) "Cancelar asistencia" else "Confirmar asistencia",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}