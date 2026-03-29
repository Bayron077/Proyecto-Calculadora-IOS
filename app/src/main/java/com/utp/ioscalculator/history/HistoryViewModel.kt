package com.utp.ioscalculator.history

import androidx.lifecycle.ViewModel

class HistoryViewModel : ViewModel() {
    val operations = HistoryRepository.operationsList

    fun clearHistory() {
        HistoryRepository.operationsList.clear()
    }
}
