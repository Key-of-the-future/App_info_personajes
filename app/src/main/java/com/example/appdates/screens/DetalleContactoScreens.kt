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
import android.media.MediaPlayer
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.ui.graphics.Brush
import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.text.style.TextAlign



@Composable
fun DetalleContactoScreen(

    nombre: String,
    telefono: String,
    direccion: String,
    descripcion: String,
    universo: String,

    onBack: () -> Unit

) {
    val context = LocalContext.current

    val contacto = listaContactos.find {
        it.nombre == nombre
    }

    val imageOffsetY = contacto?.imageOffsetY ?: 0

    val vozRes = contacto?.vozRes

    val musicaRes = contacto?.musicaRes

    var musicaActiva by remember {
        mutableStateOf(true)
    }

    val fotos = contacto?.fotos ?: emptyList()

    var indiceImagen by remember {
        mutableStateOf(0)
    }

    var musicPlayer by remember {
        mutableStateOf<MediaPlayer?>(null)
    }

    DisposableEffect(nombre) {

        val voicePlayer =
            vozRes?.let {
                MediaPlayer.create(context, it)
            }

        voicePlayer?.start()

        musicPlayer?.release()

        musicPlayer =
            musicaRes?.let {
                MediaPlayer.create(context, it)
            }

        musicPlayer?.isLooping = true

        if (musicaActiva) {
            musicPlayer?.start()
        }

        onDispose {

            voicePlayer?.release()
            musicPlayer?.release()
        }
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

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(

                Brush.verticalGradient(

                    colors = listOf(

                        Color(0xFFFFE4F1),

                        Color(0xFFE8D5FF)

                    )

                )

            )
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(
                onClick = onBack
            ) {

                Icon(
                    imageVector =
                        Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar"
                )
            }

            IconButton(
                onClick = {

                    musicaActiva = !musicaActiva

                    if (musicaActiva) {
                        musicPlayer?.start()
                    } else {
                        musicPlayer?.pause()
                    }
                }
            ) {

                Icon(
                    imageVector =
                        if (musicaActiva)
                            Icons.Default.VolumeUp
                        else
                            Icons.Default.VolumeOff,

                    contentDescription = "Música"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AnimatedContent(
                targetState = indiceImagen,
                label = "cambioImagen"
            ) { imagenActual ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .clip(RoundedCornerShape(24.dp))
                ) {

                    Image(
                        painter = painterResource(
                            fotos[imagenActual]
                        ),
                        contentDescription = nombre,

                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .offset(
                                y = imageOffsetY.dp
                            ),

                        contentScale = ContentScale.FillWidth
                    )
                }
            }


                Row {

                    IconButton(
                        onClick = {

                            if (indiceImagen > 0) {
                                indiceImagen--
                            }

                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Anterior"
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    IconButton(
                        onClick = {

                            if (indiceImagen < fotos.lastIndex) {
                                indiceImagen++
                            }

                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = "Siguiente"
                        )
                    }
                }



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

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFE4F1)
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Sobre mí:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = descripcion,
                        textAlign = TextAlign.Justify
                    )
                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    HorizontalDivider()

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Text(
                        text = "📞 $telefono",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "📍 $direccion",
                        style = MaterialTheme.typography.bodyLarge
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
                            imageVector = Icons.Default.Dialpad,
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
}