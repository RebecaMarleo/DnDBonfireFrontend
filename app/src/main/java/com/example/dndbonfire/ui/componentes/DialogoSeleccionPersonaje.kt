package com.example.dndbonfire.ui.componentes

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.dndbonfire.modelo.Partida
import com.example.dndbonfire.ui.theme.ButtonStyle
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.ui.theme.InformacionPrincipalStyle
import com.example.dndbonfire.ui.theme.Titulo5Style
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaUsuarioViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel

@Composable
fun DialogoSeleccionPersonaje(
    navController: NavController,
    mainViewModel: MainViewModel,
    partidaUsuarioViewModel: PartidaUsuarioViewModel,
    partida: Partida,
    personajeViewModel: PersonajeViewModel,
    onDismiss: () -> Unit
) {
    val personajes = personajeViewModel.personajes
    val personaje by partidaUsuarioViewModel.personaje
    val modificandoRelacionPartidaUsuario by partidaUsuarioViewModel.modificandoRelacionPartidaUsuario

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
                        bottom = 12.dp,
                        start = 12.dp,
                        end = 12.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .widthIn(max = 220.dp),
                        text = "Elige un personaje para participar",
                        style = InformacionPrincipalStyle
                    )
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

                if (personajes.isEmpty()) {
                    // si solo es que la lista está vacía es porque la cadena introducida no encaja con ningún personaje
                    Text(
                        text = "No se encontraron resultados"
                    )
                } else {
                    LazyColumn() {
                        itemsIndexed(personajes) { indice, personaje ->
                            ElementoSeleccionPersonaje(personaje, navController, mainViewModel, partidaUsuarioViewModel, personajeViewModel)
                        }
                    }
                }

                Button(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
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
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        disabledContainerColor = Gris
                    ),
                    shape = CutCornerShape(16.dp),
                    onClick = {
                        if (personaje != null) {
                            if (modificandoRelacionPartidaUsuario) {
                                partidaUsuarioViewModel.modificarRelacionPartidaUsuario(partida)
                            } else {
                                partidaUsuarioViewModel.crearRelacionPartidaUsuario(partida)
                            }
                        }
                    },
                    enabled = personaje != null
                ) {
                    Text(
                        text = if (modificandoRelacionPartidaUsuario) "Cambiar personaje" else "Unirse",
                        style = ButtonStyle
                    )
                }
            }
        }
    }
}