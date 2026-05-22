package com.example.dndbonfire.ui.componentes

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dndbonfire.modelo.Partida
import com.example.dndbonfire.modelo.Sesion
import com.example.dndbonfire.modelo.Usuario
import com.example.dndbonfire.ui.theme.ButtonStyle
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.ui.theme.Titulo5Style
import com.example.dndbonfire.util.ConversorUnix
import com.example.dndbonfire.util.Validar
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaUsuarioViewModel
import com.example.dndbonfire.viewmodel.PartidaViewModel
import com.example.dndbonfire.viewmodel.SesionViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun ElementoPartidaSesion(
    sesion: Sesion?,
    partida: Partida,
    navController: NavController,
    mainViewModel: MainViewModel,
    partidaViewModel: PartidaViewModel,
    partidaUsuarioViewModel: PartidaUsuarioViewModel,
    sesionViewModel: SesionViewModel
) {
    var accion by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val usuarioLogeado by mainViewModel.usuarioLogeado
    val idDM = partidaViewModel.partida.value?.idDM
    val idsSesionesABorrar = sesionViewModel.idsSesionesABorrar
    val descripcion by sesionViewModel.descripcion

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                sesionViewModel.modificarSesionABorrar(sesion)
                mainViewModel.mostrarDialogoConfirmacionBorrado(true)
            }
            false
        }
    )

    // si el id de la sesión actual está entre los ids en la lista de seleccionados lo marca como seleccionado
    var estaSeleccionado = false
    idsSesionesABorrar.forEach { id ->
        if (id == sesion?.id) estaSeleccionado = true
    }

    val contentColor by animateColorAsState(
        targetValue = if ((isPressed || estaSeleccionado) && sesion != null)
            MaterialTheme.colorScheme.onSecondary
        else
            MaterialTheme.colorScheme.onPrimaryContainer,
        animationSpec = tween(200)
    )
    val containerColor by animateColorAsState(
        targetValue = if ((isPressed || estaSeleccionado) && sesion != null)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.primaryContainer,
        animationSpec = tween(200)
    )
    val borderColor by animateColorAsState(
        targetValue = if ((isPressed || estaSeleccionado) && sesion != null)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.tertiary
    )

    if (usuarioLogeado?.id == idDM && sesion != null) {
        SwipeToDismissBox(
            state = dismissState,
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = true,
            backgroundContent = {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = CutCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Borrar",
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        ) {
            TarjetaSesion(
                accion,
                usuarioLogeado,
                idDM,
                idsSesionesABorrar,
                coroutineScope,
                sesion,
                navController,
                sesionViewModel,
                partidaViewModel,
                partidaUsuarioViewModel,
                interactionSource,
                borderColor,
                contentColor,
                containerColor
            )
        }
    } else {
        TarjetaSesion(
            accion,
            usuarioLogeado,
            idDM,
            idsSesionesABorrar,
            coroutineScope,
            sesion,
            navController,
            sesionViewModel,
            partidaViewModel,
            partidaUsuarioViewModel,
            interactionSource,
            borderColor,
            contentColor,
            containerColor
        )
    }

    // botones de acción
    if (sesion == null) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Button(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .weight(1f)
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
                    accion = ""
                    sesionViewModel.modificarCreandoSesion(false)
                }
            ) {
                Text(
                    text = "Cancelar",
                    style = ButtonStyle
                )
            }
            Button(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1f)
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
                    var datosValidos = true
                    if (!Validar.validarTexto(descripcion, 2000)) {
                        datosValidos = false
                        sesionViewModel.modificarModificarSesionResultado("La descripción no puede tener más de 2000 caracteres.")
                    }
                    if (datosValidos) {
                        sesionViewModel.crearSesion(partida)
                    }
                }
            ) {
                Text(
                    text = "Crear",
                    style = ButtonStyle
                )
            }
        }
    }
}

@Composable
private fun TarjetaSesion(
    accion: String,
    usuarioLogeado: Usuario?,
    idDM: Int?,
    idsSesionesABorrar: SnapshotStateList<Long>,
    coroutineScope: CoroutineScope,
    sesion: Sesion?,
    navController: NavController,
    sesionViewModel: SesionViewModel,
    partidaViewModel: PartidaViewModel,
    partidaUsuarioViewModel: PartidaUsuarioViewModel,
    interactionSource: MutableInteractionSource,
    borderColor: Color,
    contentColor: Color,
    containerColor: Color
) {
    val contexto = LocalContext.current
    var desplegado by remember { mutableStateOf(false) }
    var editando by remember { mutableStateOf(false) }
    val creandoSesion by sesionViewModel.creandoSesion
    val partida by partidaViewModel.partida
    val idSesion by sesionViewModel.idSesion
    var idEditar by remember { mutableStateOf<Long?>(null) }
    val fecha by sesionViewModel.fecha
    var fechaMod by remember { mutableLongStateOf(ConversorUnix.fechaYMDHMSConTAMilis(sesion?.fecha ?: "1970-01-01T00:00:00")) }
    var ultimaFechaGuardada by remember { mutableLongStateOf(ConversorUnix.fechaYMDHMSConTAMilis(sesion?.fecha ?: "1970-01-01T00:00:00")) }
    val descripcion by sesionViewModel.descripcion
    var descripcionMod by remember { mutableStateOf(sesion?.descripcion) }
    var ultimaDescripcionGuardada by remember { mutableStateOf(sesion?.descripcion) }
    val jugadoresApuntados = sesionViewModel.jugadoresApuntados
    val aventureros = partidaUsuarioViewModel.aventureros
    val mostrarDatePicker by sesionViewModel.mostrarDatePicker
    val mostrarDialTimePicker by sesionViewModel.mostrarDialTimePicker
    val modificarSesionResultado by sesionViewModel.modificarSesionResultado

    // estas variables son necesarias para actualizar la interfaz sin recargar la página
    var asiste by remember { mutableStateOf(false) }
    sesion?.jugadoresApuntados?.forEach { idJugador ->
        if (idJugador == usuarioLogeado?.id) {
            asiste = true
        }
    }
    var jugadoresTotalesApuntados = 0
    if (sesion != null) {
        jugadoresTotalesApuntados = sesion.jugadoresApuntados.size
        // se actualiza con asiste
        if (asiste) {
            jugadoresTotalesApuntados = sesion.jugadoresApuntados.size
        }
    }

    Card(
        modifier = Modifier
            .padding(
                horizontal = 16.dp, vertical = 4.dp
            )
            .combinedClickable(
                onClick = {
                    if (sesion != null) {
                        if (idsSesionesABorrar.isEmpty()) {
                            desplegado = !desplegado
                        } else {
                            if (idsSesionesABorrar.contains(sesion.id)) {
                                sesionViewModel.modificarSesionesABorrar("borrar", sesion.id)
                            } else {
                                sesionViewModel.modificarSesionesABorrar("añadir", sesion.id)
                            }
                        }
                    }
                },
                onLongClick = {
                    if (usuarioLogeado?.id == idDM && sesion != null) {
                        sesionViewModel.modificarSesionesABorrar("añadir", sesion.id)
                    }
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
        ) {
            if ((creandoSesion && sesion == null) || editando) {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp, start = 8.dp),
                    text = if (editando) "Editando..." else "Nueva sesión",
                    style = Titulo5Style
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // fecha
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp),
                        value = if (editando) ConversorUnix.milisAFechaDMY(fechaMod) else fecha.let { ConversorUnix.milisAFechaDMY(it) },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(
                            text = "Fecha"
                        ) },
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    sesionViewModel.modificarIdSesion(sesion?.id)
                                    sesionViewModel.mostrarDatePicker(true)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Fecha"
                                )
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
                            unfocusedTextColor = MaterialTheme.colorScheme.primary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            focusedContainerColor = MaterialTheme.colorScheme.background
                        ),
                        shape = CutCornerShape(16.dp),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        )
                    )

                    // hora
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp),
                        value = if (editando) ConversorUnix.milisAHoraHMS(fechaMod) else fecha.let { ConversorUnix.milisAHoraHMS(it) },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Hora") },
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    sesionViewModel.modificarIdSesion(sesion?.id)
                                    sesionViewModel.mostrarDialTimePicker(true)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = "Hora"
                                )
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
                            unfocusedTextColor = MaterialTheme.colorScheme.primary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            focusedContainerColor = MaterialTheme.colorScheme.background
                        ),
                        shape = CutCornerShape(16.dp),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        )
                    )
                }
                // descripción
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp, 4.dp),
                    value = if (editando) descripcionMod ?: "" else descripcion,
                    onValueChange = {
                        if (editando) {
                            descripcionMod = it
                        } else {
                            sesionViewModel.modificarDescripcion(it)
                        }
                    },
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
                        unfocusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background
                    ),
                    shape = CutCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )
                if (editando) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
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
                                editando = false
                                fechaMod = ultimaFechaGuardada
                                descripcionMod = ultimaDescripcionGuardada
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cancelar cambios",
                                modifier = Modifier.size(24.dp)
                            )
                        }
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
                                idEditar = sesion?.id

                                var datosValidos = true
                                if (ultimaFechaGuardada >= System.currentTimeMillis()) {
                                    // si la fecha modificada es menor que la fecha actual
                                    if (Validar.validarFechas(fechaMod, System.currentTimeMillis())) {
                                            datosValidos = false
                                            sesionViewModel.modificarModificarSesionResultado("En una sesión programada la fecha de la sesión debe ser igual o posterior a la fecha actual.")
                                        }
                                } else if (ultimaFechaGuardada < System.currentTimeMillis()) {
                                    // si la fecha modificada NO es menor que la fecha actual
                                    if (!Validar.validarFechas(fechaMod, System.currentTimeMillis())) {
                                        datosValidos = false
                                        sesionViewModel.modificarModificarSesionResultado("En una sesión pasada la fecha de la sesión debe ser anterior a la fecha actual.")
                                    }
                                } else if (!Validar.validarTexto(descripcionMod ?: "", 2000)) {
                                    datosValidos = false
                                    sesionViewModel.modificarModificarSesionResultado("La descripción no puede tener más de 2000 caracteres.")
                                }

                                if (datosValidos) {
                                    editando = false
                                    sesionViewModel.modificarSesionDatos(sesion!!, fechaMod, descripcionMod ?: "")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "Guardar cambios",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            } else {
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, top = 4.dp),
                    text = "Fecha: ${ConversorUnix.fechaYMDHMSaFechaDMYHMS(ConversorUnix.milisAFechaYMDHMS(fechaMod))}"
                )
                if (descripcionMod != null) {
                    if (desplegado) {
                        Text(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
                            text = descripcionMod.toString()
                        )
                        if (descripcionMod.toString().length >= 85) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
                                text = "Mostrar menos",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    } else {
                        Text(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
                            text = descripcionMod.toString(),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )
                        if (descripcionMod.toString().length >= 85) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
                                text = "Mostrar más...",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                } else {
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
                        text = "No se ha incluido una descripción",
                        fontSize = 14.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (System.currentTimeMillis() < ConversorUnix.fechaYMDHMSConTAMilis(sesion?.fecha!!)) {
                        Text(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
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
                            text = if (sesion.jugadoresApuntados.size == 1) "⟡ Asistió un aventurero." else "⟡ Asistieron ${sesion.jugadoresApuntados.size} aventureros.",
                            fontSize = 14.sp
                        )
                    }
                    if (usuarioLogeado?.id == partida?.idDM) {
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
                                editando = true
//                        partidaUsuarioViewModel.modificarModificandoRelacionPartidaUsuario(true)
//                        partidaUsuarioViewModel.seleccionarPersonaje(personaje)
//                        partidaUsuarioViewModel.mostrarDialogoSeleccionPersonaje(true)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar sesión",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        if (System.currentTimeMillis() < ConversorUnix.fechaYMDHMSConTAMilis(sesion.fecha)) {
                            aventureros.forEach { aventurero ->
                                if (aventurero.key.id == usuarioLogeado?.id) {
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
                                                sesionViewModel.modificarJugadoresApuntados(sesion, "eliminar", usuarioLogeado.id)
                                                Toast.makeText(contexto, "Cancelaste tu asistencia a esta sesión", Toast.LENGTH_SHORT).show()
                                            } else {
                                                sesionViewModel.modificarJugadoresApuntados(sesion, "añadir", usuarioLogeado.id)
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
            }
        }
    }

    if (idEditar == sesion?.id) {
        // requiere launched effect para esperar los cambios en la variable modificarSesionResultado del view model
        // este cambio se produce al pulsar el botón
        LaunchedEffect(modificarSesionResultado) {
            when (modificarSesionResultado) {
                "Exito" -> {
                    sesionViewModel.modificarModificarSesionResultado("")
                    ultimaFechaGuardada = fechaMod
                    ultimaDescripcionGuardada = descripcionMod
                    idEditar = null
                }
                "" -> {
                    // no hace nada pero tampoco saca el diálogo de error
                }
                else -> {
                    idEditar = null
                    partidaViewModel.mostrarDialogo(true)
                }
            }
        }
    }

    if (mostrarDatePicker && sesion?.id == idSesion) {
        DatePickerModal(
            initialMillis = if (editando) fechaMod else fecha,
            onDateSelected = { millis ->
                if (editando) {
                    fechaMod = millis ?: fecha
                } else {
                    sesionViewModel.modificarFecha(millis ?: fecha)
                }
            },
            onDismiss = {
                sesionViewModel.modificarIdSesion(sesion?.id)
                sesionViewModel.mostrarDatePicker(false)
            },
        )
    }

    if (mostrarDialTimePicker && sesion?.id == idSesion) {
        DialTimePickerModal(
            initialMillis = if (editando) fechaMod else fecha,
            sesionViewModel = sesionViewModel,
            onConfirm = { millis ->
                if (editando) {
                    fechaMod = millis
                } else {
                    sesionViewModel.modificarFecha(millis)
                }
                sesionViewModel.mostrarDialTimePicker(false)
            },
            onDismiss = {
                sesionViewModel.modificarIdSesion(sesion?.id)
                sesionViewModel.mostrarDialTimePicker(false)
            },
        )
    }
}