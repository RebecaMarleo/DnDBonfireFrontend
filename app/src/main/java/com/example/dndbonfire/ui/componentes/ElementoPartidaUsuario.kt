package com.example.dndbonfire.ui.componentes

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.dndbonfire.R
import com.example.dndbonfire.modelo.Usuario
import com.example.dndbonfire.ui.theme.InformacionPrincipalStyle
import com.example.dndbonfire.ui.theme.InformacionSecundariaStyle
import com.example.dndbonfire.ui.theme.Titulo5Style

@Composable
fun ElementoPartidaUsuario(
    usuario: Usuario
) {
    Card(
        modifier = Modifier
            .padding(
                horizontal = 16.dp
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = CutCornerShape(4.dp)
            )
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = usuario.imagen?.let { "data:image/jpg;base64,$it" } ?: R.drawable.avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CutCornerShape(8.dp))
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = CutCornerShape(8.dp)
                    ),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
            Column(
                modifier = Modifier
                    .padding(
                        start = 8.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
            ) {
                Text(
                    style = Titulo5Style,
                    text = usuario.nombre
                )
                Row(
                    modifier = Modifier
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "@${usuario.username}",
                        modifier = Modifier
                            .widthIn(max = 100.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        text = "-"
                    )
                    Text(
                        modifier = Modifier
                            .widthIn(max = 200.dp),
                        text = when (usuario.rango) {
                            1 -> "Aventurero novato"
                            2 -> "Aventurero intermedio"
                            3 -> "Aventurero experimentado"
                            4 -> "Aventurero legendario"
                            else -> ""
                        },
                        style = InformacionPrincipalStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}