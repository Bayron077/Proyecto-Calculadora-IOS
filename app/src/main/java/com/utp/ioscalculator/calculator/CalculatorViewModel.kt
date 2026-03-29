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
            else -> { // Digits
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
        // 1. Validar que la lista no esté vacía
        if (contentResult.isEmpty()) return

        // Si el usuario termina la operación con un operador (Ej. "5 + "), lo ignoramos
        val last = contentResult.last()
        if (isOperator(last) || last.endsWith(".")) {
            contentResult.removeAt(contentResult.size - 1)
        }

        // 2. Crear una copia de la lista para ir resolviendo paso a paso
        var tempList = contentResult.toMutableList()

        // 3. Primer recorrido: Resolver multiplicaciones, divisiones y módulo (x, ÷, %)
        var i = 0
        while (i < tempList.size) {
            val token = tempList[i]
            if (token == "x" || token == "÷" || token == "%") {
                val num1 = tempList[i - 1] // El número de la izquierda
                val num2 = tempList[i + 1] // El número de la derecha

                val res = when (token) {
                    "x" -> multiply(num1, num2)
                    "÷" -> divide(num1, num2)
                    "%" -> { // Operación módulo básica
                        val n1 = num1.toDoubleOrNull() ?: 0.0
                        val n2 = num2.toDoubleOrNull() ?: 0.0
                        (n1 % n2).toString()
                    }
                    else -> "0"
                }

                // --- PREVENCIÓN DE ERROR: DIVISIÓN POR CERO ---
                if (res == "Sin definir") {
                    contentResult.clear()
                    contentResult.add("Sin definir")
                    isCalculated = true
                    showContent()
                    return // Detenemos el cálculo aquí mismo
                }

                // Reemplazamos la operación por el resultado en la lista
                tempList[i - 1] = res
                tempList.removeAt(i) // Borramos el operador
                tempList.removeAt(i) // Borramos el num2
                // No sumamos a 'i' porque la lista se encogió y debemos seguir comprobando
            } else {
                i++ // Si no es x, ÷, o %, pasamos al siguiente elemento
            }
        }

        // 4. Segundo recorrido: Resolver sumas y restas (+, -)
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

        // 5. El resultado final será el único elemento que queda en la lista
        var finalAnswer = tempList.first()

        // Si es un número entero (ej. 8.0), le quitamos el ".0" para que se vea limpio
        if (finalAnswer.endsWith(".0")) {
            finalAnswer = finalAnswer.removeSuffix(".0")
        }

        // --- CONEXIÓN CON EL HISTORIAL ---
        // Aquí generas el formato exacto que pide el PDF: "5+3=8"
        val operacionHistorial = contentResult.joinToString("") + "=" + finalAnswer

        // Guarda la operación en el repositorio compartido
        com.utp.ioscalculator.history.HistoryRepository.addOperation(operacionHistorial)

        // 6. Actualizar la lista original y la pantalla
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
            "Sin definir"
        }
    }
}
