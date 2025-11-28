package com.example.ttmrepuestos.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ttmrepuestos.model.Usuario
import com.example.ttmrepuestos.viewmodel.AuthResult
import com.example.ttmrepuestos.viewmodel.UsuarioViewModel
import com.example.ttmrepuestos.viewmodel.UsuarioViewModelFactory

@Composable
fun RegisterScreen(
    factory: UsuarioViewModelFactory,
    onRegisterSuccess: () -> Unit,
    onLoginClicked: () -> Unit
) {
    val viewModel: UsuarioViewModel = viewModel(factory = factory)
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    val context = LocalContext.current

    // CORRECCIÓN 1: Usar el nombre correcto 'registroResult'
    val registroResult by viewModel.registroResult.collectAsState()

    // CORRECCIÓN 2: Implementar la lógica de AuthResult
    LaunchedEffect(registroResult) {
        when (val result = registroResult) {
            is AuthResult.Success -> {
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                onRegisterSuccess()
            }
            is AuthResult.Error -> {
                Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_LONG).show()
            }
            is AuthResult.Loading -> {
                // Opcional: Mostrar un indicador de carga
            }
            is AuthResult.Idle -> {
                // Estado inicial
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
            Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo Electrónico") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = contrasena, onValueChange = { contrasena = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))

            if (registroResult is AuthResult.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        val usuario = Usuario(
                            nombre = nombre,
                            apellido = apellido,
                            edad = edad.toIntOrNull() ?: 0,
                            correo = correo,
                            telefono = telefono,
                            contrasena = contrasena
                        )
                        // CORRECCIÓN 3: Usar el nombre correcto 'registrar'
                        viewModel.registrar(usuario)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onLoginClicked) {
                Text("¿Ya tienes cuenta? Inicia Sesión")
            }
        }
    }
}
