package com.example.appdates.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.appdates.screens.DetalleContactoScreen
import com.example.appdates.screens.ListaContactosScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "lista"
    ) {

        composable("lista") {

            ListaContactosScreen { contacto ->

                navController.navigate(
                    "detalle/" +
                            Uri.encode(contacto.nombre) + "/" +
                            Uri.encode(contacto.telefono) + "/" +
                            Uri.encode(contacto.direccion) + "/" +
                            Uri.encode(contacto.descripcion) + "/" +
                            Uri.encode(contacto.universo)
                )
            }
        }

        composable(
            route = "detalle/{nombre}/{telefono}/{direccion}/{descripcion}/{universo}",

            arguments = listOf(
                navArgument("nombre") {
                    type = NavType.StringType
                },
                navArgument("telefono") {
                    type = NavType.StringType
                },
                navArgument("direccion") {
                    type = NavType.StringType
                },
                navArgument("descripcion") {
                    type = NavType.StringType
                },
                navArgument("universo") {
                    type = NavType.StringType
                }
            )
        ) {

            val nombre =
                Uri.decode(it.arguments?.getString("nombre") ?: "")

            val telefono =
                Uri.decode(it.arguments?.getString("telefono") ?: "")

            val direccion =
                Uri.decode(it.arguments?.getString("direccion") ?: "")

            val descripcion =
                Uri.decode(it.arguments?.getString("descripcion") ?: "")

            val universo =
                Uri.decode(it.arguments?.getString("universo") ?: "")

            DetalleContactoScreen(
                nombre = nombre,
                telefono = telefono,
                direccion = direccion,
                descripcion = descripcion,
                universo = universo,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}