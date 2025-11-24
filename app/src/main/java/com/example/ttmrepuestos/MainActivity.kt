package com.example.ttmrepuestos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ttmrepuestos.data.local.AppDatabase
import com.example.ttmrepuestos.data.repository.ProductoRepository
import com.example.ttmrepuestos.data.repository.UsuarioRepository
import com.example.ttmrepuestos.repository.PostRepository
import com.example.ttmrepuestos.ui.AppNavigation
import com.example.ttmrepuestos.ui.screens.PostScreen
import com.example.ttmrepuestos.ui.theme.TTMRepuestosTheme
import com.example.ttmrepuestos.viewmodel.PostViewModel
import com.example.ttmrepuestos.viewmodel.PostViewModelFactory
import com.example.ttmrepuestos.viewmodel.ProductoViewModelFactory
import com.example.ttmrepuestos.viewmodel.UsuarioViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TTMRepuestosTheme {
                val vm: PostViewModel = viewModel(
                    factory =
                        PostViewModelFactory(PostRepository())
                )
                PostScreen(viewModel = vm)
            }
        }
    }
}
