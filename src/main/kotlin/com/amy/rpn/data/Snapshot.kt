package com.amy.rpn.data

sealed class Snapshot {
    abstract val resCount: Int
}

data class BinaryInstructionSnapshot(val operandA: Double, val operandB: Double, override val resCount: Int): Snapshot()
data class UnaryInstructionSnapshot(val operand: Double, override val resCount: Int): Snapshot()
object PushInstructionSnapshot: Snapshot() {
    override val resCount: Int get() = 1
}