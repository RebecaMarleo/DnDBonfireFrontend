package com.example.dndbonfire.ui.pantallas

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dndbonfire.navegacion.Ruta
import com.example.dndbonfire.ui.componentes.DialogoCuenta
import com.example.dndbonfire.ui.theme.ButtonStyle
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import com.example.dndbonfire.viewmodel.UsuarioViewModel
import com.example.dndbonfire.viewmodel.SesionViewModel

@Composable
fun PantallaIniciarSesion(
    navController: NavController,
    mainViewModel: MainViewModel,
    usuarioViewModel: UsuarioViewModel,
    partidaViewModel: PartidaViewModel,
    personajeViewModel: PersonajeViewModel,
    sesionViewModel: SesionViewModel
) {
    val username by usuarioViewModel.username
    val contrasena by usuarioViewModel.contrasena
    val mantenerseLogeado by usuarioViewModel.mantenerseLogeado
    val resultado by mainViewModel.loginResultado
    val usuarioLogeado by mainViewModel.usuarioLogeado
    val mostrarDialogo by usuarioViewModel.mostrarDialogoCuenta

    var mostrarContrasena by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
            onValueChange = {
                usuarioViewModel.modificarContrasena(it)
            },
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
                if (username.isEmpty() || contrasena.isEmpty()) {
                    mainViewModel.modificarLoginResultado("Hay campos vacíos.")

                    usuarioViewModel.mostrarDialogo(true)
                } else {
                    mainViewModel.logearUsuario(username, contrasena)
                }
            }
        ) {
            Text(
                text = "Iniciar sesión",
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
                    mainViewModel.modificarLoginResultado("")
                }
                "" -> {
                    // no hace nada pero tampoco saca el diálogo de error
                }
                else -> usuarioViewModel.mostrarDialogo(true)
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
                    // carga los datos del usuario
                    partidaViewModel.recargarPartidas()
                    personajeViewModel.recargarPersonajes()
                    sesionViewModel.recargarSesionesCalendario(usuarioLogeado!!)

                    navController.popBackStack()
                }
            }
        }

        TextButton(
            onClick = {
                usuarioViewModel.modificarNombre("")
                usuarioViewModel.modificarUsername("")
                usuarioViewModel.modificarContrasena("")
                usuarioViewModel.cambiarEstadoMantenerseLogeado(false)
                navController.navigate(Ruta.CrearCuenta) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            shape = RectangleShape
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "¿No tienes cuenta aun? Únete a las aventuras aquí"
            )
        }
    }

    if (mostrarDialogo) {
        DialogoCuenta(
            resultado = resultado,
            onDismiss = {
                mainViewModel.modificarLoginResultado("")
                usuarioViewModel.mostrarDialogo(false)
            }
        )
    }
}