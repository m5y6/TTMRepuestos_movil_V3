package com.example.ttmrepuestos.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttmrepuestos.model.Producto
import com.example.ttmrepuestos.remote.RetrofitInstance
import com.example.ttmrepuestos.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
        // Obtenemos la tasa de conversion al iniciar el ViewModel
        fetchConversionRate()
    }

    val products = repository.products.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    private val _fotosDeProductos = MutableStateFlow<Map<Int, Bitmap>>(emptyMap())
    val fotosDeProductos = _fotosDeProductos.asStateFlow()

    // StateFlow para guardar la tasa de conversion de CLP a USD
    private val _clpToUsdRate = MutableStateFlow<Double?>(null)
    val clpToUsdRate: StateFlow<Double?> = _clpToUsdRate.asStateFlow()

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

    // Funcion para obtener la tasa de conversion
    private fun fetchConversionRate() {
        viewModelScope.launch {
            try {
                val apiKey = "48addf92a0bc9db6c40ae7ee" // Tu API Key
                val response = RetrofitInstance.currencyApi.getLatestRates(apiKey, "CLP")

                if (response.isSuccessful) {
                    _clpToUsdRate.value = response.body()?.conversionRates?.get("USD")
                }
            } catch (e: Exception) {
                // Manejar error de red, por ejemplo, loguearlo
                _clpToUsdRate.value = null
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
