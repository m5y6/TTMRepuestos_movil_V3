package com.example.ttmrepuestos.ui.verproductos

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ttmrepuestos.R
import com.example.ttmrepuestos.model.Producto
import com.example.ttmrepuestos.ui.theme.CustomButton
import com.example.ttmrepuestos.viewmodel.ProductoViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerProductosScreen(viewModel: ProductoViewModel) {
    val products by viewModel.products.collectAsState(initial = emptyList())
    val conversionRate by viewModel.clpToUsdRate.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var productoToEdit by remember { mutableStateOf<Producto?>(null) }
    var newNombre by remember { mutableStateOf("") }
    var newPrecio by remember { mutableStateOf("") }
    var newDescripcion by remember { mutableStateOf("") }
    var newCategoria by remember { mutableStateOf("") }

    var showDetailsDialog by remember { mutableStateOf(false) }
    var productoToShow by remember { mutableStateOf<Producto?>(null) }

    // Estado para controlar la moneda a mostrar (CLP o USD)
    var showInUsd by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.logo3),
            contentDescription = "Logo de fondo",
            modifier = Modifier.fillMaxSize().graphicsLayer(alpha = 0.15f),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                // Boton flotante para cambiar de moneda
                if (conversionRate != null) {
                    FloatingActionButton(
                        onClick = { showInUsd = !showInUsd },
                    ) {
                        Icon(
                            imageVector = if (showInUsd) Icons.Default.MoneyOff else Icons.Default.AttachMoney,
                            contentDescription = if (showInUsd) "Mostrar en CLP" else "Mostrar en USD"
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Text(
                    text = "Listado de Productos",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(products) { producto ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    productoToShow = producto
                                    showDetailsDialog = true
                                }
                        ) {
                            ListItem(
                                headlineContent = { Text(producto.nombre) },
                                supportingContent = {
                                    // Logica para mostrar precio en CLP o USD
                                    val precioMostrado = if (showInUsd && conversionRate != null) {
                                        val precioEnUsd = producto.precio * conversionRate!!
                                        String.format(Locale.US, "USD \$%.2f", precioEnUsd)
                                    } else {
                                        "CLP \$${producto.precio}"
                                    }
                                    Text("$precioMostrado - Cat: ${producto.categoria}")
                                },
                                leadingContent = {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(producto.fotoUri)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(R.drawable.logo3),
                                        error = painterResource(R.drawable.logo3),
                                        contentDescription = "Foto de ${producto.nombre}",
                                        modifier = Modifier.size(56.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                },
                                trailingContent = {
                                    Row {
                                        IconButton(onClick = {
                                            productoToEdit = producto
                                            newNombre = producto.nombre
                                            newPrecio = producto.precio.toString()
                                            newDescripcion = producto.descripcion
                                            newCategoria = producto.categoria
                                            showEditDialog = true
                                        }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                                        }
                                        IconButton(onClick = { viewModel.deleteProduct(producto) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showEditDialog && productoToEdit != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar Producto") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newNombre,
                        onValueChange = { newNombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPrecio,
                        onValueChange = { newPrecio = it },
                        label = { Text("Precio") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newCategoria,
                        onValueChange = { newCategoria = it },
                        label = { Text("Categoría") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newDescripcion,
                        onValueChange = { newDescripcion = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updatedProduct = productoToEdit!!.copy(
                            nombre = newNombre,
                            precio = newPrecio.toIntOrNull() ?: productoToEdit!!.precio,
                            descripcion = newDescripcion,
                            categoria = newCategoria
                        )
                        viewModel.updateProduct(updatedProduct)
                        showEditDialog = false
                    },
                    colors = CustomButton.colors
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showDetailsDialog && productoToShow != null) {
        ProductDetailsDialog(
            producto = productoToShow!!,
            conversionRate = conversionRate,
            onDismiss = { showDetailsDialog = false }
        )
    }
}

@Composable
fun ProductDetailsDialog(
    producto: Producto,
    conversionRate: Double?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = producto.nombre, style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(producto.fotoUri)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.logo3),
                    error = painterResource(R.drawable.logo3),
                    contentDescription = "Foto de ${producto.nombre}",
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.height(16.dp))
                // Precio en CLP
                Text("Precio: CLP \$${producto.precio}", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.fillMaxWidth())

                // Precio en USD (si la tasa esta disponible)
                if (conversionRate != null) {
                    val precioEnUsd = producto.precio * conversionRate
                    Text(
                        text = "Aprox: USD \$${String.format(Locale.US, "%.2f", precioEnUsd)}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(8.dp))
                Text("Categoría: ${producto.categoria}", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Descripción: ${producto.descripcion}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = CustomButton.colors
            ) { Text("Cerrar") }
        }
    )
}
