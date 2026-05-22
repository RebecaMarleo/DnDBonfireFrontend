package com.example.dndbonfire.navegacion

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.example.dndbonfire.ui.pantallas.PantallaIniciarSesion
import com.example.dndbonfire.ui.pantallas.PantallaBestia
import com.example.dndbonfire.ui.pantallas.PantallaBestiario
import com.example.dndbonfire.ui.pantallas.PantallaCalendario
import com.example.dndbonfire.ui.pantallas.PantallaCrearCuenta
import com.example.dndbonfire.ui.pantallas.PantallaCrearPartida
import com.example.dndbonfire.ui.pantallas.PantallaCrearPersonaje
import com.example.dndbonfire.ui.pantallas.PantallaCuenta
import com.example.dndbonfire.ui.pantallas.PantallaPartida
import com.example.dndbonfire.ui.pantallas.PantallaPartidas
import com.example.dndbonfire.ui.pantallas.PantallaPersonaje
import com.example.dndbonfire.ui.pantallas.PantallaPersonajes
import com.example.dndbonfire.viewmodel.BestiaViewModel
import com.example.dndbonfire.viewmodel.ImagenViewModel
import com.example.dndbonfire.viewmodel.UsuarioViewModel
import com.example.dndbonfire.viewmodel.MainViewModel
import com.example.dndbonfire.viewmodel.PartidaUsuarioViewModel
import com.example.dndbonfire.viewmodel.PartidaViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import com.example.dndbonfire.viewmodel.SesionViewModel

@Composable
fun navHost(
    modificador: Modifier = Modifier,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    imagenViewModel: ImagenViewModel,
    usuarioViewModel: UsuarioViewModel,
    personajeViewModel: PersonajeViewModel,
    partidaUsuarioViewModel: PartidaUsuarioViewModel,
    partidaViewModel: PartidaViewModel,
    sesionViewModel: SesionViewModel,
    bestiaViewModel: BestiaViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Ruta.Partidas,
        modifier = modificador
    ) {
        composable<Ruta.Partidas> {
            PantallaPartidas(
                navController = navController,
                mainViewModel = mainViewModel,
                partidaViewModel = partidaViewModel,
                partidaUsuarioViewModel = partidaUsuarioViewModel,
                usuarioViewModel = usuarioViewModel,
                personajeViewModel = personajeViewModel,
                sesionViewModel = sesionViewModel
            )
        }

        composable<Ruta.Partida> { backStackEntry ->
            val rutaPartida = backStackEntry.toRoute<Ruta.Partida>()
            PantallaPartida(
                navController = navController,
                idPartida = rutaPartida.idPartida,
                mainViewModel = mainViewModel,
                partidaViewModel = partidaViewModel,
                partidaUsuarioViewModel = partidaUsuarioViewModel,
                usuarioViewModel = usuarioViewModel,
                personajeViewModel = personajeViewModel,
                sesionViewModel = sesionViewModel
            )
        }

        composable<Ruta.CrearPartida> {
            PantallaCrearPartida(
                navController = navController,
                mainViewModel = mainViewModel,
                partidaViewModel = partidaViewModel,
                partidaUsuarioViewModel = partidaUsuarioViewModel
            )
        }

        composable<Ruta.Personajes> {
            PantallaPersonajes(
                navController = navController,
                mainViewModel = mainViewModel,
                personajeViewModel = personajeViewModel
            )
        }

        composable<Ruta.Personaje> { backStackEntry ->
            val rutaPersonaje = backStackEntry.toRoute<Ruta.Personaje>()
            PantallaPersonaje(
                navController = navController,
                idPersonaje = rutaPersonaje.idPersonaje,
                personajeViewModel = personajeViewModel,
                imagenViewModel = imagenViewModel
            )
        }

        composable<Ruta.CrearPersonaje> {
            PantallaCrearPersonaje(
                navController = navController,
                mainViewModel = mainViewModel,
                imagenViewModel = imagenViewModel,
                personajeViewModel = personajeViewModel
            )
        }

        composable<Ruta.Bestiario> {
            PantallaBestiario(
                navController = navController,
                bestiaViewModel = bestiaViewModel
            )
        }

        composable<Ruta.Bestia> { backStackEntry ->
            val rutaBestia = backStackEntry.toRoute<Ruta.Bestia>()
            PantallaBestia(
                navController = navController,
                idBestia = rutaBestia.idBestia,
                bestiaViewModel = bestiaViewModel
            )
        }

        composable<Ruta.Calendario> {
            PantallaCalendario(
                navController = navController,
                mainViewModel = mainViewModel,
                sesionViewModel = sesionViewModel,
                partidaUsuarioViewModel = partidaUsuarioViewModel,
                partidaViewModel = partidaViewModel,
                usuarioViewModel = usuarioViewModel,
                personajeViewModel = personajeViewModel
            )
        }

        composable<Ruta.IniciarSesion> {
            PantallaIniciarSesion(
                navController = navController,
                mainViewModel = mainViewModel,
                usuarioViewModel = usuarioViewModel,
                partidaViewModel = partidaViewModel,
                personajeViewModel = personajeViewModel,
                sesionViewModel = sesionViewModel
            )
        }

        composable<Ruta.CrearCuenta> {
            PantallaCrearCuenta(
                navController = navController,
                mainViewModel = mainViewModel,
                usuarioViewModel = usuarioViewModel,
                partidaViewModel = partidaViewModel,
                personajeViewModel = personajeViewModel,
                sesionViewModel = sesionViewModel
            )
        }

        composable<Ruta.Cuenta> {
            PantallaCuenta(
                mainViewModel = mainViewModel,
                imagenViewModel = imagenViewModel,
                usuarioViewModel = usuarioViewModel,
                navController = navController
            )
        }

        composable(
            route = "partida/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
            deepLinks = listOf(navDeepLink { uriPattern = "https://dnd-bonfire.vercel.app/partida/{id}" })
        ) { backStackEntry ->
            val idStr = backStackEntry.arguments?.getString("id")
            val id = idStr?.toIntOrNull()
            if (id != null) {
                PantallaPartida(
                    navController = navController,
                    idPartida = id,
                    mainViewModel = mainViewModel,
                    usuarioViewModel = usuarioViewModel,
                    personajeViewModel = personajeViewModel,
                    sesionViewModel = sesionViewModel,
                    partidaViewModel = partidaViewModel,
                    partidaUsuarioViewModel = partidaUsuarioViewModel
                )
            } else {
                PantallaPartidas(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    partidaViewModel = partidaViewModel,
                    partidaUsuarioViewModel = partidaUsuarioViewModel,
                    usuarioViewModel = usuarioViewModel,
                    personajeViewModel = personajeViewModel,
                    sesionViewModel = sesionViewModel
                )
            }
        }
    }
}