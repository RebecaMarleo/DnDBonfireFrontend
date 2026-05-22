package com.example.dndbonfire.ui.componentes

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.dndbonfire.modelo.Aventurero
import com.example.dndbonfire.modelo.Partida
import com.example.dndbonfire.modelo.Personaje
import com.example.dndbonfire.modelo.Sesion
import com.example.dndbonfire.modelo.Usuario
import com.example.dndbonfire.ui.theme.ButtonStyle
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.ui.theme.InformacionPrincipalStyle
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaUsuarioViewModel
import com.example.dndbonfire.viewmodel.PartidaViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import com.example.dndbonfire.viewmodel.SesionViewModel
import com.example.dndbonfire.viewmodel.UsuarioViewModel

@Composable
fun DialogoConfirmacionBorrado(
    titulo: String,
    mensaje: String,
    usuario: Usuario? = null,
    personaje: Personaje? = null,
    idsPersonajes: List<Int>? = null,
    partida: Partida? = null,
    autoEliminado: Boolean? = null,
    aventurero: Aventurero? = null,
    idsAventureros: List<Int>? = null,
    sesion: Sesion? = null,
    idsSesiones: List<Long>? = null,
    mainViewModel: MainViewModel,
    usuarioViewModel: UsuarioViewModel? = null,
    personajeViewModel: PersonajeViewModel? = null,
    partidaUsuarioViewModel: PartidaUsuarioViewModel? = null,
    sesionViewModel: SesionViewModel? = null,
    partidaViewModel: PartidaViewModel? = null,
    navController: NavController,
    onDismiss: () -> Unit
) {
    val eliminarUsuarioResultado by mainViewModel.eliminarUsuarioResultado
    val eliminarPersonajeResultado = personajeViewModel?.eliminarPersonajeResultado?.value
    val eliminarPersonajesMultiplesResultado = personajeViewModel?.eliminarPersonajesMultiplesResultado?.value
    val eliminarRelacionResultado = partidaUsuarioViewModel?.eliminarRelacionResultado?.value
    val eliminarRelacionesMultiplesResultado = partidaUsuarioViewModel?.eliminarRelacionesMultiplesResultado?.value
    val eliminarPartidaResultado = partidaViewModel?.eliminarPartidaResultado?.value
    val eliminarSesionResultado = sesionViewModel?.eliminarSesionResultado?.value
    val eliminarSesionesMultiplesResultado = sesionViewModel?.eliminarSesionesMultiplesResultado?.value

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
            Column(
                modifier = Modifier
                    .padding(
                        vertical = 12.dp,
                        horizontal = 16.dp
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Precaución",
                            modifier = Modifier
                                .size(36.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = titulo,
                            style = InformacionPrincipalStyle,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .padding(0.dp),
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
                }
                Text(
                    text = mensaje
                )
                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        modifier = Modifier
                            .padding(end = 8.dp)
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
                            mainViewModel.mostrarDialogoConfirmacionBorrado(false)
                        }
                    ) {
                        Text(
                            text = "Cancelar",
                            style = ButtonStyle
                        )
                    }
                    Button(
                        modifier = Modifier
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
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            disabledContentColor = MaterialTheme.colorScheme.onBackground,
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            disabledContainerColor = Gris
                        ),
                        shape = CutCornerShape(16.dp),
                        onClick = {
                            if (usuario != null) {
                                mainViewModel.eliminarUsuario(usuario.id)
                            } else if (idsAventureros != null && idsAventureros.isNotEmpty()) {
                                partidaUsuarioViewModel?.eliminarRelacionesPartidaUsuarioMultiples(partida!!, idsAventureros)
                            } else if (aventurero != null) {
                                partidaUsuarioViewModel?.eliminarRelacionPartidaUsuario(partida!!, aventurero.usuario.id)
                            } else if (idsSesiones != null && idsSesiones.isNotEmpty()) {
                                sesionViewModel?.eliminarSesionesMultiples(idsSesiones)
                            } else if (sesion != null) {
                                sesionViewModel?.eliminarSesion(sesion.id)
                            } else if (idsPersonajes != null && idsPersonajes.isNotEmpty()) {
                                personajeViewModel?.eliminarPersonajesMultiples(idsPersonajes)
                            } else if (personaje != null) {
                                personajeViewModel?.eliminarPersonaje(personaje.id)
                            } else {
                                partidaViewModel?.eliminarPartida(partida!!.id)
                            }
                        }
                    ) {
                        Text(
                            text =
                                if (autoEliminado == true) {
                                    "Abandonar"
                                } else if (aventurero != null || (idsAventureros != null && idsAventureros.isNotEmpty())) {
                                    "Expulsar"
                                } else {
                                    "Borrar"
                                },
                            style = ButtonStyle
                        )
                    }

                    // TODO ponerle LaunchedEffect al resto de salidas de borrado
                    // requiere launched effect para esperar los cambios en la variable eliminarPersonajesMultiplesResultado del view model
                    // este cambio se produce al pulsar el botón
                    LaunchedEffect(eliminarUsuarioResultado) {
                        when (eliminarUsuarioResultado) {
                            "Exito" -> {
                                mainViewModel.modificarEliminarUsuarioResultado("")
                                navController.popBackStack()
                                mainViewModel.mostrarDialogoConfirmacionBorrado(false)
                            }
                            "" -> {
                                // no hace nada pero tampoco saca el diálogo de error
                            }
                            else -> {
                                usuarioViewModel?.mostrarDialogo(true)
                            }
                        }
                    }

                    // requiere launched effect para esperar los cambios en la variable eliminarPersonajeResultado del view model
                    // este cambio se produce al pulsar el botón
                    LaunchedEffect(eliminarPersonajeResultado) {
                        when (eliminarPersonajeResultado) {
                            "Exito" -> {
                                personajeViewModel.modificarPersonajeABorrar(null)
                                personajeViewModel.recargarPersonajes()
                                personajeViewModel.modificarEliminarPersonajeResultado("")
                                mainViewModel.mostrarDialogoConfirmacionBorrado(false)
                            }
                            "" -> {
                                // no hace nada pero tampoco saca el diálogo de error
                            }
                            else -> {
                                personajeViewModel?.mostrarDialogo(true)
                            }
                        }
                    }

                    // requiere launched effect para esperar los cambios en la variable eliminarPersonajesMultiplesResultado del view model
                    // este cambio se produce al pulsar el botón
                    LaunchedEffect(eliminarPersonajesMultiplesResultado) {
                        when (eliminarPersonajesMultiplesResultado) {
                            "Exito" -> {
                                personajeViewModel.modificarPersonajesABorrar("vaciar", null)
                                personajeViewModel.recargarPersonajes()
                                personajeViewModel.modificarEliminarPersonajesMultiplesResultado("")
                                mainViewModel.mostrarDialogoConfirmacionBorrado(false)
                            }
                            "" -> {
                                // no hace nada pero tampoco saca el diálogo de error
                            }
                            else -> {
                                personajeViewModel?.mostrarDialogo(true)
                            }
                        }
                    }

                    // requiere launched effect para esperar los cambios en la variable eliminarRelacionResultado del view model
                    // este cambio se produce al pulsar el botón
                    LaunchedEffect(eliminarRelacionResultado) {
                        when (eliminarRelacionResultado) {
                            "Exito" -> {
                                partidaUsuarioViewModel.modificarAventurerosABorrar("vaciar", null)
                                partidaUsuarioViewModel.modificarAventureroABorrar(null)
                                partidaUsuarioViewModel.recargarAventureros(partida!!)
                                partidaUsuarioViewModel.modificarEliminarRelacionResultado("")
                                mainViewModel.mostrarDialogoConfirmacionBorrado(false)
                            }
                            "" -> {
                                // no hace nada pero tampoco saca el diálogo de error
                            }
                            else -> {
                                partidaViewModel?.mostrarDialogo(true)
                            }
                        }
                    }

                    // requiere launched effect para esperar los cambios en la variable eliminarRelacionesMultiplesResultado del view model
                    // este cambio se produce al pulsar el botón
                    LaunchedEffect(eliminarRelacionesMultiplesResultado) {
                        when (eliminarRelacionesMultiplesResultado) {
                            "Exito" -> {
                                partidaUsuarioViewModel.modificarAventurerosABorrar("vaciar", null)
                                partidaUsuarioViewModel.recargarAventureros(partida!!)
                                partidaUsuarioViewModel.modificarEliminarRelacionesMultiplesResultado("")
                                mainViewModel.mostrarDialogoConfirmacionBorrado(false)
                            }
                            "" -> {
                                // no hace nada pero tampoco saca el diálogo de error
                            }
                            else -> {
                                partidaViewModel?.mostrarDialogo(true)
                            }
                        }
                    }

                    // requiere launched effect para esperar los cambios en la variable eliminarSesionResultado del view model
                    // este cambio se produce al pulsar el botón
                    LaunchedEffect(eliminarSesionResultado) {
                        when (eliminarSesionResultado) {
                            "Exito" -> {
                                sesionViewModel.modificarSesionesABorrar("vaciar", null)
                                sesionViewModel.modificarSesionABorrar(null)
                                sesionViewModel.recargarSesiones(partida!!)
                                sesionViewModel.modificarEliminarSesionResultado("")
                                mainViewModel.mostrarDialogoConfirmacionBorrado(false)
                            }
                            "" -> {
                                // no hace nada pero tampoco saca el diálogo de error
                            }
                            else -> {
                                partidaViewModel?.mostrarDialogo(true)
                            }
                        }
                    }

                    // requiere launched effect para esperar los cambios en la variable eliminarSesionesMultiplesResultado del view model
                    // este cambio se produce al pulsar el botón
                    LaunchedEffect(eliminarSesionesMultiplesResultado) {
                        when (eliminarSesionesMultiplesResultado) {
                            "Exito" -> {
                                sesionViewModel.modificarSesionesABorrar("vaciar", null)
                                sesionViewModel.recargarSesiones(partida!!)
                                sesionViewModel.modificarEliminarSesionesMultiplesResultado("")
                                mainViewModel.mostrarDialogoConfirmacionBorrado(false)
                            }
                            "" -> {
                                // no hace nada pero tampoco saca el diálogo de error
                            }
                            else -> {
                                partidaViewModel?.mostrarDialogo(true)
                            }
                        }
                    }

                    // requiere launched effect para esperar los cambios en la variable eliminarPartidaResultado del view model
                    // este cambio se produce al pulsar el botón
                    LaunchedEffect(eliminarPartidaResultado) {
                        when (eliminarPartidaResultado) {
                            "Exito" -> {
                                partidaUsuarioViewModel?.modificarAventurerosABorrar("vaciar", null)
                                partidaViewModel.recargarPartidas()
                                partidaViewModel.modificarEliminarPartidaResultado("")
                                mainViewModel.mostrarDialogoConfirmacionBorrado(false)
                                navController.popBackStack()
                            }
                            "" -> {
                                // no hace nada pero tampoco saca el diálogo de error
                            }
                            else -> {
                                partidaViewModel?.mostrarDialogo(true)
                            }
                        }
                    }
                }
            }
        }
    }
}