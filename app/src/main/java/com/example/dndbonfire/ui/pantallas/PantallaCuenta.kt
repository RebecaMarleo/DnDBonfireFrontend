package com.example.dndbonfire.ui.pantallas

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.dndbonfire.R
import com.example.dndbonfire.ui.componentes.DialogoConfirmacionBorrado
import com.example.dndbonfire.ui.componentes.DialogoCuenta
import com.example.dndbonfire.ui.componentes.DialogoImagen
import com.example.dndbonfire.ui.theme.ButtonStyle
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.ui.theme.InformacionPrincipalStyle
import com.example.dndbonfire.ui.theme.InformacionSecundariaStyle
import com.example.dndbonfire.ui.theme.Titulo2Style
import com.example.dndbonfire.ui.theme.Titulo6Style
import com.example.dndbonfire.ui.theme.Transaparente
import com.example.dndbonfire.viewmodel.ImagenViewModel
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.UsuarioViewModel

@Composable
fun PantallaCuenta(
    mainViewModel: MainViewModel,
    imagenViewModel: ImagenViewModel,
    usuarioViewModel: UsuarioViewModel,
    navController: NavController
) {
    val imagenEliminada by imagenViewModel.imagenEliminada
    val imagenUri by imagenViewModel.imagenUri
    val bitmap by imagenViewModel.bitmap
    val usuario by mainViewModel.usuarioLogeado
    val modoEdicion by usuarioViewModel.modoEdicion
    val nombre by usuarioViewModel.nombre
    val username by usuarioViewModel.username
    val contrasena by usuarioViewModel.contrasena
    val contrasenaRe by usuarioViewModel.contrasenaRe
    val imagen by usuarioViewModel.imagen
    val resultado by mainViewModel.modificarUsuarioResultado
    val mostrarDialogoCuenta by usuarioViewModel.mostrarDialogoCuenta
    val mostrarDialogoImagen by imagenViewModel.mostrarDialogoImagen
    val mostrarDialogoConfirmacionBorrado by mainViewModel.mostrarDialogoConfirmacionBorrado

    var mostrarContrasena by remember { mutableStateOf(false) }
    var mostrarContrasenaRe by remember { mutableStateOf(false) }

    val menuRangoExpandido by usuarioViewModel.menuRangoExpandido
    val rango by usuarioViewModel.rango
    val rangosCuenta = listOf(
        "Novato",
        "Intermedio",
        "Experto",
        "Legendario"
    )

    val scrollState = rememberScrollState()

    LaunchedEffect(usuario) {
        usuarioViewModel.modificarImagen(usuario?.imagen)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (modoEdicion) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondary
                        )
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = CutCornerShape(32.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RectangleShape
                        )
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = CutCornerShape(75.dp)
                        )
                )
                AsyncImage(
                    // si hay una imagen recién sacada con la cámara carga esa
                    // si hay una imagen recién cargada de la galería carga esa
                    // si tiene una imagen guardada carga esa
                    // si no tiene ninguna de las anteriores carga el placeholder
                    model = bitmap ?: imagenUri ?: if (!imagenEliminada) { usuario?.imagen?.let { "data:image/jpg;base64,$it" } ?: R.drawable.avatar } else R.drawable.avatar,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                        .size(250.dp)
                        .clip(CutCornerShape(32.dp))
                        .border(
                            width = 10.dp,
                            color = MaterialTheme.colorScheme.background,
                            shape = CutCornerShape(32.dp)
                        )
                        .border(
                            width = 12.dp,
                            color = MaterialTheme.colorScheme.secondary,
                            shape = CutCornerShape(32.dp)
                        )
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.secondary,
                            shape = CutCornerShape(60.dp)
                        )
                        .border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.secondary,
                            shape = CircleShape
                        ).combinedClickable(
                            onClick = {
                                imagenViewModel.mostrarDialogo(true)
                            }
                        ),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 4.dp),
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
                    .fillMaxWidth()
                    .padding(16.dp, 4.dp),
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
                    .fillMaxWidth()
                    .padding(16.dp, 4.dp),
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
                    .fillMaxWidth()
                    .padding(16.dp, 4.dp),
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

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = TextAlign.Center,
                text = "¿Cuál es tu rango de aventurero?",
                style = Titulo6Style,
                color = MaterialTheme.colorScheme.primary
            )

            Box(
                modifier = Modifier
                    .padding(16.dp, 4.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CutCornerShape(16.dp)
                        ),
                    colors = ButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = Transaparente,
                        disabledContainerColor = Gris
                    ),
                    shape = CutCornerShape(16.dp),
                    onClick = {
                        usuarioViewModel.modificarMenuRangoExpandido(!menuRangoExpandido)
                    }
                ) {
                    Text(
                        text = rangosCuenta[rango - 1]
                    )
                }
                DropdownMenu(
                    modifier = Modifier
                        .fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    expanded = menuRangoExpandido,
                    onDismissRequest = {
                        usuarioViewModel.modificarMenuRangoExpandido(false)
                    }
                ) {
                    (rangosCuenta).forEach {
                        DropdownMenuItem(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = { Text(it) },
                            onClick = {
                                usuarioViewModel.modificarRango(rangosCuenta.indexOf(it) + 1)
                                usuarioViewModel.modificarMenuRangoExpandido(false)
                            }
                        )
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondary
                        )
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = CutCornerShape(32.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RectangleShape
                        )
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = CutCornerShape(75.dp)
                        )
                )
                AsyncImage(
                    model = if (!imagenEliminada) { usuario?.imagen?.let { "data:image/jpg;base64,$it" } ?: R.drawable.avatar } else R.drawable.avatar,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                        .size(250.dp)
                        .clip(CutCornerShape(32.dp))
                        .border(
                            width = 10.dp,
                            color = MaterialTheme.colorScheme.background,
                            shape = CutCornerShape(32.dp)
                        )
                        .border(
                            width = 12.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = CutCornerShape(32.dp)
                        )
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = CutCornerShape(60.dp)
                        )
                        .border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = CircleShape
                        ),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            }

            Text(
                text = usuario?.nombre ?: "",
                style = Titulo2Style,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "@${usuario?.username}",
                style = InformacionSecundariaStyle
            )

            Text(
                modifier = Modifier
                    .padding(top = 16.dp),
                text = when (usuario?.rango) {
                    1 -> "Aventurero novato"
                    2 -> "Aventurero intermedio"
                    3 -> "Aventurero experimentado"
                    4 -> "Aventurero legendario"
                    else -> ""
                },
                style = InformacionPrincipalStyle
            )

            Text(
                text = when (usuario?.rango) {
                    1 -> "⟡"
                    2 -> "⟡⟡"
                    3 -> "⟡⟡⟡"
                    4 -> "⟡⟡⟡⟡"
                    else -> ""
                }
            )

            Button(
                modifier = Modifier
                    .padding(vertical = 16.dp)
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
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onBackground,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    disabledContainerColor = Gris
                ),
                shape = CutCornerShape(16.dp),
                onClick = {
                    mainViewModel.mostrarDialogoConfirmacionBorrado(true)
                }
            ) {
                Text(
                    text = "Borrar cuenta",
                    style = ButtonStyle
                )
            }
        }
    }

    if (mostrarDialogoCuenta) {
        DialogoCuenta(
            resultado = resultado,
            onDismiss = {
                usuarioViewModel.mostrarDialogo(false)
                mainViewModel.modificarModificarUsuarioResultado("")
            }
        )
    }

    if (mostrarDialogoImagen) {
        DialogoImagen(
            imagenViewModel = imagenViewModel,
            onDismiss = {
                imagenViewModel.mostrarDialogo(false)
            }
        )
    }

    if (mostrarDialogoConfirmacionBorrado) {
        DialogoConfirmacionBorrado(
            titulo = "Eliminar cuenta",
            mensaje = "Estás a punto de borrar tu cuenta. Esta acción no es reversible, ¿deseas continuar?",
            usuario = usuario,
            mainViewModel = mainViewModel,
            usuarioViewModel = usuarioViewModel,
            navController = navController,
            onDismiss = {
                mainViewModel.modificarEliminarUsuarioResultado("")
                mainViewModel.mostrarDialogoConfirmacionBorrado(false)
            }
        )
    }
}