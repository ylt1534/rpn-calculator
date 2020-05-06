# RPN calulator

This is a simple implementation of Command-line based RPN (Reverse Polish notation) calculator

http://en.wikipedia.org/wiki/Reverse_Polish_notation

### Requirements

The calculator waits for user input and expects to receive strings containing whitespace separated lists of numbers and operators.

Numbers are pushed on to the stack. Operators operate on numbers that are on the stack.

Available operators are `+`, `-`, `*`, `/`, `sqrt`, `undo`, `clear`

| operators          | behavior                                                     |
| ------------------ | ------------------------------------------------------------ |
| `+`, `-`, `*`, `/` | addition, subtraction, multiplication and division respectively on the top two items from the stack |
| `sqrt`             | a square root on the top item from the stack                 |
| `undo`             | undoes the previous operation.  "undo undo" will undo the previous two operations. |
| `clear`            | removes all items from the stack.                            |

After processing an input string, the calculator displays the current contents of the stack as a space-separated list.

Numbers should be stored on the stack to at least 15 decimal places of precision, but displayed to 10 decimal places (or less if it causes no loss of precision).

 All numbers should be formatted as plain decimal strings (ie. no engineering formatting).

If an operator cannot find a sufficient number of parameters on the stack, a warning is displayed:
`operator <operator> (position: <pos>): insufficient parameters`

After displaying the warning, all further processing of the string terminates and the current state of the stack is displayed.

### Development Tool

- Language: Kotlin 1.3.7
- Build Tool: Gradle
- Tests: JUnit and Kotest

### Getting Started

- clone the project
- run `./gradlew build` under project
- the `rpn-calculator-xx-SNAPSHOT.zip` file appears under `build/distributions` directory
- unzip the  `rpn-calculator-xx-SNAPSHOT.zip` file and cd into the `bin` directory
- run the executable files under the `bin` directory, for linux: `./rpn-calculator`





