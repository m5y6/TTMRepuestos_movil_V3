package com.example.ttmrepuestos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ttmrepuestos.model.Post
import com.example.ttmrepuestos.model.Producto
import com.example.ttmrepuestos.model.Usuario

@Database(entities = [Producto::class, Usuario::class, Post::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun postDao(): PostDao
}
