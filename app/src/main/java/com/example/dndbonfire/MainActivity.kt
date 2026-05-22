package com.example.dndbonfire

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dndbonfire.data.ProveedorBestia
import com.example.dndbonfire.navegacion.Ruta
import com.example.dndbonfire.navegacion.navHost
import com.example.dndbonfire.ui.componentes.MenuLateral
import com.example.dndbonfire.ui.componentes.BottomBar
import com.example.dndbonfire.ui.componentes.TopAppBar
import com.example.dndbonfire.ui.theme.DnDBonfireTheme
import com.example.dndbonfire.viewmodel.BestiaViewModel
import com.example.dndbonfire.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.core.content.ContextCompat
import com.example.dndbonfire.data.DatosUsuario
import com.example.dndbonfire.data.ProveedorPartida
import com.example.dndbonfire.data.ProveedorPersonaje
import com.example.dndbonfire.data.ProveedorPersonaje.Companion.razas
import com.example.dndbonfire.data.ProveedorSesion
import com.example.dndbonfire.data.ProveedorTokenDispositivo
import com.example.dndbonfire.data.ProveedorUsuario
import com.example.dndbonfire.modelo.Usuario
import com.example.dndbonfire.ui.theme.Negro
import com.example.dndbonfire.viewmodel.ImagenViewModel
import com.example.dndbonfire.viewmodel.PartidaUsuarioViewModel
import com.example.dndbonfire.viewmodel.PartidaViewModel
import com.example.dndbonfire.viewmodel.PersonajeViewModel
import com.example.dndbonfire.viewmodel.SesionViewModel
import com.example.dndbonfire.viewmodel.TemaApp
import com.example.dndbonfire.viewmodel.UsuarioViewModel
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    private lateinit var pref: SharedPreferences
    private var usuarioLogeado: Usuario? = null
    private var tema: String = "sistema"

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->

            if (isGranted) {
                Log.d("FCM", "Permiso de notificaciones concedido")
            } else {
                Log.d("FCM", "Permiso de notificaciones denegado")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pedirPermisoNotificaciones()

        lifecycleScope.launch {
            ProveedorPersonaje.cargarRazas()
            ProveedorPersonaje.cargarIdiomas()
            ProveedorPersonaje.cargarClases()
            ProveedorBestia.cargarBestias()
        }
        enableEdgeToEdge()

        cargarDatosUsuario()

        val mainViewModel: MainViewModel by viewModels()

        cargarTema(mainViewModel)

        ProveedorUsuario.inicializar(pref, this)
        ProveedorPartida.inicializar(pref, this)
        ProveedorSesion.inicializar(pref, this)
        ProveedorPersonaje.inicializar(pref, this)
        ProveedorBestia.inicializar(this)

        // después de haber inicializado DatosUsuario, ProveedorUsuario y MainViewModel EN ESE ORDEN se puede
        // obtener el usuario logeado
        mainViewModel.cargarUsuarioLogeado()

        val usuario = ProveedorUsuario.usuarioLogeado
        if (usuario != null) {
            lifecycleScope.launch {
                ProveedorPartida.cargarPartidas(usuario)
                ProveedorPersonaje.cargarPersonajes(usuario)
                ProveedorSesion.cargarSesionesCalendario(usuario)
            }
        }

        setContent {
            DnDBonfireTheme(
                temaApp = mainViewModel.temaApp.value,
                // si no crea un esquema propio para primary y secondary
                dynamicColor = false
            ) {
                PantallaPrincipal(
                    mainViewModel = mainViewModel,
                    pref = pref
                )
            }
        }
    }

    private fun cargarDatosUsuario() {
        pref = getSharedPreferences("datos", Context.MODE_PRIVATE)
        usuarioLogeado = DatosUsuario.cargarUsuarioLogeado(pref)
        tema = DatosUsuario.cargarTema(pref)
    }

    private fun cargarTema(mainViewModel: MainViewModel) {
        when (tema) {
            "claro" -> mainViewModel.cambiarTema(TemaApp.CLARO)
            "oscuro" -> mainViewModel.cambiarTema(TemaApp.OSCURO)
            else -> mainViewModel.cambiarTema(TemaApp.SISTEMA)
        }
    }

    private fun pedirPermisoNotificaciones() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            when {

                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {

                    Log.d("FCM", "El permiso ya estaba concedido")
                }

                else -> {

                    requestPermissionLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaPrincipal(
    mainViewModel: MainViewModel,
    pref: SharedPreferences
) {
    val navController = rememberNavController()
    var elementoSeleccionado by remember { mutableStateOf<Ruta>(Ruta.Partidas) }

    // la funcionalidad de fab cambia dependiendo de la pestaña
    val navState by navController.currentBackStackEntryAsState()
    val rutaActual = navState?.destination?.route

    val imagenViewModel: ImagenViewModel = viewModel()
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val partidaViewModel: PartidaViewModel = viewModel()
    val partidaUsuarioViewModel: PartidaUsuarioViewModel = viewModel()
    val personajeViewModel: PersonajeViewModel = viewModel()
    val sesionViewModel: SesionViewModel = viewModel()
    val bestiaViewModel: BestiaViewModel = viewModel()

    val esSubpantalla = when {
        rutaActual?.contains("Partidas") == true ||
        rutaActual?.contains("Personajes") == true ||
        rutaActual?.contains("Bestiario") == true ||
        rutaActual?.contains("Calendario") == true ||
        rutaActual?.contains("Chats") == true -> false
        else -> true
    }

    val anchuraMenuLateral = 280.dp
    val animacionMenuLateral by animateDpAsState(
        targetValue = if (mainViewModel.menuLateralDesplegado.collectAsState().value) 0.dp else anchuraMenuLateral
    )

    val usuarioLogedo by mainViewModel.usuarioLogeado

    Box() {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopAppBar(
                    navController = navController,
                    usuarioViewModel = usuarioViewModel,
                    imagenViewModel = imagenViewModel,
                    bestiaViewModel = bestiaViewModel,
                    mainViewModel = mainViewModel,
                    partidaViewModel = partidaViewModel,
                    partidaUsuarioViewModel = partidaUsuarioViewModel,
                    sesionViewModel = sesionViewModel,
                    personajeViewModel = personajeViewModel,
                    rutaActual = rutaActual,
                    esSubpantalla = esSubpantalla
                )
            },
            bottomBar = {
                if (
                    !esSubpantalla
                ) {
                    BottomBar(
                        elementoSeleccionado = rutaActual,
                        personajeViewModel = personajeViewModel
                    ) {
                        elementoSeleccionado = it
                        navController.navigate(elementoSeleccionado) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            },
            floatingActionButton = {
                if (usuarioLogedo != null) {
                    if (
                        rutaActual?.endsWith(".Partidas") == true ||
                        rutaActual?.endsWith(".Personajes") == true
                    ) {
                        FloatingActionButton(
                            modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = CutCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = CutCornerShape(16.dp)
                                ),
                            shape = CutCornerShape(16.dp),
                            onClick = {
                                if (rutaActual.endsWith(".Personajes")) {
                                    // vacía posibles datos restantes
                                    personajeViewModel.modificarNombre("")
                                    personajeViewModel.modificarHp(0)
                                    personajeViewModel.modificarFuerza(0)
                                    personajeViewModel.modificarDestreza(0)
                                    personajeViewModel.modificarConstitucion(0)
                                    personajeViewModel.modificarInteligencia(0)
                                    personajeViewModel.modificarSabiduria(0)
                                    personajeViewModel.modificarCarisma(0)
                                    personajeViewModel.cargarRaza("")
                                    personajeViewModel.modificarEdad(0)
                                    personajeViewModel.modificarAlineamiento("Neutral verdadero")
                                    personajeViewModel.cargarClase("")
                                    personajeViewModel.modificarNivel(0)
                                    personajeViewModel.modificarCompetenciasSeleccionadas("", null, null)
                                    personajeViewModel.modificarTrucosConocidos("", null)
                                    personajeViewModel.modificarConjurosConocidos("", null)
                                    imagenViewModel.modificarImagenUri(null)
                                    imagenViewModel.modificarBitmap(null)

                                    navController.navigate(Ruta.CrearPersonaje) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                } else if (rutaActual.endsWith(".Partidas")) {
                                    // vacía posibles datos restantes
                                    partidaViewModel.modificarTitulo("")
                                    partidaViewModel.modificarDescripcion("")
                                    partidaViewModel.modificarNumMinJugadores(0)
                                    partidaViewModel.modificarNumMaxJugadores(0)
                                    partidaViewModel.modificarRangoMinJugadores(0)
                                    partidaViewModel.modificarRangoMaxJugadores(0)
                                    partidaViewModel.modificarFechaInicio(null)
                                    partidaViewModel.modificarFechaFinalizacion(null)
                                    partidaViewModel.modificarFinalizada(false)
                                    partidaViewModel.modificarInfoVisible(false)

                                    navController.navigate(Ruta.CrearPartida) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(36.dp),
                                imageVector = Icons.Default.Add,
                                contentDescription = {
                                    if (rutaActual.endsWith(".Partidas")) "Crear partida"
                                    if (rutaActual.endsWith(".Personajes")) "Crear personaje"
                                    else ""
                                }.toString()
                            )
                        }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            navHost(
                modificador = Modifier.padding(it),
                navController = navController,
                mainViewModel = mainViewModel,
                imagenViewModel = imagenViewModel,
                usuarioViewModel = usuarioViewModel,
                partidaViewModel = partidaViewModel,
                partidaUsuarioViewModel = partidaUsuarioViewModel,
                personajeViewModel = personajeViewModel,
                sesionViewModel = sesionViewModel,
                bestiaViewModel = bestiaViewModel
            )
        }

        // oscurecimiento (scrim)
        if (mainViewModel.menuLateralDesplegado.collectAsState().value) {
            Box(
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(Negro.copy(alpha = 0.4f))
                    .clickable { mainViewModel.desplegarMenu(false) }
            )
        }

        // contenedor para ocultar y mostrar el menu lateral
        Box(
            Modifier
                .fillMaxHeight()
                .statusBarsPadding()
                .width(anchuraMenuLateral)
                .align(Alignment.CenterEnd)
                .offset(x = animacionMenuLateral)
                .background(MaterialTheme.colorScheme.background)
        ) {
            MenuLateral(
                navController = navController,
                mainViewModel = mainViewModel,
                partidaViewModel = partidaViewModel,
                partidaUsuarioViewModel = partidaUsuarioViewModel,
                sesionViewModel = sesionViewModel,
                usuarioViewModel = usuarioViewModel,
                personajeViewModel = personajeViewModel,
                rutaActual = rutaActual,
                pref = pref
            )
        }
    }
}

val urlDnDApi = "https://www.dnd5eapi.co"
val urlBonfireApi = "http://at-sv-erat.duckdns.org:1090"
val json = Json {
    ignoreUnknownKeys = true  // ← Ignora campos extra
    isLenient = true
    encodeDefaults = true
    allowStructuredMapKeys = true
}