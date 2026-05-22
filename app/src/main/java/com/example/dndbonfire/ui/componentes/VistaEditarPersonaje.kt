package com.example.dndbonfire.ui.componentes

import android.util.Log
import android.widget.Toast
import androidx.collection.mutableIntListOf
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.dndbonfire.R
import com.example.dndbonfire.ui.pantallas.AlineamientoCard
import com.example.dndbonfire.ui.theme.ButtonStyle
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.ui.theme.InformacionPrincipalStyle
import com.example.dndbonfire.ui.theme.InformacionSecundariaStyle
import com.example.dndbonfire.ui.theme.Titulo3Style
import com.example.dndbonfire.ui.theme.Titulo4Style
import com.example.dndbonfire.ui.theme.Titulo5Style
import com.example.dndbonfire.ui.theme.Titulo6Style
import com.example.dndbonfire.viewmodel.ImagenViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun VistaEditarPersonaje(
    personajeViewModel: PersonajeViewModel,
    imagenViewModel: ImagenViewModel
) {
    val context = LocalContext.current
    val personaje by personajeViewModel.personaje
    val razas = personajeViewModel.razas
    val clases = personajeViewModel.clases
    val competenciasSeleccionadas = personajeViewModel.competenciasSeleccionadas
    val conjuros = personajeViewModel.conjuros
    val trucosConocidos = personajeViewModel.trucosConocidos
    val conjurosConocidos = personajeViewModel.conjurosConocidos
    val seleccion by personajeViewModel.infoSeleccionada
    val mostrarDialogo by personajeViewModel.mostrarDialogo
    val mostrarDialogoImagen by imagenViewModel.mostrarDialogoImagen

    val imagenEliminada by imagenViewModel.imagenEliminada
    val imagenUri by imagenViewModel.imagenUri
    val bitmap by imagenViewModel.bitmap
    val nombre by personajeViewModel.nombre
    val hp by personajeViewModel.hp
    val hpAuto by personajeViewModel.hpAuto
    val fuerza by personajeViewModel.fuerza
    val destreza by personajeViewModel.destreza
    val constitucion by personajeViewModel.constitucion
    val inteligencia by personajeViewModel.inteligencia
    val sabiduria by personajeViewModel.sabiduria
    val carisma by personajeViewModel.carisma
    val razaMod by personajeViewModel.razaMod
    val razaTraducidaMod by personajeViewModel.razaTraducidaMod
    val menuRazaExpandido by personajeViewModel.menuRazaExpandido
    val subrazaMod by personajeViewModel.subrazaMod
    val subrazaTraducidaMod by personajeViewModel.subrazaTraducidaMod
    val menuSubrazaExpandido by personajeViewModel.menuSubrazaExpandido
    val edad by personajeViewModel.edad
    val alineamiento by personajeViewModel.alineamiento
    val idiomas = personajeViewModel.idiomas
    val menuIdiomaExpandido by personajeViewModel.menuIdiomaExpandido
    val idiomaSeleccionado by personajeViewModel.idiomaSeleccionado
    val claseMod by personajeViewModel.claseMod
    val claseTraducidaMod by personajeViewModel.claseTraducidaMod
    val menuClaseExpandido by personajeViewModel.menuClaseExpandido
    val subclaseMod by personajeViewModel.subclaseMod
    val subclaseTraducidaMod by personajeViewModel.subclaseTraducidaMod
    val menuSubclaseExpandido by personajeViewModel.menuSubclaseExpandido
    val nivel by personajeViewModel.nivel
    val infoNivelesMod = personajeViewModel.infoNiveles
    val modificarPersonajeResultado by personajeViewModel.modificarPersonajeResultado

    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                model = bitmap ?: imagenUri ?: if (!imagenEliminada) { personaje?.imagen?.let { "data:image/jpg;base64,$it" } ?: R.drawable.avatar } else R.drawable.avatar,
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
                // si hay una imagen recién sacada con la cámara carga esa
                // si hay una imagen recién cargada de la galería carga esa
                // si no tiene ninguna de las anteriores carga el placeholder
                model = bitmap ?: imagenUri ?: if (!imagenEliminada) { personaje?.imagen?.let { "data:image/jpg;base64,$it" } ?: R.drawable.avatar } else R.drawable.avatar,
                contentDescription = "Imagen del personaje",
                modifier = Modifier
                    .matchParentSize()
                    .combinedClickable(
                        onClick = {
                            imagenViewModel.mostrarDialogo(true)
                        }
                    )
            )
        }

        // nombre
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 4.dp),
            value = nombre,
            onValueChange = { personajeViewModel.modificarNombre(it) },
            label = { Text(text = "Nombre") },
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
            ) {
                Text(
                    text = "Estadísticas",
                    style = Titulo5Style,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )
                Text(
                    text = "Puntos de vida: ${hp}",
                    style = InformacionPrincipalStyle
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.onSecondary,
                            checkmarkColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        checked = hpAuto,
                        onCheckedChange = {
                            personajeViewModel.modificarHpAuto(true)
                            var hpAutomatico = 0
                            if (claseMod != null && nivel > 0) {
                                hpAutomatico = (ceil(((claseMod!!.hit_die+1).toDouble()/2) + ceil((constitucion - 10).toDouble()/2)) * (nivel - 1) + claseMod!!.hit_die + ceil((constitucion - 10).toDouble()/2)).toInt()
                            }
                            personajeViewModel.modificarHp(hpAutomatico)
                        }
                    )
                    Column() {
                        Text(text = "Calcular automáticamente")
                        Text(text = "(Requiere nivel y clase)")
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(start = 8.dp),
                        value = hp.toString(),
                        onValueChange = { try {
                            var nuevoValor = it
                            if (hp == 0) {
                                nuevoValor = it.replace("0", "")
                            }
                            personajeViewModel.modificarHp(Integer.valueOf(nuevoValor))
                            // desactiva el cálculo automático de la vida
                            personajeViewModel.modificarHpAuto(false)
                        } catch (e: Exception) {
                            personajeViewModel.modificarHp(0)
                        }},
                        label = { Text(text = "Añadir a mano") },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSecondary,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        shape = CutCornerShape(16.dp),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(4f)) {
                        // stats
                        Row(modifier = Modifier.padding(top = 8.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row() {
                                    OutlinedTextField(
                                        modifier = Modifier.width(120.dp),
                                        value = fuerza.toString(),
                                        onValueChange = { try {
                                            var nuevoValor = it
                                            if (fuerza == 0) {
                                                nuevoValor = it.replace("0", "")
                                            }
                                            // no permite que supere 20 como valor
                                            if (Integer.valueOf(nuevoValor) > 20) {
                                                nuevoValor = "20"
                                            }
                                            personajeViewModel.modificarFuerza(Integer.valueOf(nuevoValor))
                                        } catch (e: Exception) {
                                            personajeViewModel.modificarFuerza(0)
                                        }},
                                        label = { Text(text = "Fuerza") },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSecondary,
                                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                                            focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                                            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                            cursorColor = MaterialTheme.colorScheme.primary,
                                            focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedTextColor = MaterialTheme.colorScheme.onSecondary
                                        ),
                                        shape = CutCornerShape(16.dp),
                                        maxLines = 1,
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Done
                                        )
                                    )
                                    razaMod?.ability_bonuses?.forEach {
                                        if (it.ability_score.index == "str") {
                                            Text(
                                                text = "+${it.bonus}",
                                                style = Titulo4Style
                                            )
                                        }
                                    }
                                    subrazaMod?.ability_bonuses?.forEach {
                                        if (it.ability_score.index == "str") {
                                            Text(
                                                text = "+${it.bonus}",
                                                style = Titulo4Style
                                            )
                                        }
                                    }
                                }
                                Row() {
                                    OutlinedTextField(
                                        modifier = Modifier.width(120.dp),
                                        value = destreza.toString(),
                                        onValueChange = { try {
                                            var nuevoValor = it
                                            if (destreza == 0) {
                                                nuevoValor = it.replace("0", "")
                                            }
                                            // no permite que supere 20 como valor
                                            if (Integer.valueOf(nuevoValor) > 20) {
                                                nuevoValor = "20"
                                            }
                                            personajeViewModel.modificarDestreza(Integer.valueOf(nuevoValor))
                                        } catch (e: Exception) {
                                            personajeViewModel.modificarDestreza(0)
                                        }},
                                        label = { Text(text = "Destreza") },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSecondary,
                                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                                            focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                                            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                            cursorColor = MaterialTheme.colorScheme.primary,
                                            focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedTextColor = MaterialTheme.colorScheme.onSecondary
                                        ),
                                        shape = CutCornerShape(16.dp),
                                        maxLines = 1,
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Done
                                        )
                                    )
                                    razaMod?.ability_bonuses?.forEach {
                                        if (it.ability_score.index == "dex") {
                                            Text(
                                                text = "+${it.bonus}",
                                                style = Titulo4Style
                                            )
                                        }
                                    }
                                    subrazaMod?.ability_bonuses?.forEach {
                                        if (it.ability_score.index == "dex") {
                                            Text(
                                                text = "+${it.bonus}",
                                                style = Titulo4Style
                                            )
                                        }
                                    }
                                }
                                Row() {
                                    OutlinedTextField(
                                        modifier = Modifier.width(120.dp),
                                        value = constitucion.toString(),
                                        onValueChange = { try {
                                            var nuevoValor = it
                                            if (constitucion == 0) {
                                                nuevoValor = it.replace("0", "")
                                            }
                                            // no permite que supere 20 como valor
                                            if (Integer.valueOf(nuevoValor) > 20) {
                                                nuevoValor = "20"
                                            }
                                            personajeViewModel.modificarConstitucion(Integer.valueOf(nuevoValor))

                                            if (hpAuto) {
                                                var hpAutomatico = 0
                                                if (claseMod != null && nivel > 0) {
                                                    hpAutomatico = (ceil(((claseMod!!.hit_die+1).toDouble()/2) + ceil((constitucion - 10).toDouble()/2)) * (nivel - 1) + claseMod!!.hit_die + ceil((constitucion - 10).toDouble()/2)).toInt()
                                                }
                                                personajeViewModel.modificarHp(hpAutomatico)
                                            }
                                        } catch (e: Exception) {
                                            personajeViewModel.modificarConstitucion(0)
                                        }},
                                        label = { Text(text = "Constitución") },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSecondary,
                                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                                            focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                                            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                            cursorColor = MaterialTheme.colorScheme.primary,
                                            focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedTextColor = MaterialTheme.colorScheme.onSecondary
                                        ),
                                        shape = CutCornerShape(16.dp),
                                        maxLines = 1,
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Done
                                        )
                                    )
                                    razaMod?.ability_bonuses?.forEach {
                                        if (it.ability_score.index == "con") {
                                            Text(
                                                text = "+${it.bonus}",
                                                style = Titulo4Style
                                            )
                                        }
                                    }
                                    subrazaMod?.ability_bonuses?.forEach {
                                        if (it.ability_score.index == "con") {
                                            Text(
                                                text = "+${it.bonus}",
                                                style = Titulo4Style
                                            )
                                        }
                                    }
                                }
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row() {
                                    OutlinedTextField(
                                        modifier = Modifier.width(120.dp),
                                        value = inteligencia.toString(),
                                        onValueChange = { try {
                                            var nuevoValor = it
                                            if (inteligencia == 0) {
                                                nuevoValor = it.replace("0", "")
                                            }
                                            // no permite que supere 20 como valor
                                            if (Integer.valueOf(nuevoValor) > 20) {
                                                nuevoValor = "20"
                                            }

                                            // si es un mago comprueba si la inteligencia actual es menor a la inteligencia anterior y si se cumple vacía la lista de hechizos
                                            claseTraducidaMod?.let { clase ->
                                                if (clase.index == "wizard" && Integer.valueOf(nuevoValor) < inteligencia) {
                                                    personajeViewModel.modificarTrucosConocidos("", null)
                                                    personajeViewModel.modificarConjurosConocidos("", null)
                                                }
                                            }

                                            personajeViewModel.modificarInteligencia(Integer.valueOf(nuevoValor))
                                        } catch (e: Exception) {
                                            personajeViewModel.modificarInteligencia(0)
                                        }},
                                        label = { Text(text = "Inteligencia") },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSecondary,
                                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                                            focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                                            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                            cursorColor = MaterialTheme.colorScheme.primary,
                                            focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedTextColor = MaterialTheme.colorScheme.onSecondary
                                        ),
                                        shape = CutCornerShape(16.dp),
                                        maxLines = 1,
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Done
                                        )
                                    )
                                    razaMod?.ability_bonuses?.forEach {
                                        if (it.ability_score.index == "int") {
                                            Text(
                                                text = "+${it.bonus}",
                                                style = Titulo4Style
                                            )
                                        }
                                    }
                                    subrazaMod?.ability_bonuses?.forEach {
                                        if (it.ability_score.index == "int") {
                                            Text(
                                                text = "+${it.bonus}",
                                                style = Titulo4Style
                                            )
                                        }
                                    }
                                }
                                Row() {
                                    OutlinedTextField(
                                        modifier = Modifier.width(120.dp),
                                        value = sabiduria.toString(),
                                        onValueChange = { try {
                                            var nuevoValor = it
                                            if (sabiduria == 0) {
                                                nuevoValor = it.replace("0", "")
                                            }
                                            // no permite que supere 20 como valor
                                            if (Integer.valueOf(nuevoValor) > 20) {
                                                nuevoValor = "20"
                                            }

                                            // si es un clérigo o un druida comprueba si la sabiduría actual es menor a la sabiduría anterior y si se cumple vacía la lista de hechizos
                                            claseTraducidaMod?.let { clase ->
                                                if ((clase.index == "cleric" || clase.index == "druid") && Integer.valueOf(nuevoValor) < sabiduria) {
                                                    personajeViewModel.modificarTrucosConocidos("", null)
                                                    personajeViewModel.modificarConjurosConocidos("", null)
                                                }
                                            }

                                            personajeViewModel.modificarSabiduria(Integer.valueOf(nuevoValor))
                                        } catch (e: Exception) {
                                            personajeViewModel.modificarSabiduria(0)
                                        }},
                                        label = { Text(text = "Sabiduría") },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSecondary,
                                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                                            focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                                            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                            cursorColor = MaterialTheme.colorScheme.primary,
                                            focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedTextColor = MaterialTheme.colorScheme.onSecondary
                                        ),
                                        shape = CutCornerShape(16.dp),
                                        maxLines = 1,
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Done
                                        )
                                    )
                                    razaMod?.ability_bonuses?.forEach {
                                        if (it.ability_score.index == "wis") {
                                            Text(
                                                text = "+${it.bonus}",
                                                style = Titulo4Style
                                            )
                                        }
                                    }
                                    subrazaMod?.ability_bonuses?.forEach {
                                        if (it.ability_score.index == "wis") {
                                            Text(
                                                text = "+${it.bonus}",
                                                style = Titulo4Style
                                            )
                                        }
                                    }
                                }
                                Row() {
                                    OutlinedTextField(
                                        modifier = Modifier.width(120.dp),
                                        value = carisma.toString(),
                                        onValueChange = { try {
                                            var nuevoValor = it
                                            if (carisma == 0) {
                                                nuevoValor = it.replace("0", "")
                                            }
                                            // no permite que supere 20 como valor
                                            if (Integer.valueOf(nuevoValor) > 20) {
                                                nuevoValor = "20"
                                            }
                                            personajeViewModel.modificarCarisma(Integer.valueOf(nuevoValor))
                                        } catch (e: Exception) {
                                            personajeViewModel.modificarCarisma(0)
                                        }},
                                        label = { Text(text = "Carisma") },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSecondary,
                                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                                            focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                                            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                            cursorColor = MaterialTheme.colorScheme.primary,
                                            focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                                            unfocusedTextColor = MaterialTheme.colorScheme.onSecondary
                                        ),
                                        shape = CutCornerShape(16.dp),
                                        maxLines = 1,
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Done
                                        )
                                    )
                                    razaMod?.ability_bonuses?.forEach {
                                        if (it.ability_score.index == "cha") {
                                            Text(
                                                text = "+${it.bonus}",
                                                style = Titulo4Style
                                            )
                                        }
                                    }
                                    subrazaMod?.ability_bonuses?.forEach {
                                        if (it.ability_score.index == "cha") {
                                            Text(
                                                text = "+${it.bonus}",
                                                style = Titulo4Style
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (claseMod != null) {
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
                                text = claseMod?.hit_die.toString(),
                                style = Titulo4Style
                            )
                        }
                    }
                }
                Text (
                    text = "*Las estadísticas de un personaje no pueden superar 20 (excepto mediante uso de artilugios externos)."
                )
            }
        }

        // raza
        Text(
            textAlign = TextAlign.Center,
            text = "Raza",
            style = Titulo3Style,
            color = MaterialTheme.colorScheme.secondary
        )
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box() {
                Button(
                    modifier = Modifier
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
                        personajeViewModel.modificarMenuRazaExpandido(!menuRazaExpandido)
                    }
                ) {
                    Text(
                        text = razaTraducidaMod?.name ?: "- Raza -",
                        style = ButtonStyle
                    )
                }
                DropdownMenu(
                    expanded = menuRazaExpandido,
                    onDismissRequest = {
                        personajeViewModel.modificarMenuRazaExpandido(false)
                    }
                ) {
                    (razas).forEach {
                        DropdownMenuItem(
                            text = { Text(it.name) },
                            onClick = {
                                personajeViewModel.cargarRazaMod(it.index)
                                personajeViewModel.modificarMenuRazaExpandido(false)
                            }
                        )
                    }
                }
            }
            if (razaTraducidaMod != null && razaMod?.subraces?.isEmpty() != true) {
                Box() {
                    Button(
                        modifier = Modifier
                            .padding(start = 4.dp)
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
                            personajeViewModel.modificarMenuSubrazaExpandido(!menuSubrazaExpandido)
                        }
                    ) {
                        Text(
                            text = subrazaTraducidaMod?.name ?: "- Subraza -",
                            style = ButtonStyle
                        )
                    }
                    DropdownMenu(
                        expanded = menuSubrazaExpandido,
                        onDismissRequest = {
                            personajeViewModel.modificarMenuSubrazaExpandido(false)
                        }
                    ) {
                        (razaTraducidaMod?.subraces)?.forEach {
                            DropdownMenuItem(
                                text = { Text(it.name) },
                                onClick = {
                                    personajeViewModel.cargarSubrazaMod(it.index)
                                    personajeViewModel.modificarMenuSubrazaExpandido(false)
                                }
                            )
                        }
                    }
                }
                if (subrazaMod != null) {
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
            if (razaTraducidaMod != null) {
                VerticalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp)
                )
                Text(
                    modifier = Modifier
                        .width(IntrinsicSize.Min),
                    textAlign = TextAlign.Center,
                    text = "Tamaño: ${razaTraducidaMod!!.size}",
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
            }
        }

        // velocidad
        if (razaTraducidaMod != null) {
            Text(
                modifier = Modifier
                    .padding(top = 8.dp),
                text = "Velocidad",
                style = Titulo4Style,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "${razaTraducidaMod!!.speed.toString()} ft.",
                color = MaterialTheme.colorScheme.onBackground,
                style = Titulo4Style
            )
        }

        // edad
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(16.dp, 4.dp),
                value = edad.toString(),
                onValueChange = { try {
                    var nuevoValor = it
                    if (edad == 0) {
                        nuevoValor = it.replace("0", "")
                    }
                    personajeViewModel.modificarEdad(Integer.valueOf(nuevoValor))
                } catch (e: Exception) {
                    personajeViewModel.modificarEdad(0)
                }},
                label = { Text(text = "Edad") },
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
            if (razaTraducidaMod != null) {
                TextButton(
                    onClick = {
                        personajeViewModel.modificarInfoSeleccionada("edad")
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
            }
        }

        // alineamiento
        Text(
            modifier = Modifier
                .padding(top = 8.dp),
            text = "Alineamiento",
            style = Titulo4Style,
            color = MaterialTheme.colorScheme.secondary
        )
        Row (
            Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .fillMaxWidth()
        ) {
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                AlineamientoCard(
                    texto = "Legal bueno",
                    alineamientoActual = alineamiento,
                    onClick = {
                        personajeViewModel.modificarAlineamiento("Legal bueno")
                    }
                )
                AlineamientoCard(
                    texto = "Legal neutral",
                    alineamientoActual = alineamiento,
                    onClick = {
                        personajeViewModel.modificarAlineamiento("Legal neutral")
                    }
                )
                AlineamientoCard(
                    texto = "Legal malo",
                    alineamientoActual = alineamiento,
                    onClick = {
                        personajeViewModel.modificarAlineamiento("Legal malo")
                    }
                )
            }
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                AlineamientoCard(
                    texto = "Neutral bueno",
                    alineamientoActual = alineamiento,
                    onClick = {
                        personajeViewModel.modificarAlineamiento("Neutral bueno")
                    }
                )
                AlineamientoCard(
                    texto = "Neutral verdadero",
                    alineamientoActual = alineamiento,
                    onClick = {
                        personajeViewModel.modificarAlineamiento("Neutral verdadero")
                    }
                )
                AlineamientoCard(
                    texto = "Neutral malo",
                    alineamientoActual = alineamiento,
                    onClick = {
                        personajeViewModel.modificarAlineamiento("Neutral malo")
                    }
                )
            }
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                AlineamientoCard(
                    texto = "Caótico bueno",
                    alineamientoActual = alineamiento,
                    onClick = {
                        personajeViewModel.modificarAlineamiento("Caótico bueno")
                    }
                )
                AlineamientoCard(
                    texto = "Caótico neutral",
                    alineamientoActual = alineamiento,
                    onClick = {
                        personajeViewModel.modificarAlineamiento("Caótico neutral")
                    }
                )
                AlineamientoCard(
                    texto = "Caótico malo",
                    alineamientoActual = alineamiento,
                    onClick = {
                        personajeViewModel.modificarAlineamiento("Caótico malo")
                    }
                )
            }
            if (razaTraducidaMod != null) {
                TextButton(
                    onClick = {
                        personajeViewModel.modificarInfoSeleccionada("alineamiento")
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
            }
        }

        // idiomas que habla
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(vertical = 4.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (razaTraducidaMod != null) {
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var idiomas = "Idiomas:"
                    razaTraducidaMod!!.languages.forEach {
                        idiomas += " ${it.name},"
                    }
                    idiomas = idiomas.dropLast(1)
                    Text(
                        textAlign = TextAlign.Start,
                        text = idiomas,
                        style = InformacionSecundariaStyle
                    )
                    TextButton(
                        onClick = {
                            personajeViewModel.modificarInfoSeleccionada("idioma")
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
                }
            }

            if (razaTraducidaMod != null) {
                var idiomaExtra = false
                if (razaTraducidaMod!!.name == "Humano") {
                    idiomaExtra = true
                } else {
                    if (subrazaTraducidaMod != null) {
                        subrazaTraducidaMod!!.racial_traits.forEach { rasgo ->
                            if (rasgo.name == "Lenguaje extra") {
                                idiomaExtra = true
                            }
                        }
                    }
                }

                if (idiomaExtra) {
                    Box() {
                        Button(
                            modifier = Modifier
                                .padding(start = 4.dp)
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
                                // se asegura de que idiomas no se quede vacío en caso de que haya habido algún problema
                                if (idiomas.isEmpty()) {
                                    personajeViewModel.cargarIdiomas()
                                }
                                personajeViewModel.modificarMenuIdiomaExpandido(!menuIdiomaExpandido)
                            }
                        ) {
                            Text(
                                text = idiomaSeleccionado?.name ?: "- Idioma adicional -",
                                style = ButtonStyle
                            )
                        }
                        DropdownMenu(
                            expanded = menuIdiomaExpandido,
                            onDismissRequest = {
                                personajeViewModel.modificarMenuIdiomaExpandido(false)
                            }
                        ) {
                            (idiomas).forEach {
                                // no incluye los idiomas que ya conozca el personaje
                                var noIncluir = false
                                razaTraducidaMod!!.languages.forEach { idioma ->
                                    if (idioma == it) noIncluir = true
                                }

                                if (!noIncluir) {
                                    DropdownMenuItem(
                                        text = { Text(it.name) },
                                        onClick = {
                                            personajeViewModel.cargarIdioma(it.index)
                                            personajeViewModel.modificarMenuIdiomaExpandido(false)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // rasgos raciales
        if (razaTraducidaMod != null && !razaTraducidaMod!!.traits.isEmpty()) {
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
                razaTraducidaMod!!.traits.forEach { rasgo ->
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
        if (subrazaTraducidaMod != null && !subrazaTraducidaMod!!.racial_traits.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                subrazaTraducidaMod!!.racial_traits.forEach { rasgo ->
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

        // desplegable de clases y estadísticas dependientes de la clase
        // clase
        Text(
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Center,
            text = "Clase",
            style = Titulo3Style,
            color = MaterialTheme.colorScheme.secondary
        )
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // clase
            Box() {
                Button(
                    modifier = Modifier
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
                        personajeViewModel.modificarMenuClaseExpandido(!menuClaseExpandido)
                    }
                ) {
                    Text(
                        text = claseTraducidaMod?.name ?: "- Clase -",
                        style = ButtonStyle
                    )
                }
                DropdownMenu(
                    expanded = menuClaseExpandido,
                    onDismissRequest = {
                        personajeViewModel.modificarMenuClaseExpandido(false)
                    }
                ) {
                    (clases).forEach {
                        DropdownMenuItem(
                            text = { Text(it.name) },
                            onClick = {
                                personajeViewModel.modificarTrucosConocidos("", null)
                                personajeViewModel.modificarConjurosConocidos("", null)
                                personajeViewModel.modificarCompetenciasSeleccionadas("", null, null)

                                personajeViewModel.cargarClaseMod(it.index)
                                personajeViewModel.modificarMenuClaseExpandido(false)
                            }
                        )
                    }
                }
            }
            // subclase
            if (claseTraducidaMod != null) {
                Box() {
                    Button(
                        modifier = Modifier
                            .padding(start = 4.dp)
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
                            personajeViewModel.modificarMenuSubclaseExpandido(!menuSubclaseExpandido)
                        }
                    ) {
                        Text(
                            modifier = Modifier
                                .width(IntrinsicSize.Min),
                            textAlign = TextAlign.Center,
                            text = subclaseTraducidaMod?.name ?: "-Subclase-",
                            style = ButtonStyle
                        )
                    }
                    DropdownMenu(
                        expanded = menuSubclaseExpandido,
                        onDismissRequest = {
                            personajeViewModel.modificarMenuSubclaseExpandido(false)
                        }
                    ) {
                        (claseTraducidaMod?.subclasses)?.forEach {
                            DropdownMenuItem(
                                text = { Text(it.name) },
                                onClick = {
                                    personajeViewModel.cargarSubclaseMod(it.index)
                                    personajeViewModel.modificarMenuSubclaseExpandido(false)
                                }
                            )
                        }
                    }
                }
                if (subclaseMod != null) {
                    TextButton(
                        onClick = {
                            personajeViewModel.modificarInfoSeleccionada("subclase")
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
            // nivel
            OutlinedTextField(
                modifier = Modifier
                    .width(75.dp)
                    .padding(start = 8.dp),
                value = nivel.toString(),
                onValueChange = { try {
                    var nuevoValor = it
                    if (nivel == 0) {
                        nuevoValor = it.replace("0", "")
                    }
                    // no permite que supere 20 como valor
                    if (Integer.valueOf(nuevoValor) > 20) {
                        nuevoValor = "20"
                    }

                    // comprueba si el nivel actual es menor al nivel anterior y si se cumple vacía la lista de hechizos
                    if (Integer.valueOf(nuevoValor) < nivel) {
                        personajeViewModel.modificarTrucosConocidos("", null)
                        personajeViewModel.modificarConjurosConocidos("", null)
                    }

                    personajeViewModel.modificarNivel(Integer.valueOf(nuevoValor))

                    if (hpAuto) {
                        var hpAutomatico = 0
                        if (claseMod != null && nivel > 0) {
                            hpAutomatico = (ceil(((claseMod!!.hit_die+1).toDouble()/2) + ceil((constitucion - 10).toDouble()/2)) * (nivel - 1) + claseMod!!.hit_die + ceil((constitucion - 10).toDouble()/2)).toInt()
                        }
                        personajeViewModel.modificarHp(hpAutomatico)
                    }
                } catch (e: Exception) {
                    personajeViewModel.modificarTrucosConocidos("", null)
                    personajeViewModel.modificarConjurosConocidos("", null)

                    personajeViewModel.modificarNivel(0)
                }},
                label = { Text(text = "Nivel") },
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
                        var nuevoNivel = nivel+1
                        // no permite que supere 20 como valor
                        if (Integer.valueOf(nuevoNivel) >= 20) {
                            nuevoNivel = 20
                        }

                        personajeViewModel.modificarNivel(Integer.valueOf(nuevoNivel))
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
                        var nuevoNivel = nivel-1
                        // no permite que baje de 1
                        if (nuevoNivel <= 0) {
                            nuevoNivel = 1
                        }

                        personajeViewModel.modificarTrucosConocidos("", null)
                        personajeViewModel.modificarConjurosConocidos("", null)

                        personajeViewModel.modificarNivel(Integer.valueOf(nuevoNivel))
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

        // atributos especificos de personaje
        if (!infoNivelesMod.isEmpty() && nivel > 0) {
            val infoNivel = infoNivelesMod.find { it.level == nivel }
            when (claseMod?.index) {
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
        if (claseTraducidaMod != null) {
            claseTraducidaMod!!.saving_throws.forEach {
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
        if (claseTraducidaMod != null && !claseTraducidaMod!!.proficiencies.isEmpty()) {
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
                        claseTraducidaMod!!.proficiencies.forEach { competencia ->
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
                    if (!infoNivelesMod.isEmpty() && nivel > 0) {
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
                                text = infoNivelesMod[nivel-1].prof_bonus.toString(),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = Titulo4Style
                            )
                        }
                    }
                }

                // de clase en base al nivel
                if (!infoNivelesMod.isEmpty()) {
                    infoNivelesMod.forEach {
                        if (it.level <= nivel) {
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
        if (claseTraducidaMod != null && !claseTraducidaMod!!.proficiency_choices.isEmpty()) {
            var eleccionesPorLista = mutableIntListOf()
            claseTraducidaMod!!.proficiency_choices.forEach { eleccionCompetencia ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 8.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 4.dp
                        ),
                    textAlign = TextAlign.Start,
                    text = eleccionCompetencia.desc,
                    style = InformacionPrincipalStyle
                )

                eleccionesPorLista.add(eleccionCompetencia.choose)

                eleccionCompetencia.from.options?.forEach { opcion ->
                    if (opcion.option_type == "reference") {
                        var estaSeleccionado = false
                        competenciasSeleccionadas.forEach { listaCompetencias ->
                            listaCompetencias.forEach { competencia ->
                                if (opcion.item?.index == competencia) estaSeleccionado = true
                            }
                        }

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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Max)
                                    .padding(
                                        horizontal = 12.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = opcion.item!!.name,
                                    style = InformacionPrincipalStyle,
                                    overflow = TextOverflow.Visible
                                )
                                Checkbox(
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.secondary,
                                        uncheckedColor = MaterialTheme.colorScheme.tertiary,
                                        checkmarkColor = MaterialTheme.colorScheme.onSecondary
                                    ),
                                    checked = estaSeleccionado,
                                    onCheckedChange = {
                                        var numLista = 0
                                        var contador = 0
                                        claseTraducidaMod!!.proficiency_choices.forEach { eleccionCompetencia ->
                                            eleccionCompetencia.from.options?.forEach { nuevaOpcion ->
                                                if (opcion.item.index == nuevaOpcion.item?.index) numLista = contador
                                            }
                                            contador++
                                        }

                                        if (estaSeleccionado) {
                                            personajeViewModel.modificarCompetenciasSeleccionadas(
                                                "borrar",
                                                numLista,
                                                opcion.item.index
                                            )
                                        } else {
                                            if (competenciasSeleccionadas[numLista].size < eleccionesPorLista[numLista]) {
                                                personajeViewModel.modificarCompetenciasSeleccionadas(
                                                    "añadir",
                                                    numLista,
                                                    opcion.item.index
                                                )
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "No se pueden seleccionar más de ${eleccionCompetencia.choose} competencias en esta sección.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                )

                            }
                        }
                    }
                    else {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 8.dp,
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 4.dp
                                ),
                            textAlign = TextAlign.Start,
                            text = "${opcion.choice?.desc}:"
                        )

                        opcion.choice?.from?.options?.forEach { opcionDeOpcion ->
                            var estaSeleccionado = false
                            competenciasSeleccionadas.forEach { listaCompetencias ->
                                listaCompetencias.forEach { competencia ->
                                    if (opcionDeOpcion.item?.index == competencia) estaSeleccionado = true
                                }
                            }

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
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Max)
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = opcionDeOpcion.item?.name ?: "",
                                        style = InformacionPrincipalStyle,
                                        overflow = TextOverflow.Visible
                                    )
                                    Checkbox(
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = MaterialTheme.colorScheme.secondary,
                                            uncheckedColor = MaterialTheme.colorScheme.tertiary,
                                            checkmarkColor = MaterialTheme.colorScheme.onSecondary
                                        ),
                                        checked = estaSeleccionado,
                                        onCheckedChange = {
                                            var numLista = 0
                                            var contador = 0
                                            claseTraducidaMod!!.proficiency_choices.forEach { eleccionCompetencia ->
                                                eleccionCompetencia.from.options?.forEach { nuevaOpcion ->
                                                    nuevaOpcion.choice?.from?.options?.forEach { nuevaOpcionDeOpcion ->
                                                        if (opcionDeOpcion.item?.index == nuevaOpcionDeOpcion.item?.index) numLista = contador
                                                    }
                                                }
                                                contador++
                                            }

                                            if (estaSeleccionado) {
                                                personajeViewModel.modificarCompetenciasSeleccionadas(
                                                    "borrar",
                                                    numLista,
                                                    opcionDeOpcion.item?.index
                                                )
                                            } else {
                                                if (competenciasSeleccionadas[numLista].size < eleccionesPorLista[numLista]) {
                                                    personajeViewModel.modificarCompetenciasSeleccionadas(
                                                        "añadir",
                                                        numLista,
                                                        opcionDeOpcion.item?.index
                                                    )
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "No se pueden seleccionar más de ${eleccionCompetencia.choose} competencias en esta sección.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }

        // equipamiento inicial
        if (claseTraducidaMod != null && !claseTraducidaMod!!.starting_equipment.isEmpty()) {
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
                claseTraducidaMod!!.starting_equipment.forEach { equipo ->
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
        if (claseTraducidaMod != null && claseTraducidaMod!!.spellcasting != null) {
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
                text = "Aprende conjuros desde nivel: ${claseTraducidaMod!!.spellcasting!!.level}"
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
                    when (claseTraducidaMod!!.spellcasting!!.spellcasting_ability!!.index) {
                        "cha" -> "carisma"
                        "int" -> "inteligencia"
                        "wis" -> "sabiduría"
                        else -> ""
                    }
                }."
            )
            claseTraducidaMod!!.spellcasting!!.info.forEach { topico ->
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
            if (!infoNivelesMod.isEmpty() && nivel > 0) {
                var nivelMaxConjuro: Int
                if (infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_9 != 0 && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_9 != null) {
                    nivelMaxConjuro = 9
                } else if (infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_8 != 0 && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_8 != null) {
                    nivelMaxConjuro = 8
                } else if (infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_7 != 0 && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_7 != null) {
                    nivelMaxConjuro = 7
                } else if (infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_6 != 0 && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_6 != null) {
                    nivelMaxConjuro = 6
                } else if (infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_5 != 0 && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_5 != null) {
                    nivelMaxConjuro = 5
                } else if (infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_4 != 0 && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_4 != null) {
                    nivelMaxConjuro = 4
                } else if (infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_3 != 0 && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_3 != null) {
                    nivelMaxConjuro = 3
                } else if (infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_2 != 0 && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_2 != null) {
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
                    text = "Conoce ${infoNivelesMod[nivel-1].spellcasting?.cantrips_known} trucos."
                )
                if (infoNivelesMod[nivel-1].spellcasting?.spells_known != null) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 8.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = if (claseTraducidaMod!!.index == "warlock") 0.dp else 8.dp
                            ),
                        textAlign = TextAlign.Start,
                        text = "Conoce ${infoNivelesMod[nivel-1].spellcasting?.spells_known} conjuros de hasta nivel ${nivelMaxConjuro}."
                    )
                } else {
                    if (claseTraducidaMod!!.index == "cleric" || claseTraducidaMod!!.index == "druid" || claseTraducidaMod!!.index == "ranger") {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 8.dp,
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 8.dp
                                ),
                            textAlign = TextAlign.Start,
                            text = "Conoce ${(nivel+ceil((sabiduria - 10).toDouble()/2)).toInt()} conjuros de hasta nivel ${nivelMaxConjuro}."
                        )
                    } else if (claseTraducidaMod!!.index == "wizard") {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 8.dp,
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 8.dp
                                ),
                            textAlign = TextAlign.Start,
                            text = "Conoce ${(nivel+ceil((inteligencia - 10).toDouble()/2)).toInt()} conjuros de hasta nivel ${nivelMaxConjuro}."
                        )
                    } else {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 8.dp,
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 8.dp
                                ),
                            textAlign = TextAlign.Start,
                            text = "Conoce ${(nivel+ceil((carisma - 10).toDouble()/2)).toInt()} conjuros de hasta nivel ${nivelMaxConjuro}."
                        )
                    }
                }
                if (claseTraducidaMod!!.index == "warlock") {
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
                if (nivelMaxConjuro >= 1 && !(claseTraducidaMod!!.index == "warlock" && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_1 == 0)) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp
                            ),
                        textAlign = TextAlign.Start,
                        text = "Puede lanzar ${infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_1} hechizos de nivel 1 antes de necesitar un descanso ${if (claseTraducidaMod!!.index == "warlock") "corto" else "largo"}."
                    )
                }
                if (nivelMaxConjuro >= 2 && !(claseTraducidaMod!!.index == "warlock" && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_2 == 0)) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp
                            ),
                        textAlign = TextAlign.Start,
                        text = "Puede lanzar ${infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_2} hechizos de nivel 2 antes de necesitar un descanso ${if (claseTraducidaMod!!.index == "warlock") "corto" else "largo"}."
                    )
                }
                if (nivelMaxConjuro >= 3 && !(claseTraducidaMod!!.index == "warlock" && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_3 == 0)) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp
                            ),
                        textAlign = TextAlign.Start,
                        text = "Puede lanzar ${infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_3} hechizos de nivel 3 antes de necesitar un descanso ${if (claseTraducidaMod!!.index == "warlock") "corto" else "largo"}."
                    )
                }
                if (nivelMaxConjuro >= 4 && !(claseTraducidaMod!!.index == "warlock" && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_4 == 0)) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp
                            ),
                        textAlign = TextAlign.Start,
                        text = "Puede lanzar ${infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_4} hechizos de nivel 4 antes de necesitar un descanso ${if (claseTraducidaMod!!.index == "warlock") "corto" else "largo"}."
                    )
                }
                if (nivelMaxConjuro >= 5 && !(claseTraducidaMod!!.index == "warlock" && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_5 == 0)) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp
                            ),
                        textAlign = TextAlign.Start,
                        text = "Puede lanzar ${infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_5} hechizos de nivel 5 antes de necesitar un descanso ${if (claseTraducidaMod!!.index == "warlock") "corto" else "largo"}."
                    )
                }
                if (nivelMaxConjuro >= 6 && !(claseTraducidaMod!!.index == "warlock" && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_6 == 0)) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp
                            ),
                        textAlign = TextAlign.Start,
                        text = "Puede lanzar ${infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_6} hechizos de nivel 6 antes de necesitar un descanso ${if (claseTraducidaMod!!.index == "warlock") "corto" else "largo"}."
                    )
                }
                if (nivelMaxConjuro >= 7 && !(claseTraducidaMod!!.index == "warlock" && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_7 == 0)) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp
                            ),
                        textAlign = TextAlign.Start,
                        text = "Puede lanzar ${infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_7} hechizos de nivel 7 antes de necesitar un descanso ${if (claseTraducidaMod!!.index == "warlock") "corto" else "largo"}."
                    )
                }
                if (nivelMaxConjuro >= 8 && !(claseTraducidaMod!!.index == "warlock" && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_8 == 0)) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp
                            ),
                        textAlign = TextAlign.Start,
                        text = "Puede lanzar ${infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_8} hechizos de nivel 8 antes de necesitar un descanso ${if (claseTraducidaMod!!.index == "warlock") "corto" else "largo"}."
                    )
                }
                if (nivelMaxConjuro >= 9 && !(claseTraducidaMod!!.index == "warlock" && infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_9 == 0)) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp
                            ),
                        textAlign = TextAlign.Start,
                        text = "Puede lanzar ${infoNivelesMod[nivel-1].spellcasting?.spell_slots_level_9} hechizos de nivel 9 antes de necesitar un descanso ${if (claseTraducidaMod!!.index == "warlock") "corto" else "largo"}."
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
                            if (i != 0 || infoNivelesMod[nivel-1].spellcasting?.cantrips_known != null) {
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
                                    trucosConocidos.forEach {
                                        if (conjuro.index == it) estaSeleccionado = true
                                    }
                                } else {
                                    conjurosConocidos.forEach {
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
                                            .padding(
                                                horizontal = 12.dp
                                            ),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = conjuro.name,
                                            style = InformacionPrincipalStyle,
                                            overflow = TextOverflow.Visible
                                        )
                                        Checkbox(
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = MaterialTheme.colorScheme.secondary,
                                                uncheckedColor = MaterialTheme.colorScheme.tertiary,
                                                checkmarkColor = MaterialTheme.colorScheme.onSecondary
                                            ),
                                            checked = estaSeleccionado,
                                            onCheckedChange = {
                                                // si esta pulsando un elemento de la lista de trucos
                                                if (i == 0) {
                                                    if (estaSeleccionado) {
                                                        personajeViewModel.modificarTrucosConocidos("borrar", conjuro.index)
                                                    } else {
                                                        if (trucosConocidos.size < infoNivelesMod[nivel-1].spellcasting?.cantrips_known!!) {
                                                            personajeViewModel.modificarTrucosConocidos("añadir", conjuro.index)
                                                        } else {
                                                            Toast.makeText(context, "Este personaje no puede aprender más de ${infoNivelesMod[nivel-1].spellcasting?.cantrips_known!!} trucos", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                } else {
                                                    // si esta pulsando un elemento de cualquiera de las listas de conjuros
                                                    if (estaSeleccionado) {
                                                        personajeViewModel.modificarConjurosConocidos("borrar", conjuro.index)
                                                    } else {
                                                        // antes de añadir comprueba que no se haya alcanzado el limite de conjuros que puede aprender
                                                        var cantidadMaxConjuros: Int
                                                        if (claseTraducidaMod!!.index == "cleric" || claseTraducidaMod!!.index == "druid" || claseTraducidaMod!!.index == "ranger") {
                                                            // si es clerigo o druida calcula el limite en base a su nivel + mod de sabiduria
                                                            cantidadMaxConjuros = (nivel+ceil((sabiduria - 10).toDouble()/2)).toInt()
                                                        } else if (claseTraducidaMod!!.index == "wizard") {
                                                            // si es mago calcula el limite en base a su nivel + mod de inteligencia
                                                            cantidadMaxConjuros = (nivel+ceil((inteligencia - 10).toDouble()/2)).toInt()
                                                        } else if (claseTraducidaMod!!.index == "paladin") {
                                                            // si es paladin calcula el limite en base a su nivel + mod de carisma
                                                            cantidadMaxConjuros = (floor((nivel/2).toDouble())+ceil((carisma - 10).toDouble()/2)).toInt()
                                                        } else {
                                                            cantidadMaxConjuros = infoNivelesMod[nivel-1].spellcasting?.spells_known!!
                                                        }

                                                        if (conjurosConocidos.size < cantidadMaxConjuros) {
                                                            personajeViewModel.modificarConjurosConocidos("añadir", conjuro.index)
                                                        } else {
                                                            Toast.makeText(context, "Este personaje no puede aprender más de $cantidadMaxConjuros conjuros", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                }
                                            }
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

    if (mostrarDialogoImagen) {
        DialogoImagen(
            imagenViewModel = imagenViewModel,
            onDismiss = {
                imagenViewModel.mostrarDialogo(false)
            }
        )
    }

    if (mostrarDialogo) {
        DialogoPersonaje(
            personajeViewModel = personajeViewModel,
            raza = razaMod,
            razaTraducida = razaTraducidaMod,
            subraza = subrazaMod,
            subrazaTraducida = subrazaTraducidaMod,
            subclase = subclaseMod,
            subclaseTraducida = subclaseTraducidaMod,
            resultado = modificarPersonajeResultado,
            seleccion = seleccion,
            onDismiss = {
                personajeViewModel.modificarModificarPersonajeResultado("")
                personajeViewModel.cargarDescripcion("")
                personajeViewModel.mostrarDialogo(false)
            }
        )
    }
}