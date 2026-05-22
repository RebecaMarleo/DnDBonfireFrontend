package com.example.dndbonfire.ui.componentes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.dndbonfire.R
import com.example.dndbonfire.data.ProveedorPersonaje
import com.example.dndbonfire.modelo.Personaje
import com.example.dndbonfire.navegacion.Ruta
import com.example.dndbonfire.ui.theme.Primary
import com.example.dndbonfire.ui.theme.Titulo3Style
import com.example.dndbonfire.ui.theme.Titulo5Style
import com.example.dndbonfire.ui.theme.Transaparente
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaUsuarioViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import kotlinx.coroutines.launch

@Composable
fun ElementoSeleccionPersonaje(
    personaje: Personaje,
    navController: NavController,
    mainViewModel: MainViewModel,
    partidaUsuarioViewModel: PartidaUsuarioViewModel,
    personajeViewModel: PersonajeViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val personajeSeleccionado by partidaUsuarioViewModel.personaje
    var nuevaLetra = false
    personajeViewModel.personajesInicial.forEach { p ->
        if (personaje.id == p.id) {
            nuevaLetra = true
        }
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // si el id del usuario actual está entre los ids en la lista de seleccionados lo marca como seleccionado
    val contentColor by animateColorAsState(
        targetValue = if (isPressed)
            MaterialTheme.colorScheme.onSecondary
        else
            MaterialTheme.colorScheme.onPrimary,
        animationSpec = tween(200)
    )
    val containerColor by animateColorAsState(
        targetValue = if (isPressed)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.primary,
        animationSpec = tween(200)
    )
    val borderColor by animateColorAsState(
        targetValue = if (isPressed)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.tertiary
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 0.dp,
                vertical = 4.dp
            )
    ) {
        Box(
            modifier = Modifier
                .size(20.dp, 20.dp)
                .padding(0.dp, 0.dp)
        ) {
            Text(
                text = if (personaje.nombre == "Sin nombre") "-" else personaje.nombre?.first().toString(),
                style = Titulo3Style,
                color = if (nuevaLetra) {
                    Primary
                } else {
                    Transaparente
                },
                overflow = TextOverflow.Visible
            )
        }

        Card(
            modifier = Modifier
                .padding(
                    start = 16.dp
                )
                .combinedClickable(
                    onClick = {
                        partidaUsuarioViewModel.seleccionarPersonaje(personaje)
                    },
                    onLongClick = {
                        coroutineScope.launch {
                            ProveedorPersonaje.cargarPersonaje(personaje.id)
                        }
                        navController.navigate(Ruta.Personaje(personaje.id))
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
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = personaje.imagen?.let { "data:image/jpg;base64,$it" } ?: R.drawable.avatar,
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
                    Text(
                        modifier = Modifier
                            .padding(
                                start = 8.dp,
                                end = 0.dp,
                                top = 8.dp,
                                bottom = 8.dp
                            ),
                        style = Titulo5Style,
                        text = personaje.nombre!!
                    )
                }
                Checkbox(
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.secondary,
                        uncheckedColor = MaterialTheme.colorScheme.tertiary,
                        checkmarkColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    checked = personaje.id == personajeSeleccionado?.id,
                    onCheckedChange = {
                        partidaUsuarioViewModel.seleccionarPersonaje(personaje)
                    }
                )
            }
        }
    }
}