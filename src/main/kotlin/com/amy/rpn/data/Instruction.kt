package com.amy.rpn.data

import com.amy.rpn.exception.CalculatorException
import java.util.*

sealed class Instruction {
    @Throws(CalculatorException::class)
    abstract fun instruct(stack: Stack<Double>, history: Stack<Snapshot>)
}

data class Push(val num: Double): Instruction() {
    override fun instruct(stack: Stack<Double>, history: Stack<Snapshot>) {
        stack.push(num)
        history.push(PushInstructionSnapshot)
    }
}

data class UnaryInstruction(val position: Int, val operation: UnaryOperation): Instruction() {
    @Throws(CalculatorException::class)
    override fun instruct(stack: Stack<Double>, history: Stack<Snapshot>) {
        if (stack.empty()) {
            throw CalculatorException("operator $operation (position: ${position + 1}): insufficient parameters")
        } else {
            val operand = stack.pop()
            val res = operation.apply(operand)
            stack.addAll(res)
            history.push(UnaryInstructionSnapshot(operand, res.size))
        }
    }
}

data class BinaryInstruction(val position: Int, val operation: BinaryOperation): Instruction() {
    @Throws(CalculatorException::class)
    override fun instruct(stack: Stack<Double>, history: Stack<Snapshot>) {
        if (stack.size < 2) {
            throw CalculatorException("operator $operation (position: ${position + 1}): insufficient parameters")
        } else {
            val operandB = stack.pop()
            val operandA = stack.pop()
            try {
                val res = operation.apply(operandA, operandB)
                stack.addAll(res)
                history.push(
                    BinaryInstructionSnapshot(
                        operandA,
                        operandB,
                        res.size
                    )
                )
            } catch (e: CalculatorException) {
                stack.push(operandA)
                stack.push(operandB)
                throw  e
            }
        }
    }
}

object Undo: Instruction() {
    override fun instruct(stack: Stack<Double>, history: Stack<Snapshot>) {
        if (!history.empty()) {
            val snapshot= history.pop()

            popFromStack(snapshot.resCount, stack)

            when (snapshot) {
                is BinaryInstructionSnapshot -> {
                    stack.push(snapshot.operandA)
                    stack.push(snapshot.operandB)
                }
                is UnaryInstructionSnapshot -> stack.push(snapshot.operand)
                PushInstructionSnapshot -> Unit
            }
        }
    }

    private fun popFromStack(resCount: Int, stack: Stack<Double>) {
        for (i in (1..resCount)) {
            if (!stack.empty()) {
                stack.pop()
            } else {
                break
            }
        }
    }
}

object Clear: Instruction() {
    override fun instruct(stack: Stack<Double>, history: Stack<Snapshot>) {
        stack.clear()
        history.clear()
    }
}

object UndefinedInstruction: Instruction() {
    @Throws(CalculatorException::class)
    override fun instruct(stack: Stack<Double>, history: Stack<Snapshot>) {
        throw CalculatorException("undefined argument")
    }
}