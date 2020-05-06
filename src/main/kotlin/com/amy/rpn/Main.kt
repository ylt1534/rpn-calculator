package com.amy.rpn

import com.amy.rpn.biz.Calculator
import com.amy.rpn.exception.CalculatorException

fun main() {
    println("Welcome to RPN Calculator! Input your RPN Expression here and Enter exit to exit the procedure.")

    val calculator = Calculator()

    while (true) {
        val input = readLine()?.trim()
        if (input == "exit") {
            break
        } else {
            try {
                calculator.evaluate(input)
            } catch (e: CalculatorException) {
                println(e.message)
            }
            print("Stack:")
            println(calculator.getStackContent())
        }
    }
}
