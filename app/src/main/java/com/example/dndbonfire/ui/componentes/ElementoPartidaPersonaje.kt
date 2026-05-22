package com.example.dndbonfire.ui.componentes

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.dndbonfire.R
import com.example.dndbonfire.data.ProveedorPersonaje
import com.example.dndbonfire.modelo.Aventurero
import com.example.dndbonfire.modelo.Personaje
import com.example.dndbonfire.modelo.Usuario
import com.example.dndbonfire.navegacion.Ruta
import com.example.dndbonfire.ui.theme.InformacionPrincipalStyle
import com.example.dndbonfire.ui.theme.Titulo5Style
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaUsuarioViewModel
import com.example.dndbonfire.viewmodel.PartidaViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.Int

@Composable
fun ElementoPartidaPersonaje(
    aventurero: Map.Entry<Usuario, Personaje>,
    navController: NavController,
    mainViewModel: MainViewModel,
    partidaUsuarioViewModel: PartidaUsuarioViewModel,
    partidaViewModel: PartidaViewModel,
    personajeViewModel: PersonajeViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val usuarioLogeado by mainViewModel.usuarioLogeado
    val idDM = partidaViewModel.partida.value?.idDM
    val idsAventurerosABorrar = partidaUsuarioViewModel.idsAventurerosABorrar

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                partidaUsuarioViewModel.modificarAventureroABorrar(Aventurero(aventurero.key, aventurero.value))
                mainViewModel.mostrarDialogoConfirmacionBorrado(true)
            }
            false
        }
    )

    // si el id del usuario actual está entre los ids en la lista de seleccionados lo marca como seleccionado
    var estaSeleccionado = false
    idsAventurerosABorrar.forEach { id ->
        if (id == aventurero.key.id) estaSeleccionado = true
    }
    val contentColor by animateColorAsState(
        targetValue = if (isPressed || estaSeleccionado)
            MaterialTheme.colorScheme.onSecondary
        else
            MaterialTheme.colorScheme.onPrimaryContainer,
        animationSpec = tween(200)
    )
    val containerColor by animateColorAsState(
        targetValue = if (isPressed || estaSeleccionado)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.primaryContainer,
        animationSpec = tween(200)
    )
    val borderColor by animateColorAsState(
        targetValue = if (isPressed || estaSeleccionado)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.tertiary
    )

    if (usuarioLogeado?.id == idDM) {
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
            TarjetaPersonaje(
                usuarioLogeado,
                idDM,
                idsAventurerosABorrar,
                coroutineScope,
                aventurero.value,
                aventurero.key,
                navController,
                partidaUsuarioViewModel,
                partidaViewModel,
                interactionSource,
                borderColor,
                contentColor,
                containerColor
            )
        }
    } else {
        TarjetaPersonaje(
            usuarioLogeado,
            idDM,
            idsAventurerosABorrar,
            coroutineScope,
            aventurero.value,
            aventurero.key,
            navController,
            partidaUsuarioViewModel,
            partidaViewModel,
            interactionSource,
            borderColor,
            contentColor,
            containerColor
        )
    }
}

@Composable
private fun TarjetaPersonaje(
    usuarioLogeado: Usuario?,
    idDM: Int?,
    idsAventurerosABorrar: SnapshotStateList<Int>,
    coroutineScope: CoroutineScope,
    personaje: Personaje,
    usuario: Usuario,
    navController: NavController,
    partidaUsuarioViewModel: PartidaUsuarioViewModel,
    partidaViewModel: PartidaViewModel,
    interactionSource: MutableInteractionSource,
    borderColor: Color,
    contentColor: Color,
    containerColor: Color
) {
    var desplegado by remember { mutableStateOf(false) }
    val partida by partidaViewModel.partida
    val modificandoRelacionPartidaUsuario by partidaUsuarioViewModel.modificandoRelacionPartidaUsuario

    val contentColorIcono by animateColorAsState(
        targetValue = if (desplegado)
            MaterialTheme.colorScheme.onSecondary
        else
            MaterialTheme.colorScheme.onPrimary,
        animationSpec = tween(200)
    )
    val containerColorIcono by animateColorAsState(
        targetValue = if (desplegado)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.primary,
        animationSpec = tween(200)
    )

    val contentColorEditar by animateColorAsState(
        targetValue = if (modificandoRelacionPartidaUsuario)
            MaterialTheme.colorScheme.onSecondary
        else
            MaterialTheme.colorScheme.onPrimary,
        animationSpec = tween(200)
    )
    val containerColorEditar by animateColorAsState(
        targetValue = if (modificandoRelacionPartidaUsuario)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.primary,
        animationSpec = tween(200)
    )

    Card(
        modifier = Modifier
            .padding(
                horizontal = 16.dp, vertical = 4.dp
            )
            .combinedClickable(
                onClick = {
                    if (idsAventurerosABorrar.isEmpty()) {
                        if (
                            usuario.id == usuarioLogeado?.id
                            || partida?.idDM == usuarioLogeado?.id
                            || partida?.infoVisible == true
                        ) {
                            coroutineScope.launch {
                                ProveedorPersonaje.cargarPersonaje(personaje.id)
                            }
                            navController.navigate(Ruta.Personaje(personaje.id))
                        }
                    } else {
                        if (idsAventurerosABorrar.contains(usuario.id)) {
                            partidaUsuarioViewModel.modificarAventurerosABorrar("borrar", usuario.id)
                        } else {
                            partidaUsuarioViewModel.modificarAventurerosABorrar("añadir", usuario.id)
                        }
                    }
                },
                onLongClick = {
                    if (usuarioLogeado?.id == idDM) {
                        partidaUsuarioViewModel.modificarAventurerosABorrar("añadir", usuario.id)
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
        Column() {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .width(IntrinsicSize.Min)
            ) {
                AsyncImage(
                    model = personaje.imagen?.let { "data:image/jpg;base64,$it" } ?: R.drawable.avatar,
                    contentDescription = null,
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CutCornerShape(8.dp))
                        .border(
                            width = 2.dp,
                            color = borderColor,
                            shape = CutCornerShape(8.dp)
                        ),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
                Column(
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column() {
                            Text(
                                style = Titulo5Style,
                                text = personaje.nombre!!
                            )
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = personaje.raza!!,
                                    style = InformacionPrincipalStyle,
                                    modifier = Modifier
                                        .widthIn(max = 70.dp)
                                        .padding(end = 8.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    modifier = Modifier
                                        .widthIn(max = 70.dp),
                                    text = personaje.subraza!!,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Row() {
                            if (usuarioLogeado?.id == usuario.id) {
                                IconButton(
                                    modifier = Modifier
                                        .clip(CutCornerShape(30.dp)),
                                    colors = IconButtonColors(
                                        containerColor = containerColorEditar,
                                        contentColor = contentColorEditar,
                                        disabledContainerColor = MaterialTheme.colorScheme.primary,
                                        disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                                    ),
                                    onClick = {
                                        partidaUsuarioViewModel.modificarModificandoRelacionPartidaUsuario(true)
                                        partidaUsuarioViewModel.seleccionarPersonaje(personaje)
                                        partidaUsuarioViewModel.mostrarDialogoSeleccionPersonaje(true)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Cambiar personaje",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            IconButton(
                                modifier = Modifier
                                    .clip(CutCornerShape(30.dp)),
                                colors = IconButtonColors(
                                    containerColor = containerColorIcono,
                                    contentColor = contentColorIcono,
                                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                                    disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                                ),
                                onClick = {
                                    desplegado = !desplegado
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Información sobre el jugador",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .widthIn(max = 100.dp)
                                    .padding(end = 16.dp),
                                text = personaje.clase!!,
                                style = InformacionPrincipalStyle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                modifier = Modifier
                                    .widthIn(max = 150.dp),
                                text = personaje.subclase!!,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Text(
                            modifier = Modifier
                                .widthIn(max = 100.dp),
                            text = "Nvl: ${personaje.nivel}",
                            style = InformacionPrincipalStyle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            if (desplegado) {
                HorizontalDivider(
                    thickness = 3.dp,
                    color = borderColor,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 4.dp, start = 10.dp)
                )
                HorizontalDivider(
                    thickness = 2.dp,
                    color = borderColor,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 10.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = usuario.imagen?.let { "data:image/jpg;base64,$it" } ?: R.drawable.avatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CutCornerShape(8.dp))
                            .border(
                                width = 2.dp,
                                color = borderColor,
                                shape = CutCornerShape(8.dp)
                            ),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                    Column(
                        modifier = Modifier
                            .padding(
                                start = 8.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 8.dp
                            )
                    ) {
                        Text(
                            style = Titulo5Style,
                            text = usuario.nombre
                        )
                        Row(
                            modifier = Modifier
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "@${usuario.username}",
                                modifier = Modifier
                                    .widthIn(max = 100.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp),
                                text = "-"
                            )
                            Text(
                                modifier = Modifier
                                    .widthIn(max = 200.dp),
                                text = when (usuario.rango) {
                                    1 -> "Aventurero novato"
                                    2 -> "Aventurero intermedio"
                                    3 -> "Aventurero experimentado"
                                    4 -> "Aventurero legendario"
                                    else -> ""
                                },
                                style = InformacionPrincipalStyle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}