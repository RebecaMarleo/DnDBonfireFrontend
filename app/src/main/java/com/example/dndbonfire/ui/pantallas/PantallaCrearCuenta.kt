package com.example.dndbonfire.ui.pantallas

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dndbonfire.ui.componentes.DialogoCuenta
import com.example.dndbonfire.ui.theme.ButtonStyle
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.util.Validar
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import com.example.dndbonfire.viewmodel.SesionViewModel
import com.example.dndbonfire.viewmodel.UsuarioViewModel

@Composable
fun PantallaCrearCuenta(
    navController: NavController,
    mainViewModel: MainViewModel,
    usuarioViewModel: UsuarioViewModel,
    partidaViewModel: PartidaViewModel,
    personajeViewModel: PersonajeViewModel,
    sesionViewModel: SesionViewModel
) {
    val nombre by usuarioViewModel.nombre
    val username by usuarioViewModel.username
    val contrasena by usuarioViewModel.contrasena
    val contrasenaRe by usuarioViewModel.contrasenaRe
    val mantenerseLogeado by usuarioViewModel.mantenerseLogeado
    val resultado by mainViewModel.crearCuentaResultado
    val usuarioLogeado by mainViewModel.usuarioLogeado
    val mostrarDialogo by usuarioViewModel.mostrarDialogoCuenta

    var mostrarContrasena by remember { mutableStateOf(false) }
    var mostrarContrasenaRe by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = nombre,
            onValueChange = { usuarioViewModel.modificarNombre(it) },
            label = { Text(text = "Tu nombre") },
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

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = username,
            onValueChange = { usuarioViewModel.modificarUsername(it) },
            label = { Text(text = "Nombre de usuario") },
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

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = contrasena,
            onValueChange = { usuarioViewModel.modificarContrasena(it) },
            visualTransformation = if (mostrarContrasena) VisualTransformation.None
            else PasswordVisualTransformation(),
            label = { Text(text = "Contraseña") },
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
                focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary
            ),
            shape = CutCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(
                    onClick = {
                        mostrarContrasena = !mostrarContrasena
                    }
                ) {
                    Icon(
                        imageVector = if (mostrarContrasena) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Guardar cambios",
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = contrasenaRe,
            onValueChange = { usuarioViewModel.modificarContrasenaRe(it) },
            visualTransformation = if (mostrarContrasenaRe) VisualTransformation.None
            else PasswordVisualTransformation(),
            label = { Text(text = "Repite la contraseña") },
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
                focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary
            ),
            shape = CutCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(
                    onClick = {
                        mostrarContrasenaRe = !mostrarContrasenaRe
                    }
                ) {
                    Icon(
                        imageVector = if (mostrarContrasenaRe) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Guardar cambios",
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.secondary,
                    uncheckedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = MaterialTheme.colorScheme.onSecondary
                ),
                checked = mantenerseLogeado,
                onCheckedChange = {
                    usuarioViewModel.cambiarEstadoMantenerseLogeado(it)
                }
            )
            Text(text = "Mantener la sesión iniciada")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
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
                if (nombre.isEmpty() || username.isEmpty() || contrasena.isEmpty()) {
                    mainViewModel.modificarCrearCuentaResultado("Hay campos vacíos.")

                    usuarioViewModel.mostrarDialogo(true)
                } else {
                    var datosValidos = true
                    if (!Validar.validarUsername(username)) {
                        datosValidos = false
                        mainViewModel.modificarCrearCuentaResultado("El nombre de usuario debe tener entre 3 y 20 caracteres. Además sólo puede contener letras mayúsculas y minúsculas, números y barra bajas.")
                    } else if (!Validar.validarTexto(username, 20)) {
                        datosValidos = false
                        mainViewModel.modificarCrearCuentaResultado("El nombre de usuario no puede tener más de 20 caracteres.")
                    } else if (!Validar.validarTexto(nombre, 200)) {
                        datosValidos = false
                        mainViewModel.modificarCrearCuentaResultado("El nombre no puede tener más de 200 caracteres.")
                    } else if (!Validar.validarContrasena(contrasena)) {
                        datosValidos = false
                        mainViewModel.modificarCrearCuentaResultado("La contraseña debe tener entre 6 y 30 caracteres. Además debe contener como mínimo una mayúscula, una minúscula, un número y un caracter especial.")
                    } else if (contrasena.trim() != "" && !Validar.validarRecontrasena(contrasena, contrasenaRe)) {
                        datosValidos = false
                        mainViewModel.modificarCrearCuentaResultado("Las contraseñas no coinciden.")
                    } else if (!Validar.validarTexto(contrasena, 30)) {
                        datosValidos = false
                        mainViewModel.modificarCrearCuentaResultado("La contraseña no puede tener más de 30 caracteres.")
                    }
                    if (datosValidos) {
                        mainViewModel.crearUsuario(nombre, username, contrasena)
                    }
                }
            }
        ) {
            Text(
                text = "Crear cuenta",
                style = ButtonStyle
            )
        }

        // requiere launched effect para esperar los cambios en la variable loginResultado del view model
        // este cambio se produce al pulsar el botón
        LaunchedEffect(resultado) {
            when (resultado) {
                "Exito" -> {
                    usuarioViewModel.modificarNombre("")
                    usuarioViewModel.modificarUsername("")
                    usuarioViewModel.modificarContrasena("")
                    usuarioViewModel.modificarContrasenaRe("")
                    usuarioViewModel.cambiarEstadoMantenerseLogeado(false)
                    mainViewModel.modificarCrearCuentaResultado("")
                }
                "" -> {
                    // no hace nada pero tampoco saca el diálogo de error
                }
                else -> {
                    usuarioViewModel.mostrarDialogo(true)
                }
            }
        }

        // requiere launched effect para esperar los cambios en la variable usuarioLogeado del view model
        // este cambio se produce al pulsar el botón
        LaunchedEffect(usuarioLogeado) {
            when (usuarioLogeado) {
                null -> {
                    // no hace nada pero tampoco saca el diálogo de error
                }
                else -> {
                    navController.popBackStack()
                    navController.popBackStack()

                    // carga los datos del usuario
                    partidaViewModel.recargarPartidas()
                    personajeViewModel.recargarPersonajes()
                    sesionViewModel.recargarSesionesCalendario(usuarioLogeado!!)
                }
            }
        }
    }

    if (mostrarDialogo) {
        DialogoCuenta(
            resultado = resultado,
            onDismiss = {
                mainViewModel.modificarCrearCuentaResultado("")
                usuarioViewModel.mostrarDialogo(false)
            }
        )
    }
}