package com.amy.rpn.data

import com.amy.rpn.exception.CalculatorException
import kotlin.math.sqrt

enum class UnaryOperation(private val symbol: String) {
    SQRT("sqrt") {
        override fun apply(x: Double) = sqrt(x)
        override val resCount: Int get() = 1
    };

    abstract fun apply(x: Double): Double

    override fun toString(): String = symbol

    // The resCount represents count of numbers in result
    // For operator sqrt, sqrt(9.0) -> 3.0, resCount is 1 (the 3.0)
    // For operator swap, swap(3.0, 4.0) -> 4.0, 3.0, resCount is 2 (the 3.0 and 4.0)
    abstract val resCount: Int

    companion object {
        @JvmStatic
        fun symbolToEnum(symbol: String) = values().firstOrNull { it.symbol == symbol }
    }

}

enum class BinaryOperation(private val symbol: String) {
    ADDITION("+") {
        override fun apply(x: Double, y: Double) =  x + y
        override val resCount: Int get() = 1
    },
    SUBTRACTION("-") {
        override fun apply(x: Double, y: Double) =  x - y
        override val resCount: Int get() = 1
    },
    MULTIPLICATION("*") {
        override fun apply(x: Double, y: Double) =  x * y
        override val resCount: Int get() = 1
    },
    DIVISION("/") {
        @Throws(CalculatorException::class)
        override fun apply(x: Double, y: Double): Double {
            if (y == 0.0) {
                throw CalculatorException("Divide by 0 is forbidden")
            }
            return x / y
        }

        override val resCount: Int get() = 1
    };

    @Throws(CalculatorException::class)
    abstract fun apply(x: Double, y: Double): Double

    abstract val resCount: Int

    override fun toString(): String = symbol

    companion object {
        @JvmStatic
        fun symbolToEnum(symbol: String) = values().firstOrNull { it.symbol == symbol }
    }
}
