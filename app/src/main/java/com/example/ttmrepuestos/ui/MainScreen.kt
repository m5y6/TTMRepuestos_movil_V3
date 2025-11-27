package com.example.ttmrepuestos.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ttmrepuestos.ui.inicio.InicioScreen
import com.example.ttmrepuestos.ui.registrarproducto.RegistrarProductoScreen
import com.example.ttmrepuestos.ui.resumen.ResumenScreen
import com.example.ttmrepuestos.ui.verproductos.VerProductosScreen
import com.example.ttmrepuestos.viewmodel.ProductoViewModel
import com.example.ttmrepuestos.viewmodel.ProductoViewModelFactory

sealed class BottomBarScreen(val route: String, val title: String, val icon: ImageVector) {
    object Inicio : BottomBarScreen("inicio", "Inicio", Icons.Default.Home)
    object Informe : BottomBarScreen("informe", "Informe", Icons.Default.List)
    object Productos : BottomBarScreen("productos", "Productos", Icons.Default.Store)
    object RegistrarProducto : BottomBarScreen("registrar_producto", "Registrar", Icons.Default.Add)
}

val bottomBarItems = listOf(
    BottomBarScreen.Inicio,
    BottomBarScreen.Informe,
    BottomBarScreen.Productos,
    BottomBarScreen.RegistrarProducto
)

@Composable
fun MainScreen(factory: ProductoViewModelFactory) {
    val navController = rememberNavController()
    val viewModel: ProductoViewModel = viewModel(factory = factory)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomBarItems.forEach { screen ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Inicio.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomBarScreen.Inicio.route) {
                InicioScreen()
            }
            composable(BottomBarScreen.Informe.route) {
                ResumenScreen(viewModel = viewModel)
            }
            composable(BottomBarScreen.Productos.route) {
                VerProductosScreen(viewModel = viewModel)
            }
            composable(BottomBarScreen.RegistrarProducto.route) {
                RegistrarProductoScreen(viewModel = viewModel)
            }
        }
    }
}
