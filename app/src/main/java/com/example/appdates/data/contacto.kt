package com.example.appdates.data

data class Contacto(
    val nombre: String,
    val telefono: String,
    val fotos: List<Int>,
    val direccion: String,
    val descripcion: String,
    val universo: String,
    val imageOffsetY: Int,
    val vozRes: Int,
    val musicaRes: Int
)