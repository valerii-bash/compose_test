package com.vbash.compose.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onShowMessage: (message: String) -> Unit,
) {
    val state = viewModel.state.collectAsState()
    Box(Modifier.fillMaxSize()) {
        if (state.value.isError) {
            Button({ viewModel.uploadData() }, modifier = Modifier.align(Alignment.Center)) {
                Text("Retry")
            }
        }
        if (state.value.isProgress) {
            Box(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.5f))
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .clickable {  }
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp).align(Alignment.Center),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
        LazyColumn {
            items(state.value.data) { item ->
                Text("${item.description}; ${item.currencyAmount}")
            }
        }
    }

    val localContext = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is HomeViewModel.Event.ShowError -> onShowMessage(localContext.getString(event.message))
            }
        }
    }
}