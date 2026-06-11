package com.example.appdates.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Share
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.appdates.ui.theme.PinkPrimary
import com.example.appdates.data.listaContactos
import androidx.compose.runtime.*
import androidx.compose.foundation.clickable


@Composable
fun DetalleContactoScreen(

    nombre: String,
    telefono: String,
    direccion: String,
    descripcion: String,
    universo: String,

    onBack: () -> Unit

)

{
    val context = LocalContext.current

    val contacto = listaContactos.find {
        it.nombre == nombre
    }

    val fotos = contacto?.fotos ?: emptyList()

    var indiceImagen by remember {
        mutableStateOf(0)
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {

                val intent = Intent(
                    Intent.ACTION_CALL,
                    Uri.parse("tel:$telefono")
                )

                context.startActivity(intent)

            } else {

                Toast.makeText(
                    context,
                    "Se necesita permiso para llamar",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        IconButton(
            onClick = onBack
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Regresar"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(fotos[indiceImagen]),
                contentDescription = nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row {

                Text(
                    text = "◀",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.clickable {

                        if (indiceImagen > 0) {
                            indiceImagen--
                        }

                    }
                )

                Spacer(modifier = Modifier.width(32.dp))

                Text(
                    text = "▶",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.clickable {

                        if (indiceImagen < fotos.lastIndex) {
                            indiceImagen++
                        }

                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row {

                fotos.forEachIndexed { index, _ ->

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(

                                if (index == indiceImagen)
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.LightGray

                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = nombre,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = universo,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = descripcion
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "📞 $telefono"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "📍 $direccion",
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {

                    val uri = Uri.parse(
                        "geo:0,0?q=${Uri.encode(direccion)}"
                    )

                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        uri
                    )

                    intent.setPackage(
                        "com.google.android.apps.maps"
                    )

                    if (
                        intent.resolveActivity(
                            context.packageManager
                        ) != null
                    ) {

                        context.startActivity(intent)

                    } else {

                        Toast.makeText(
                            context,
                            "Google Maps no encontrado",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            ) {

                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Abrir mapa"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                FloatingActionButton(
                    onClick = {

                        val intent = Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse("tel:$telefono")
                        )

                        context.startActivity(intent)
                    },
                    containerColor = PinkPrimary,
                    contentColor = Color.White
                ) {

                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Abrir marcador"
                    )
                }

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Marcador",
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                FloatingActionButton(
                    onClick = {

                        if (
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CALL_PHONE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {

                            val intent = Intent(
                                Intent.ACTION_CALL,
                                Uri.parse("tel:$telefono")
                            )

                            context.startActivity(intent)

                        } else {

                            permissionLauncher.launch(
                                Manifest.permission.CALL_PHONE
                            )

                        }
                    },
                    containerColor = PinkPrimary,
                    contentColor = Color.White
                ) {

                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Llamar"
                    )
                }

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Llamar",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                FloatingActionButton(
                    onClick = {

                        val mensaje = """
✨ Character Connect

Personaje: $nombre

Universo: $universo

$descripcion

📞 $telefono

📍 $direccion
""".trimIndent()

                        val intent = Intent(Intent.ACTION_SEND)

                        intent.type = "text/plain"

                        intent.putExtra(
                            Intent.EXTRA_TEXT,
                            mensaje
                        )

                        val chooser =
                            Intent.createChooser(
                                intent,
                                "Compartir personaje vía"
                            )

                        context.startActivity(chooser)
                    },
                    containerColor = PinkPrimary,
                    contentColor = Color.White
                ) {

                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartir"
                    )
                }

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Compartir",
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                FloatingActionButton(
                    onClick = {

                        val uri = Uri.parse(
                            "geo:0,0?q=${Uri.encode(direccion)}"
                        )

                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            uri
                        )

                        intent.setPackage(
                            "com.google.android.apps.maps"
                        )

                        if (
                            intent.resolveActivity(
                                context.packageManager
                            ) != null
                        ) {

                            context.startActivity(intent)

                        }
                    },
                    containerColor = PinkPrimary,
                    contentColor = Color.White
                ) {

                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Mapa"
                    )
                }

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Mapa",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}