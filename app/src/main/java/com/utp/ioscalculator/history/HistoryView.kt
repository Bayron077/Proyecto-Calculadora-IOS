package com.utp.ioscalculator.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryView(
    navController: NavController,
    viewModel: HistoryViewModel = viewModel()
) {
    val operations = viewModel.operations

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Historial",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFFFFA70E)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.clearHistory() }) {
                        Text("Limpiar", color = Color(0xFFFFA70E))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        if (operations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay operaciones recientes",
                    color = Color.Gray,
                    fontSize = 18.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(operations.asReversed()) { operation ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = operation,
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Light
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 8.dp),
                            thickness = 0.5.dp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}
