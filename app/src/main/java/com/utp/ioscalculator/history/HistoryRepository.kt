package com.utp.ioscalculator.history

import androidx.compose.runtime.mutableStateListOf

// Este objeto compartirá los datos entre tu CalculatorViewModel y el HistoryViewModel de Bayron
object HistoryRepository {
    // Lista reactiva para que Compose se actualice en tiempo real
    val operationsList = mutableStateListOf<String>()

    fun addOperation(operation: String) {
        operationsList.add(operation)
    }
}