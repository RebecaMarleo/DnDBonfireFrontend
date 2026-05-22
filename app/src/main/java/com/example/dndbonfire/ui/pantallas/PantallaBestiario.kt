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
import com.example.dndbonfire.ui.componentes.ElementoBestiario
import com.example.dndbonfire.ui.theme.InformacionSecundariaStyle
import com.example.dndbonfire.viewmodel.BestiaViewModel

@Composable
fun PantallaBestiario(
    navController: NavController,
    bestiaViewModel: BestiaViewModel
) {
    LaunchedEffect("") {
        bestiaViewModel.cargarBestia(null)
    }

    var textoBusqueda by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (bestiaViewModel.bestias.isEmpty() && textoBusqueda.isEmpty()) {
            // si no hay elementos en la lista de bestias y la búsqueda está vacía es porque no se han cargado
            // aun las traducciones por lo que muestra un gif de espera y recarga la página cuando sea necesario
            bestiaViewModel.filtrarBestias("")
            AsyncImage(
                model = R.drawable.cargando,
                contentDescription = "GIF de carga",
                modifier = Modifier
                    .size(200.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                text = "Cargando bestiario...",
                style = InformacionSecundariaStyle,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
                text = "Si esta operación tarda demasiado comprueba tu conexión a internet.",
                style = InformacionSecundariaStyle
            )
        } else {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 4.dp),
                value = textoBusqueda,
                onValueChange = {
                    textoBusqueda = it
                    bestiaViewModel.filtrarBestias(textoBusqueda)
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

            if (bestiaViewModel.bestias.isEmpty()) {
                // si solo es que la lista está vacía es porque la cadena introducida no encaja con ninguna bestia
                Text(
                    text = "No se encontraron resultados"
                )
            } else {
                // en cualquier otro caso se carga la lista necesaria
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    itemsIndexed(bestiaViewModel.bestias) { indice, bestia ->
                        ElementoBestiario(bestia, navController, bestiaViewModel)
                    }
                }
            }
        }
    }
}