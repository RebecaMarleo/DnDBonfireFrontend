package com.example.dndbonfire.ui.componentes

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.dndbonfire.ui.theme.ButtonStyle
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.util.ConversorUnix
import com.example.dndbonfire.viewmodel.SesionViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialTimePickerModal(
    initialMillis: Long,
    sesionViewModel: SesionViewModel,
    onConfirm: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance().apply {
        timeInMillis = initialMillis
    }

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = CutCornerShape(8.dp)
                )
                .border(
                    width = 3.dp,
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
            IconButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 16.dp, end = 16.dp)
                    .size(24.dp),
                onClick = onDismiss
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(0.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(
                    colors = TimePickerColors(
                        clockDialColor = MaterialTheme.colorScheme.background,
                        selectorColor = MaterialTheme.colorScheme.secondary,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        periodSelectorBorderColor = MaterialTheme.colorScheme.secondary,
                        clockDialSelectedContentColor = MaterialTheme.colorScheme.onSecondary,
                        clockDialUnselectedContentColor = MaterialTheme.colorScheme.onBackground,
                        periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.secondary,
                        periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onSecondary,
                        periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.secondary,
                        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onSecondary,
                        timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    state = timePickerState,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Cancelar",
                            style = ButtonStyle,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
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
                            sesionViewModel.modificarIdSesion(null)
                            val nuevaFecha = ConversorUnix.actualizarFechaYMDHMS(initialMillis, timePickerState.hour, timePickerState.minute)
                            onConfirm(ConversorUnix.fechaYMDHMSAMilis(nuevaFecha))
                        }
                    ) {
                        Text(
                            text = "Aceptar",
                            style = ButtonStyle
                        )
                    }
                }
            }
        }
    }
}