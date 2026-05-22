package com.example.dndbonfire.ui.componentes

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.dndbonfire.R
import com.example.dndbonfire.data.DatosUsuario
import com.example.dndbonfire.navegacion.Ruta
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.ui.theme.InformacionPrincipalStyle
import com.example.dndbonfire.ui.theme.InformacionSecundariaStyle
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaUsuarioViewModel
import com.example.dndbonfire.viewmodel.PartidaViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import com.example.dndbonfire.viewmodel.SesionViewModel
import com.example.dndbonfire.viewmodel.TemaApp
import com.example.dndbonfire.viewmodel.UsuarioViewModel

@Composable
fun MenuLateral(
    navController: NavController,
    mainViewModel: MainViewModel,
    partidaViewModel: PartidaViewModel,
    partidaUsuarioViewModel: PartidaUsuarioViewModel,
    sesionViewModel: SesionViewModel,
    usuarioViewModel: UsuarioViewModel,
    personajeViewModel: PersonajeViewModel,
    rutaActual: String?,
    pref: SharedPreferences
) {
    val temaActual = mainViewModel.temaApp.value
    val usuario by mainViewModel.usuarioLogeado
    val mostrarDialogoSeleccionPersonaje by partidaUsuarioViewModel.mostrarDialogoSeleccionPersonaje
    val idsAventurerosABorrar = partidaUsuarioViewModel.idsAventurerosABorrar
    val idsSesionesABorrar = sesionViewModel.idsSesionesABorrar
    val cerrarSesionResultado by mainViewModel.cerrarSesionResultado
    val mostrarDialogo by usuarioViewModel.mostrarDialogoCuenta

    Box() {
        Box(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary
                ),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .width(IntrinsicSize.Max)
        ) {
            if (mainViewModel.usuarioLogeado.value == null) {
                Column() {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CutCornerShape(8.dp)
                            ),
                        colors = CardDefaults
                            .cardColors(
                                contentColor = MaterialTheme.colorScheme.onBackground,
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                        shape = CutCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp, top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = R.drawable.icono,
                                contentDescription = "Logo de D&D Bonfire",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(8.dp),
                                contentScale = ContentScale.Fit
                            )

                            TextButton(
                                modifier = Modifier
                                    .border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CutCornerShape(8.dp)
                                    )
                                    .border(
                                        width = 3.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CutCornerShape(16.dp)
                                    ),
                                colors = ButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledContentColor = MaterialTheme.colorScheme.onBackground,
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    disabledContainerColor = Gris
                                ),
                                shape = CutCornerShape(16.dp),
                                onClick = {
                                    mainViewModel.desplegarMenu(false)
                                    navController.navigate(Ruta.IniciarSesion) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Iniciar sesión"
                                )
                            }
                        }
                    }
                }
            } else {
                Column() {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = {
                                    mainViewModel.desplegarMenu(false)
                                    navController.navigate(Ruta.Cuenta) {
//                                    popUpTo(navController.graph.startDestinationId) {
//                                        saveState = true
//                                    }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CutCornerShape(8.dp)
                            ),
                        colors = CardDefaults
                            .cardColors(
                                contentColor = MaterialTheme.colorScheme.onBackground,
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                        shape = CutCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp, top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AsyncImage(
                                model = usuario?.imagen?.let { "data:image/jpg;base64,$it" } ?: R.drawable.avatar,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(64.dp),
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center
                            )
                            Button(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CutCornerShape(8.dp)
                                    )
                                    .border(
                                        width = 3.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CutCornerShape(16.dp)
                                    ),
                                colors = ButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledContentColor = MaterialTheme.colorScheme.onBackground,
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    disabledContainerColor = Gris
                                ),
                                shape = CutCornerShape(16.dp),
                                onClick = {
                                    if (rutaActual?.endsWith(".CrearPersonaje") == true
                                        || rutaActual?.endsWith(".Personaje/{idPersonaje}") == true
                                        || rutaActual?.endsWith(".CrearPartida") == true
                                        || rutaActual?.endsWith(".Partida/{idPartida}") == true) {
                                        navController.popBackStack()

                                        if (mostrarDialogoSeleccionPersonaje) {
                                            partidaUsuarioViewModel.mostrarDialogoSeleccionPersonaje(false)
                                            navController.popBackStack()
                                        }

                                        if (rutaActual.endsWith(".Personaje/{idPersonaje}") && partidaViewModel.partida.value != null) {
                                            navController.popBackStack()
                                        }
                                    }
                                    // desactiva los tokens
                                    mainViewModel.cerrarSesion()

                                    partidaViewModel.vaciarPartidas()
                                    partidaViewModel.cargarPartida(null)
                                    sesionViewModel.vaciarSesiones()
                                    partidaUsuarioViewModel.vaciarAventureros()
                                    personajeViewModel.vaciarPersonajes()
                                    personajeViewModel.cargarPersonaje(null)
                                    sesionViewModel.modificarCreandoSesion(false)
                                    sesionViewModel.inicializarFecha()
                                    sesionViewModel.modificarDescripcion("")

                                    if (idsAventurerosABorrar.isNotEmpty()) {
                                        partidaUsuarioViewModel.modificarAventurerosABorrar("vaciar", null)
                                    }

                                    if (idsSesionesABorrar.isNotEmpty()) {
                                        sesionViewModel.modificarSesionesABorrar("vaciar", null)
                                    }
                                }
                            ) {
                                Text(text = "Cerrar sesión")
                            }

                            // requiere launched effect para esperar los cambios en la variable eliminarPersonajesMultiplesResultado del view model
                            // este cambio se produce al pulsar el botón
                            LaunchedEffect(cerrarSesionResultado) {
                                when (cerrarSesionResultado) {
                                    "Exito" -> {
                                        mainViewModel.logearUsuario("", "")
                                        mainViewModel.modificarCerrarSesionResultado("")
                                    }
                                    "" -> {
                                        // no hace nada pero tampoco saca el diálogo de error
                                    }
                                    else -> {
                                        usuarioViewModel.mostrarDialogo(true)
                                    }
                                }
                            }
                        }

                        FlowRow(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .align(Alignment.CenterVertically),
                                text = "${mainViewModel.usuarioLogeado.value?.nombre}"
                            )
                            Text(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                text = "@${mainViewModel.usuarioLogeado.value?.username}",
                                style = InformacionSecundariaStyle,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
            )

            Text(
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp),
                text = "Tema de la aplicación",
                style = InformacionPrincipalStyle,
                color = MaterialTheme.colorScheme.onBackground
            )

            Button(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                colors = ButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    disabledContentColor = MaterialTheme.colorScheme.onBackground,
                    containerColor = MaterialTheme.colorScheme.background,
                    disabledContainerColor = Gris
                ),
                shape = RectangleShape,
                onClick = {
                    mainViewModel.cambiarTema(TemaApp.CLARO)
                    DatosUsuario.guardarTema(pref, "claro")
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = temaActual == TemaApp.CLARO,
                        onClick = {
                            mainViewModel.cambiarTema(TemaApp.CLARO)
                            DatosUsuario.guardarTema(pref, "claro")
                        }
                    )
                    Text(
                        text = "Claro",
                        style = InformacionPrincipalStyle,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                colors = ButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    disabledContentColor = MaterialTheme.colorScheme.onBackground,
                    containerColor = MaterialTheme.colorScheme.background,
                    disabledContainerColor = Gris
                ),
                shape = RectangleShape,
                onClick = {
                    mainViewModel.cambiarTema(TemaApp.OSCURO)
                    DatosUsuario.guardarTema(pref, "oscuro")
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = temaActual == TemaApp.OSCURO,
                        onClick = {
                            mainViewModel.cambiarTema(TemaApp.OSCURO)
                            DatosUsuario.guardarTema(pref, "oscuro")
                        }
                    )
                    Text(
                        text = "Oscuro",
                        style = InformacionPrincipalStyle,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                colors = ButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    disabledContentColor = MaterialTheme.colorScheme.onBackground,
                    containerColor = MaterialTheme.colorScheme.background,
                    disabledContainerColor = Gris
                ),
                shape = RectangleShape,
                onClick = {
                    mainViewModel.cambiarTema(TemaApp.SISTEMA)
                    DatosUsuario.guardarTema(pref, "sistema")
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = temaActual == TemaApp.SISTEMA,
                        onClick = {
                            mainViewModel.cambiarTema(TemaApp.SISTEMA)
                            DatosUsuario.guardarTema(pref, "sistema")
                        }
                    )
                    Text(
                        text = "Sistema",
                        style = InformacionPrincipalStyle,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 16.dp)
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "⟡   ⟡   ⟡   ⟡",
                style = InformacionSecundariaStyle
            )
        }
    }

    if (mostrarDialogo) {
        DialogoCuenta(
            resultado = cerrarSesionResultado,
            onDismiss = {
                mainViewModel.modificarCrearCuentaResultado("")
                usuarioViewModel.mostrarDialogo(false)
            }
        )
    }
}