package com.example.ttmrepuestos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ttmrepuestos.model.Producto
import com.example.ttmrepuestos.model.Usuario

@Database(entities = [Producto::class, Usuario::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun usuarioDao(): UsuarioDao
}
