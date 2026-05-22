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
import com.example.dndbonfire.ui.componentes.DialogoConfirmacionBorrado
import com.example.dndbonfire.ui.componentes.DialogoPersonaje
import com.example.dndbonfire.ui.componentes.ElementoListaPersonajes
import com.example.dndbonfire.ui.theme.InformacionSecundariaStyle
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel

@Composable
fun PantallaPersonajes(
    navController: NavController,
    mainViewModel: MainViewModel,
    personajeViewModel: PersonajeViewModel,
) {
    LaunchedEffect("") {
        personajeViewModel.cargarPersonaje(null)
    }

    val usuario by mainViewModel.usuarioLogeado
    val personajes = personajeViewModel.personajes
    val sinPersonajes by personajeViewModel.sinPersonajes
    var textoBusqueda by rememberSaveable { mutableStateOf("") }
    val personajeABorrar by personajeViewModel.personajeABorrar
    val idsPersonajesABorrar = personajeViewModel.idsPersonajesABorrar
    val mostrarDialogoConfirmacionBorrado by mainViewModel.mostrarDialogoConfirmacionBorrado
    val mostrarDialogo by personajeViewModel.mostrarDialogo
    val eliminarPersonajeResultado by personajeViewModel.eliminarPersonajeResultado
    val eliminarPersonajesMultiplesResultado by personajeViewModel.eliminarPersonajesMultiplesResultado

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
                text = "Inicia sesión para ver tus personajes.",
                style = InformacionSecundariaStyle,
                textAlign = TextAlign.Center
            )
        } else if (sinPersonajes) {
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
                text = "Aun no tienes ningún personaje. ¡Pulsa el botón en la esquina inferior derecha para crear tu primer personaje!",
                style = InformacionSecundariaStyle,
                textAlign = TextAlign.Center
            )
        } else if (personajes.isEmpty() && textoBusqueda.isEmpty()) {
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
                text = "Cargando tus personajes...",
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
            personajeViewModel.cargarPersonajes()
        } else {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 4.dp),
                value = textoBusqueda,
                onValueChange = {
                    textoBusqueda = it
                    personajeViewModel.filtrarPersonajes(textoBusqueda)
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

            if (personajes.isEmpty()) {
                // si solo es que la lista está vacía es porque la cadena introducida no encaja con ningún personaje
                Text(
                    text = "No se encontraron resultados"
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    itemsIndexed(personajes) { indice, personaje ->
                        ElementoListaPersonajes(personaje, navController, mainViewModel, personajeViewModel)
                    }
                }
            }
        }
    }

    if (mostrarDialogoConfirmacionBorrado) {
        DialogoConfirmacionBorrado(
            titulo = if (idsPersonajesABorrar.size > 1) "Eliminar personajes" else "Eliminar personaje",
            mensaje = if (idsPersonajesABorrar.size > 1) {
                "Estás a punto de borrar a los ${idsPersonajesABorrar.size} personajes seleccionados. Esta acción no es reversible, ¿deseas continuar?"
            } else if (idsPersonajesABorrar.size == 1) {
                "Estás a punto de borrar al personaje seleccionado. Esta acción no es reversible, ¿deseas continuar?"
            } else {
                "Estás a punto de borrar a ${personajeABorrar?.nombre}. Esta acción no es reversible, ¿deseas continuar?"
            },
            personaje = personajeABorrar,
            idsPersonajes = idsPersonajesABorrar,
            mainViewModel = mainViewModel,
            personajeViewModel = personajeViewModel,
            navController = navController,
            onDismiss = {
                personajeViewModel.modificarPersonajeABorrar(null)
                mainViewModel.mostrarDialogoConfirmacionBorrado(false)
            }
        )
    }

    if (mostrarDialogo) {
        DialogoPersonaje(
            personajeViewModel = personajeViewModel,
            resultadoBorrarRelacion = eliminarPersonajeResultado,
            resultadoBorrarRelaciones = eliminarPersonajesMultiplesResultado,
            onDismiss = {
                personajeViewModel.modificarEliminarPersonajeResultado("")
                personajeViewModel.modificarEliminarPersonajesMultiplesResultado("")
                personajeViewModel.cargarDescripcion("")
                personajeViewModel.mostrarDialogo(false)
            }
        )
    }
}