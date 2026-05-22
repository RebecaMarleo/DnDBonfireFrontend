package com.example.dndbonfire.ui.componentes

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.dndbonfire.R
import com.example.dndbonfire.data.ProveedorPersonaje
import com.example.dndbonfire.modelo.Personaje
import com.example.dndbonfire.navegacion.Ruta
import com.example.dndbonfire.ui.theme.InformacionPrincipalStyle
import com.example.dndbonfire.ui.theme.Primary
import com.example.dndbonfire.ui.theme.Titulo1Style
import com.example.dndbonfire.ui.theme.Titulo2Style
import com.example.dndbonfire.ui.theme.Titulo5Style
import com.example.dndbonfire.ui.theme.Titulo6Style
import com.example.dndbonfire.ui.theme.Transaparente
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import kotlinx.coroutines.launch

@Composable
fun ElementoListaPersonajes(
    personaje: Personaje,
    navController: NavController,
    mainViewModel: MainViewModel,
    personajeViewModel: PersonajeViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    var nuevaLetra = false
    personajeViewModel.personajesInicial.forEach { p ->
        if (personaje.id == p.id) {
            nuevaLetra = true
        }
    }
    val idsPersonajesABorrar = personajeViewModel.idsPersonajesABorrar

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                personajeViewModel.modificarPersonajeABorrar(personaje)
                mainViewModel.mostrarDialogoConfirmacionBorrado(true)
            }
            false
        }
    )

    // si el id del usuario actual está entre los ids en la lista de seleccionados lo marca como seleccionado
    var estaSeleccionado = false
    idsPersonajesABorrar.forEach { id ->
        if (id == personaje.id) estaSeleccionado = true
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
                .size(40.dp, 30.dp)
                .padding(8.dp, 0.dp)
        ) {
            Text(
                text = if (personaje.nombre == "Sin nombre") "-" else personaje.nombre?.first().toString(),
                maxLines = 1,
                style = Titulo2Style,
                color = if (nuevaLetra) {
                    Primary
                } else {
                    Transaparente
                },
                overflow = TextOverflow.Visible
            )
        }

        SwipeToDismissBox(
            state = dismissState,
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = true,
            backgroundContent = {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp),
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
            Card(
                modifier = Modifier
                    .padding(
                        start = 16.dp
                    )
                    .combinedClickable(
                        onClick = {
                            if (idsPersonajesABorrar.isEmpty()) {
                                coroutineScope.launch {
                                    ProveedorPersonaje.cargarPersonaje(personaje.id)
                                }
                                navController.navigate(Ruta.Personaje(personaje.id))
                            } else {
                                if (idsPersonajesABorrar.contains(personaje.id)) {
                                    personajeViewModel.modificarPersonajesABorrar("borrar", personaje.id)
                                } else {
                                    personajeViewModel.modificarPersonajesABorrar("añadir", personaje.id)
                                }
                            }
                        },
                        onLongClick = {
                            personajeViewModel.modificarPersonajesABorrar("añadir", personaje.id)
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
                    AsyncImage(
                        model = personaje.imagen?.let { "data:image/jpg;base64,$it" } ?: R.drawable.avatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp)
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
                            style = Titulo6Style,
                            text = personaje.nombre!!
                        )
                        Row(
                            modifier = Modifier
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = personaje.raza!!,
                                modifier = Modifier
                                    .widthIn(max = 100.dp)
                                    .padding(end = 8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                modifier = Modifier
                                    .widthIn(max = 150.dp),
                                fontSize = 14.sp,
                                text = personaje.subraza!!,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
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
                                        .widthIn(max = 80.dp)
                                        .padding(end = 8.dp),
                                    text = personaje.clase!!,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    modifier = Modifier
                                        .widthIn(max = 80.dp),
                                    fontSize = 14.sp,
                                    text = personaje.subclase!!,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Text(
                                modifier = Modifier
                                    .widthIn(max = 100.dp),
                                text = "Nvl: ${personaje.nivel}",
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