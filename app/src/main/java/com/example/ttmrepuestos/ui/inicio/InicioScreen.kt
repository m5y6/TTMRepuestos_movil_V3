package com.example.ttmrepuestos.ui.inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ttmrepuestos.R

@Composable
fun InicioScreen() {
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
            verticalArrangement = Arrangement.Center
        ) {
            Text("La Leyenda de TTM Repuestos", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Un dia julia gutierrez decidio crear la empresa TTM repuestos el cual ofrece al cliente la venta de repuestos de camiones, otrogandoles un servicio de calidad y un precio justo para el cliente.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text("Nuestra Ubicación Mítica", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "La tienda TTM repuestos se encuentra en Vicuña Mackena, frente al construmark , en el local 1120",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}