package com.example.dndbonfire.ui.componentes

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dndbonfire.ui.theme.ButtonStyle
import com.example.dndbonfire.ui.theme.Gris
import com.example.dndbonfire.viewmodel.PartidaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    initialMillis: Long?,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    DatePickerDialog(
        modifier = Modifier
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
        colors = DatePickerDefaults
            .colors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
        shape = CutCornerShape(16.dp),
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                modifier = Modifier
                    .padding(bottom = 8.dp, end = 8.dp)
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
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }
            ) {
                Text(
                    text = "Aceptar",
                    style = ButtonStyle
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    style = ButtonStyle,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) {
        Box() {
            DatePicker(
                state = datePickerState,
                title = {
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp),
                        text = "Selecciona una fecha",
                        fontSize = 16.sp
                    )
                },
                colors = DatePickerDefaults.colors(
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    dividerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedDayContainerColor = MaterialTheme.colorScheme.secondary,
                    selectedDayContentColor = MaterialTheme.colorScheme.onSecondary,
                    headlineContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    dayContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    dateTextFieldColors = OutlinedTextFieldDefaults.colors(
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

                    )
                )
            )
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
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
        }
    }
}