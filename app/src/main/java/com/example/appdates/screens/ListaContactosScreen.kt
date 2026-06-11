package com.example.appdates.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appdates.data.Contacto
import com.example.appdates.data.listaContactos

@Composable
fun ListaContactosScreen(
    onContactoClick: (Contacto) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 12.dp)
    ) {

        Text(
            text = "Character Connect",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "Conecta con tus personajes favoritos",
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
        )

        LazyColumn {

            items(listaContactos) { contacto ->

                ContactoCard(
                    contacto = contacto,
                    onClick = {
                        onContactoClick(contacto)
                    }
                )
            }
        }
    }
}

@Composable
fun ContactoCard(
    contacto: Contacto,
    onClick: () -> Unit
) {

    ElevatedCard(

        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(24.dp),

        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp
        )
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(contacto.fotos.first()),
                contentDescription = contacto.nombre,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Text(
                    text = contacto.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "🎮 ${contacto.universo}"
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "📞 ${contacto.telefono}"
                )
            }
        }
    }
}