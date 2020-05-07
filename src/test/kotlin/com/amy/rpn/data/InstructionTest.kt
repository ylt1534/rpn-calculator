package com.amy.rpn.data

import com.amy.rpn.exception.CalculatorException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.data.row
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import java.util.Stack
import kotlin.math.sqrt
import kotlin.random.Random
import io.kotest.data.blocking.forAll

class InstructionTest: StringSpec() {

    override fun beforeTest(testCase: TestCase) {
        clear()
    }

    init {

        "Push#instruct should push value into stack and update history successfully" {
            val pushes = listOf(
                Push(3.0),
                Push(2.0)
            )
            pushes.forEach { it.instruct(
                stack,
                history
            ) }
            stack shouldContainExactlyInAnyOrder listOf(3.0, 2.0)
            history shouldContainExactlyInAnyOrder listOf(
                PushInstructionSnapshot,
                PushInstructionSnapshot
            )
        }

        "UnaryInstruction#instruct should evaluate sqrt successfully when stack is not empty" {
            val unaryInstruction =
                UnaryInstruction(anyPosition, UnaryOperation.SQRT)
            repeat((1..10).count()) {
                clear()
                val num = initializeStackWithOneNum()
                unaryInstruction.instruct(
                    stack,
                    history
                )
                stack shouldContainExactlyInAnyOrder listOf(sqrt(num))
                history shouldContainExactlyInAnyOrder listOf(
                    UnaryInstructionSnapshot(
                        num,
                        1
                    )
                )
            }
        }

        "UnaryInstruction#instruct should throw exception when stack is empty" {
            val unaryInstruction =
                UnaryInstruction(anyPosition, UnaryOperation.SQRT)
            shouldThrow<CalculatorException> {
                unaryInstruction.instruct(
                    stack,
                    history
                )
            }.message shouldBe "operator ${unaryInstruction.operation} (position: ${anyPosition + 1}): insufficient parameters"
        }

        "BinaryInstruction#instruct should evaluate sqrt successfully when stack size is greater than 1" {
            BinaryOperation.values().forEach { operation ->
                val binaryInstruction = BinaryInstruction(anyPosition, operation)
                repeat((1..10).count()) {
                    clear()
                    val (operandA, operandB) = initializeStackWithTwoNums()
                    binaryInstruction.instruct(
                        stack,
                        history
                    )
                    val res = operation.apply(operandA, operandB)
                    stack shouldContainExactlyInAnyOrder res
                    history shouldContainExactlyInAnyOrder listOf(
                        BinaryInstructionSnapshot(
                            operandA,
                            operandB,
                            res.size
                        )
                    )
                }
            }
        }

        "BinaryInstruction#instruct should throw exception when stack size is less than 2" {
            BinaryOperation.values().forEach { operation ->
                val binaryInstruction = BinaryInstruction(anyPosition, operation)
                repeat((1..10).count()) {
                    clear()
                    initializeStackWithOneNum()
                    shouldThrow<CalculatorException> {
                        binaryInstruction.instruct(
                            stack,
                            history
                        )
                    }.message shouldBe "operator $operation (position: ${anyPosition + 1}): insufficient parameters"

                }
            }
        }

        "Undo#instruct should update stack and history successfully when stack and history are not empty neither" {
            forAll(
                row(2.0, BinaryInstructionSnapshot(6.0, 3.0, 1)),
                row(36.0, UnaryInstructionSnapshot(6.0, 1)),
                row(1.0, PushInstructionSnapshot)
            ) { num, snapshot ->
                clear()
                stack.push(num)
                history.push(snapshot)
                Undo.instruct(stack, history)

                stack shouldContainExactlyInAnyOrder when (snapshot) {
                    is BinaryInstructionSnapshot -> listOf(snapshot.operandA, snapshot.operandB)
                    is UnaryInstructionSnapshot -> listOf(snapshot.operand)
                    PushInstructionSnapshot -> emptyList()
                }

                history.size shouldBe 0
            }
        }

        "Clear#instruct should clear stack and history successfully" {
            initializeStackWithTwoNums()
            Clear.instruct(stack, history)
            stack.size shouldBe 0
            history.size shouldBe 0
        }
    }
}

private fun initializeStackWithOneNum(): Double {
    val num = Random.nextDouble()
    stack.push(num)
    return num
}

private fun initializeStackWithTwoNums(): Pair<Double, Double> {
    val operandA = Random.nextDouble()
    val operandB = Random.nextDouble()
    stack.push(operandA)
    stack.push(operandB)
    return Pair(operandA, operandB)
}

private fun clear() {
    stack.clear()
    history.clear()
}

private val anyPosition = Random.nextInt()
private val stack = Stack<Double>()
private val history = Stack<Snapshot>()