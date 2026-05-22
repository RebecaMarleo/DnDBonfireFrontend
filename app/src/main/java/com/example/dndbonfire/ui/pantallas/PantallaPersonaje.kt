package com.example.dndbonfire.ui.pantallas

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.dndbonfire.R
import com.example.dndbonfire.ui.componentes.DialogoPersonaje
import com.example.dndbonfire.ui.componentes.VistaEditarPersonaje
import com.example.dndbonfire.ui.theme.InformacionPrincipalStyle
import com.example.dndbonfire.ui.theme.InformacionSecundariaStyle
import com.example.dndbonfire.ui.theme.Titulo2Style
import com.example.dndbonfire.ui.theme.Titulo4Style
import com.example.dndbonfire.ui.theme.Titulo5Style
import com.example.dndbonfire.ui.theme.Titulo6Style
import com.example.dndbonfire.viewmodel.ImagenViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import kotlin.math.ceil

@Composable
fun PantallaPersonaje(
    navController: NavController,
    idPersonaje: Int,
    personajeViewModel: PersonajeViewModel,
    imagenViewModel: ImagenViewModel
) {
    LaunchedEffect(idPersonaje) {
        personajeViewModel.cargarPersonaje(idPersonaje)
    }

    val personaje by personajeViewModel.personaje
    val personajeTraducido by personajeViewModel.personajeTraducido
    val razas = personajeViewModel.razas
    val raza by personajeViewModel.raza
    val razaTraducida by personajeViewModel.razaTraducida
    val subraza by personajeViewModel.subraza
    val subrazaTraducida by personajeViewModel.subrazaTraducida
    val clases = personajeViewModel.clases
    val clase by personajeViewModel.clase
    val claseTraducida by personajeViewModel.claseTraducida
    val subclase by personajeViewModel.subclase
    val subclaseTraducida by personajeViewModel.subclaseTraducida
    val infoNiveles = personajeViewModel.infoNiveles
    val competenciasSeleccionadas = personajeViewModel.competenciasSeleccionadas
    val conjuros = personajeViewModel.conjuros
    val seleccion by personajeViewModel.infoSeleccionada
    val modoEdicion by personajeViewModel.modoEdicion
    val mostrarDialogo by personajeViewModel.mostrarDialogo
    val modificarPersonajeResultado by personajeViewModel.modificarPersonajeResultado
    val eliminarPersonajeResultado by personajeViewModel.eliminarPersonajeResultado
    val eliminarPersonajesMultiplesResultado by personajeViewModel.eliminarPersonajesMultiplesResultado

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (personaje == null) {
            // si aun no se ha cargado el personaje se queda cargando
            // aun las traducciones por lo que muestra un gif de espera y recarga la página cuando sea necesario
            AsyncImage(
                model = R.drawable.cargando,
                contentDescription = "GIF de carga",
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Fit
            )
            Text (
                text = "Buscando personaje...",
                style = InformacionSecundariaStyle
            )
            if (razas.isEmpty()) personajeViewModel.cargarRazas()
            if (clases.isEmpty()) personajeViewModel.cargarClases()
        } else if ((razaTraducida == null && personaje?.raza != null)
            || (subrazaTraducida == null && personaje?.subraza != null)
        ) {
            AsyncImage(
                model = R.drawable.cargando,
                contentDescription = "GIF de carga",
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Fit
            )
            Text (
                text = "Cargando datos de raza...",
                style = InformacionSecundariaStyle
            )
            if (raza == null) personajeViewModel.cargarRaza(personaje?.raza ?: "")
            if (subraza == null) personajeViewModel.cargarSubraza(personaje?.subraza ?: "")
        } else if ((claseTraducida == null && personaje?.clase != null)
            || (subclaseTraducida == null && personaje?.subclase != null)
        ) {
            AsyncImage(
                model = R.drawable.cargando,
                contentDescription = "GIF de carga",
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Fit
            )
            Text (
                text = "Cargando datos de clase...",
                style = InformacionSecundariaStyle
            )
            if (clase == null) personajeViewModel.cargarClase(personaje?.clase ?: "")
            if (subclase == null) personajeViewModel.cargarSubclase(personaje?.subclase ?: "")
        } else {
            if (!modoEdicion) {
                // en cualquier otro caso se carga el personaje
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(
                            (300f * (1f - (scrollState.value / 500f).coerceAtLeast(0f)
                                .coerceAtMost(0.5f))).dp
                        )
                ) {
                    // imagen del personaje borrosa
                    AsyncImage(
                        model = personaje?.imagen?.let { "data:image/jpg;base64,$it" } ?: R.drawable.avatar,
                        contentDescription = "Imagen del personaje",
                        modifier = Modifier
                            .fillMaxWidth()
                            .blur(
                                radius = 16.dp,
                                edgeTreatment = BlurredEdgeTreatment.Unbounded
                            ),
                        contentScale = ContentScale.FillWidth,
                        alpha = 0.7f
                    )
                    // imagen del personaje
                    AsyncImage(
                        // si el personaje tiene una imagen carga esa
                        // si no tiene ninguna carga el placeholder
                        model = personaje?.imagen?.let { "data:image/jpg;base64,$it" } ?: R.drawable.avatar,
                        contentDescription = "Imagen del personaje",
                        modifier = Modifier
                            .matchParentSize()
                    )
                }

                // nombre del jugador
                if (personaje?.nombre != null) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp, 0.dp),
                        textAlign = TextAlign.Center,
                        text = personaje?.nombre!!,
                        style = Titulo2Style,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // estadísticas
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
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
                    colors = CardDefaults
                        .cardColors(
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                    shape = CutCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                            .width(IntrinsicSize.Min)
                    ) {
                        Text(
                            text = "Estadísticas",
                            style = Titulo5Style,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Puntos de vida: ${personaje?.hp}",
                            style = InformacionPrincipalStyle
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    var total = personaje?.fuerza

                                    Row() {
                                        Text(
                                            text = "Fuerza: ${personaje?.fuerza}"
                                        )
                                        raza?.ability_bonuses?.forEach {
                                            if (it.ability_score.index == "str") {
                                                Text(
                                                    text = "+${it.bonus}"
                                                )
                                                total = total?.plus(it.bonus)
                                            }
                                        }
                                        subraza?.ability_bonuses?.forEach {
                                            if (it.ability_score.index == "str") {
                                                Text(
                                                    text = "+${it.bonus}"
                                                )
                                                total = total?.plus(it.bonus)
                                            }
                                        }
                                    }

                                    if (total != null) {
                                        Text (
                                            text = "= ${if (total > 20) 20 else total}"
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    var total = personaje?.destreza

                                    Row() {
                                        Text(
                                            text = "Destreza: ${personaje?.destreza}"
                                        )
                                        raza?.ability_bonuses?.forEach {
                                            if (it.ability_score.index == "dex") {
                                                Text(
                                                    text = "+${it.bonus}"
                                                )
                                                total = total?.plus(it.bonus)
                                            }
                                        }
                                        subraza?.ability_bonuses?.forEach {
                                            if (it.ability_score.index == "dex") {
                                                Text(
                                                    text = "+${it.bonus}"
                                                )
                                                total = total?.plus(it.bonus)
                                            }
                                        }
                                    }

                                    if (total != null) {
                                        Text (
                                            text = "= ${if (total > 20) 20 else total}"
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    var total = personaje?.constitucion

                                    Row() {
                                        Text(
                                            text = "Constitu.: ${personaje?.constitucion}"
                                        )
                                        raza?.ability_bonuses?.forEach {
                                            if (it.ability_score.index == "con") {
                                                Text(
                                                    text = "+${it.bonus}"
                                                )
                                                total = total?.plus(it.bonus)
                                            }
                                        }
                                        subraza?.ability_bonuses?.forEach {
                                            if (it.ability_score.index == "con") {
                                                Text(
                                                    text = "+${it.bonus}"
                                                )
                                                total = total?.plus(it.bonus)
                                            }
                                        }
                                    }

                                    if (total != null) {
                                        Text (
                                            text = "= ${if (total > 20) 20 else total}"
                                        )
                                    }
                                }
                            }
                            VerticalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.background,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(horizontal = 8.dp)
                            )
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    var total = personaje?.inteligencia

                                    Row() {
                                        Text(
                                            text = "Inteligen.: ${personaje?.inteligencia}"
                                        )
                                        raza?.ability_bonuses?.forEach {
                                            if (it.ability_score.index == "int") {
                                                Text(
                                                    text = "+${it.bonus}"
                                                )
                                                total = total?.plus(it.bonus)
                                            }
                                        }
                                        subraza?.ability_bonuses?.forEach {
                                            if (it.ability_score.index == "int") {
                                                Text(
                                                    text = "+${it.bonus}"
                                                )
                                                total = total?.plus(it.bonus)
                                            }
                                        }
                                    }

                                    if (total != null) {
                                        Text (
                                            text = "= ${if (total > 20) 20 else total}"
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    var total = personaje?.sabiduria

                                    Row() {
                                        Text(
                                            text = "Sabiduría: ${personaje?.sabiduria}"
                                        )
                                        raza?.ability_bonuses?.forEach {
                                            if (it.ability_score.index == "wis") {
                                                Text(
                                                    text = "+${it.bonus}"
                                                )
                                                total = total?.plus(it.bonus)
                                            }
                                        }
                                        subraza?.ability_bonuses?.forEach {
                                            if (it.ability_score.index == "wis") {
                                                Text(
                                                    text = "+${it.bonus}"
                                                )
                                                total = total?.plus(it.bonus)
                                            }
                                        }
                                    }

                                    if (total != null) {
                                        Text (
                                            text = "= ${if (total > 20) 20 else total}"
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    var total = personaje?.carisma

                                    Row() {
                                        Text(
                                            text = "Carisma: ${personaje?.carisma}"
                                        )
                                        raza?.ability_bonuses?.forEach {
                                            if (it.ability_score.index == "cha") {
                                                Text(
                                                    text = "+${it.bonus}"
                                                )
                                                total = total?.plus(it.bonus)
                                            }
                                        }
                                        subraza?.ability_bonuses?.forEach {
                                            if (it.ability_score.index == "cha") {
                                                Text(
                                                    text = "+${it.bonus}"
                                                )
                                                total = total?.plus(it.bonus)
                                            }
                                        }
                                    }

                                    if (total != null) {
                                        Text (
                                            text = "= ${if (total > 20) 20 else total}"
                                        )
                                    }
                                }
                            }
                        }

                        if (clase != null) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.background,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(vertical = 8.dp)
                            )
                            Row() {
                                Column(
                                    modifier = Modifier
                                        .weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        textAlign = TextAlign.Center,
                                        text = "Dado de golpe:",
                                    )
                                    Text(
                                        text = clase?.hit_die.toString(),
                                        style = Titulo4Style
                                    )
                                }
                            }
                        }
                    }
                }

                // raza, subraza, tamaño y velocidad
                Row(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 12.dp)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (personajeTraducido?.raza != null) {
                            // raza
                            Text(
                                text = personajeTraducido?.raza ?: "",
                                style = Titulo4Style
                            )
                        }
                        if (personajeTraducido?.subraza != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // subraza
                                Text(
                                    text = personajeTraducido?.subraza ?: "",
                                    style = InformacionPrincipalStyle
                                )
                                if (subraza != null) {
                                    TextButton(
                                        onClick = {
                                            personajeViewModel.modificarInfoSeleccionada("subraza")
                                            personajeViewModel.mostrarDialogo(true)
                                        },
                                        modifier = Modifier
                                            .width(20.dp)
                                            .padding(0.dp),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(
                                            text = "?",
                                            style = Titulo6Style
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (razaTraducida != null) {
                        VerticalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(horizontal = 12.dp)
                        )
                        // tamaño
                        Text(
                            modifier = Modifier
                                .width(IntrinsicSize.Min),
                            textAlign = TextAlign.Center,
                            text = "Tamaño: ${razaTraducida!!.size}",
                            style = InformacionSecundariaStyle
                        )
                        TextButton(
                            onClick = {
                                personajeViewModel.modificarInfoSeleccionada("tamaño")
                                personajeViewModel.mostrarDialogo(true)
                            },
                            modifier = Modifier
                                .width(20.dp)
                                .padding(0.dp),
                            contentPadding = PaddingValues(0.dp),
                        ) {
                            Text(
                                text = "?",
                                style = Titulo6Style
                            )
                        }
                        VerticalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(horizontal = 12.dp)
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // velocidad
                            Text(
                                text = "Velocidad",
                                style = Titulo4Style,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = "${razaTraducida!!.speed.toString()} ft.",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = Titulo4Style
                            )
                        }
                    }
                }

                // alineamiento
                if (razaTraducida != null) {
                    Row(
                        modifier = Modifier
                            .padding(
                                vertical = 4.dp,
                                horizontal = 12.dp
                            )
                            .align(Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            textAlign = TextAlign.Start,
                            text = "Alineamiento: ${personaje?.alineamiento}",
                            style = InformacionSecundariaStyle
                        )
                        TextButton(
                            onClick = {
                                personajeViewModel.modificarInfoSeleccionada("alineamiento")
                                personajeViewModel.mostrarDialogo(true)
                            },
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                                .padding(0.dp),
                            contentPadding = PaddingValues(0.dp),
                        ) {
                            Text(
                                text = "?",
                                style = Titulo6Style
                            )
                        }
                    }
                }

                // idiomas que habla
                if (razaTraducida != null) {
                    Row(
                        modifier = Modifier
                            .padding(
                                horizontal = 12.dp
                            )
                            .align(Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var idiomas = "Idiomas:"
                        razaTraducida!!.languages.forEach {
                            idiomas += " ${it.name},"
                        }
                        idiomas = idiomas.dropLast(1)
                        Text(
                            textAlign = TextAlign.Start,
                            text = if (personajeTraducido!!.idiomaExtra != null) "$idiomas, ${personajeTraducido!!.idiomaExtra}" else idiomas,
                            style = InformacionSecundariaStyle
                        )
                        TextButton(
                            onClick = {
                                personajeViewModel.modificarInfoSeleccionada("idioma")
                                personajeViewModel.mostrarDialogo(true)
                            },
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                                .padding(0.dp),
                            contentPadding = PaddingValues(0.dp),
                        ) {
                            Text(
                                text = "?",
                                style = Titulo6Style
                            )
                        }
                    }
                }

                // edad
                if (razaTraducida != null) {
                    Row(
                        modifier = Modifier
                            .padding(
                                vertical = 4.dp,
                                horizontal = 12.dp
                            )
                            .align(Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            textAlign = TextAlign.Start,
                            text = "Edad: ${personaje?.edad}",
                            style = InformacionSecundariaStyle
                        )
                        TextButton(
                            onClick = {
                                personajeViewModel.modificarInfoSeleccionada("edad")
                                personajeViewModel.mostrarDialogo(true)
                            },
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                                .padding(0.dp),
                            contentPadding = PaddingValues(0.dp),
                        ) {
                            Text(
                                text = "?",
                                style = Titulo6Style
                            )
                        }
                    }
                }

                // rasgos raciales
                if (razaTraducida != null && !razaTraducida!!.traits.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .width(IntrinsicSize.Max),
                        textAlign = TextAlign.Center,
                        text = "Rasgos raciales",
                        style = Titulo4Style,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    ) {
                        razaTraducida!!.traits.forEach { rasgo ->
                            val interactionSource = remember { MutableInteractionSource() }
                            val isPressed by interactionSource.collectIsPressedAsState()
                            val contentColor by animateColorAsState(
                                targetValue = if (isPressed)
                                    MaterialTheme.colorScheme.onSecondary
                                else
                                    MaterialTheme.colorScheme.onPrimaryContainer,
                                animationSpec = tween(200)
                            )
                            val containerColor by animateColorAsState(
                                targetValue = if (isPressed)
                                    MaterialTheme.colorScheme.secondary
                                else
                                    MaterialTheme.colorScheme.primaryContainer,
                                animationSpec = tween(200)
                            )
                            val borderColor by animateColorAsState(
                                targetValue = if (isPressed)
                                    MaterialTheme.colorScheme.secondary
                                else
                                    MaterialTheme.colorScheme.tertiary
                            )

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        bottom = 4.dp
                                    )
                                    .combinedClickable(
                                        onClick = {
                                            personajeViewModel.modificarInfoSeleccionada("url")
                                            personajeViewModel.cargarDescripcion(rasgo.url)
                                            personajeViewModel.mostrarDialogo(true)
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
                                Text(
                                    modifier = Modifier
                                        .padding(12.dp),
                                    text = rasgo.name,
                                    style = InformacionPrincipalStyle,
                                    overflow = TextOverflow.Visible
                                )
                            }
                        }
                    }
                }

                // rasgos raciales de subraza
                if (subrazaTraducida != null && !subrazaTraducida!!.racial_traits.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    ) {
                        subrazaTraducida!!.racial_traits.forEach { rasgo ->
                            val interactionSource = remember { MutableInteractionSource() }
                            val isPressed by interactionSource.collectIsPressedAsState()
                            val contentColor by animateColorAsState(
                                targetValue = if (isPressed)
                                    MaterialTheme.colorScheme.onSecondary
                                else
                                    MaterialTheme.colorScheme.onPrimaryContainer,
                                animationSpec = tween(200)
                            )
                            val containerColor by animateColorAsState(
                                targetValue = if (isPressed)
                                    MaterialTheme.colorScheme.secondary
                                else
                                    MaterialTheme.colorScheme.primaryContainer,
                                animationSpec = tween(200)
                            )
                            val borderColor by animateColorAsState(
                                targetValue = if (isPressed)
                                    MaterialTheme.colorScheme.secondary
                                else
                                    MaterialTheme.colorScheme.tertiary
                            )

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        bottom = 4.dp
                                    )
                                    .combinedClickable(
                                        onClick = {
                                            personajeViewModel.modificarInfoSeleccionada("url")
                                            personajeViewModel.cargarDescripcion(rasgo.url)
                                            personajeViewModel.mostrarDialogo(true)
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
                                Text(
                                    modifier = Modifier
                                        .padding(12.dp),
                                    text = rasgo.name,
                                    style = InformacionPrincipalStyle,
                                    overflow = TextOverflow.Visible
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 12.dp)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (personajeTraducido?.clase != null) {
                            // clase
                            Text(
                                text = personajeTraducido?.clase ?: "",
                                style = Titulo4Style
                            )
                        }
                        if (personajeTraducido?.subclase != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // subclase
                                Text(
                                    text = personajeTraducido?.subclase ?: "",
                                    style = InformacionPrincipalStyle
                                )
                                if (subclase != null) {
                                    TextButton(
                                        onClick = {
                                            personajeViewModel.modificarInfoSeleccionada("subclase")
                                            personajeViewModel.mostrarDialogo(true)
                                        },
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(20.dp)
                                            .padding(0.dp),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(
                                            text = "?",
                                            style = Titulo6Style
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (claseTraducida != null) {
                        VerticalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(horizontal = 12.dp)
                        )
                        // nivel
                        Text(
                            text = "Nivel: ${personaje?.nivel}",
                            style = InformacionSecundariaStyle
                        )
                    }
                }

                // atributos especificos de personaje
                if (!infoNiveles.isEmpty() && personaje?.nivel!! > 0)  {
                    val infoNivel = infoNiveles.find { it.level == personaje?.nivel }
                    when (clase?.index) {
                        "barbarian" -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Puede entrar ${infoNivel!!.class_specific.rage_count} veces en estado de Furia al día. Los usos se restauran con un descanso largo."
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Al estar en estado de Furia hace ${infoNivel.class_specific.rage_damage_bonus} de daño extra."
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Al asestar un golpe crítico puede lanzar ${infoNivel.class_specific.brutal_critical_dice} dados extra."
                            )
                        }
                        "bard" -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Inspiración Bárdica utiliza un dado de ${infoNivel!!.class_specific.bardic_inspiration_die} caras."
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Canción del Descanso utiliza un dado de ${infoNivel.class_specific.song_of_rest_die} caras."
                            )
                        }
                        "cleric" -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Tiene ${infoNivel!!.class_specific.channel_divinity_charges} Cargas de Canalizar Divinidad al día. Los usos se restauran con un descanso largo."
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Las criaturas con valor de desafío igual o inferior a ${infoNivel.class_specific.destroy_undead_cr} morirán instantaneamente al recibir un golpe Canalizar Divinidad."
                            )
                        }
                        "druid" -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Se puede convertir en una criatura con valor de desafío igual o inferior a ${infoNivel!!.class_specific.wild_shape_max_cr}."
                            )
                            if (infoNivel.class_specific.wild_shape_swim == true) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 8.dp,
                                            start = 16.dp,
                                            end = 16.dp
                                        ),
                                    textAlign = TextAlign.Start,
                                    style = InformacionPrincipalStyle,
                                    text = "Se puede convertir en una criatura con la capacidad de nadar."
                                )
                            }
                            if (infoNivel.class_specific.wild_shape_fly == true) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 8.dp,
                                            start = 16.dp,
                                            end = 16.dp
                                        ),
                                    textAlign = TextAlign.Start,
                                    style = InformacionPrincipalStyle,
                                    text = "Se puede convertir en una criatura con la capacidad de volar."
                                )
                            }
                        }
                        "fighter" -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Puede usar acción súbita ${infoNivel!!.class_specific.action_surges} veces al día. Los usos se restauran con un descanso largo."
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Si falla una tirada de salvación puede volver a tirar el dado ${infoNivel!!.class_specific.indomitable_uses} veces al día. Los usos se restauran con un descanso largo."
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Tiene ${infoNivel!!.class_specific.extra_attacks} ataques extra."
                            )
                        }
                        "monk" -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Cuando ataca sin usar armas lanza ${infoNivel!!.class_specific.martial_arts?.dice_count} ${if(infoNivel.class_specific.martial_arts?.dice_count == 1) "dado" else "dados"} de ${infoNivel.class_specific.martial_arts?.dice_value} caras."
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Tiene ${infoNivel.class_specific.ki_points} puntos de Ki."
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Si no lleva armadura se mueve ${infoNivel.class_specific.unarmored_movement} pies extra."
                            )
                        }
                        "paladin" -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "El rango de su aura abarca ${infoNivel!!.class_specific.aura_range} pies a su alrededor."
                            )
                        }
                        "ranger" -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Puede rastrear ${infoNivel!!.class_specific.favored_enemies} tipos de criaturas diferentes al mismo tiempo."
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Tiene ventaja en ${infoNivel.class_specific.favored_terrain} tipos de terrenos diferentes tras viajar por ellos durante al menos 1 hora."
                            )
                        }
                        "rogue" -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Al lanzar un ataque sorpresa inflinge un daño equivalente al resultado de lanzar ${infoNivel!!.class_specific.sneak_attack?.dice_count} dados de ${infoNivel.class_specific.sneak_attack?.dice_value} caras."
                            )
                        }
                        "sorcerer" -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Tiene ${infoNivel!!.class_specific.sorcery_points} puntos de hechicería."
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Conoce ${infoNivel.class_specific.metamagic_known} metamagias."
                            )
                            infoNivel.class_specific.creating_spell_slots?.forEach {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 8.dp,
                                            start = 16.dp,
                                            end = 16.dp
                                        ),
                                    textAlign = TextAlign.Start,
                                    style = InformacionPrincipalStyle,
                                    text = "Puede crear un espacio de conjuro de nivel ${it.spell_slot_level} gastando ${it.sorcery_point_cost} puntos de hechicería. De la misma manera puede gastar un espacio de conjuro de nivel ${it.spell_slot_level} para obtener ${it.sorcery_point_cost} puntos de hechicería."
                                )
                            }
                        }
                        "warlock" -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Conoce ${infoNivel!!.class_specific.invocations_known} invocaciones."
                            )
                        }
                        "wizard" -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                style = InformacionPrincipalStyle,
                                text = "Al realizar un descanso corto puede recuperar ${infoNivel!!.class_specific.arcane_recovery_levels} niveles de conjuro al día. Los usos se restauran con un descanso largo."
                            )
                        }
                    }
                }
                // tiradas de salvación
                if (claseTraducida != null) {
                    claseTraducida!!.saving_throws.forEach {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 8.dp,
                                    start = 16.dp
                                ),
                            textAlign = TextAlign.Start,
                            text = "Ventaja en tiradas de salvación de ${it.name}.",
                            style = InformacionPrincipalStyle
                        )
                    }
                }

                // compentencias de clase
                if (claseTraducida != null && !claseTraducida!!.proficiencies.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .width(IntrinsicSize.Max),
                        textAlign = TextAlign.Center,
                        text = "Competencias",
                        style = Titulo4Style,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Column(
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(4f)
                            ) {
                                // de clase base
                                claseTraducida!!.proficiencies.forEach { competencia ->
                                    if (!competencia.name.startsWith("Tiro de ahorro:"))
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    bottom = 4.dp
                                                )
                                                .border(
                                                    width = 1.dp,
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
                                            Text(
                                                modifier = Modifier
                                                    .padding(12.dp),
                                                text = competencia.name,
                                                style = InformacionPrincipalStyle,
                                                overflow = TextOverflow.Visible
                                            )
                                        }
                                }
                            }
                            if (!infoNiveles.isEmpty() && personaje?.nivel!! > 0) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Bonus",
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = infoNiveles[personaje?.nivel!!-1].prof_bonus.toString(),
                                        color = MaterialTheme.colorScheme.onBackground,
                                        style = Titulo4Style
                                    )
                                }
                            }
                        }

                        // de clase en base al nivel
                        if (!infoNiveles.isEmpty()) {
                            infoNiveles.forEach {
                                if (it.level <= personaje?.nivel!!) {
                                    it.features.forEach { rasgo ->
                                        val interactionSource = remember { MutableInteractionSource() }
                                        val isPressed by interactionSource.collectIsPressedAsState()
                                        val contentColor by animateColorAsState(
                                            targetValue = if (isPressed)
                                                MaterialTheme.colorScheme.onSecondary
                                            else
                                                MaterialTheme.colorScheme.onPrimaryContainer,
                                            animationSpec = tween(200)
                                        )
                                        val containerColor by animateColorAsState(
                                            targetValue = if (isPressed)
                                                MaterialTheme.colorScheme.secondary
                                            else
                                                MaterialTheme.colorScheme.primaryContainer,
                                            animationSpec = tween(200)
                                        )
                                        val borderColor by animateColorAsState(
                                            targetValue = if (isPressed)
                                                MaterialTheme.colorScheme.secondary
                                            else
                                                MaterialTheme.colorScheme.tertiary
                                        )

                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    bottom = 4.dp
                                                )
                                                .combinedClickable(
                                                    onClick = {
                                                        personajeViewModel.modificarInfoSeleccionada(
                                                            "url"
                                                        )
                                                        personajeViewModel.cargarDescripcion(rasgo.url)
                                                        personajeViewModel.mostrarDialogo(true)
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
                                            Text(
                                                modifier = Modifier
                                                    .padding(12.dp),
                                                text = rasgo.name,
                                                style = InformacionPrincipalStyle,
                                                overflow = TextOverflow.Visible
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // elección de competencias
                if (claseTraducida != null && !claseTraducida!!.proficiency_choices.isEmpty()) {
                    claseTraducida!!.proficiency_choices.forEach { eleccionCompetencia ->
                        eleccionCompetencia.from.options?.forEach { opcion ->
                            if (opcion.option_type == "reference") {
                                var estaSeleccionado = false
                                personaje?.competenciasSeleccionadas?.forEach { listaCompetencias ->
                                    listaCompetencias.forEach { competencia ->
                                        if (opcion.item?.index == competencia) estaSeleccionado = true
                                    }
                                }

                                if (estaSeleccionado) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                bottom = 4.dp,
                                                start = 16.dp,
                                                end = 16.dp
                                            )
                                            .border(
                                                width = 2.dp,
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
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(IntrinsicSize.Max)
                                                .padding(12.dp),
                                            text = opcion.item!!.name,
                                            style = InformacionPrincipalStyle,
                                            overflow = TextOverflow.Visible
                                        )
                                    }
                                }
                            }
                            else {
                                opcion.choice?.from?.options?.forEach { opcionDeOpcion ->
                                    var estaSeleccionado = false
                                    competenciasSeleccionadas.forEach { listaCompetencias ->
                                        listaCompetencias.forEach { competencia ->
                                            if (opcionDeOpcion.item?.index == competencia) estaSeleccionado = true
                                        }
                                    }

                                    if (estaSeleccionado) {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    bottom = 4.dp,
                                                    start = 16.dp,
                                                    end = 16.dp
                                                )
                                                .border(
                                                    width = 2.dp,
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
                                            Text(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(IntrinsicSize.Max)
                                                    .padding(
                                                        horizontal = 12.dp
                                                    ),
                                                text = opcionDeOpcion.item?.name ?: "",
                                                style = InformacionPrincipalStyle,
                                                overflow = TextOverflow.Visible
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // equipamiento inicial
                if (claseTraducida != null && !claseTraducida!!.starting_equipment.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .width(IntrinsicSize.Max),
                        textAlign = TextAlign.Center,
                        text = "Equipamiento inicial",
                        style = Titulo4Style,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    ) {
                        claseTraducida!!.starting_equipment.forEach { equipo ->
                            val interactionSource = remember { MutableInteractionSource() }
                            val isPressed by interactionSource.collectIsPressedAsState()
                            val contentColor by animateColorAsState(
                                targetValue = if (isPressed)
                                    MaterialTheme.colorScheme.onSecondary
                                else
                                    MaterialTheme.colorScheme.onPrimaryContainer,
                                animationSpec = tween(200)
                            )
                            val containerColor by animateColorAsState(
                                targetValue = if (isPressed)
                                    MaterialTheme.colorScheme.secondary
                                else
                                    MaterialTheme.colorScheme.primaryContainer,
                                animationSpec = tween(200)
                            )
                            val borderColor by animateColorAsState(
                                targetValue = if (isPressed)
                                    MaterialTheme.colorScheme.secondary
                                else
                                    MaterialTheme.colorScheme.tertiary
                            )

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        bottom = 4.dp
                                    )
                                    .combinedClickable(
                                        onClick = {
                                            personajeViewModel.modificarInfoSeleccionada("url")
                                            personajeViewModel.cargarDescripcion(equipo.equipment.url)
                                            personajeViewModel.mostrarDialogo(true)
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
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Max)
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = equipo.equipment.name,
                                        style = InformacionPrincipalStyle,
                                        overflow = TextOverflow.Visible
                                    )
                                    Text(
                                        text = "Cantidad: ${equipo.quantity}",
                                        style = InformacionPrincipalStyle,
                                        overflow = TextOverflow.Visible
                                    )
                                }
                            }
                        }
                    }
                }

                // eleccion de spells (e info sobre spellcasting por nivel de hechizo)
                if (claseTraducida != null && claseTraducida!!.spellcasting != null) {
                    // información
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .width(IntrinsicSize.Max),
                        textAlign = TextAlign.Center,
                        text = "Lanzamiento de conjuros",
                        style = Titulo4Style,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 4.dp
                            ),
                        textAlign = TextAlign.Start,
                        style = InformacionPrincipalStyle,
                        text = "Aprende conjuros desde nivel: ${claseTraducida!!.spellcasting!!.level}"
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 4.dp
                            ),
                        textAlign = TextAlign.Start,
                        style = InformacionPrincipalStyle,
                        text = "Su habilidad para lanzar conjuros proviene de su: ${
                            when (claseTraducida!!.spellcasting!!.spellcasting_ability!!.index) {
                                "cha" -> "carisma"
                                "int" -> "inteligencia"
                                "wis" -> "sabiduría"
                                else -> ""
                            }
                        }."
                    )
                    claseTraducida!!.spellcasting!!.info.forEach { topico ->
                        var desplegado by remember { mutableStateOf(false) }

                        Row(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {
                                        desplegado = !desplegado
                                    }
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                color = MaterialTheme.colorScheme.secondary,
                                style = Titulo6Style,
                                text = topico.name
                            )
                            Icon(
                                imageVector = if(desplegado) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Desplegar",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        if (desplegado)
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp),
                                text = topico.desc.joinToString("")
                            )
                    }

                    // conjuros
                    if (!infoNiveles.isEmpty() && personaje?.nivel!! > 0) {
                        var nivelMaxConjuro: Int
                        if (infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_9 != 0 && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_9 != null) {
                            nivelMaxConjuro = 9
                        } else if (infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_8 != 0 && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_8 != null) {
                            nivelMaxConjuro = 8
                        } else if (infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_7 != 0 && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_7 != null) {
                            nivelMaxConjuro = 7
                        } else if (infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_6 != 0 && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_6 != null) {
                            nivelMaxConjuro = 6
                        } else if (infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_5 != 0 && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_5 != null) {
                            nivelMaxConjuro = 5
                        } else if (infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_4 != 0 && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_4 != null) {
                            nivelMaxConjuro = 4
                        } else if (infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_3 != 0 && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_3 != null) {
                            nivelMaxConjuro = 3
                        } else if (infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_2 != 0 && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_2 != null) {
                            nivelMaxConjuro = 2
                        } else {
                            nivelMaxConjuro = 1
                        }
                        // numero de ranuras de conjuro y conjuros conocidos
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 8.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                            textAlign = TextAlign.Start,
                            text = "Conoce ${infoNiveles[personaje?.nivel!!-1].spellcasting?.cantrips_known} trucos."
                        )
                        if (infoNiveles[personaje?.nivel!!-1].spellcasting?.spells_known != null) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = if (claseTraducida!!.index == "warlock") 0.dp else 8.dp
                                    ),
                                textAlign = TextAlign.Start,
                                text = "Conoce ${infoNiveles[personaje?.nivel!!-1].spellcasting?.spells_known} conjuros de hasta nivel ${nivelMaxConjuro}."
                            )
                        } else {
                            if (claseTraducida!!.index == "cleric" || claseTraducida!!.index == "druid") {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 8.dp,
                                            start = 16.dp,
                                            end = 16.dp,
                                            bottom = if (claseTraducida!!.index == "warlock") 0.dp else 8.dp
                                        ),
                                    textAlign = TextAlign.Start,
                                    text = "Conoce ${(personaje?.nivel!!+ceil((personaje?.sabiduria!! - 10).toDouble()/2)).toInt()} conjuros de hasta nivel ${nivelMaxConjuro}."
                                )
                            } else {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 8.dp,
                                            start = 16.dp,
                                            end = 16.dp,
                                            bottom = if (claseTraducida!!.index == "warlock") 0.dp else 8.dp
                                        ),
                                    textAlign = TextAlign.Start,
                                    text = "Conoce ${(personaje?.nivel!!+ceil((personaje?.inteligencia!! - 10).toDouble()/2)).toInt()} conjuros de hasta nivel ${nivelMaxConjuro}."
                                )
                            }
                        }
                        if (claseTraducida!!.index == "warlock") {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 8.dp
                                    ),
                                textAlign = TextAlign.Start,
                                text = "Todos sus hechizos se lanzan a nivel $nivelMaxConjuro sin importar cual fuese su nivel original."
                            )
                        }
                        if (nivelMaxConjuro >= 1 && !(claseTraducida!!.index == "warlock" && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_1 == 0)) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                text = "Puede lanzar ${infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_1} hechizos de nivel 1 antes de necesitar un descanso ${if (claseTraducida!!.index == "warlock") "corto" else "largo"}."
                            )
                        }
                        if (nivelMaxConjuro >= 2 && !(claseTraducida!!.index == "warlock" && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_2 == 0)) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                text = "Puede lanzar ${infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_2} hechizos de nivel 2 antes de necesitar un descanso ${if (claseTraducida!!.index == "warlock") "corto" else "largo"}."
                            )
                        }
                        if (nivelMaxConjuro >= 3 && !(claseTraducida!!.index == "warlock" && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_3 == 0)) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                text = "Puede lanzar ${infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_3} hechizos de nivel 3 antes de necesitar un descanso ${if (claseTraducida!!.index == "warlock") "corto" else "largo"}."
                            )
                        }
                        if (nivelMaxConjuro >= 4 && !(claseTraducida!!.index == "warlock" && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_4 == 0)) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                text = "Puede lanzar ${infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_4} hechizos de nivel 4 antes de necesitar un descanso ${if (claseTraducida!!.index == "warlock") "corto" else "largo"}."
                            )
                        }
                        if (nivelMaxConjuro >= 5 && !(claseTraducida!!.index == "warlock" && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_5 == 0)) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                text = "Puede lanzar ${infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_5} hechizos de nivel 5 antes de necesitar un descanso ${if (claseTraducida!!.index == "warlock") "corto" else "largo"}."
                            )
                        }
                        if (nivelMaxConjuro >= 6 && !(claseTraducida!!.index == "warlock" && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_6 == 0)) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                text = "Puede lanzar ${infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_6} hechizos de nivel 6 antes de necesitar un descanso ${if (claseTraducida!!.index == "warlock") "corto" else "largo"}."
                            )
                        }
                        if (nivelMaxConjuro >= 7 && !(claseTraducida!!.index == "warlock" && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_7 == 0)) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                text = "Puede lanzar ${infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_7} hechizos de nivel 7 antes de necesitar un descanso ${if (claseTraducida!!.index == "warlock") "corto" else "largo"}."
                            )
                        }
                        if (nivelMaxConjuro >= 8 && !(claseTraducida!!.index == "warlock" && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_8 == 0)) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                text = "Puede lanzar ${infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_8} hechizos de nivel 8 antes de necesitar un descanso ${if (claseTraducida!!.index == "warlock") "corto" else "largo"}."
                            )
                        }
                        if (nivelMaxConjuro >= 9 && !(claseTraducida!!.index == "warlock" && infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_9 == 0)) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp
                                    ),
                                textAlign = TextAlign.Start,
                                text = "Puede lanzar ${infoNiveles[personaje?.nivel!!-1].spellcasting?.spell_slots_level_9} hechizos de nivel 9 antes de necesitar un descanso ${if (claseTraducida!!.index == "warlock") "corto" else "largo"}."
                            )
                        }

                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .width(IntrinsicSize.Max),
                            textAlign = TextAlign.Center,
                            text = "Conjuros disponibles",
                            style = Titulo4Style,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        for (i in 0..9) {
                            var desplegado by remember { mutableStateOf(false) }

                            if (i <= nivelMaxConjuro) {
                                Row(
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
                                        .fillMaxWidth()
                                        .combinedClickable(
                                            onClick = {
                                                desplegado = !desplegado
                                            }
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    if (i != 0 || infoNiveles[personaje?.nivel!!-1].spellcasting?.cantrips_known != null) {
                                        Text(
                                            modifier = Modifier
                                                .padding(top = 8.dp)
                                                .width(IntrinsicSize.Max),
                                            text = if(i == 0) "Trucos" else "Conjuros de nivel $i",
                                            color = MaterialTheme.colorScheme.secondary,
                                            style = Titulo6Style
                                        )
                                        Icon(
                                            imageVector = if(desplegado) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Desplegar",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }
                            }

                            if (desplegado) {
                                conjuros.forEach { conjuro ->
                                    // filtra los conjuros hasta el nivel máximo que puede aprender
                                    if (conjuro.level <= nivelMaxConjuro && conjuro.level == i) {
                                        var estaSeleccionado = false
                                        if (i == 0) {
                                            personaje?.trucosConocidos!!.forEach {
                                                if (conjuro.index == it) estaSeleccionado = true
                                            }
                                        } else {
                                            personaje?.conjurosConocidos!!.forEach {
                                                if (conjuro.index == it) estaSeleccionado = true
                                            }
                                        }

                                        val interactionSource = remember { MutableInteractionSource() }
                                        val isPressed by interactionSource.collectIsPressedAsState()
                                        val contentColor by animateColorAsState(
                                            targetValue = if (isPressed)
                                                MaterialTheme.colorScheme.onSecondary
                                            else
                                                MaterialTheme.colorScheme.onPrimaryContainer,
                                            animationSpec = tween(200)
                                        )
                                        val containerColor by animateColorAsState(
                                            targetValue = if (isPressed)
                                                MaterialTheme.colorScheme.secondary
                                            else
                                                MaterialTheme.colorScheme.primaryContainer,
                                            animationSpec = tween(200)
                                        )
                                        val borderColor by animateColorAsState(
                                            targetValue = if (isPressed)
                                                MaterialTheme.colorScheme.secondary
                                            else
                                                MaterialTheme.colorScheme.tertiary
                                        )

                                        if (estaSeleccionado) {
                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(
                                                        bottom = 4.dp,
                                                        start = 16.dp,
                                                        end = 16.dp
                                                    )
                                                    .combinedClickable(
                                                        onClick = {
                                                            personajeViewModel.modificarInfoSeleccionada("url")
                                                            personajeViewModel.cargarDescripcion(conjuro.url)
                                                            personajeViewModel.mostrarDialogo(true)
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
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(IntrinsicSize.Max)
                                                        .padding(12.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = conjuro.name,
                                                        style = InformacionPrincipalStyle,
                                                        overflow = TextOverflow.Visible
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                VistaEditarPersonaje(
                    personajeViewModel = personajeViewModel,
                    imagenViewModel = imagenViewModel
                )
            }

            // requiere launched effect para esperar los cambios en la variable modificarPersonajeResultado del view model
            // este cambio se produce al pulsar el botón
            LaunchedEffect(modificarPersonajeResultado) {
                when (modificarPersonajeResultado) {
                    "Exito" -> {
                        // se borran los datos una vez guardada la personaje
                        personajeViewModel.modificarNombre("")
                        personajeViewModel.modificarHp(0)
                        personajeViewModel.modificarFuerza(0)
                        personajeViewModel.modificarDestreza(0)
                        personajeViewModel.modificarConstitucion(0)
                        personajeViewModel.modificarInteligencia(0)
                        personajeViewModel.modificarSabiduria(0)
                        personajeViewModel.modificarCarisma(0)
                        personajeViewModel.cargarRazaMod("")
                        personajeViewModel.modificarEdad(0)
                        personajeViewModel.modificarAlineamiento("Neutral verdadero")
                        personajeViewModel.cargarClaseMod("")
                        personajeViewModel.modificarNivel(0)
                        personajeViewModel.modificarCompetenciasSeleccionadas("", null, null)
                        personajeViewModel.modificarTrucosConocidos("", null)
                        personajeViewModel.modificarConjurosConocidos("", null)
                        imagenViewModel.modificarImagenUri(null)
                        imagenViewModel.modificarBitmap(null)

                        personajeViewModel.activarModoEdicion(false)

                        personajeViewModel.modificarModificarPersonajeResultado("")
                    }
                    "" -> {
                        // no hace nada pero tampoco saca el diálogo de error
                    }
                    else -> {
                        personajeViewModel.mostrarDialogo(true)
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        DialogoPersonaje(
            personajeViewModel = personajeViewModel,
            raza = raza,
            razaTraducida = razaTraducida,
            subraza = subraza,
            subrazaTraducida = subrazaTraducida,
            subclase = subclase,
            subclaseTraducida = subclaseTraducida,
            resultado = modificarPersonajeResultado,
            resultadoBorrarRelacion = eliminarPersonajeResultado,
            resultadoBorrarRelaciones = eliminarPersonajesMultiplesResultado,
            seleccion = seleccion,
            onDismiss = {
                personajeViewModel.modificarModificarPersonajeResultado("")
                personajeViewModel.modificarEliminarPersonajeResultado("")
                personajeViewModel.modificarEliminarPersonajesMultiplesResultado("")
                personajeViewModel.cargarDescripcion("")
                personajeViewModel.mostrarDialogo(false)
            }
        )
    }
}