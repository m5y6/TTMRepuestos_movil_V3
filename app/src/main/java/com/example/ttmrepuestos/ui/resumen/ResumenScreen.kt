package com.example.ttmrepuestos.ui.resumen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ttmrepuestos.R
import com.example.ttmrepuestos.viewmodel.ProductoViewModel

@Composable
fun ResumenScreen(viewModel: ProductoViewModel) {
    val products by viewModel.products.collectAsState(initial = emptyList())
    val productsByCategory = products.groupBy { it.categoria }


    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.logo3),
            contentDescription = "Logo de fondo",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.15f),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Resumen de Inventario", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(24.dp))

            // Mostramos el total de productos
            Text("Total de productos: ${products.size}", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            Text("Desglose por Categoría", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (productsByCategory.isEmpty()) {
                Text("No hay productos para mostrar.")
            } else {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        productsByCategory.toSortedMap().forEach { (categoria, productosEnCategoria) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = categoria,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = productosEnCategoria.size.toString(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Divider()
                        }
                    }
                }
            }
        }
    }
}
