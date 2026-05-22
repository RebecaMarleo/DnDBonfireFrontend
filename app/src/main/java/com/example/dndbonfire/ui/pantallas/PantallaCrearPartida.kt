package com.example.dndbonfire.ui.pantallas

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dndbonfire.ui.componentes.DatePickerModal
import com.example.dndbonfire.ui.componentes.DialogoPartida
import com.example.dndbonfire.ui.theme.ButtonStyle
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.ui.theme.InformacionSecundariaStyle
import com.example.dndbonfire.ui.theme.Titulo4Style
import com.example.dndbonfire.util.ConversorUnix
import com.example.dndbonfire.util.Validar
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaUsuarioViewModel
import com.example.dndbonfire.viewmodel.PartidaViewModel

@Composable
fun PantallaCrearPartida(
    navController: NavController,
    mainViewModel: MainViewModel,
    partidaViewModel: PartidaViewModel,
    partidaUsuarioViewModel: PartidaUsuarioViewModel
) {
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
    val crearPartidaResultado by partidaViewModel.crearPartidaResultado
    val mostrarDialogo by partidaViewModel.mostrarDialogo
    val mostrarDatePicker by partidaViewModel.mostrarDatePicker

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
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
                    .padding(end = 4.dp),
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

        Button(
            modifier = Modifier
                .padding(vertical = 16.dp)
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
                var partidaValida = true
                if (!Validar.validarTexto(titulo, 150)) {
                    partidaValida = false
                    partidaViewModel.modificarCrearPartidaResultado("El título no puede tener más de 150 caracteres.")
                    partidaViewModel.mostrarDialogo(true)
                } else if (!Validar.validarTexto(descripcion, 2000)) {
                    partidaValida = false
                    partidaViewModel.modificarCrearPartidaResultado("La descripción no puede tener más de 2000 caracteres.")
                    partidaViewModel.mostrarDialogo(true)
                } else if (!Validar.validarNumJugadores(numMinJugadores, numMaxJugadores)) {
                    partidaValida = false
                    partidaViewModel.modificarCrearPartidaResultado("El número máximo de jugadores no puede ser menor al número mínimo de jugadores.")
                    partidaViewModel.mostrarDialogo(true)
                } else if (!Validar.validarRangoJugadores(rangoMinJugadores, rangoMaxJugadores)) {
                    partidaValida = false
                    partidaViewModel.modificarCrearPartidaResultado("El rango máximo de jugadores no puede ser menor al rango mínimo de jugadores.")
                    partidaViewModel.mostrarDialogo(true)
                } else if (fechaInicio != null && fechaFinalizacion != null) {
                    if (!Validar.validarFechas(fechaInicio!!, fechaFinalizacion!!)) {
                        partidaValida = false
                        partidaViewModel.modificarCrearPartidaResultado("La fecha de inicio no puede ser posterior a la fecha de finalización.")
                        partidaViewModel.mostrarDialogo(true)
                    }
                } else if (!Validar.validarFinalizada(fechaFinalizacion, finalizada)) {
                    partidaValida = false
                    partidaViewModel.modificarCrearPartidaResultado("Si la campaña ha finalizado, debe tener una fecha de finalización posterior al día actual.")
                    partidaViewModel.mostrarDialogo(true)
                }

                if (partidaValida) {
                    partidaViewModel.crearPartida(titulo, descripcion, numMinJugadores, numMaxJugadores, rangoMinJugadores, rangoMaxJugadores, fechaInicio, fechaFinalizacion, finalizada, privada, infoVisible)
                }
            }
        ) {
            Text(
                text = "Crear partida",
                style = ButtonStyle
            )
        }

        // requiere launched effect para esperar los cambios en la variable crearPartidaResultado del view model
        // este cambio se produce al pulsar el botón
        LaunchedEffect(crearPartidaResultado) {
            when (crearPartidaResultado) {
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
                    partidaViewModel.modificarPrivada(false)
                    partidaViewModel.modificarInfoVisible(false)

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

    if (mostrarDialogo) {
        DialogoPartida(
            resultado = crearPartidaResultado,
            onDismiss = {
                partidaViewModel.modificarCrearPartidaResultado("")
                partidaViewModel.mostrarDialogo(false)
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