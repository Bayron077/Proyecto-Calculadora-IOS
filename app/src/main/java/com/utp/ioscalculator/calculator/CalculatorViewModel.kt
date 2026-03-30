package com.utp.ioscalculator.calculator

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CalculatorViewModel: ViewModel() {

    private val model = CalculatorModel()
    val result = mutableStateOf("0")
    var contentResult = mutableListOf("0")
    private var isCalculated = false

    fun showContent() {
        result.value = if (contentResult.isNotEmpty()) {
            contentResult.joinToString("").replace(".", ",")
        } else {
            "0"
        }
    }

    private fun isOperator(s: String): Boolean = s == "÷" || s == "x" || s == "-" || s == "+" || s == "%"

    fun manageClickButtons(valueButton: String) {
        if (valueButton.isEmpty()) return

        if (contentResult.size == 1 && contentResult[0] == "Sin definir" && valueButton != "AC") {
            contentResult.clear()
            contentResult.add("0")
        }

        when (valueButton) {
            "AC" -> {
                contentResult.clear()
                contentResult.add("0")
                isCalculated = false
            }
            "⁺/₋" -> {
                val lastIndex = contentResult.size - 1
                if (lastIndex >= 0) {
                    val last = contentResult[lastIndex]
                    if (!isOperator(last)) {
                        val currentVal = last.toDoubleOrNull() ?: 0.0
                        if (currentVal != 0.0) {
                            val toggled = if (last.startsWith("-")) last.substring(1) else "-$last"
                            contentResult[lastIndex] = toggled
                        }
                    }
                }
                isCalculated = false
            }
            "%","÷", "x", "-", "+" -> {
                val last = contentResult.lastOrNull()
                if (last != null) {
                    if (isOperator(last)) {
                        contentResult[contentResult.size - 1] = valueButton
                    } else {
                        contentResult.add(valueButton)
                    }
                }
                isCalculated = false
            }
            "=" -> {
                calculateResult()
            }
            "," -> {
                if (isCalculated) {
                    contentResult.clear()
                    contentResult.add("0.")
                    isCalculated = false
                } else {
                    val lastIndex = contentResult.size - 1
                    if (lastIndex >= 0) {
                        val last = contentResult[lastIndex]
                        if (!isOperator(last)) {
                            if (!last.contains(".")) {
                                contentResult[lastIndex] = "$last."
                            }
                        } else {
                            contentResult.add("0.")
                        }
                    } else {
                        contentResult.add("0.")
                    }
                }
            }
            else -> {
                if (isCalculated) {
                    contentResult.clear()
                    contentResult.add(valueButton)
                    isCalculated = false
                } else {
                    val lastIndex = contentResult.size - 1
                    if (lastIndex < 0 || isOperator(contentResult[lastIndex])) {
                        contentResult.add(valueButton)
                    } else {
                        val last = contentResult[lastIndex]
                        if (last == "0") {
                            contentResult[lastIndex] = valueButton
                        } else {
                            contentResult[lastIndex] = last + valueButton
                        }
                    }
                }
            }
        }
        showContent()
    }

    fun calculateResult() {

        if (contentResult.isEmpty()) return


        val last = contentResult.last()
        if (isOperator(last) || last.endsWith(".")) {
            contentResult.removeAt(contentResult.size - 1)
        }


        var tempList = contentResult.toMutableList()


        var i = 0
        while (i < tempList.size) {
            val token = tempList[i]
            if (token == "x" || token == "÷" || token == "%") {
                val num1 = tempList[i - 1]
                val num2 = tempList[i + 1]

                val res = when (token) {
                    "x" -> multiply(num1, num2)
                    "÷" -> divide(num1, num2)
                    "%" -> {
                        val n1 = num1.toDoubleOrNull() ?: 0.0
                        val n2 = num2.toDoubleOrNull() ?: 0.0
                        (n1 % n2).toString()
                    }
                    else -> "0"
                }


                if (res == "Sin definir") {
                    contentResult.clear()
                    contentResult.add("Sin definir")
                    isCalculated = true
                    showContent()
                    return
                }


                tempList[i - 1] = res
                tempList.removeAt(i)
                tempList.removeAt(i)

            } else {
                i++
            }
        }


        i = 0
        while (i < tempList.size) {
            val token = tempList[i]
            if (token == "+" || token == "-") {
                val num1 = tempList[i - 1]
                val num2 = tempList[i + 1]

                val res = when (token) {
                    "+" -> add(num1, num2)
                    "-" -> subtract(num1, num2)
                    else -> "0"
                }

                tempList[i - 1] = res
                tempList.removeAt(i)
                tempList.removeAt(i)
            } else {
                i++
            }
        }

        var finalAnswer = tempList.first()

        if (finalAnswer.endsWith(".0")) {
            finalAnswer = finalAnswer.removeSuffix(".0")
        }


        val operacionHistorial = contentResult.joinToString("") + "=" + finalAnswer

        com.utp.ioscalculator.history.HistoryRepository.addOperation(operacionHistorial)

        contentResult.clear()
        contentResult.add(finalAnswer)
        isCalculated = true
        showContent()
    }

    fun add(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return model.add(n1, n2).toString()
    }

    fun subtract(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return model.subtract(n1, n2).toString()
    }

    fun multiply(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return model.multiply(n1, n2).toString()
    }

    fun divide(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return if (n2 != 0.0) {
            model.divide(n1, n2).toString()
        } else {
            "Error"
        }
    }
}
