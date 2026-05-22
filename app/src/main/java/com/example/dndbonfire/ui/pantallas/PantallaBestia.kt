package com.example.dndbonfire.ui.pantallas

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
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.dndbonfire.ui.componentes.DialogoBestia
import com.example.dndbonfire.ui.theme.InformacionPrincipalStyle
import com.example.dndbonfire.ui.theme.InformacionSecundariaStyle
import com.example.dndbonfire.ui.theme.Titulo4Style
import com.example.dndbonfire.ui.theme.Titulo2Style
import com.example.dndbonfire.urlDnDApi
import com.example.dndbonfire.viewmodel.BestiaViewModel
import java.util.Locale
import androidx.compose.ui.platform.LocalLocale
import com.example.dndbonfire.ui.theme.Gris

@Composable
fun PantallaBestia(
    navController: NavController,
    idBestia: String,
    bestiaViewModel: BestiaViewModel
) {
    LaunchedEffect(idBestia) {
        bestiaViewModel.cargarBestia(idBestia)
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (bestiaViewModel.bestiaSeleccionadaTraducida.value == null) {
            // si no hay elementos en la lista de bestias y la búsqueda está vacía es porque no se han cargado
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
                text = "Cargando datos de la criatura...",
                style = InformacionSecundariaStyle
            )
        } else {
            // en cualquier otro caso se carga la bestia
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(
                        (300f * (1f - (scrollState.value / 500f).coerceAtLeast(0f)
                            .coerceAtMost(0.5f))).dp
                    )
            ) {
                // imagen de la bestia borrosa
                AsyncImage(
                    model = "$urlDnDApi${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.image}",
                    contentDescription = "Imagen bestia",
                    modifier = Modifier
                        .fillMaxWidth()
                        .blur(
                            radius = 16.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        ),
                    contentScale = ContentScale.FillWidth,
                    alpha = 0.7f
                )
                // imagen de la bestia
                AsyncImage(
                    model = "$urlDnDApi${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.image}",
                    contentDescription = "Imagen bestia",
                    modifier = Modifier
                        .matchParentSize(),
                )
                // challenge rating
                Box(
                    modifier = Modifier
                        .size(100.dp, 100.dp)
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
//                    AsyncImage(
//                        model = R.drawable.cargando,
//                        contentDescription = "GIF de carga",
//                        contentScale = ContentScale.Fit
//                    )
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        shape = CutCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .height(IntrinsicSize.Max)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CutCornerShape(8.dp)
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                textAlign = TextAlign.Center,
                                text = "Valor de desafío",
                                color = MaterialTheme.colorScheme.onSecondary,
                                style = InformacionSecundariaStyle
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxSize(),
                                textAlign = TextAlign.Center,
                                text = bestiaViewModel.bestiaSeleccionadaTraducida.value!!.challenge_rating.toString(),
                                style = Titulo4Style,
                                overflow = TextOverflow.Visible
                            )
                        }
                    }
                }
            }

            // nombre de la bestia
            Text(
                modifier = Modifier
                    .padding(8.dp, 0.dp),
                textAlign = TextAlign.Center,
                text = bestiaViewModel.bestiaSeleccionadaTraducida.value!!.name,
                style = Titulo2Style,
                color = MaterialTheme.colorScheme.primary
            )
            // tamaño y tipo de la bestia
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 32.dp),
                    text = "Tamaño: ${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.size}",
                    style = InformacionSecundariaStyle
                )
                VerticalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxHeight()
                )
                Text(
                    modifier = Modifier
                        .padding(start = 32.dp),
                    text = "Tipo: ${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.type.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(LocalLocale.current.platformLocale) else it.toString()
                    }}",
                    style = InformacionSecundariaStyle
                )
            }

            // stats
            Card(
                modifier = Modifier
                    .padding(16.dp)
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
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(3f)
                    ) {
                        Text(
                            text = "Puntos de vida: ${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.hit_points} (${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.hit_points_roll})",
                            style = InformacionPrincipalStyle
                        )
                        Row(
                            modifier = Modifier
                                .padding(top = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    text = "Fuerza: ${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.strength}"
                                )
                                Text(
                                    text = "Destreza: ${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.dexterity}"
                                )
                                Text(
                                    text = "Constitución: ${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.constitution}"
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    text = "Inteligencia: ${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.intelligence}"
                                )
                                Text(
                                    text = "Sabiduría: ${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.wisdom}"
                                )
                                Text(
                                    text = "Carisma: ${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.charisma}"
                                )
                            }
                        }
                    }
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
                            text = bestiaViewModel.bestiaSeleccionadaTraducida.value!!.hit_dice,
                            style = Titulo4Style
                        )
                    }
                }
            }

            // velocidades
            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(IntrinsicSize.Max),
                textAlign = TextAlign.Center,
                text = "Velocidad",
                style = Titulo4Style,
                color = MaterialTheme.colorScheme.secondary
            )
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .height(IntrinsicSize.Min)
            ) {
                if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.walk != null) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Paso",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.walk.toString(),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = Titulo4Style
                        )
                    }
                }
                if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.walk != null && (
                            bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.fly != null ||
                            bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.swim != null ||
                            bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.burrow != null ||
                            bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.climb != null
                        )
                ) {
                    VerticalDivider(
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxHeight()
                    )
                }
                if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.fly != null) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Vuelo",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.fly.toString(),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = Titulo4Style
                        )
                    }
                }
                if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.fly != null && (
                            bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.swim != null ||
                            bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.burrow != null ||
                            bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.climb != null
                        )
                ) {
                    VerticalDivider(
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxHeight()
                    )
                }
                if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.swim != null) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nado",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.swim.toString(),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = Titulo4Style
                        )
                    }
                }
                if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.swim != null && (
                            bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.burrow != null ||
                            bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.climb != null
                        )
                ) {
                    VerticalDivider(
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxHeight()
                    )
                }
                if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.burrow != null) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Excavación",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.burrow.toString(),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = Titulo4Style
                        )
                    }
                }
                if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.burrow != null &&
                    bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.climb != null
                ) {
                    VerticalDivider(
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxHeight()
                    )
                }
                if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.climb != null) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Escalada",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = bestiaViewModel.bestiaSeleccionadaTraducida.value!!.speed.climb.toString(),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = Titulo4Style
                        )
                    }
                }
            }

            // armaduras
            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(IntrinsicSize.Max),
                textAlign = TextAlign.Center,
                text = "Clase de armadura",
                style = Titulo4Style,
                color = MaterialTheme.colorScheme.secondary
            )
            Row(
                modifier = Modifier
            ) {
                bestiaViewModel.bestiaSeleccionadaTraducida.value!!.armor_class.forEach { armadura ->
                    Box(
                        modifier = Modifier
                            .size(124.dp)
                            .padding(
                                start = 8.dp,
                                end = 8.dp,
                                bottom = 16.dp
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Shield,
                            contentDescription = "Guardar cambios",
                            modifier = Modifier.size(124.dp),
                            tint = Gris
                        )
                        Column(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier,
                                textAlign = TextAlign.Center,
                                text = armadura.type.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(LocalLocale.current.platformLocale) else it.toString()
                                },
                                color = MaterialTheme.colorScheme.onBackground,
                                style = InformacionSecundariaStyle,
                                overflow = TextOverflow.Visible
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxSize(),
                                textAlign = TextAlign.Center,
                                text = armadura.value.toString(),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = Titulo2Style,
                                overflow = TextOverflow.Visible
                            )
                        }
                    }
                }
            }

            // alineamiento de la bestia
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        start = 16.dp
                    ),
                textAlign = TextAlign.Start,
                text = "Alineamiento: ${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.alignment}",
                style = InformacionSecundariaStyle
            )

            // idiomas que habla
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        start = 16.dp
                    ),
                textAlign = TextAlign.Start,
                text = "Idiomas: ${
                    bestiaViewModel.bestiaSeleccionadaTraducida.value!!.languages.ifEmpty {
                        "esta criatura no habla ningún idioma"
                    }
                }",
                style = InformacionSecundariaStyle
            )

            // experiencia
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        start = 16.dp
                    ),
                textAlign = TextAlign.Start,
                text = "Experiencia ganada al derrotarlo: ${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.xp}",
                style = InformacionSecundariaStyle
            )

            // sentidos
            Row(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column() {
                    if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.senses.blindsight != null) {
                        Text(
                            text = "Visión ciega: ${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.senses.blindsight}",
                            style = InformacionSecundariaStyle
                        )
                    }
                    if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.senses.darkvision != null) {
                        Text(
                            text = "Visión en la oscuridad: ${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.senses.darkvision}",
                            style = InformacionSecundariaStyle
                        )
                    }
                    if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.senses.tremorsense != null) {
                        Text(
                            text = "Mecanorrecepción : ${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.senses.tremorsense}",
                            style = InformacionSecundariaStyle
                        )
                    }
                    if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.senses.truesight != null) {
                        Text(
                            text = "Visión verdadera: ${bestiaViewModel.bestiaSeleccionadaTraducida.value!!.senses.truesight}",
                            style = InformacionSecundariaStyle
                        )
                    }
                }
                Column() {
                    Text(
                        modifier = Modifier
                            .fillMaxSize(),
                        textAlign = TextAlign.Center,
                        text = "Percepción pasiva",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxSize(),
                        textAlign = TextAlign.Center,
                        text = bestiaViewModel.bestiaSeleccionadaTraducida.value!!.senses.passive_perception.toString(),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = Titulo4Style
                    )
                }
            }

            // competencias
            if(!bestiaViewModel.bestiaSeleccionadaTraducida.value!!.proficiencies.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(IntrinsicSize.Max),
                    textAlign = TextAlign.Center,
                    text = "Competencias",
                    style = Titulo4Style,
                    color = MaterialTheme.colorScheme.secondary
                )
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!bestiaViewModel.bestiaSeleccionadaTraducida.value!!.proficiencies.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .weight(4f)
                        ) {
                            bestiaViewModel.bestiaSeleccionadaTraducida.value!!.proficiencies.forEach { competencia ->
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
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(IntrinsicSize.Max)
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = competencia.proficiency.name,
                                            style = InformacionPrincipalStyle,
                                            overflow = TextOverflow.Visible
                                        )
                                        Text(
                                            text = competencia.value.toString(),
                                            style = InformacionPrincipalStyle,
                                            overflow = TextOverflow.Visible
                                        )
                                    }
                                }
                            }
                        }
                    }
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
                            text = bestiaViewModel.bestiaSeleccionadaTraducida.value!!.proficiency_bonus.toString(),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = Titulo4Style
                        )
                    }
                }
            }

            var vulnerabilidades = ""
            var contadorVulnerabilidades = 1
            bestiaViewModel.bestiaSeleccionadaTraducida.value!!.damage_vulnerabilities.forEach {
                if(bestiaViewModel.bestiaSeleccionadaTraducida.value!!.damage_vulnerabilities.size == contadorVulnerabilidades) {
                    if (vulnerabilidades == "") {
                        vulnerabilidades = "$vulnerabilidades ${it.lowercase()}"
                    } else {
                        vulnerabilidades = "$vulnerabilidades y ${it.lowercase()}"
                    }
                } else {
                    if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.condition_immunities.size == contadorVulnerabilidades+1) {
                        vulnerabilidades = "$vulnerabilidades ${it.lowercase()}"
                    } else {
                        vulnerabilidades = "$vulnerabilidades ${it.lowercase()},"
                    }
                }
                contadorVulnerabilidades++
            }
            // vulnerabilidades de daño
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                textAlign = TextAlign.Start,
                text = if (!bestiaViewModel.bestiaSeleccionadaTraducida.value!!.damage_vulnerabilities.isEmpty()) {
                    "Esta criatura es vulnerable al daño de$vulnerabilidades"
                } else {
                    "Esta criatura no tiene vulnerabilidades de daño"
                },
                style = InformacionPrincipalStyle
            )

            var resistencias = ""
            var contadorResistencias = 1
            bestiaViewModel.bestiaSeleccionadaTraducida.value!!.damage_resistances.forEach {
                if(bestiaViewModel.bestiaSeleccionadaTraducida.value!!.damage_resistances.size == contadorResistencias) {
                    if (resistencias == "") {
                        resistencias = "$resistencias ${it.lowercase()}"
                    } else {
                        resistencias = "$resistencias y ${it.lowercase()}"
                    }
                } else {
                    if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.condition_immunities.size == contadorResistencias+1) {
                        resistencias = "$resistencias ${it.lowercase()}"
                    } else {
                        resistencias = "$resistencias ${it.lowercase()},"
                    }
                }
                contadorResistencias++
            }
            // resistencias de daño
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                textAlign = TextAlign.Start,
                text = if (!bestiaViewModel.bestiaSeleccionadaTraducida.value!!.damage_resistances.isEmpty()) {
                    "Esta criatura es resistente al daño de$resistencias"
                } else {
                    "Esta criatura no tiene resistencias de daño"
                },
                style = InformacionPrincipalStyle
            )

            var inmunidades = ""
            var contadorInmunidades = 1
            bestiaViewModel.bestiaSeleccionadaTraducida.value!!.damage_immunities.forEach {
                if(bestiaViewModel.bestiaSeleccionadaTraducida.value!!.damage_immunities.size == contadorInmunidades) {
                    if (inmunidades == "") {
                        inmunidades = "$inmunidades ${it.lowercase()}"
                    } else {
                        inmunidades = "$inmunidades y ${it.lowercase()}"
                    }
                } else {
                    if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.condition_immunities.size == contadorInmunidades+1) {
                        inmunidades = "$inmunidades ${it.lowercase()}"
                    } else {
                        inmunidades = "$inmunidades ${it.lowercase()},"
                    }
                }
                contadorInmunidades++
            }
            // inmunidades de daño
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                textAlign = TextAlign.Start,
                text = if (!bestiaViewModel.bestiaSeleccionadaTraducida.value!!.damage_immunities.isEmpty()) {
                    "Esta criatura es inmune al daño de$inmunidades"
                } else {
                    "Esta criatura no tiene inmunidades de daño"
                },
                style = InformacionPrincipalStyle
            )

            var inmunidadesDeCondicion = ""
            var contadorInmunidadesDeCondicion = 1
            bestiaViewModel.bestiaSeleccionadaTraducida.value!!.condition_immunities.forEach {
                if(bestiaViewModel.bestiaSeleccionadaTraducida.value!!.condition_immunities.size == contadorInmunidadesDeCondicion) {
                    if (inmunidadesDeCondicion == "") {
                        inmunidadesDeCondicion = "$inmunidadesDeCondicion ${it.name.lowercase()}"
                    } else {
                        inmunidadesDeCondicion = "$inmunidadesDeCondicion y ${it.name.lowercase()}"
                    }
                } else {
                    if (bestiaViewModel.bestiaSeleccionadaTraducida.value!!.condition_immunities.size == contadorInmunidadesDeCondicion+1) {
                        inmunidadesDeCondicion = "$inmunidadesDeCondicion ${it.name.lowercase()}"
                    } else {
                        inmunidadesDeCondicion = "$inmunidadesDeCondicion ${it.name.lowercase()},"
                    }
                }
                contadorInmunidadesDeCondicion++
            }
            // inmunidades de daño
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                textAlign = TextAlign.Start,
                text = if (!bestiaViewModel.bestiaSeleccionadaTraducida.value!!.condition_immunities.isEmpty()) {
                    "Esta criatura es inmune a las siguientes condiciones:$inmunidadesDeCondicion"
                } else {
                    "Esta criatura no tiene inmunidades de daño"
                },
                style = InformacionPrincipalStyle
            )

            // habilidades especiales
            if(!bestiaViewModel.bestiaSeleccionadaTraducida.value!!.special_abilities.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(IntrinsicSize.Max),
                    textAlign = TextAlign.Center,
                    text = "Habilidades especiales",
                    style = Titulo4Style,
                    color = MaterialTheme.colorScheme.secondary
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    bestiaViewModel.bestiaSeleccionadaTraducida.value!!.special_abilities.forEach { habilidad ->
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
                                        bestiaViewModel.cargarDescripcion(habilidad.desc)
                                        bestiaViewModel.mostrarDialogo(true)
                                        bestiaViewModel.seleccionarHabilidad(habilidad)
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
                                text = habilidad.name,
                                style = InformacionPrincipalStyle,
                                overflow = TextOverflow.Visible
                            )
                        }
                    }
                }
            }

            // acciones
            if(!bestiaViewModel.bestiaSeleccionadaTraducida.value!!.actions.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(IntrinsicSize.Max),
                    textAlign = TextAlign.Center,
                    text = "Acciones",
                    style = Titulo4Style,
                    color = MaterialTheme.colorScheme.secondary
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    bestiaViewModel.bestiaSeleccionadaTraducida.value!!.actions.forEach { accion ->
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
                                        bestiaViewModel.cargarDescripcion(accion.desc)
                                        bestiaViewModel.mostrarDialogo(true)
                                        bestiaViewModel.seleccionarAccion(accion)
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
                                text = accion.name,
                                style = InformacionPrincipalStyle,
                                overflow = TextOverflow.Visible
                            )
                        }
                    }
                }
            }

            // acciones legendarias
            if(!bestiaViewModel.bestiaSeleccionadaTraducida.value!!.legendary_actions.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(IntrinsicSize.Max),
                    textAlign = TextAlign.Center,
                    text = "Acciones legendarias",
                    style = Titulo4Style,
                    color = MaterialTheme.colorScheme.secondary
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    bestiaViewModel.bestiaSeleccionadaTraducida.value!!.legendary_actions.forEach { accion ->
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
                                        bestiaViewModel.cargarDescripcion(accion.desc)
                                        bestiaViewModel.mostrarDialogo(true)
                                        bestiaViewModel.seleccionarAccionLegendaria(accion)
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
                                text = accion.name,
                                style = InformacionPrincipalStyle,
                                overflow = TextOverflow.Visible
                            )
                        }
                    }
                }
            }

            // reacciones
            if(!bestiaViewModel.bestiaSeleccionadaTraducida.value!!.reactions.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(IntrinsicSize.Max),
                    textAlign = TextAlign.Center,
                    text = "Reacciones",
                    style = Titulo4Style,
                    color = MaterialTheme.colorScheme.secondary
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    bestiaViewModel.bestiaSeleccionadaTraducida.value!!.reactions.forEach { reaccion ->
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
                                        bestiaViewModel.cargarDescripcion(reaccion.desc)
                                        bestiaViewModel.mostrarDialogo(true)
                                        bestiaViewModel.seleccionarReaccion(reaccion)
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
                                text = reaccion.name,
                                style = InformacionPrincipalStyle,
                                overflow = TextOverflow.Visible
                            )
                        }
                    }
                }
            }
        }
    }

    if (bestiaViewModel.mostrarDialogo.value) {
        DialogoBestia(
            bestiaViewModel = bestiaViewModel,
            habilidad = bestiaViewModel.habilidad.value,
            accion = bestiaViewModel.accion.value,
            accionLegendaria = bestiaViewModel.accionLegendaria.value,
            reaccion = bestiaViewModel.reaccion.value,
            bestia = bestiaViewModel.bestiaSeleccionada.value,
            onDismiss = {
                bestiaViewModel.mostrarDialogo(false)
                bestiaViewModel.seleccionarHabilidad(null)
                bestiaViewModel.seleccionarAccion(null)
                bestiaViewModel.seleccionarAccionLegendaria(null)
                bestiaViewModel.seleccionarReaccion(null)
                bestiaViewModel.cargarDescripcion(null)
            }
        )
    }
}