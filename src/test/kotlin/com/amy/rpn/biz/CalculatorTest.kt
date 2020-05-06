package com.amy.rpn.biz

import com.amy.rpn.data.BinaryInstruction
import com.amy.rpn.data.BinaryOperation
import com.amy.rpn.data.Clear
import com.amy.rpn.data.Push
import com.amy.rpn.data.UnaryInstruction
import com.amy.rpn.data.UnaryOperation
import com.amy.rpn.data.UndefinedInstruction
import com.amy.rpn.data.Undo
import com.amy.rpn.exception.CalculatorException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class CalculatorTest: StringSpec(){

    override fun beforeTest(testCase: TestCase) {
        calculator.clear()
    }

    init {
        "toInstruction method revert string to Instruction object successfully" {
            val anyPosition = 0
            forAll(
                row("0", Push("0".toDouble())),
                row("12", Push("12".toDouble())),
                row("+", BinaryInstruction(anyPosition, BinaryOperation.ADDITION)),
                row("-",
                    BinaryInstruction(anyPosition, BinaryOperation.SUBTRACTION)
                ),
                row("*",
                    BinaryInstruction(anyPosition, BinaryOperation.MULTIPLICATION)
                ),
                row("/", BinaryInstruction(anyPosition, BinaryOperation.DIVISION)),
                row("sqrt", UnaryInstruction(anyPosition, UnaryOperation.SQRT)),
                row("undo", Undo),
                row("clear", Clear),
                row("other", UndefinedInstruction)
            ) { input, instruction ->
                calculator.toInstruction(anyPosition, input) shouldBe instruction
            }
        }

        "operate method should update stack successfully and correctly - case 1" {
            forAll(
                row("5 2", "5 2")
            ) { input, stackContent ->
                validateStackContent(input, stackContent)
            }
        }

        "operate method should update stack successfully and correctly - case 2" {
            forAll(
                row("2 sqrt", "1.4142135624"),
                row("clear 9 sqrt", "3")
            ) { input, stackContent ->
                validateStackContent(input, stackContent)
            }
        }

        "operate method should update stack successfully and correctly - case 3" {
            forAll(
                row("5 2 -", "3"),
                row("3 -", "0"),
                row("clear", "")
            ) { input, stackContent ->
                validateStackContent(input, stackContent)
            }
        }

        "operate method should update stack successfully and correctly - case 4" {
            forAll(
                row("5 4 3 2", "5 4 3 2"),
                row("undo undo *", "20"),
                row("5 *", "100"),
                row("undo", "20 5")
            ) { input, stackContent ->
                validateStackContent(input, stackContent)
            }
        }

        "operate method should update stack successfully and correctly - case 5" {
            forAll(
                row("7 12 2 /", "7 6"),
                row("*", "42"),
                row("4 /", "10.5")
            ) { input, stackContent ->
                validateStackContent(input, stackContent)
            }
        }

        "operate method should update stack successfully and correctly - case 6" {
            forAll(
                row("1 2 3 4 5", "1 2 3 4 5"),
                row("*", "1 2 3 20"),
                row("clear 3 4 -", "-1")
            ) { input, stackContent ->
                validateStackContent(input, stackContent)
            }
        }

        "operate method should update stack successfully and correctly - case 7" {
            forAll(
                row("1 2 3 4 5", "1 2 3 4 5"),
                row("* * * *", "120")
            ) { input, stackContent ->
                validateStackContent(input, stackContent)
            }
        }

        "operate method should update stack successfully and correctly - case 8" {
            forAll(
                row("1 2 3 * 5 + * * 6 5", "11")
            ) { input, stackContent ->
                shouldThrow<CalculatorException> {
                    calculator.evaluate(input)
                }.message shouldBe "operator * (position: 8): insufficient parameters"
                calculator.getStackContent() shouldBe stackContent
            }
        }

        "operate method should throw CalculatorException when input is illegal format" {
            forAll(
                row(null),
                row("1 2+"),
                row(""),
                row("1 2/"),
                row("#% @")
            ) {
                shouldThrow<CalculatorException> {
                    calculator.evaluate(it)
                }.message shouldBe "Illegal input format, please enter again."
                calculator.getStackContent() shouldBe ""
            }
        }

        "operate method should throw CalculatorException when input results in divide by 0" {
            forAll(
                row("1 0 /", "1 0")
            ) { input, stackContent ->
                shouldThrow<CalculatorException> {
                    calculator.evaluate(input)
                }.message shouldBe "Divide by 0 is forbidden"
                calculator.getStackContent() shouldBe stackContent
            }
        }
    }

    private fun validateStackContent(input: String, stackContent: String) {
        calculator.evaluate(input)
        calculator.getStackContent() shouldBe stackContent
    }
}

private val calculator = Calculator()


