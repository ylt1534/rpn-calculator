package com.amy.rpn.data

import com.amy.rpn.exception.CalculatorException
import kotlin.math.sqrt

enum class UnaryOperation(private val symbol: String) {
    SQRT("sqrt") {
        override fun apply(x: Double) = listOf(sqrt(x))
    };

    abstract fun apply(x: Double): List<Double>

    override fun toString(): String = symbol
    companion object {
        @JvmStatic
        fun symbolToEnum(symbol: String) = values().firstOrNull { it.symbol == symbol }
    }

}

enum class BinaryOperation(private val symbol: String) {
    ADDITION("+") {
        override fun apply(x: Double, y: Double) =  listOf(x + y)
    },
    SUBTRACTION("-") {
        override fun apply(x: Double, y: Double) =  listOf(x - y)
    },
    MULTIPLICATION("*") {
        override fun apply(x: Double, y: Double) =  listOf(x * y)
    },
    DIVISION("/") {
        @Throws(CalculatorException::class)
        override fun apply(x: Double, y: Double): List<Double> {
            if (y == 0.0) {
                throw CalculatorException("Divide by 0 is forbidden")
            }
            return listOf(x / y)
        }
    };

    @Throws(CalculatorException::class)
    abstract fun apply(x: Double, y: Double): List<Double>

    override fun toString(): String = symbol

    companion object {
        @JvmStatic
        fun symbolToEnum(symbol: String) = values().firstOrNull { it.symbol == symbol }
    }
}
