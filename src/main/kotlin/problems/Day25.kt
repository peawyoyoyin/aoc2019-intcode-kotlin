package problems

import machine.IntCodeMachine
import problems.util.InputReader

fun main() {
    val program = InputReader.readBigInputFromFile("F:/code/kotlin/aoc2019-intcode/src/main/resources/inputs/day25.txt")

    val machine = IntCodeMachine(program.toMutableList())

    machine.run()

    while (true) {
        println(machine.ASCIIOutput().joinToString(""))
        machine.flushOutput()

        val command = readln()

        machine.feedASCIIStringAndRun(command.trim() + Char(10))
    }
}