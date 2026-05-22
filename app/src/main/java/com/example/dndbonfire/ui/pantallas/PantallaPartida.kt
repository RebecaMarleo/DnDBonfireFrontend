package com.example.dndbonfire.ui.pantallas

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.dndbonfire.R
import com.example.dndbonfire.modelo.Aventurero
import com.example.dndbonfire.ui.componentes.DatePickerModal
import com.example.dndbonfire.ui.componentes.DialogoConfirmacionBorrado
import com.example.dndbonfire.ui.componentes.DialogoPartida
import com.example.dndbonfire.ui.componentes.DialogoSeleccionPersonaje
import com.example.dndbonfire.ui.componentes.ElementoPartidaPersonaje
import com.example.dndbonfire.ui.componentes.ElementoPartidaSesion
import com.example.dndbonfire.ui.componentes.ElementoPartidaUsuario
import com.example.dndbonfire.ui.theme.ButtonStyle
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.ui.theme.InformacionPrincipalStyle
import com.example.dndbonfire.ui.theme.InformacionSecundariaStyle
import com.example.dndbonfire.ui.theme.Titulo2Style
import com.example.dndbonfire.ui.theme.Titulo4Style
import com.example.dndbonfire.util.ConversorUnix
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaUsuarioViewModel
import com.example.dndbonfire.viewmodel.PartidaViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import com.example.dndbonfire.viewmodel.SesionViewModel
import com.example.dndbonfire.viewmodel.UsuarioViewModel

@Composable
fun PantallaPartida(
    navController: NavController,
    idPartida: Int,
    mainViewModel: MainViewModel,
    partidaViewModel: PartidaViewModel,
    partidaUsuarioViewModel: PartidaUsuarioViewModel,
    usuarioViewModel: UsuarioViewModel,
    personajeViewModel: PersonajeViewModel,
    sesionViewModel: SesionViewModel
) {
    LaunchedEffect(idPartida) {
        partidaViewModel.cargarPartida(idPartida)
    }

    val partida by partidaViewModel.partida
    val usuarioLogeado by mainViewModel.usuarioLogeado
    val usuarioDM by usuarioViewModel.usuarioDM
    val aventureros = partidaUsuarioViewModel.aventureros
    val sinAventureros by partidaUsuarioViewModel.sinAventureros
    val personajes = personajeViewModel.personajes
    val sinPersonajes by personajeViewModel.sinPersonajes
    val aventureroABorrar by partidaUsuarioViewModel.aventureroABorrar
    val idsAventurerosABorrar = partidaUsuarioViewModel.idsAventurerosABorrar
    val mostrarDialogoSeleccionPersonaje by partidaUsuarioViewModel.mostrarDialogoSeleccionPersonaje
    val mostrarDialogoConfirmacionBorrado by mainViewModel.mostrarDialogoConfirmacionBorrado
    val crearRelacionResultado by partidaUsuarioViewModel.crearRelacionResultado
    val eliminarRelacionResultado by partidaUsuarioViewModel.eliminarRelacionResultado
    val eliminarRelacionesResultado by partidaUsuarioViewModel.eliminarRelacionesMultiplesResultado
    val modificarRelacionResultado by partidaUsuarioViewModel.modificarRelacionResultado
    val eliminarPartidaResultado by partidaViewModel.eliminarPartidaResultado
    val modificarPartidaResultado by partidaViewModel.modificarPartidaResultado
    val autoEliminado by partidaUsuarioViewModel.autoEliminado
    val sinSesiones by sesionViewModel.sinSesiones
    val sesionesProgramadas = sesionViewModel.sesionesProgramadas
    val sesionesPasadas = sesionViewModel.sesionesPasadas
    val creandoSesion by sesionViewModel.creandoSesion
    val crearSesionResultado by sesionViewModel.crearSesionResultado
    val modificarSesionResultado by sesionViewModel.modificarSesionResultado
    val idsSesionesABorrar = sesionViewModel.idsSesionesABorrar
    val sesionABorrar by sesionViewModel.sesionABorrar

    // para el modo edición
    val titulo by partidaViewModel.titulo
    val descripcion by partidaViewModel.descripcion
    val numMinJugadores by partidaViewModel.numMinJugadores
    val numMaxJugadores by partidaViewModel.numMaxJugadores
    val rangoMinJugadores by partidaViewModel.rangoMinJugadores
    val rangoMaxJugadores by partidaViewModel.rangoMaxJugadores
    val tipoFecha by partidaViewModel.tipoFecha
    val fechaInicio by partidaViewModel.fechaInicio
    val fechaFinalizacion by partidaViewModel.fechaFinalizacion
    val finalizada by partidaViewModel.finalizada
    val privada by partidaViewModel.privada
    val infoVisible by partidaViewModel.infoVisible
    val mostrarDialogo by partidaViewModel.mostrarDialogo
    val mostrarDatePicker by partidaViewModel.mostrarDatePicker
    val modoEdicion by partidaViewModel.modoEdicion

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (partida == null) {
            // si aun no se ha cargado la partida se queda cargando
            AsyncImage(
                model = R.drawable.cargando,
                contentDescription = "GIF de carga",
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Fit
            )
            Text (
                text = "Buscando partida...",
                style = InformacionSecundariaStyle
            )
        } else if (usuarioDM == null) {
            // si aun no se ha cargado la partida se queda cargando
            AsyncImage(
                model = R.drawable.cargando,
                contentDescription = "GIF de carga",
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Fit
            )
            Text (
                text = "Cargando datos del director de juego...",
                style = InformacionSecundariaStyle
            )
            usuarioViewModel.cargarUsuarioDM(partida!!.idDM)
        } else if (!sinAventureros && aventureros.isEmpty()) {
            // si aun no se ha cargado la partida se queda cargando
            AsyncImage(
                model = R.drawable.cargando,
                contentDescription = "GIF de carga",
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Fit
            )
            Text (
                text = "Cargando datos de los aventureros...",
                style = InformacionSecundariaStyle
            )
            partidaUsuarioViewModel.recargarAventureros(partida!!)
        } else if (!sinSesiones && sesionesProgramadas.isEmpty() && sesionesPasadas.isEmpty()) {
            // si aun no se ha cargado la partida se queda cargando
            AsyncImage(
                model = R.drawable.cargando,
                contentDescription = "GIF de carga",
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Fit
            )
            Text (
                text = "Cargando datos de las sesiones...",
                style = InformacionSecundariaStyle
            )
            sesionViewModel.recargarSesiones(partida!!)
        } else if (!sinPersonajes && personajes.isEmpty()) {
            // si aun no se ha cargado la partida se queda cargando
            AsyncImage(
                model = R.drawable.cargando,
                contentDescription = "GIF de carga",
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Fit
            )
            Text (
                text = "Cargando datos de los personajes...",
                style = InformacionSecundariaStyle
            )
            personajeViewModel.cargarPersonajes()
        } else {
            if (!modoEdicion) {
                // titulo de la partida
                Text(
                    modifier = Modifier
                        .padding(
                            top = 8.dp,
                            start = 8.dp,
                            end = 8.dp
                        ),
                    textAlign = TextAlign.Center,
                    text = partida?.titulo ?: "Sin título",
                    style = Titulo2Style,
                    color = MaterialTheme.colorScheme.primary
                )

                // descripción
                if (partida?.descripcion != null) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp, 0.dp)
                            .align(Alignment.Start),
                        text = partida?.descripcion!!,
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .padding(8.dp, 0.dp),
                        textAlign = TextAlign.Center,
                        text = "No se ha incluido una descripción.",
                        style = InformacionSecundariaStyle
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(IntrinsicSize.Max),
                    textAlign = TextAlign.Center,
                    text = "Información",
                    style = Titulo4Style,
                    color = MaterialTheme.colorScheme.secondary
                )

                // número de jugadores
                if (partida!!.numMinJugadores != null && partida!!.numMaxJugadores != null) {
                    Text(
                        text = "De ${partida!!.numMinJugadores} a ${partida!!.numMaxJugadores} aventureros.",
                        style = InformacionPrincipalStyle,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .align(Alignment.Start)
                    )
                } else if (partida!!.numMinJugadores != null) {
                    Text(
                        text = "A partir de ${partida!!.numMinJugadores} ${if (partida!!.numMinJugadores == 1) "aventurero" else "aventureros"}.",
                        style = InformacionPrincipalStyle,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .align(Alignment.Start)
                    )
                } else if (partida!!.numMaxJugadores != null) {
                    Text(
                        text = "Hasta ${partida!!.numMaxJugadores} ${if (partida!!.numMaxJugadores == 1) "aventurero" else "aventureros"}.",
                        style = InformacionPrincipalStyle,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .align(Alignment.Start)
                    )
                } else {
                    Text(
                        text = "Sin límite de aventureros.",
                        style = InformacionPrincipalStyle,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .align(Alignment.Start)
                    )
                }

                // rango de los jugadores
                val rangoMin = when (partida!!.rangoMinJugadores) {
                    1 -> "novatos"
                    2 -> "intermedios"
                    3 -> "experimentados"
                    4 -> "legendarios"
                    else -> null
                }
                val rangoMax = when (partida!!.rangoMaxJugadores) {
                    1 -> "novatos"
                    2 -> "intermedios"
                    3 -> "experimentados"
                    4 -> "legendarios"
                    else -> null
                }
                if (partida!!.rangoMinJugadores != null && partida!!.rangoMaxJugadores != null) {
                    Text(
                        text = "Pueden participar desde aventureros $rangoMin hasta aventureros $rangoMax.",
                        style = InformacionPrincipalStyle,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .align(Alignment.Start)
                    )
                } else if (partida!!.rangoMinJugadores != null) {
                    Text(
                        text = "Pueden participar aventureros $rangoMin${if(partida!!.rangoMinJugadores == 4) "" else " o con mayor experiencia"}.",
                        style = InformacionPrincipalStyle,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .align(Alignment.Start)
                    )
                } else if (partida!!.rangoMaxJugadores != null) {
                    Text(
                        text = "Pueden participar aventureros $rangoMax${if(partida!!.rangoMinJugadores == 1) "" else " o con menor experiencia"}.",
                        style = InformacionPrincipalStyle,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .align(Alignment.Start)
                    )
                } else {
                    Text(
                        text = "Sin restricción de experiencia.",
                        style = InformacionPrincipalStyle,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .align(Alignment.Start)
                    )
                }

                // fecha de inicio
                if (partida!!.fechaInicio != null) {
                    Text(
                        text = if (System.currentTimeMillis() > ConversorUnix.fechaYMDaMilis(partida!!.fechaInicio)!!) {
                            "La partida inició el ${ConversorUnix.fechaYMDaFechaDMY(partida!!.fechaInicio!!)}."
                        } else {
                            "La partida iniciará el ${ConversorUnix.fechaYMDaFechaDMY(partida!!.fechaInicio!!)}."
                        },
                        style = InformacionPrincipalStyle,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .align(Alignment.Start)
                    )
                }

                // fecha de finalización
                if (partida!!.fechaFinalizacion != null) {
                    Text(
                        text = if (System.currentTimeMillis() > ConversorUnix.fechaYMDaMilis(partida!!.fechaFinalizacion)!!) {
                            "La partida finalizó el ${ConversorUnix.fechaYMDaFechaDMY(partida!!.fechaFinalizacion!!)}."
                        } else {
                            "La partida finalizará el ${ConversorUnix.fechaYMDaFechaDMY(partida!!.fechaFinalizacion!!)}."
                        },
                        style = InformacionPrincipalStyle,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .align(Alignment.Start)
                    )
                }

                // DM
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(IntrinsicSize.Max),
                    textAlign = TextAlign.Center,
                    text = "Director del juego",
                    style = Titulo4Style,
                    color = MaterialTheme.colorScheme.secondary
                )
                ElementoPartidaUsuario(usuarioDM!!)

                // jugadores
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(IntrinsicSize.Max),
                    textAlign = TextAlign.Center,
                    text = "Aventureros",
                    style = Titulo4Style,
                    color = MaterialTheme.colorScheme.secondary
                )
                if (aventureros.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .padding(bottom = 4.dp),
                        text = "¡Sé el primero en unirte a la aventura!"
                    )
                } else {
                    aventureros.forEach { aventurero ->
                        ElementoPartidaPersonaje(aventurero, navController, mainViewModel, partidaUsuarioViewModel, partidaViewModel, personajeViewModel)
                    }
                }

                // botón para unirse/abandonar la partida solo visible si no se es el dm
                if (usuarioLogeado?.id != usuarioDM!!.id) {
                    var participa = false
                    aventureros.forEach { aventurero ->
                        if (aventurero.key.id == usuarioLogeado?.id) {
                            participa = true
                        }
                    }

                    // solo se muestra si ya participa o si aun hay huecos restantes
                    if (participa || partida!!.numMaxJugadores == null || (partida!!.numMaxJugadores != null && aventureros.size < partida!!.numMaxJugadores!!)) {
                        Button(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .fillMaxWidth()
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
                                if (participa) {
                                    aventureros.forEach { a ->
                                        if (a.key.id == usuarioLogeado?.id) {
                                            partidaUsuarioViewModel.modificarAventureroABorrar(
                                                Aventurero(
                                                    a.key,
                                                    a.value
                                                )
                                            )
                                        }
                                    }
                                    partidaUsuarioViewModel.modificarAutoEliminado(true)
                                    mainViewModel.mostrarDialogoConfirmacionBorrado(true)
                                } else {
                                    partidaUsuarioViewModel.mostrarDialogoSeleccionPersonaje(true)
                                }
                                // recarga el calendario de sesiones
                                sesionViewModel.recargarSesionesCalendario(usuarioLogeado!!)
                            }
                        ) {
                            Text(
                                text = if (participa) "Abandonar la campaña" else "Unirse a la campaña",
                                style = ButtonStyle
                            )
                        }
                    }
                }

                if (mostrarDialogoSeleccionPersonaje) {
                    DialogoSeleccionPersonaje(
                        partida = partida!!,
                        navController = navController,
                        mainViewModel = mainViewModel,
                        partidaUsuarioViewModel = partidaUsuarioViewModel,
                        personajeViewModel = personajeViewModel,
                        onDismiss = {
                            partidaUsuarioViewModel.seleccionarPersonaje(null)
                            partidaUsuarioViewModel.mostrarDialogoSeleccionPersonaje(false)
                        }
                    )
                }

                // requiere launched effect para esperar los cambios en la variable crearRelacionResultado del view model
                // este cambio se produce al pulsar el botón
                LaunchedEffect(crearRelacionResultado) {
                    when (crearRelacionResultado) {
                        "Exito" -> {
                            partidaUsuarioViewModel.modificarCrearRelacionResultado("")
                            partidaUsuarioViewModel.seleccionarPersonaje(null)
                            partidaUsuarioViewModel.mostrarDialogoSeleccionPersonaje(false)

                            partidaUsuarioViewModel.recargarAventureros(partida!!)
                        }
                        "" -> {
                            // no hace nada pero tampoco saca el diálogo de error
                        }
                        else -> {
                            partidaUsuarioViewModel.seleccionarPersonaje(null)
                            partidaUsuarioViewModel.mostrarDialogoSeleccionPersonaje(false)
                            partidaViewModel.mostrarDialogo(true)

                            partidaUsuarioViewModel.recargarAventureros(partida!!)
                        }
                    }
                }

                // requiere launched effect para esperar los cambios en la variable modificarRelacionResultado del view model
                // este cambio se produce al pulsar el botón
                LaunchedEffect(modificarRelacionResultado) {
                    when (modificarRelacionResultado) {
                        "Exito" -> {
                            partidaUsuarioViewModel.modificarModificandoRelacionPartidaUsuario(false)
                            partidaUsuarioViewModel.modificarModificarRelacionResultado("")
                            partidaUsuarioViewModel.seleccionarPersonaje(null)
                            partidaUsuarioViewModel.mostrarDialogoSeleccionPersonaje(false)

                            partidaUsuarioViewModel.recargarAventureros(partida!!)
                        }
                        "" -> {
                            // no hace nada pero tampoco saca el diálogo de error
                        }
                        else -> {
                            partidaUsuarioViewModel.modificarModificandoRelacionPartidaUsuario(false)
                            partidaUsuarioViewModel.seleccionarPersonaje(null)
                            partidaUsuarioViewModel.mostrarDialogoSeleccionPersonaje(false)
                            partidaViewModel.mostrarDialogo(true)

                            partidaUsuarioViewModel.recargarAventureros(partida!!)
                        }
                    }
                }

                // Sesiones
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(IntrinsicSize.Max),
                    textAlign = TextAlign.Center,
                    text = "Sesiones programadas",
                    style = Titulo4Style,
                    color = MaterialTheme.colorScheme.secondary
                )
                Column() {
                    if (sesionesProgramadas.isEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 4.dp),
                            text = "Actualmente no hay ninguna sesión planeada."
                        )
                    } else {
                        sesionesProgramadas.forEach { sesion ->
                            ElementoPartidaSesion(sesion, partida!!, navController, mainViewModel, partidaViewModel, partidaUsuarioViewModel, sesionViewModel)
                        }
                    }
                }
                // botón para crear una sesión nueva solo visible si se es el dm, si no se ha establecido un mínimo de jugadores o si el mínimo es igual o superior a la cantidad actual de jugadores
                if (usuarioLogeado?.id == usuarioDM!!.id && (partida!!.numMinJugadores == null || (partida!!.numMinJugadores != null && aventureros.size >= partida!!.numMinJugadores!!))) {
                    if (!creandoSesion) {
                        Button(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .fillMaxWidth()
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
                                sesionViewModel.modificarCreandoSesion(true)
                            }
                        ) {
                            Text(
                                text = "Programar sesión",
                                style = ButtonStyle
                            )
                        }
                    } else {
                        ElementoPartidaSesion(null, partida!!, navController, mainViewModel, partidaViewModel, partidaUsuarioViewModel, sesionViewModel)

                        // requiere launched effect para esperar los cambios en la variable crearSesionResultado del view model
                        // este cambio se produce al pulsar el botón
                        LaunchedEffect(crearSesionResultado) {
                            when (crearSesionResultado) {
                                "Exito" -> {
                                    sesionViewModel.modificarCreandoSesion(false)
                                    sesionViewModel.modificarCrearSesionResultado("")
                                    sesionViewModel.inicializarFecha()
                                    sesionViewModel.modificarDescripcion("")

                                    sesionViewModel.recargarSesiones(partida!!)
                                }
                                "" -> {
                                    // no hace nada pero tampoco saca el diálogo de error
                                }
                                else -> {
                                    partidaViewModel.mostrarDialogo(true)
                                }
                            }
                        }
                    }
                }
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(IntrinsicSize.Max),
                    textAlign = TextAlign.Center,
                    text = "Sesiones pasadas",
                    style = Titulo4Style,
                    color = MaterialTheme.colorScheme.secondary
                )
                Column() {
                    if (sesionesPasadas.isEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 4.dp),
                            text = "Esta campaña no tiene aventuras registradas."
                        )
                    } else {
                        sesionesPasadas.forEach { sesion ->
                            ElementoPartidaSesion(sesion, partida!!, navController, mainViewModel, partidaViewModel, partidaUsuarioViewModel, sesionViewModel)
                        }
                    }
                }
                // TODO

                if (usuarioLogeado?.id == usuarioDM!!.id) {
                    Button(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
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
                            mainViewModel.mostrarDialogoConfirmacionBorrado(true)
                        }
                    ) {
                        Text(
                            text = "Borrar campaña",
                            style = ButtonStyle
                        )
                    }
                }
            }
            else {
                // título
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 4.dp),
                    value = titulo,
                    onValueChange = { partidaViewModel.modificarTitulo(it) },
                    label = { Text(text = "Título") },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                        cursorColor = MaterialTheme.colorScheme.secondary,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = CutCornerShape(16.dp),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )

                // descripción
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp, 4.dp),
                    value = descripcion,
                    onValueChange = { partidaViewModel.modificarDescripcion(it) },
                    label = { Text(text = "Descripción") },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                        cursorColor = MaterialTheme.colorScheme.secondary,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = CutCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )

                // restricciones sobre jugadores
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(IntrinsicSize.Max),
                    textAlign = TextAlign.Center,
                    text = "Jugadores",
                    style = Titulo4Style,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text = "Deja estos campos en blanco si no quieres establecer restricciones por cantidad de jugadores o su nivel de experiencia en tu partida.",
                    style = InformacionSecundariaStyle
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // cantidad
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Cantidad"
                        )
                        Row() {
                            OutlinedTextField(
                                modifier = Modifier
                                    .width(120.dp)
                                    .padding(end = 8.dp),
                                value = numMaxJugadores.toString(),
                                onValueChange = { try {
                                    var nuevoValor = it
                                    if (numMaxJugadores == 0) {
                                        nuevoValor = it.replace("0", "")
                                    }
                                    // no permite que baje de 0
                                    if (Integer.valueOf(nuevoValor) < 0) {
                                        nuevoValor = "0"
                                    }
                                    partidaViewModel.modificarNumMaxJugadores(Integer.valueOf(nuevoValor))
                                } catch (e: Exception) {
                                    partidaViewModel.modificarNumMaxJugadores(0)
                                }},
                                label = { Text(text = "Máximo") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                                    focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                                    cursorColor = MaterialTheme.colorScheme.secondary,
                                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                    unfocusedTextColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = CutCornerShape(16.dp),
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                )
                            )
                            Column() {
                                IconButton(
                                    modifier = Modifier
                                        .height(30.dp)
                                        .width(30.dp),
                                    onClick = {
                                        val nuevoValor = numMaxJugadores+1
                                        partidaViewModel.modificarNumMaxJugadores(Integer.valueOf(nuevoValor))
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Aumentar un nivel",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .padding(0.dp)
                                            .size(30.dp),
                                    )
                                }
                                IconButton(
                                    modifier = Modifier
                                        .height(30.dp)
                                        .width(30.dp),
                                    onClick = {
                                        var nuevoValor = numMaxJugadores-1
                                        // no permite que baje de 0
                                        if (Integer.valueOf(nuevoValor) < 0) {
                                            nuevoValor = 0
                                        }
                                        partidaViewModel.modificarNumMaxJugadores(Integer.valueOf(nuevoValor))
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Disminuir un nivel",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .padding(0.dp)
                                            .size(30.dp)
                                    )
                                }
                            }
                        }

                        Row() {
                            OutlinedTextField(
                                modifier = Modifier
                                    .width(120.dp)
                                    .padding(end = 8.dp),
                                value = numMinJugadores.toString(),
                                onValueChange = { try {
                                    var nuevoValor = it
                                    if (numMinJugadores == 0) {
                                        nuevoValor = it.replace("0", "")
                                    }
                                    // no permite que baje de 0
                                    if (Integer.valueOf(nuevoValor) < 0) {
                                        nuevoValor = "0"
                                    }
                                    partidaViewModel.modificarNumMinJugadores(Integer.valueOf(nuevoValor))
                                } catch (e: Exception) {
                                    partidaViewModel.modificarNumMinJugadores(0)
                                }},
                                label = { Text(text = "Mínimo") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                                    focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                                    cursorColor = MaterialTheme.colorScheme.secondary,
                                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                    unfocusedTextColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = CutCornerShape(16.dp),
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                )
                            )
                            Column() {
                                IconButton(
                                    modifier = Modifier
                                        .height(30.dp)
                                        .width(30.dp),
                                    onClick = {
                                        var nuevoValor = numMinJugadores+1
                                        // no permite que baje de 0
                                        if (Integer.valueOf(nuevoValor) < 0) {
                                            nuevoValor = 0
                                        }
                                        partidaViewModel.modificarNumMinJugadores(Integer.valueOf(nuevoValor))
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Aumentar un nivel",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .padding(0.dp)
                                            .size(30.dp),
                                    )
                                }
                                IconButton(
                                    modifier = Modifier
                                        .height(30.dp)
                                        .width(30.dp),
                                    onClick = {
                                        var nuevoValor = numMinJugadores-1
                                        // no permite que baje de 0
                                        if (Integer.valueOf(nuevoValor) < 0) {
                                            nuevoValor = 0
                                        }
                                        partidaViewModel.modificarNumMinJugadores(Integer.valueOf(nuevoValor))
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Disminuir un nivel",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .padding(0.dp)
                                            .size(30.dp)
                                    )
                                }
                            }
                        }
                    }
                    // rango
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Rango")
                        Row() {
                            OutlinedTextField(
                                modifier = Modifier
                                    .width(120.dp)
                                    .padding(end = 8.dp),
                                value = rangoMaxJugadores.toString(),
                                onValueChange = { try {
                                    var nuevoValor = it
                                    if (rangoMaxJugadores == 0) {
                                        nuevoValor = it.replace("0", "")
                                    }
                                    // no permite que supere 4 como valor
                                    if (Integer.valueOf(nuevoValor) > 4) {
                                        nuevoValor = "4"
                                    }
                                    // no permite que baje de 0
                                    if (Integer.valueOf(nuevoValor) < 0) {
                                        nuevoValor = "0"
                                    }
                                    partidaViewModel.modificarRangoMaxJugadores(Integer.valueOf(nuevoValor))
                                } catch (e: Exception) {
                                    partidaViewModel.modificarRangoMaxJugadores(0)
                                }},
                                label = { Text(text = "Máximo") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                                    focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                                    cursorColor = MaterialTheme.colorScheme.secondary,
                                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                    unfocusedTextColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = CutCornerShape(16.dp),
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                )
                            )
                            Column() {
                                IconButton(
                                    modifier = Modifier
                                        .height(30.dp)
                                        .width(30.dp),
                                    onClick = {
                                        var nuevoValor = rangoMaxJugadores+1
                                        // no permite que supere 4 como valor
                                        if (Integer.valueOf(nuevoValor) > 4) {
                                            nuevoValor = 4
                                        }
                                        // no permite que baje de 0
                                        if (Integer.valueOf(nuevoValor) < 0) {
                                            nuevoValor = 0
                                        }
                                        partidaViewModel.modificarRangoMaxJugadores(Integer.valueOf(nuevoValor))
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Aumentar un nivel",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .padding(0.dp)
                                            .size(30.dp),
                                    )
                                }
                                IconButton(
                                    modifier = Modifier
                                        .height(30.dp)
                                        .width(30.dp),
                                    onClick = {
                                        var nuevoValor = rangoMaxJugadores-1
                                        // no permite que supere 4 como valor
                                        if (Integer.valueOf(nuevoValor) > 4) {
                                            nuevoValor = 4
                                        }
                                        // no permite que baje de 0
                                        if (Integer.valueOf(nuevoValor) < 0) {
                                            nuevoValor = 0
                                        }
                                        partidaViewModel.modificarRangoMaxJugadores(Integer.valueOf(nuevoValor))
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Disminuir un nivel",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .padding(0.dp)
                                            .size(30.dp)
                                    )
                                }
                            }
                        }

                        Row() {
                            OutlinedTextField(
                                modifier = Modifier
                                    .width(120.dp)
                                    .padding(end = 8.dp),
                                value = rangoMinJugadores.toString(),
                                onValueChange = { try {
                                    var nuevoValor = it
                                    if (rangoMinJugadores == 0) {
                                        nuevoValor = it.replace("0", "")
                                    }
                                    // no permite que supere 4 como valor
                                    if (Integer.valueOf(nuevoValor) > 4) {
                                        nuevoValor = "4"
                                    }
                                    // no permite que baje de 0
                                    if (Integer.valueOf(nuevoValor) < 0) {
                                        nuevoValor = "0"
                                    }
                                    partidaViewModel.modificarRangoMinJugadores(Integer.valueOf(nuevoValor))
                                } catch (e: Exception) {
                                    partidaViewModel.modificarRangoMinJugadores(0)
                                }},
                                label = { Text(text = "Mínimo") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                                    focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                                    cursorColor = MaterialTheme.colorScheme.secondary,
                                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                    unfocusedTextColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = CutCornerShape(16.dp),
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                )
                            )
                            Column() {
                                IconButton(
                                    modifier = Modifier
                                        .height(30.dp)
                                        .width(30.dp),
                                    onClick = {
                                        var nuevoValor = rangoMinJugadores+1
                                        // no permite que supere 4 como valor
                                        if (Integer.valueOf(nuevoValor) > 4) {
                                            nuevoValor = 4
                                        }
                                        // no permite que baje de 0
                                        if (Integer.valueOf(nuevoValor) < 0) {
                                            nuevoValor = 0
                                        }
                                        partidaViewModel.modificarRangoMinJugadores(Integer.valueOf(nuevoValor))
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Aumentar un nivel",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .padding(0.dp)
                                            .size(30.dp),
                                    )
                                }
                                IconButton(
                                    modifier = Modifier
                                        .height(30.dp)
                                        .width(30.dp),
                                    onClick = {
                                        var nuevoValor = rangoMinJugadores-1
                                        // no permite que supere 4 como valor
                                        if (Integer.valueOf(nuevoValor) > 4) {
                                            nuevoValor = 4
                                        }
                                        // no permite que baje de 0
                                        if (Integer.valueOf(nuevoValor) < 0) {
                                            nuevoValor = 0
                                        }
                                        partidaViewModel.modificarRangoMinJugadores(Integer.valueOf(nuevoValor))
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Disminuir un nivel",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .padding(0.dp)
                                            .size(30.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // fechas
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(IntrinsicSize.Max),
                    textAlign = TextAlign.Center,
                    text = "Fechas",
                    style = Titulo4Style,
                    color = MaterialTheme.colorScheme.secondary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // fecha inicio
                    OutlinedTextField(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        value = fechaInicio.let { ConversorUnix.milisAFechaDMY(it) },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Inicio") },
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    partidaViewModel.modificarTipoFecha("inicio")
                                    partidaViewModel.mostrarDatePicker(true)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Fecha de inicio"
                                )
                            }
                        },
                        trailingIcon = {
                            if (fechaInicio != null) {
                                IconButton(
                                    onClick = {
                                        partidaViewModel.modificarFechaInicio(null)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Limpiar fecha de inicio"
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                            unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                            focusedBorderColor = MaterialTheme.colorScheme.secondary,
                            focusedLabelColor = MaterialTheme.colorScheme.secondary,
                            focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                            focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.secondary,
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = CutCornerShape(16.dp),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        )
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // fecha finalización
                        OutlinedTextField(
                            modifier = Modifier
                                .width(200.dp)
                                .padding(end = 4.dp),
                            value = fechaFinalizacion.let { ConversorUnix.milisAFechaDMY(it) },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Finalización") },
                            leadingIcon = {
                                IconButton(
                                    onClick = {
                                        partidaViewModel.modificarTipoFecha("finalizacion")
                                        partidaViewModel.mostrarDatePicker(true)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Fecha de finalización"
                                    )
                                }
                            },
                            trailingIcon = {
                                if (fechaFinalizacion != null) {
                                    IconButton(
                                        onClick = {
                                            partidaViewModel.modificarFechaFinalizacion(null)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Limpiar fecha de finalización"
                                        )
                                    }
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                                unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                                focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                                focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                                cursorColor = MaterialTheme.colorScheme.secondary,
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = CutCornerShape(16.dp),
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            )
                        )

                        // finalizada?
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.secondary,
                                    uncheckedColor = MaterialTheme.colorScheme.primary,
                                    checkmarkColor = MaterialTheme.colorScheme.onSecondary
                                ),
                                checked = finalizada,
                                onCheckedChange = {
                                    partidaViewModel.modificarFinalizada(!finalizada)
                                }
                            )
                            Text(text = "Ha finalizado")
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.secondary,
                            uncheckedColor = MaterialTheme.colorScheme.primary,
                            checkmarkColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        checked = privada,
                        onCheckedChange = {
                            partidaViewModel.modificarPrivada(!privada)
                        }
                    )
                    Text(text = "Privada")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.secondary,
                            uncheckedColor = MaterialTheme.colorScheme.primary,
                            checkmarkColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        checked = infoVisible,
                        onCheckedChange = {
                            partidaViewModel.modificarInfoVisible(!infoVisible)
                        }
                    )
                    Text(text = "Permitir que los participantes puedan ver la información de los personajes de los demás aventureros.")
                }

                // requiere launched effect para esperar los cambios en la variable modificarPartidaResultado del view model
                // este cambio se produce al pulsar el botón
                LaunchedEffect(modificarPartidaResultado) {
                    when (modificarPartidaResultado) {
                        "Exito" -> {
                            // se borran los datos una vez guardada la partida
                            partidaViewModel.modificarTitulo("")
                            partidaViewModel.modificarDescripcion("")
                            partidaViewModel.modificarNumMinJugadores(0)
                            partidaViewModel.modificarNumMaxJugadores(0)
                            partidaViewModel.modificarRangoMinJugadores(0)
                            partidaViewModel.modificarRangoMaxJugadores(0)
                            partidaViewModel.modificarFechaInicio(null)
                            partidaViewModel.modificarFechaFinalizacion(null)
                            partidaViewModel.modificarFinalizada(false)
                            partidaViewModel.modificarInfoVisible(false)

                            partidaViewModel.activarModoEdicion(false)

                            partidaViewModel.recargarPartidas()
                            partidaViewModel.modificarCrearPartidaResultado("")
                            navController.popBackStack()
                        }
                        "" -> {
                            // no hace nada pero tampoco saca el diálogo de error
                        }
                        else -> {
                            partidaViewModel.mostrarDialogo(true)
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        DialogoPartida(
            resultado = modificarPartidaResultado,
            resultadoBorrar = eliminarPartidaResultado,
            resultadoCrearRelacion = crearRelacionResultado,
            resultadoEliminarRelacion = eliminarRelacionResultado,
            resultadoEliminarRelaciones = eliminarRelacionesResultado,
            resultadoCrearSesion = crearSesionResultado,
            resultadoModificarSesion = modificarSesionResultado,
            onDismiss = {
                partidaViewModel.modificarModificarPartidaResultado("")
                partidaViewModel.modificarEliminarPartidaResultado("")
                partidaUsuarioViewModel.modificarCrearRelacionResultado("")
                partidaUsuarioViewModel.modificarEliminarRelacionResultado("")
                partidaUsuarioViewModel.modificarEliminarRelacionesMultiplesResultado("")
                sesionViewModel.modificarModificarSesionResultado("")
                partidaViewModel.mostrarDialogo(false)
            }
        )
    }

    if (mostrarDialogoConfirmacionBorrado) {
        DialogoConfirmacionBorrado(
            titulo =
                if (idsAventurerosABorrar.size > 1)
                    "Expulsar aventureros"
                else if (autoEliminado)
                    "Abandonar aventura"
                else if (aventureroABorrar != null)
                    "Expulsar aventurero"
                else if (idsSesionesABorrar.size > 1)
                    "Borrar sesiones"
                else if (sesionABorrar != null)
                    "Borrar sesión"
                else
                    "Borrar campaña",
            mensaje =
                if (idsAventurerosABorrar.size > 1) {
                    "Estás a punto de expulsar a los ${idsAventurerosABorrar.size} aventureros seleccionados. Esta acción no es reversible, ¿deseas continuar?"
                } else if (idsAventurerosABorrar.size == 1) {
                    "Estás a punto de expulsar al aventurero seleccionado. Esta acción no es reversible, ¿deseas continuar?"
                } else if (autoEliminado) {
                    "Estás a punto de abandonar esta aventura. Esta acción no es reversible, ¿deseas continuar?"
                } else if (aventureroABorrar != null) {
                    "Estás a punto de expulsar a ${aventureroABorrar?.personaje?.nombre}. Esta acción no es reversible, ¿deseas continuar?"
                } else if (idsSesionesABorrar.size > 1) {
                    "Estás a punto de borrar las ${idsSesionesABorrar.size} sesiones seleccionadas. Esta acción no es reversible, ¿deseas continuar?"
                } else if (idsSesionesABorrar.size == 1) {
                    "Estás a punto de borrar la sesión seleccionada. Esta acción no es reversible, ¿deseas continuar?"
                } else if (sesionABorrar != null) {
                    "Estás a punto de borrar esta sesión. Esta acción no es reversible, ¿deseas continuar?"
                } else {
                    "Estás a punto de borrar esta campaña junto con todos su datos. Esta acción no es reversible, ¿deseas continuar?"
                },
            partida = partida,
            autoEliminado = autoEliminado,
            aventurero = aventureroABorrar,
            idsAventureros = idsAventurerosABorrar,
            sesion = sesionABorrar,
            idsSesiones = idsSesionesABorrar,
            mainViewModel = mainViewModel,
            usuarioViewModel = usuarioViewModel,
            personajeViewModel = personajeViewModel,
            partidaUsuarioViewModel = partidaUsuarioViewModel,
            sesionViewModel = sesionViewModel,
            partidaViewModel = partidaViewModel,
            navController = navController,
            onDismiss = {
                partidaUsuarioViewModel.modificarAventureroABorrar(null)
                partidaUsuarioViewModel.modificarAutoEliminado(false)
                sesionViewModel.modificarSesionABorrar(null)
                mainViewModel.mostrarDialogoConfirmacionBorrado(false)
            }
        )
    }

    if (mostrarDatePicker) {
        DatePickerModal(
            initialMillis =
                when (tipoFecha) {
                    "inicio" -> fechaInicio
                    "finalizacion" -> fechaFinalizacion
                    else -> null
                },
            onDateSelected = { millis ->
                when (tipoFecha) {
                    "inicio" -> partidaViewModel.modificarFechaInicio(millis ?: fechaInicio)
                    "finalizacion" -> partidaViewModel.modificarFechaFinalizacion(millis ?: fechaFinalizacion)
                }
            },
            onDismiss = {
                partidaViewModel.mostrarDatePicker(false)
            },
        )
    }
}