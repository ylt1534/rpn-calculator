package com.amy.rpn.biz

import com.amy.rpn.exception.CalculatorException
import com.amy.rpn.data.Snapshot
import com.amy.rpn.data.BinaryInstruction
import com.amy.rpn.data.BinaryOperation
import com.amy.rpn.data.Clear
import com.amy.rpn.data.Instruction
import com.amy.rpn.data.Push
import com.amy.rpn.data.UnaryInstruction
import com.amy.rpn.data.UnaryOperation
import com.amy.rpn.data.UndefinedInstruction
import com.amy.rpn.data.Undo
import com.google.common.annotations.VisibleForTesting
import java.text.DecimalFormat
import java.util.*

class Calculator {

    private val stack: Stack<Double> = Stack()
    private val history: Stack<Snapshot> = Stack()

    /**
     * Evaluate expression from console and push result into stack and record history,
     * throw exception if the expression is illegal
     *
     * @param input   expression from console
     * @throws CalculatorException
     */
    @Throws(CalculatorException::class)
    fun evaluate(input: String?) {
        if (input?.validateInput() == true) {
            val arguments = input.split(" ")
            for (position in arguments.indices) {
                val instruction = toInstruction(position, arguments[position])
                try {
                    instruction.instruct(stack, history)
                } catch (e: CalculatorException) {
                    throw e
                }
            }
        } else {
            throw CalculatorException("Illegal input format, please enter again.")
        }
    }

    private fun String?.validateInput() = this?.split(" ")?.all { it.matches(VALID_REGEX) }

    /**
     * Convert each argument to corresponding Instruction Object
     *
     * @param position   position of argument in expression from console
     * @param value      value of argument in expression from console
     */
    @VisibleForTesting
    internal fun toInstruction(position: Int, value: String): Instruction {
        return when {
            value.matches(DIGIT_REGEX) -> Push(value.toDouble())
            value.matches(BINARY_OPERATOR_REGEX) -> BinaryInstruction(
                position,
                BinaryOperation.symbolToEnum(value)!!
            )
            value.matches(UNARY_OPERATOR_REGEX) -> UnaryInstruction(
                position,
                UnaryOperation.symbolToEnum(value)!!
            )
            value == UNDO -> Undo
            value == CLEAR -> Clear
            else -> UndefinedInstruction
        }
    }

    /**
     * Clear content of stack and history of Calculator
     */
    internal fun clear() {
        stack.clear()
        history.clear()
    }

    /**
     * Return formatted stack content
     */
    internal fun getStackContent() = stack.joinToString(" ") { format.format(it) }

    companion object {
        private val VALID_REGEX = Regex("(0|[1-9]\\d*|\\+|-|\\*|/|sqrt|undo|clear)")
        private val DIGIT_REGEX = Regex("(0|[1-9]\\d*)")
        private val BINARY_OPERATOR_REGEX = Regex("[+\\-*/]")
        private val UNARY_OPERATOR_REGEX = Regex("(sqrt)")
        private const val UNDO = "undo"
        private const val CLEAR = "clear"
        private val format = DecimalFormat("0.##########")
    }
}
