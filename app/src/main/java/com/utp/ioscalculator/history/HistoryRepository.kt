package com.utp.ioscalculator.history

import androidx.compose.runtime.mutableStateListOf


object HistoryRepository {
    val operationsList = mutableStateListOf<String>()

    fun addOperation(operation: String) {
        operationsList.add(operation)
    }
}