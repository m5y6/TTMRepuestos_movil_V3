package com.example.ttmrepuestos.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttmrepuestos.model.Producto
import com.example.ttmrepuestos.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductoViewModel(
    application: Application,
    private val repository: ProductoRepository
) : AndroidViewModel(application) {

    init {
        viewModelScope.launch {
            repository.refreshProducts()
        }
    }

    val products = repository.products.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    private val _fotosDeProductos = MutableStateFlow<Map<Int, Bitmap>>(emptyMap())
    val fotosDeProductos = _fotosDeProductos.asStateFlow()

    fun asignarFotoAProducto(idProducto: Int, bitmap: Bitmap) {
        val nuevasFotos = _fotosDeProductos.value.toMutableMap()
        nuevasFotos[idProducto] = bitmap
        _fotosDeProductos.value = nuevasFotos
    }

    fun addProduct(productoSinFoto: Producto, foto: Bitmap?) {
        viewModelScope.launch {
            if (foto != null) {
                val imageUrl = repository.uploadImage(getApplication(), foto)
                if (imageUrl != null) {
                    val productoConFoto = productoSinFoto.copy(fotoUri = imageUrl)
                    repository.addProduct(productoConFoto)
                } else {
                    repository.addProduct(productoSinFoto)
                }
            } else {
                repository.addProduct(productoSinFoto)
            }
        }
    }

    fun updateProduct(producto: Producto) {
        viewModelScope.launch {
            repository.updateProduct(producto)
        }
    }

    fun deleteProduct(producto: Producto) {
        viewModelScope.launch {
            repository.deleteProduct(producto)
        }
    }
}
