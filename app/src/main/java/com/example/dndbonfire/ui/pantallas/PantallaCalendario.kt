package com.example.dndbonfire.ui.pantallas

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.dndbonfire.R
import com.example.dndbonfire.ui.componentes.ElementoListaSesion
import com.example.dndbonfire.ui.theme.InformacionSecundariaStyle
import com.example.dndbonfire.ui.theme.Titulo4Style
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaUsuarioViewModel
import com.example.dndbonfire.viewmodel.PartidaViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import com.example.dndbonfire.viewmodel.SesionViewModel
import com.example.dndbonfire.viewmodel.UsuarioViewModel
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Locale

@Composable
fun PantallaCalendario(
    navController: NavController,
    mainViewModel: MainViewModel,
    sesionViewModel: SesionViewModel,
    partidaUsuarioViewModel: PartidaUsuarioViewModel,
    partidaViewModel: PartidaViewModel,
    personajeViewModel: PersonajeViewModel,
    usuarioViewModel: UsuarioViewModel
) {
    LaunchedEffect("") {
        partidaViewModel.cargarPartida(null)
    }

    val usuario by mainViewModel.usuarioLogeado
    val sesionesHoy = sesionViewModel.sesionesHoy
    val sesionesEstaSemana = sesionViewModel.sesionesEstaSemana
    val sesionesEsteMes = sesionViewModel.sesionesEsteMes
    val sesionesMasDeMes = sesionViewModel.sesionesMasDeMes
    val sinSesionesCalendario by sesionViewModel.sinSesionesCalendario
    var desplegadoHoy by remember { mutableStateOf(true) }
    var desplegadoSemana by remember { mutableStateOf(true) }
    var desplegadoMes by remember { mutableStateOf(true) }
    var desplegadoMasDeMes by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (usuario == null) {
            AsyncImage(
                model = R.drawable.iniciarsesion,
                contentDescription = "Don Fire apunta menú",
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = "Inicia sesión para ver tu calendario de sesiones.",
                style = InformacionSecundariaStyle,
                textAlign = TextAlign.Center
            )
        } else if (sinSesionesCalendario) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = "Tu calendario de sesiones está vacío. ¡Únete a alguna partida en curso para empezar a rellenarlo!",
                style = InformacionSecundariaStyle,
                textAlign = TextAlign.Center
            )
        } else if (sesionesHoy.isEmpty() && sesionesEstaSemana.isEmpty() && sesionesEsteMes.isEmpty() && sesionesMasDeMes.isEmpty()) {
            AsyncImage(
                model = R.drawable.cargando,
                contentDescription = "GIF de carga",
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                text = "Cargando tus sesiones...",
                style = InformacionSecundariaStyle,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
                text = "Si tienes conexión a Internet y esta operación tarda demasiado es posible que el servidor no esté operativo. Lamentamos las molestias.",
                style = InformacionSecundariaStyle
            )
            sesionViewModel.cargarSesionesCalendario()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // hoy
                item {
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = {
                                    desplegadoHoy = !desplegadoHoy
                                }
                            )
                            .align(Alignment.Start),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier
                                .width(IntrinsicSize.Max),
                            text = "Hoy",
                            color = MaterialTheme.colorScheme.secondary,
                            style = Titulo4Style
                        )
                        Icon(
                            imageVector = if (desplegadoHoy) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Desplegar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                if (desplegadoHoy) {
                    if (sesionesHoy.isEmpty()) {
                        item { Text("No tienes ninguna sesión para hoy.") }
                    } else {
                        itemsIndexed(sesionesHoy) { indice, sesion ->
                            ElementoListaSesion(sesion, navController, mainViewModel, partidaViewModel, partidaUsuarioViewModel, usuarioViewModel, personajeViewModel, sesionViewModel)
                        }
                    }
                }

                // esta semana
                item {
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = {
                                    desplegadoSemana = !desplegadoSemana
                                }
                            )
                            .align(Alignment.Start),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier
                                .width(IntrinsicSize.Max),
                            text = "Esta semana",
                            color = MaterialTheme.colorScheme.secondary,
                            style = Titulo4Style
                        )
                        Icon(
                            imageVector = if (desplegadoSemana) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Desplegar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                if (desplegadoSemana) {
                    if (sesionesEstaSemana.isEmpty()) {
                        item { Text("No tienes ninguna sesión para esta semana.") }
                    } else {
                        itemsIndexed(sesionesEstaSemana) { indice, sesion ->
                            ElementoListaSesion(sesion, navController, mainViewModel, partidaViewModel, partidaUsuarioViewModel, usuarioViewModel, personajeViewModel, sesionViewModel)
                        }
                    }
                }

                // este mes
                item {
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = {
                                    desplegadoMes = !desplegadoMes
                                }
                            )
                            .align(Alignment.Start),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier
                                .width(IntrinsicSize.Max),
                            text = DateFormatSymbols(Locale("es", "ES")).months[Calendar.getInstance().get(Calendar.MONTH)]
                                .replaceFirstChar { it.uppercase() },
                            color = MaterialTheme.colorScheme.secondary,
                            style = Titulo4Style
                        )
                        Icon(
                            imageVector = if (desplegadoMes) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Desplegar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                if (desplegadoMes) {
                    if (sesionesEsteMes.isEmpty()) {
                        item { Text("No tienes ninguna sesión para este mes.") }
                    } else {
                        itemsIndexed(sesionesEsteMes) { indice, sesion ->
                            ElementoListaSesion(sesion, navController, mainViewModel, partidaViewModel, partidaUsuarioViewModel, usuarioViewModel, personajeViewModel, sesionViewModel)
                        }
                    }
                }

                // a partir de este mes
                item {
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = {
                                    desplegadoMasDeMes = !desplegadoMasDeMes
                                }
                            )
                            .align(Alignment.Start),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier
                                .width(IntrinsicSize.Max),
                            text = "En más de un mes",
                            color = MaterialTheme.colorScheme.secondary,
                            style = Titulo4Style
                        )
                        Icon(
                            imageVector = if (desplegadoMasDeMes) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Desplegar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                if (desplegadoMasDeMes) {
                    if (sesionesMasDeMes.isEmpty()) {
                        item { Text("No tienes ninguna sesión a futuro.") }
                    } else {
                        itemsIndexed(sesionesMasDeMes) { indice, sesion ->
                            ElementoListaSesion(sesion, navController, mainViewModel, partidaViewModel, partidaUsuarioViewModel, usuarioViewModel, personajeViewModel, sesionViewModel)
                        }
                    }
                }
            }
            Text(text = "Hoy")
            sesionesHoy.forEach { sesion ->
                ElementoListaSesion(sesion, navController, mainViewModel, partidaViewModel, partidaUsuarioViewModel, usuarioViewModel, personajeViewModel, sesionViewModel)
            }
            Text(text = "Esta semana")
            sesionesEstaSemana.forEach { sesion ->
                ElementoListaSesion(sesion, navController, mainViewModel, partidaViewModel, partidaUsuarioViewModel, usuarioViewModel, personajeViewModel, sesionViewModel)
            }
            Text(text = "Este mes")
            sesionesEsteMes.forEach { sesion ->
                ElementoListaSesion(sesion, navController, mainViewModel, partidaViewModel, partidaUsuarioViewModel, usuarioViewModel, personajeViewModel, sesionViewModel)
            }
            Text(text = "En más de un mes")
            sesionesMasDeMes.forEach { sesion ->
                ElementoListaSesion(sesion, navController, mainViewModel, partidaViewModel, partidaUsuarioViewModel, usuarioViewModel, personajeViewModel, sesionViewModel)
            }
        }
    }
}