package com.example.dndbonfire.ui.pantallas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.dndbonfire.R
import com.example.dndbonfire.ui.componentes.ElementoListaPartidas
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.ui.theme.InformacionSecundariaStyle
import com.example.dndbonfire.ui.theme.Transaparente
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaUsuarioViewModel
import com.example.dndbonfire.viewmodel.PartidaViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import com.example.dndbonfire.viewmodel.SesionViewModel
import com.example.dndbonfire.viewmodel.UsuarioViewModel

@Composable
fun PantallaPartidas(
    navController: NavController,
    mainViewModel: MainViewModel,
    partidaViewModel: PartidaViewModel,
    partidaUsuarioViewModel: PartidaUsuarioViewModel,
    usuarioViewModel: UsuarioViewModel,
    personajeViewModel: PersonajeViewModel,
    sesionViewModel: SesionViewModel
) {
    LaunchedEffect("") {
        partidaViewModel.cargarPartida(null)
    }

    val usuario by mainViewModel.usuarioLogeado
    val partidasPropias = partidaViewModel.partidasPropias
    val partidasUnidas = partidaViewModel.partidasUnidas
    val partidasDisponibles = partidaViewModel.partidasDisponibles
    val sinPartidas by partidaViewModel.sinPartidas
    var textoBusqueda by rememberSaveable { mutableStateOf("") }

    val selectedIndex by partidaViewModel.selectedIndex
    val options = listOf("Dirigidas", "Tus aventuras", "Explorar")

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
                text = "Inicia sesión para ver las partidas.",
                style = InformacionSecundariaStyle,
                textAlign = TextAlign.Center
            )
        } else if (sinPartidas) {
            AsyncImage(
                model = R.drawable.crear,
                contentDescription = "Don Fire apunta FAB",
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Parece que aun no hay campañas disponibles. ¡Sé el primero en crear una aventura!",
                style = InformacionSecundariaStyle,
                textAlign = TextAlign.Center
            )
        } else if (partidasPropias.isEmpty() && partidasUnidas.isEmpty() && partidasDisponibles.isEmpty() && textoBusqueda.isEmpty()) {
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
                text = "Cargando partidas...",
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
            partidaViewModel.cargarPartidas()
        } else {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 4.dp),
                value = textoBusqueda,
                onValueChange = {
                    textoBusqueda = it
                    partidaViewModel.filtrarPartidas(textoBusqueda)
                },
                label = {
                    Text(text = "Buscar")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
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

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size,
                            baseShape = CutCornerShape(8.dp)
                        ),
                        colors = SegmentedButtonColors(
                            activeBorderColor = MaterialTheme.colorScheme.secondary,
                            activeContentColor = MaterialTheme.colorScheme.onSecondary,
                            inactiveBorderColor = MaterialTheme.colorScheme.primary,
                            activeContainerColor = MaterialTheme.colorScheme.secondary,
                            inactiveContentColor = MaterialTheme.colorScheme.primary,
                            inactiveContainerColor = Transaparente,
                            disabledActiveBorderColor = Gris,
                            disabledActiveContentColor = MaterialTheme.colorScheme.background,
                            disabledInactiveBorderColor = Gris,
                            disabledActiveContainerColor = Gris,
                            disabledInactiveContentColor = Gris,
                            disabledInactiveContainerColor = Transaparente
                        ),
                        onClick = { partidaViewModel.modificarSelectedIndex(index) },
                        selected = index == selectedIndex,
                        label = { Text(label) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // partidas en las que el usuario es DM
                if (selectedIndex == 0) {
                    if (partidasPropias.isEmpty()) {
                        item {
                            AsyncImage(
                                model = R.drawable.crear,
                                contentDescription = "Don Fire apunta FAB",
                                modifier = Modifier
                                    .size(200.dp)
                                    .padding(top = 8.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        item {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                text = "Aun no diriges ninguna partida. ¡Pulsa el botón en la esquina inferior derecha para crear tu primera partida!",
                                style = InformacionSecundariaStyle,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        itemsIndexed(partidasPropias) { indice, partida ->
                            ElementoListaPartidas(partida, navController, mainViewModel, partidaViewModel, partidaUsuarioViewModel, usuarioViewModel, personajeViewModel, sesionViewModel)
                        }
                    }
                }

                // partidas en las que el usuario juega
                if (selectedIndex == 1) {
                    if (partidasUnidas.isEmpty()) {
                        item {
                            AsyncImage(
                                model = R.drawable.iniciarsesion,
                                contentDescription = "Don Fire apunta arriba",
                                modifier = Modifier
                                    .size(200.dp)
                                    .padding(top = 8.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        item {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                text = "No te has unido a ninguna partida. ¡Explora las partidas que han creado otros aventureros!",
                                style = InformacionSecundariaStyle,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        itemsIndexed(partidasUnidas) { indice, partida ->
                            ElementoListaPartidas(partida, navController, mainViewModel, partidaViewModel, partidaUsuarioViewModel, usuarioViewModel, personajeViewModel, sesionViewModel)
                        }
                    }
                }

                // partidas a las que se puede apuntar el usuario
                if (selectedIndex == 2) {
                    if (partidasDisponibles.isEmpty()) {
                        item {
//                            AsyncImage(
//                                model = R.drawable.crear,
//                                contentDescription = "Don Fire apunta FAB",
//                                modifier = Modifier
//                                    .size(200.dp)
//                                    .padding(top = 8.dp),
//                                contentScale = ContentScale.Fit
//                            )
                        }
                        item {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                text = "Parece que aun no hay campañas disponibles...",
                                style = InformacionSecundariaStyle,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        itemsIndexed(partidasDisponibles) { indice, partida ->
                            ElementoListaPartidas(partida, navController, mainViewModel, partidaViewModel, partidaUsuarioViewModel, usuarioViewModel, personajeViewModel, sesionViewModel)
                        }
                    }
                }
            }
        }
    }
}