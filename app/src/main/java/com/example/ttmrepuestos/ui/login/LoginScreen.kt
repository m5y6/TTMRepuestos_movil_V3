package com.example.ttmrepuestos.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ttmrepuestos.viewmodel.AuthResult
import com.example.ttmrepuestos.viewmodel.UsuarioViewModel

@Composable
fun LoginScreen(
    viewModel: UsuarioViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClicked: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Observamos el StateFlow del ViewModel
    val loginResult by viewModel.loginResult.collectAsState()

    // Usamos LaunchedEffect para reaccionar a los cambios de loginResult
    LaunchedEffect(loginResult) {
        when (val result = loginResult) {
            is AuthResult.Success -> {
                Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
            }
            is AuthResult.Error -> {
                Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_LONG).show()
            }
            is AuthResult.Loading -> {
                // Opcional: Mostrar un indicador de carga
            }
            is AuthResult.Idle -> {
                // Estado inicial, no hacer nada
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Electrónico") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Mostramos un CircularProgressIndicator si está cargando
            if (loginResult is AuthResult.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.login(correo, contrasena) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Entrar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onRegisterClicked) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}
