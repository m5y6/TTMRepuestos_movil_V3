package com.example.ttmrepuestos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.ttmrepuestos.data.local.AppDatabase
import com.example.ttmrepuestos.repository.ProductoRepository
import com.example.ttmrepuestos.repository.UsuarioRepository
import com.example.ttmrepuestos.remote.RetrofitInstance
import com.example.ttmrepuestos.ui.AppNavigation
import com.example.ttmrepuestos.ui.theme.TTMRepuestosTheme
import com.example.ttmrepuestos.viewmodel.ProductoViewModelFactory
import com.example.ttmrepuestos.viewmodel.UsuarioViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my_database"
        ).fallbackToDestructiveMigration().build()

        val apiService = RetrofitInstance.api

        val productoRepo = ProductoRepository(db.productoDao(), apiService)
        val productoFactory = ProductoViewModelFactory(application, productoRepo)

        val usuarioRepo = UsuarioRepository(apiService)
        val usuarioFactory = UsuarioViewModelFactory(usuarioRepo)

        setContent {
            TTMRepuestosTheme {
                AppNavigation(
                    context = applicationContext,
                    productoFactory = productoFactory,
                    usuarioFactory = usuarioFactory
                )
            }
        }
    }
}
