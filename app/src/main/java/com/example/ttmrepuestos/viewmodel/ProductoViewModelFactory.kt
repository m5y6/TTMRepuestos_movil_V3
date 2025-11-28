package com.example.ttmrepuestos.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ttmrepuestos.repository.ProductoRepository

class ProductoViewModelFactory(
    private val application: Application,
    private val repository: ProductoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductoViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
