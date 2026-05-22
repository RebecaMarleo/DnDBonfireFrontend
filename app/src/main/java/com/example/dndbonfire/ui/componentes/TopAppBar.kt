package com.example.dndbonfire.ui.componentes

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.dndbonfire.data.ProveedorPersonaje
import com.example.dndbonfire.data.ProveedorUsuario
import com.example.dndbonfire.ui.theme.Titulo3Style
import com.example.dndbonfire.util.ConversorUnix
import com.example.dndbonfire.util.Validar
import com.example.dndbonfire.viewmodel.BestiaViewModel
import com.example.dndbonfire.viewmodel.ImagenViewModel
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaUsuarioViewModel
import com.example.dndbonfire.viewmodel.PartidaViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import com.example.dndbonfire.viewmodel.SesionViewModel
import com.example.dndbonfire.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navController: NavController,
    usuarioViewModel: UsuarioViewModel,
    imagenViewModel: ImagenViewModel,
    bestiaViewModel: BestiaViewModel,
    mainViewModel: MainViewModel,
    partidaViewModel: PartidaViewModel,
    partidaUsuarioViewModel: PartidaUsuarioViewModel,
    sesionViewModel: SesionViewModel,
    personajeViewModel: PersonajeViewModel,
    rutaActual: String?,
    esSubpantalla: Boolean
) {
    val context = LocalContext.current
    val usuarioLogeado by mainViewModel.usuarioLogeado
    val imagenEliminada by imagenViewModel.imagenEliminada
    val imagenUri by imagenViewModel.imagenUri
    val bitmap by imagenViewModel.bitmap
    val nombre by usuarioViewModel.nombre
    val username by usuarioViewModel.username
    val contrasena by usuarioViewModel.contrasena
    val contrasenaRe by usuarioViewModel.contrasenaRe
    val rango by usuarioViewModel.rango
    val imagen by usuarioViewModel.imagen
    val personaje by personajeViewModel.personaje
    val partida by partidaViewModel.partida
    val idsPersonajesABorrar = personajeViewModel.idsPersonajesABorrar
    val idsAventurerosABorrar = partidaUsuarioViewModel.idsAventurerosABorrar
    val idsSesionesABorrar = sesionViewModel.idsSesionesABorrar
    val modificarUsuarioResultado by mainViewModel.modificarUsuarioResultado
    val modificarPersonajeResultado by personajeViewModel.modificarPersonajeResultado
    val modificarPartidaResultado by partidaViewModel.modificarPartidaResultado

    TopAppBar(
        navigationIcon = {
            if (!esSubpantalla) {

            } else {
                IconButton(
                    onClick = {
                        if (rutaActual?.endsWith(".IniciarSesion") == true || rutaActual?.endsWith(".CrearCuenta") == true) {
                            usuarioViewModel.modificarNombre("")
                            usuarioViewModel.modificarUsername("")
                            usuarioViewModel.modificarContrasena("")
                            usuarioViewModel.cambiarEstadoMantenerseLogeado(false)
                        } else if (rutaActual?.endsWith("Cuenta") == true) {
                            if (!usuarioViewModel.modoEdicion.value) {
                                usuarioViewModel.activarModoEdicion(false)
                                imagenViewModel.modificarImagenUri(null)
                                imagenViewModel.modificarBitmap(null)
                            } else {
                                imagenViewModel.modificarImagenEliminada(false)
                                imagenViewModel.modificarImagenUri(null)
                                imagenViewModel.modificarBitmap(null)
                                usuarioViewModel.modificarNombre("")
                                usuarioViewModel.modificarUsername("")
                                usuarioViewModel.modificarContrasena("")
                                usuarioViewModel.modificarContrasenaRe("")
                            }
                        } else if (rutaActual?.endsWith(".Bestia/{idBestia}") == true) {
                            bestiaViewModel.cargarBestia("")
                        } else if (rutaActual?.endsWith(".CrearPersonaje") == true) {
                            personajeViewModel.modificarNombre("")
                            personajeViewModel.modificarHp(0)
                            personajeViewModel.modificarFuerza(0)
                            personajeViewModel.modificarDestreza(0)
                            personajeViewModel.modificarConstitucion(0)
                            personajeViewModel.modificarInteligencia(0)
                            personajeViewModel.modificarSabiduria(0)
                            personajeViewModel.modificarCarisma(0)
                            personajeViewModel.cargarRaza("")
                            personajeViewModel.modificarEdad(0)
                            personajeViewModel.modificarAlineamiento("Neutral verdadero")
                            personajeViewModel.cargarClase("")
                            personajeViewModel.modificarNivel(0)
                            personajeViewModel.modificarCompetenciasSeleccionadas("", null, null)
                            personajeViewModel.modificarTrucosConocidos("", null)
                            personajeViewModel.modificarConjurosConocidos("", null)
                            imagenViewModel.modificarImagenUri(null)
                            imagenViewModel.modificarBitmap(null)
                            personajeViewModel.modificarImagen(null)
                        } else if (rutaActual?.endsWith(".Personaje/{idPersonaje}") == true) {
                            if (!personajeViewModel.modoEdicion.value) {
                                // vacía los datos del personaje seleccionado
                                personajeViewModel.cargarPersonaje(null)

                                personajeViewModel.modificarNombre("")
                                personajeViewModel.modificarHp(0)
                                personajeViewModel.modificarFuerza(0)
                                personajeViewModel.modificarDestreza(0)
                                personajeViewModel.modificarConstitucion(0)
                                personajeViewModel.modificarInteligencia(0)
                                personajeViewModel.modificarSabiduria(0)
                                personajeViewModel.modificarCarisma(0)
                                personajeViewModel.cargarRaza("")
                                personajeViewModel.modificarEdad(0)
                                personajeViewModel.modificarAlineamiento("Neutral verdadero")
                                personajeViewModel.cargarClase("")
                                personajeViewModel.modificarNivel(0)
                                personajeViewModel.modificarCompetenciasSeleccionadas("", null, null)
                                personajeViewModel.modificarTrucosConocidos("", null)
                                personajeViewModel.modificarConjurosConocidos("", null)

                                personajeViewModel.recargarPersonajes()
                            } else {
                                personajeViewModel.modificarNombre("")
                                personajeViewModel.modificarHp(0)
                                personajeViewModel.modificarFuerza(0)
                                personajeViewModel.modificarDestreza(0)
                                personajeViewModel.modificarConstitucion(0)
                                personajeViewModel.modificarInteligencia(0)
                                personajeViewModel.modificarSabiduria(0)
                                personajeViewModel.modificarCarisma(0)
                                personajeViewModel.cargarRazaMod("")
                                personajeViewModel.modificarEdad(0)
                                personajeViewModel.modificarAlineamiento("Neutral verdadero")
                                personajeViewModel.cargarClaseMod("")
                                personajeViewModel.modificarNivel(0)
                                personajeViewModel.modificarCompetenciasSeleccionadas("", null, null)
                                personajeViewModel.modificarTrucosConocidos("", null)
                                personajeViewModel.modificarConjurosConocidos("", null)
                                imagenViewModel.modificarImagenUri(null)
                                imagenViewModel.modificarBitmap(null)
                                personajeViewModel.modificarImagen(null)
                            }
                        } else if (rutaActual?.endsWith(".CrearPartida") == true) {
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
                        } else if (rutaActual?.endsWith(".Partida/{idPartida}") == true) {
                            if (!partidaViewModel.modoEdicion.value) {
                                partidaViewModel.cargarPartida(null)
                                usuarioViewModel.cargarUsuarioDM(null)
                                partidaUsuarioViewModel.vaciarAventureros()
                                sesionViewModel.vaciarSesiones()
                                personajeViewModel.modificarSinPersonajes(false)
                                sesionViewModel.modificarCreandoSesion(false)
                                sesionViewModel.inicializarFecha()
                                sesionViewModel.modificarDescripcion("")

                                if (idsAventurerosABorrar.isNotEmpty()) {
                                    partidaUsuarioViewModel.modificarAventurerosABorrar("vaciar", null)
                                }

                                if (idsSesionesABorrar.isNotEmpty()) {
                                    sesionViewModel.modificarSesionesABorrar("vaciar", null)
                                }

                                partidaViewModel.recargarPartidas()
                            } else {

                            }
                        }

                        if (!(rutaActual?.endsWith(".Personaje/{idPersonaje}") == true && personajeViewModel.modoEdicion.value)
                            && !(rutaActual?.endsWith(".Partida/{idPartida}") == true && partidaViewModel.modoEdicion.value)
                            && !(rutaActual?.endsWith(".Cuenta") == true && usuarioViewModel.modoEdicion.value)) {
                            navController.popBackStack()
                        } else {
                            if (rutaActual.endsWith(".Personaje/{idPersonaje}")) {
                                personajeViewModel.activarModoEdicion(false)
                            } else if (rutaActual.endsWith(".Partida/{idPartida}")) {
                                partidaViewModel.activarModoEdicion(false)
                            } else if (rutaActual.endsWith(".Cuenta")) {
                                usuarioViewModel.activarModoEdicion(false)
                            }
                        }
                    }
                ) {
                    if (
                        (rutaActual?.endsWith(".Personaje/{idPersonaje}") == true && personajeViewModel.modoEdicion.value)
                        || (rutaActual?.endsWith(".Partida/{idPartida}") == true && partidaViewModel.modoEdicion.value)
                        || (rutaActual?.endsWith(".Cuenta") == true && usuarioViewModel.modoEdicion.value)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            modifier = Modifier.size(36.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Retroceder",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
        },
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                if (idsPersonajesABorrar.isNotEmpty()) {
                    Text(
                        text = "${idsPersonajesABorrar.size} ${if (idsPersonajesABorrar.size == 1) "seleccionado" else "seleccionados"}"
                    )
                } else if (idsAventurerosABorrar.isNotEmpty()) {
                    Text(
                        text = "${idsAventurerosABorrar.size} ${if (idsAventurerosABorrar.size == 1) "seleccionado" else "seleccionados"}"
                    )
                } else if (idsSesionesABorrar.isNotEmpty()) {
                    Text(
                        text = "${idsSesionesABorrar.size} ${if (idsSesionesABorrar.size == 1) "seleccionado" else "seleccionados"}"
                    )
                } else {
                    Text(
                        text = "D&D Bonfire",
                        style = Titulo3Style
                    )
                }
            }
        },
        actions = {
            if (rutaActual?.endsWith(".Partida/{idPartida}") == true && !partidaViewModel.modoEdicion.value && idsAventurerosABorrar.isEmpty()) {
                IconButton(
                    modifier = Modifier
                        .size(30.dp)
                        .padding(end = 4.dp),
                    onClick = {
                        var texto = "¡Únete a la aventura conmigo!\n\n https://dnd-bonfire.vercel.app/partida/${partida?.id}"

                        val intentCompartir = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, texto)
                        }

                        context.startActivity(
                            Intent.createChooser(intentCompartir, "Compartir partida")
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartir",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            if (rutaActual?.endsWith(".Cuenta") == true) {
                if (!usuarioViewModel.modoEdicion.value) {
                    IconButton(
                        modifier = Modifier
                            .size(30.dp),
                        onClick = {
                            usuarioViewModel.activarModoEdicion(true)

                            // transfiere los datos actuales del personaje
                            imagenViewModel.modificarImagenUri(null)
                            imagenViewModel.modificarBitmap(null)
                            usuarioViewModel.modificarImagen(null)
                            usuarioViewModel.modificarNombre(usuarioLogeado?.nombre ?: "")
                            usuarioViewModel.modificarUsername(usuarioLogeado?.username ?: "")
                            usuarioViewModel.modificarRango(usuarioLogeado?.rango ?: 1)
                            usuarioViewModel.activarModoEdicion(true)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                } else {
                    IconButton(
                        modifier = Modifier
                            .size(30.dp),
                        onClick = {
                            var datosValidos = true
                            if (username.trim() != "" && !Validar.validarUsername(username)) {
                                datosValidos = false
                                mainViewModel.modificarModificarUsuarioResultado("El de usuario debe tener entre 3 y 20 caracteres. Además sólo puede contener letras mayúsculas y minúsculas, números y barra bajas.")
                            } else if (!Validar.validarTexto(username, 20)) {
                                datosValidos = false
                                mainViewModel.modificarCrearCuentaResultado("El nombre de usuario no puede tener más de 20 caracteres.")
                            } else if (!Validar.validarTexto(nombre, 200)) {
                                datosValidos = false
                                mainViewModel.modificarCrearCuentaResultado("El nombre no puede tener más de 200 caracteres.")
                            } else if (contrasena.trim() != "" && !Validar.validarContrasena(contrasena)) {
                                datosValidos = false
                                mainViewModel.modificarModificarUsuarioResultado("La contraseña debe tener entre 6 y 30 caracteres. Además debe contener como mínimo una mayúscula, una minúscula, un número y un caracter especial.")
                            } else if (contrasena.trim() != "" && !Validar.validarRecontrasena(contrasena, contrasenaRe)) {
                                datosValidos = false
                                mainViewModel.modificarModificarUsuarioResultado("Las contraseñas no coinciden.")
                            } else if (!Validar.validarTexto(contrasena, 30)) {
                                datosValidos = false
                                mainViewModel.modificarCrearCuentaResultado("La contraseña no puede tener más de 30 caracteres.")
                            }
                            if (datosValidos) {
                                imagenUri?.let {
                                    usuarioViewModel.modificarImagen(imagenViewModel.uriABase64(context, it))
                                }
                                if (imagenEliminada) {
                                    usuarioViewModel.modificarImagen(null)
                                }
                                mainViewModel.modificarUsuario(nombre, username, contrasena, rango, imagen)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Guardar cambios",
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // requiere launched effect para esperar los cambios en la variable modificarUsuarioResultado del view model
                    // este cambio se produce al pulsar el botón
                    LaunchedEffect(modificarUsuarioResultado) {
                        when (modificarUsuarioResultado) {
                            "Exito" -> {
                                imagenViewModel.modificarImagenEliminada(false)
                                imagenViewModel.modificarImagenUri(null)
                                imagenViewModel.modificarBitmap(null)
                                usuarioViewModel.modificarImagen(null)
                                usuarioViewModel.modificarNombre("")
                                usuarioViewModel.modificarUsername("")
                                usuarioViewModel.modificarContrasena("")
                                usuarioViewModel.modificarContrasenaRe("")
                                usuarioViewModel.cambiarEstadoMantenerseLogeado(false)
                                mainViewModel.modificarModificarUsuarioResultado("")
                                usuarioViewModel.activarModoEdicion(false)
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
            } else if (rutaActual?.endsWith(".Personaje/{idPersonaje}") == true && personaje?.idUsuario == usuarioLogeado?.id) {
                if (!personajeViewModel.modoEdicion.value) {
                    IconButton(
                        modifier = Modifier
                            .size(30.dp),
                        onClick = {
                            personajeViewModel.activarModoEdicion(true)

                            // transfiere los datos actuales del personaje
                            personajeViewModel.modificarNombre(personaje?.nombre ?: "")
                            personajeViewModel.modificarHp(personaje?.hp ?: 0)
                            personajeViewModel.modificarHpAuto(personaje?.hpAuto ?: true)
                            personajeViewModel.modificarFuerza(personaje?.fuerza ?: 0)
                            personajeViewModel.modificarDestreza(personaje?.destreza ?: 0)
                            personajeViewModel.modificarConstitucion(personaje?.constitucion ?: 0)
                            personajeViewModel.modificarInteligencia(personaje?.inteligencia ?: 0)
                            personajeViewModel.modificarSabiduria(personaje?.sabiduria ?: 0)
                            personajeViewModel.modificarCarisma(personaje?.carisma ?: 0)
                            personajeViewModel.forzarRazaMod()
                            personajeViewModel.forzarSubrazaMod()
                            personajeViewModel.modificarEdad(personaje?.edad ?: 0)
                            personajeViewModel.modificarAlineamiento(personaje?.alineamiento ?: "Neutral verdadero")
                            personajeViewModel.forzarClaseMod()
                            personajeViewModel.forzarSubclaseMod()
                            personajeViewModel.forzarInfoNivelesMod()
                            personajeViewModel.modificarNivel(personaje?.nivel ?: 0)
                            personajeViewModel.forzarCompetenciasSeleccionadas(personaje?.competenciasSeleccionadas!!)
                            personajeViewModel.forzarConjurosMod()
                            personajeViewModel.forzarTrucosConocidos(personaje?.trucosConocidos!!)
                            personajeViewModel.forzarConjurosConocidos(personaje?.conjurosConocidos!!)
                            personajeViewModel.modificarImagen(personaje?.imagen)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                } else {
                    IconButton(
                        modifier = Modifier
                            .size(30.dp),
                        onClick = {
                            var personajeValido = true

                            if (!Validar.validarTexto(personajeViewModel.nombre.value, 200)) {
                                personajeValido = false
                                personajeViewModel.modificarCrearPersonajeResultado("El nombre de personaje no puede tener más de 200 caracteres.")
                            }

                            if (personajeValido) {
                                imagenViewModel.imagenUri.value?.let {
                                    personajeViewModel.modificarImagen(imagenViewModel.uriABase64(context, it))
                                }
                                if (imagenViewModel.imagenUri.value == null) {
                                    personajeViewModel.modificarImagen(personajeViewModel.imagen.value)
                                }
                                if (imagenViewModel.imagenEliminada.value) {
                                    personajeViewModel.modificarImagen(null)
                                }
                                personajeViewModel.modificarPersonaje(
                                    personajeViewModel.personaje.value?.id ?: 0,
                                    personajeViewModel.nombre.value,
                                    personajeViewModel.razaMod.value?.index,
                                    personajeViewModel.subrazaMod.value?.index,
                                    personajeViewModel.hp.intValue,
                                    personajeViewModel.hpAuto.value,
                                    personajeViewModel.fuerza.intValue,
                                    personajeViewModel.destreza.intValue,
                                    personajeViewModel.constitucion.intValue,
                                    personajeViewModel.inteligencia.intValue,
                                    personajeViewModel.sabiduria.intValue,
                                    personajeViewModel.carisma.intValue,
                                    personajeViewModel.edad.intValue,
                                    personajeViewModel.idiomaSeleccionado.value?.index,
                                    personajeViewModel.alineamiento.value,
                                    personajeViewModel.claseMod.value?.index,
                                    personajeViewModel.subclaseMod.value?.index,
                                    personajeViewModel.nivel.intValue,
                                    personajeViewModel.competenciasSeleccionadas,
                                    personajeViewModel.trucosConocidos,
                                    personajeViewModel.conjurosConocidos,
                                    personajeViewModel.imagen.value
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Guardar cambios",
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // requiere launched effect para esperar los cambios en la variable loginResultado del view model
                    // este cambio se produce al pulsar el botón
                    LaunchedEffect(modificarPersonajeResultado) {
                        when (modificarPersonajeResultado) {
                            "Exito" -> {
                                personajeViewModel.activarModoEdicion(false)
                                personajeViewModel.recargarPersonajes()
                                personajeViewModel.modificarModificarPersonajeResultado("")
                            }
                            "" -> {
                                // no hace nada pero tampoco saca el diálogo de error
                            }
                            else -> {
                                personajeViewModel.modificarInfoSeleccionada("resultado")
                                personajeViewModel.mostrarDialogo(true)
                            }
                        }
                    }
                }
            } else if (rutaActual?.endsWith(".Partida/{idPartida}") == true && partida?.idDM == usuarioLogeado?.id && idsAventurerosABorrar.isEmpty() && idsSesionesABorrar.isEmpty()) {
                if (!partidaViewModel.modoEdicion.value) {
                    IconButton(
                        modifier = Modifier
                            .size(30.dp),
                        onClick = {
                            partidaViewModel.activarModoEdicion(true)

                            // transfiere los datos actuales de la partida
                            partidaViewModel.modificarTitulo(partida?.titulo ?: "")
                            partidaViewModel.modificarDescripcion(partida?.descripcion ?: "")
                            partidaViewModel.modificarNumMinJugadores(partida?.numMinJugadores ?: 0)
                            partidaViewModel.modificarNumMaxJugadores(partida?.numMaxJugadores ?: 0)
                            partidaViewModel.modificarRangoMinJugadores(partida?.rangoMinJugadores ?: 0)
                            partidaViewModel.modificarRangoMaxJugadores(partida?.rangoMaxJugadores ?: 0)
                            partidaViewModel.modificarFechaInicio(ConversorUnix.fechaYMDaMilis(partida?.fechaInicio))
                            partidaViewModel.modificarFechaFinalizacion(ConversorUnix.fechaYMDaMilis(partida?.fechaFinalizacion))
                            partidaViewModel.modificarFinalizada(partida?.finalizada ?: false)
                            partidaViewModel.modificarPrivada(partida?.privada ?: false)
                            partidaViewModel.modificarInfoVisible(partida?.infoVisible ?: false)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                } else {
                    IconButton(
                        modifier = Modifier
                            .size(30.dp),
                        onClick = {
                            var partidaValida = true
                            if (!Validar.validarTexto(partidaViewModel.titulo.value, 150)) {
                                partidaValida = false
                                partidaViewModel.modificarCrearPartidaResultado("El título no puede tener más de 150 caracteres.")
                                partidaViewModel.mostrarDialogo(true)
                            } else if (!Validar.validarTexto(partidaViewModel.descripcion.value, 2000)) {
                                partidaValida = false
                                partidaViewModel.modificarCrearPartidaResultado("La descripción no puede tener más de 2000 caracteres.")
                                partidaViewModel.mostrarDialogo(true)
                            } else if (!Validar.validarNumJugadores(partidaViewModel.numMinJugadores.intValue, partidaViewModel.numMaxJugadores.intValue)) {
                                partidaValida = false
                                partidaViewModel.modificarModificarPartidaResultado("El número máximo de jugadores no puede ser menor al número mínimo de jugadores.")
                                partidaViewModel.mostrarDialogo(true)
                            } else if (!Validar.validarRangoJugadores(partidaViewModel.rangoMinJugadores.intValue, partidaViewModel.rangoMaxJugadores.intValue)) {
                                partidaValida = false
                                partidaViewModel.modificarModificarPartidaResultado("El rango máximo de jugadores no puede ser menor al rango mínimo de jugadores.")
                                partidaViewModel.mostrarDialogo(true)
                            } else if (partidaViewModel.fechaInicio.value != null && partidaViewModel.fechaFinalizacion.value != null) {
                                if (!Validar.validarFechas(partidaViewModel.fechaInicio.value!!, partidaViewModel.fechaFinalizacion.value!!)) {
                                    partidaValida = false
                                    partidaViewModel.modificarModificarPartidaResultado("La fecha de inicio no puede ser posterior a la fecha de finalización.")
                                    partidaViewModel.mostrarDialogo(true)
                                }
                            } else if (!Validar.validarFinalizada(partidaViewModel.fechaFinalizacion.value, partidaViewModel.finalizada.value)) {
                                partidaValida = false
                                partidaViewModel.modificarModificarPartidaResultado("Si la campaña ha finalizado, debe tener una fecha de finalización posterior al día actual.")
                                partidaViewModel.mostrarDialogo(true)
                            }

                            if (partidaValida) {
                                partidaViewModel.modificarPartida(
                                    partidaViewModel.partida.value?.id ?: 0,
                                    partidaViewModel.titulo.value,
                                    partidaViewModel.descripcion.value,
                                    partidaViewModel.numMinJugadores.intValue,
                                    partidaViewModel.numMaxJugadores.intValue,
                                    partidaViewModel.rangoMinJugadores.intValue,
                                    partidaViewModel.rangoMaxJugadores.intValue,
                                    partidaViewModel.fechaInicio.value,
                                    partidaViewModel.fechaFinalizacion.value,
                                    partidaViewModel.finalizada.value,
                                    partidaViewModel.privada.value,
                                    partidaViewModel.infoVisible.value
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Editar",
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // requiere launched effect para esperar los cambios en la variable loginResultado del view model
                    // este cambio se produce al pulsar el botón
                    LaunchedEffect(modificarPartidaResultado) {
                        when (modificarPartidaResultado) {
                            "Exito" -> {
                                partidaViewModel.activarModoEdicion(false)
                                partidaViewModel.recargarPartidas()
                                partidaViewModel.modificarModificarPartidaResultado("")
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
            } else if (
                (rutaActual?.endsWith(".Personajes") == true && idsPersonajesABorrar.isNotEmpty())
                || (rutaActual?.endsWith(".Partida/{idPartida}") == true && idsAventurerosABorrar.isNotEmpty())
                || (rutaActual?.endsWith(".Partida/{idPartida}") == true && idsSesionesABorrar.isNotEmpty())
                ) {
                IconButton(
                    modifier = Modifier
                        .size(30.dp),
                    onClick = {
                        mainViewModel.mostrarDialogoConfirmacionBorrado(true)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            IconButton(
                onClick = {
                    mainViewModel.desplegarMenu(true)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menú",
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}