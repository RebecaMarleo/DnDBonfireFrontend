package com.example.dndbonfire.ui.componentes

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.dndbonfire.navegacion.Ruta
import com.example.dndbonfire.ui.theme.PrimaryAccent
import com.example.dndbonfire.viewmodel.PersonajeViewModel

sealed class BottomNavItem(
    var icono: ImageVector?, val nombre: String, val ruta: Ruta
) {
    object PartidasItem: BottomNavItem(
//        ImageVector.vectorResource(R.drawable.nombreicono)
        Icons.Default.Home,
        "Campañas",
        Ruta.Partidas
    )
    object PersonajesItem: BottomNavItem(
//        ImageVector.vectorResource(R.drawable.nombreicono)
        Icons.Default.Face,
        "Personajes",
        Ruta.Personajes
    )
    object BestiarioItem: BottomNavItem(
//        ImageVector.vectorResource(R.drawable.nombreicono)
        Icons.Default.Warning,
        "Bestiario",
        Ruta.Bestiario
    )
    object CalendarioItem: BottomNavItem(
//        ImageVector.vectorResource(R.drawable.nombreicono)
        Icons.Default.DateRange,
        "Calendario",
        Ruta.Calendario
    )
}

@Composable
fun BottomBar(
    elementoSeleccionado: String?,
    personajeViewModel: PersonajeViewModel,
    onElementoSeleccionado: (Ruta) -> Unit
) {
    val elementosBarraInferior = listOf<BottomNavItem>(
        BottomNavItem.BestiarioItem,
        BottomNavItem.PersonajesItem,
        BottomNavItem.PartidasItem,
        BottomNavItem.CalendarioItem
    )
    val idsPersonajesABorrar = personajeViewModel.idsPersonajesABorrar

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.tertiary
    ) {
        elementosBarraInferior.forEach { item ->
            NavigationBarItem(
                selected = item.ruta::class.qualifiedName == elementoSeleccionado,
                onClick = {
                    if (idsPersonajesABorrar.isNotEmpty()) {
                        personajeViewModel.modificarPersonajesABorrar("vaciar", null)
                    }
                    onElementoSeleccionado(item.ruta)
                },
                icon = {
                    Icon(item.icono!!, contentDescription = item.nombre, Modifier.size(36.dp))
                },
                label = {
                    Text(
                        text = item.nombre,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryAccent,
                    selectedTextColor = PrimaryAccent,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    unselectedIconColor = MaterialTheme.colorScheme.onTertiary,
                    unselectedTextColor = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.7f)
                )
            )
        }
    }
}