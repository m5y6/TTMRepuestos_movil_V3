package com.example.ttmrepuestos.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ttmrepuestos.model.Post
import com.example.ttmrepuestos.viewmodel.PostViewModel

@Composable
fun PostScreen(viewModel: PostViewModel = viewModel()) { // viewModel() para la app, se inyecta en el test
    val posts by viewModel.posts.collectAsState()

    LazyColumn {
        items(posts) {
            post -> PostItem(post = post)
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Titulo: ${post.title}")
            Text(text = post.body)
        }
    }
}
